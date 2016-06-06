package com.fourinone;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.Arrays;
import java.util.ArrayList;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
/**
 * 协同 领导
 * @author 朱素海
 *
 */
public class ParkLeader{
	/**
	 * 是否是master（领导、主）
	 */
	boolean ismaster = false;
	/**
	 * 总是尝试成为master
	 */
	boolean alwaystry = false;
	/**
	 * master 的服务名称
	 */
	private String parkservicecfg = "ParkService";
	/**
	 * 标志该master的 host:port
	 */
	private String[] thisserver; //thishost,thisport;cur host for service and cur leader for proxy
	/**
	 * 职介者组
	 */
	String[][] groupserver = new String[][]{{"localhost","1888"},{"localhost","1889"},{"localhost","1890"}};
	private LinkedBlockingQueue<String> bq = new LinkedBlockingQueue<String>();
	private AsyncExector rpl=null;
	
	ParkLeader(String host, int port, String parkservicecfg){
		this.parkservicecfg = parkservicecfg;
		thisserver = new String[]{host, ""+port};
		this.alwaystry = Boolean.valueOf(ConfigContext.getConfig("PARK","ALWAYSTRYLEADER",null,"false"));
	}
	/**
	 * 创建 职介者服务master（领导、主）
	 * @param host master的host
	 * @param port master的port
	 * @param groupserver 职介者群组
	 * @param parkservicecfg master的服务名称 "ParkService"
	 */
	ParkLeader(String host, int port, String[][] groupserver, String parkservicecfg){
		this(host,port,parkservicecfg);
		this.groupserver = groupserver;
	}
	
	//ParkLeader(String host, int port, String[][] groupserver)
	
	/*getMasterPark(){catch remoteexception and try next until get one};
	protected Park getMasterPark(){
		Park pk = null;
		try{
			pk = (Park)BeanService.getBean(thisserver[0],Integer.parseInt(thisserver[1]),"ParkService");
			if(pk!=null){
				if(pk.askLeader())
					return pk;
			}
		}catch(RemoteException re){
			re.printStackTrace();
			if(re instanceof ConnectException){
				pk = getNextMaster();
			}
		}
		catch(LeaderException le){
			le.printStackTrace();
			String[] ls = le.getLeaderServer();
			System.out.println(ls.getLeaderServer()[0]);
			thisserver = ls;
			pk = getMasterPark();
		}
		return pk;
	}*/
	/**
	 * 返回 领导协同服务
	 * @return
	 */
	protected Park getLeaderPark()
	{
		LogUtil.info("", "", "getLeaderPark...................");
		int index = getLeaderIndex(thisserver);
		return electionLeader(-1,index);
	}

	protected Park getNextLeader()
	{
		LogUtil.info("", "", "getNextLeader...................");
		int index = getLeaderIndex(thisserver);
		return electionLeader(index, index+1);
	}
	
	private int getLeaderIndex(String[] sa){
		int i=0;
		for(;i<groupserver.length;i++)
			if(Arrays.equals(sa,groupserver[i]))
				break;
		return i;
	}
	
	protected Park electionLeader(int b, int i){
		Park pk = null;
		boolean thesarrok = true;
		i=i<groupserver.length?i:0;
		//b=b<0?groupserver.length-1:b;
		String[] sarr = groupserver[i];
		try{
			pk = (Park)BeanService.getBean(sarr[0],Integer.parseInt(sarr[1]),parkservicecfg);
			if(pk!=null)
				pk.askLeader();
		}catch(RemoteException re){
			LogUtil.info("electionLeader", "("+sarr[0]+":"+sarr[1]+"):", re.getMessage());
			thesarrok = false;
			if(re instanceof ConnectException){
				if(b!=i)//one cycle
				{
					b=!alwaystry&&b<0?i:b;
					pk = electionLeader(b,i+1);
				}
			}
		}catch(LeaderException le){
			//le.printStackTrace();
			LogUtil.info("[electionLeader]", "[LeaderException]", le.getMessage());
			thesarrok = false;
			String[] ls = le.getLeaderServer();
			int leaderindex = getLeaderIndex(ls);
			pk = electionLeader(-1,leaderindex);
		}
		if(thesarrok)
		{
			thisserver = sarr;
			LogUtil.info("", "", "leader server is("+thisserver[0]+":"+thisserver[1]+")");
		}
		return pk;
	}
	
