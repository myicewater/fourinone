package com.fourinone;

import java.util.List;
import java.util.ArrayList;
/**
 * 工人
 * 
 * 序列化
 * 
 * @comment 朱素海
 *
 */
public class MigrantWorker extends WorkerParallel implements ParkStatg
{
	String host,workerType,workerjarname;
	int port;
	private int selfIndex=-1;
	private volatile boolean _interrupted;
	
	void waitWorkingByService(String workerType)
	{
		//publish service and reg heatbeat
		//try{
			//Worker wk = new WorkerService(this);
			/*BeanContext.startWorker(workerType, this);
			ParkPatternExector.createWorkerTypeNode(workerType, nodevalue);*/
			
			String[] wkcfg = ConfigContext.getWorkerConfig();
			waitWorkingByService(wkcfg[0], Integer.parseInt(wkcfg[1]), workerType);
		//}catch(RemoteException e){
		//	System.out.println("waitWorking:"+e);
		//}
	}
	
	void waitWorkingByService(String host, int port, String workerType)//remove protected
	{
		//String[] wkcfg = ConfigContext.getWorkerConfig();
		//startWorker(wkcfg[0], Integer.parseInt(wkcfg[1]), sn, mwk);
		this.host = host;
		this.port = port;
		this.workerType = workerType;
		//BeanContext.startWorker(host, port, workerType, this);
		BeanContext.startWorker(host, port, workerType, this, this.getClass().equals(MigrantWorker.class));
		ParkPatternExector.createWorkerTypeNode(workerType, host+":"+port);
	}
	