	protected Park electionLeader(int i){
		Park pk = null;
		boolean thesarrok = true;
		i=i<groupserver.length?i:0;
		String[] sarr = groupserver[i];
		try{
			pk = (Park)BeanService.getBean(sarr[0],Integer.parseInt(sarr[1]),parkservicecfg);
			if(pk!=null)
				pk.askLeader();
		}catch(RemoteException re){
			LogUtil.info("electionLeader", "("+sarr[0]+":"+sarr[1]+"):", re.getMessage());
			thesarrok = false;
			if(re instanceof ConnectException){
				pk = electionLeader(i+1);
			}
		}catch(LeaderException le){
			//le.printStackTrace();
			LogUtil.info("electionLeader", "LeaderException", le);
			thesarrok = false;
			String[] ls = le.getLeaderServer();
			int leaderindex = getLeaderIndex(ls);
			pk = electionLeader(leaderindex);
		}
		if(thesarrok)
		{
			thisserver = sarr;
			LogUtil.info("", "", "leader server is("+thisserver[0]+":"+thisserver[1]+")");
		}
		return pk;
	}
	
	/*
	protected Park getNextMaster(){//boolean includethisserver
		Park pk = null;
		for(int i=0;i<groupserver.length;i++)
		{
			String[] sarr = groupserver[i];
			if(Arrays.equals(thisserver,sarr))
			{
				sarr = (i+1)<groupserver.length?groupserver[i+1]:groupserver[0];
				pk = (Park)BeanService.getBean(sarr[0],Integer.parseInt(sarr[1]),"ParkService");
				thisserver=sarr;
				break;
			}
		}
		return pk;
	}*/
	
	/**
	 * 获取职介者集群中其他 职介者
	 * @return
	 */
	protected Park[] getOtherPark(){
		ArrayList<Park> pklist = new ArrayList<Park>();
		for(String[] sarr:groupserver)
		{
			if(!Arrays.equals(thisserver,sarr))
			{
				try{
					Park pk = (Park)BeanService.getBean(sarr[0],Integer.parseInt(sarr[1]),parkservicecfg);//try catch cant null
					pklist.add(pk);
				}catch(RemoteException re){
					LogUtil.fine("getOtherPark", "("+sarr[0]+":"+sarr[1]+"):", re.getMessage());
					//re.printStackTrace();
				}
			}
		}
		return pklist.toArray(new Park[pklist.size()]);
	}
	
	/**
	 * 检查是否是master，如果有（是）master或者没有master，则设为master，返回true
	 * @param sv
	 * @param pk
	 * @return 
	 */
	protected boolean checkMasterPark(String[] sv, Park pk){
		if(ismaster||getOtherMasterPark(sv)==null){//cant ismaster for double conflict in net break
			copyArray(thisserver, sv);
			setMaster(true,pk);
			return true;
		}else return false;
		/*if(ismaster){
			sv = thisserver;
			return true;
		}
		else{
			if(getOtherMasterPark(sv)==null){
				sv = thisserver;
				return true;
			}
			else
				return false;
		}*/
	}
	/**
	 * 设置成为master
	 * <br>
	 * 逻辑:首先看其他职介者中有没有master,<br>
	 * 如果没有master, <br>
	 * 	&nbsp&nbsp&nbsp&nbsp如果有其他 职介者,则把任意一个信息赋给 pk
	 *  把pk设成master
	 * 如果存在master,则把其他 职介者中master 的信息拷贝到pk 
	 * 
	 * @param pk
	 */
	protected void wantBeMaster(Park pk){
		LogUtil.info("", "", "wantBeMaster.............................");	
		String[] sv = new String[2];
		Park othermaster = getOtherMasterPark(sv);
		if(othermaster==null){//没有找到其他 职介者中的master
			LogUtil.info("", "", "get one of other parks for init parkInfo.........");
			Park[] pks = getOtherPark();
			if(pks.length>0)
				setInitParkInfo(pks[0], pk);//将其他职介者中的一个 的信息 赋给 pk
			setMaster(true,pk);
		}
		else{
			LogUtil.info("", "", "wantBeMaster,master is ("+sv[0]+":"+sv[1]+")");
			setInitParkInfo(othermaster, pk);
		}
	}
	
	/**
	 * 把toPk 的职介者信息设置成fromPk 职介者的信息
	 * @param fromPk
	 * @param toPk
	 */
	private void setInitParkInfo(Park fromPk, Park toPk)
	{
		try{
			toPk.setParkinfo(fromPk.getParkinfo());
		}catch(Exception re){
			//re.printStackTrace();
			LogUtil.info("[ParkLeader]", "[setInitParkInfo]", re);
		}
	}
	/**
	 * 设置成为master（领导）
	 * @param ismaster
	 * @param pk
	 */
	private void setMaster(boolean ismaster, Park pk){
		this.ismaster=ismaster;
		LogUtil.info("", "", "setMaster("+thisserver[0]+":"+thisserver[1]+"):"+ismaster);
		if(this.ismaster)
			HbDaemo.runClearTask((ParkService)pk);
	}
	
	/**
	 * 判断是否是 master,<br>
	 * 
	 * @return 是则返回thisserver，不是返回null
	 */
	protected String[] isMaster(){
		return ismaster?thisserver:null;
	}
	/**
	 * 获取其他职介者中的master
	 * @param sv
	 * @return 有则返回Park，没有返回null,找到其他 职介者服务中的 master 则把master的server信息拷贝到 sv
	 */
	protected Park getOtherMasterPark(String[] sv){
		Park pkmaster = null;
		try{
			Park[] pks = getOtherPark();
			for(Park pk:pks){
				String[] ask = pk.askMaster();
				if(ask!=null)//找到了其他的master
				{
					pkmaster = pk;
					//sv=ask;
					copyArray(ask, sv);
					//System.out.println("getOtherMasterPark, ask is("+ask[0]+":"+ask[1]+")");
					//System.out.println("getOtherMasterPark, sv is("+sv[0]+":"+sv[1]+")");
				}
			}
		}catch(Exception re){
			//re.printStackTrace();
			LogUtil.info("getOtherMasterPark", "exception", re);
		}
		return pkmaster;
		
		/*
		if(masterserver==null)
		{
			
			if(masterserver==null)
				masterserver=thisserver;
		}
		return masterserver;
		*/
	}
	/**
	 * 
	 * @param domainnodekey
	 * @param pk
	 */
	protected void runCopyTask(String domainnodekey, final Park pk){
		//put key into queue
		//laze run thread
		LogUtil.fine("", "", "runCopyTask:"+domainnodekey+"............................");
		
		try{
			bq.put(domainnodekey);
		}catch(InterruptedException ie){
			ie.printStackTrace();
		}
		
		//static{}
		if(rpl==null){
			LogUtil.fine("", "", "runCopyTask AsyncExector:");
			(rpl = new AsyncExector(){
				public void task(){
					try{
						while(true){
							String curkey = (String)bq.take();
							LogUtil.fine("", "", "runCopyTask bq.size():"+bq.size());
							if(bq.size()==0){
								LogUtil.fine("", "", "curkey:"+curkey);
								//Thread.sleep(1000);
								copyParkinfo(pk.getParkinfo());
							}
						}
					}catch(Exception e){
						//e.printStackTrace();
						LogUtil.info("runCopyTask", "exception", e);
					}
				}
			}).run();
		}
	}
	
	private Boolean[] copyParkinfo(ObjValue pov){
		ArrayList<Boolean> sendlist = new ArrayList<Boolean>();
		try{
			Park[] pks = getOtherPark();
			for(Park pk:pks)
				sendlist.add(pk.setParkinfo(pov));
		}catch(Exception re){
			LogUtil.info("copyParkinfo", "exception", re);
		}
		return sendlist.toArray(new Boolean[sendlist.size()]);
	}
	
	/**
	 * 把fromArr 拷贝到 toArr
	 * @param fromArr
	 * @param toArr
	 */
	private void copyArray(String[] fromArr, String[] toArr)
	{
		for(int i=0;i<toArr.length;i++)
			toArr[i]=fromArr[i];
	}
	
	/**
	 * 获取当前职介者的 host:port
	 * @return
	 */
	public String[] getThisserver()
	{
		return thisserver;
	}

	public static void main(String[] args){
		ParkLeader pl = new ParkLeader("localhost",1888,"ParkService");
		String[] sv = new String[2];
		System.out.println(pl.getOtherMasterPark(sv));
		System.out.println(sv[1]);
	}
}