	void waitWorkingByService(String parkhost, int parkport, String host, int port, String workerType)
	{
		this.host = host;
		this.port = port;
		this.workerType = workerType;
		BeanContext.startWorker(host, port, workerType, this, false);
		ParkPatternExector.createWorkerTypeNode(parkhost, parkport, workerType, host+":"+port);
	}
	/**
	 * 工人监听 workerType，进行任务等待
	 */
	void waitWorkingByPark(String workerType)
	{
		ObjectBean ob = ParkPatternExector.createWorkerTypeNode(workerType, "wk_pk");
		
		while(true)
		{
			ObjectBean lastestOb = ParkPatternExector.getLastestObjectBean(ob);
			WareHouse whouse = doTask((WareHouse)lastestOb.toObject());
			ob = ParkPatternExector.updateObjectBean(lastestOb, whouse);
		}
	}
	/**
	 * 获取该工人外其他所有相同类型的工人
	 */
	protected Workman[] getWorkerElse()
	{
		return getWorkerElse(this.workerType);
	}
	/**
	 * 获取集群中workerType类型除 该工人以外的其他工人
	 */
	protected Workman[] getWorkerElse(String workerType)
	{
		/*List<Workman> wklist = new ArrayList<Workman>();
		if(parallelPatternFlag!=1)
		{
			List<String[]> wslist = getWorkersService(host, port, workerType);
			for(String[] wsinfo:wslist)
				wklist.add(BeanContext.getWorkerElse(wsinfo[0], Integer.parseInt(wsinfo[1]), wsinfo[2]));
		}
		return wklist.toArray(new Workman[wklist.size()]);*/
		return getWorkers(host, port, workerType);
	}
	/**
	 * 获取其他某个相同类型的工人，index为在集群中的序号
	 */
	protected Workman getWorkerIndex(int index)
	{
		return getWorkerIndex(this.workerType, index);
	}
	/**
	 * 获取某个工人，workerType为工人类型，index为在集群中的序号
	 */
	protected Workman getWorkerIndex(String workerType, int index)
	{
		//if(parallelPatternFlag!=1)
		//{
			List<String[]> wslist = getWorkersService(workerType);
			if(index>=0&&index<wslist.size())
			{
				String[] wsinfo = wslist.get(index);
				return BeanContext.getWorkman(wsinfo[0], Integer.parseInt(wsinfo[1]), wsinfo[2]);
			}else return null;
		//}
		//return null;
	}
	/**
	 * 获取集群中所有工人，包括该工人自己
	 */
	protected Workman[] getWorkerAll()
	{
		return getWorkerAll(this.workerType);
	}
	/**
	 * 获取集群中所有类型为workerType的工人
	 */
	protected Workman[] getWorkerAll(String workerType)
	{
		return getWorkers(null, 0, workerType);
	}
	/**
	 * 获取集群中的某个工人，workerType为工人类型，host为ip，port为端口号
	 */
	private Workman[] getWorkers(String host, int port, String workerType)
	{
		//System.out.println(host+":"+port);
		List<Workman> wklist = new ArrayList<Workman>();
		if(parallelPatternFlag!=1)
		{
			List<String[]> wslist = getWorkersService(workerType);
			for(int i=0;i<wslist.size();i++)
			{
				String[] wsinfo = wslist.get(i);
				//System.out.println(wsinfo[0]+":"+wsinfo[1]);
				if(!wsinfo[0].equals(host)||Integer.parseInt(wsinfo[1])!=port)
					wklist.add(BeanContext.getWorkman(wsinfo[0], Integer.parseInt(wsinfo[1]), wsinfo[2]));
				else
					selfIndex = i;
			}
		}
		return wklist.toArray(new Workman[wklist.size()]);
	}	
	/**
	 * 获取自己在集群中的位置序号
	 */
	protected int getSelfIndex()
	{
		if(selfIndex==-1)
		{
			List<String[]> wslist = getWorkersService(workerType);
			for(int i=0;i<wslist.size();i++)
			{
				String[] wsinfo = wslist.get(i);
				if(wsinfo[0].equals(host)&&Integer.parseInt(wsinfo[1])==port)
					return i;
			}
		}
		return selfIndex;
	}
	/**
	 * 获取集群中的某个工人，workerType为工人类型，host为ip，port为端口号
	 */
	protected Workman getWorkerElse(String workerType, String host, int port)
	{
		if(this.host.equals(host)&&this.port==port)
			return null;
			
		List<String[]> wslist = getWorkersService(workerType);
		if(wslist!=null){
			for(int i=0;i<wslist.size();i++)
			{
				String[] wsinfo = wslist.get(i);
				//System.out.println(wsinfo[0]+":"+wsinfo[1]);
				
				if(wsinfo[0].equals(host)&&Integer.parseInt(wsinfo[1])==port)
					return BeanContext.getWorkman(wsinfo[0], Integer.parseInt(wsinfo[1]), wsinfo[2]);
			}
		}
		return null;
	}
	
	synchronized boolean receiveMaterials(WareHouse inhouse)
	{
		return receive(inhouse);
	}
	/**
	 * 接收来自其他工人发送的内容
	 */
	protected boolean receive(WareHouse inhouse)
	{
		return true;
	}
	
	protected WareHouse doTask(WareHouse inhouse)
	{
		WareHouse outhouse = new WareHouse();
		return outhouse;
	}
	
	protected boolean isInterrupted(){
		return _interrupted;
	}
	
	void interrupted(boolean _interrupted){
		this._interrupted = _interrupted;
	}
	
	public void setWorkerJar(String workerjarname){
		this.workerjarname = workerjarname;
	}
	
	public String getWorkerJar(){
		return workerjarname;
	}
	
	void setHost(String host){
		this.host = host;
	}
	
	String getHost(){
		return host;
	}
	
	void setWorkerType(String workerType){
		this.workerType = workerType;
	}
	
	String getWorkerType(){
		return workerType;
	}
	
	void setPort(int port){
		this.port = port;
	}
	
	int getPort(){
		return port;
	}
	
	void setSelfIndex(int selfIndex){
		this.selfIndex = selfIndex;
	}
	
	/*int getSelfIndex(){
		return selfIndex;
	}*/
	
	public static void main(String[] args){
		MigrantWorker mw = new MigrantWorker();
		//System.out.println(args.length);
		if(args.length==4)
			mw.setWorkerJar(args[3]);
		mw.waitWorking(args[0],Integer.parseInt(args[1]),args[2]);
	}
}