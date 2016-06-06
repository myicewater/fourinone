package com.fourinone;

import java.util.List;
import java.util.ArrayList;
/**
 * 并行服务类
 * @author 朱素海
 *
 */
abstract class ParallelService
{
	
	/**
	 * 在host:port 上处于等待工作状态,设置自己 工人类型为workerType
	 * @param host
	 * @param port
	 * @param workerType
	 */
	abstract public void waitWorking(String host, int port, String workerType);
	/**
	 * 等待工作状态，指定工人类型
	 * @param workerType 工人类型
	 */
	abstract public void waitWorking(String workerType);
	
	/*WorkerLocal[] getWorkersService(String workerType)
	{
		//LogUtil.fine("", "", "getWorkersService:"+workerType);
		List<ObjectBean> oblist = ParkPatternExector.getWorkerTypeList(workerType);
		List<WorkerLocal> wklist = new ArrayList<WorkerLocal>();
		for(ObjectBean ob:oblist)
		{
			String[] hostport = ((String)ob.toObject()).split(":");
			wklist.add(BeanContext.getWorkerLocal(hostport[0], Integer.parseInt(hostport[1]), workerType));
		}
		return wklist.toArray(new WorkerLocal[wklist.size()]);
	}
	
	Workman[] getWorkersService(String host, int port, String workerType)
	{
		//LogUtil.fine("", "", "getWorkersService:"+workerType);
		List<ObjectBean> oblist = ParkPatternExector.getWorkerTypeList(workerType);
		List<Workman> wklist = new ArrayList<Workman>();
		for(ObjectBean ob:oblist)
		{
			String[] hostport = ((String)ob.toObject()).split(":");
			if(!hostport[0].equals(host)&&!Integer.parseInt(hostport[1])!=port)
				wklist.add(BeanContext.getWorkman(hostport[0], Integer.parseInt(hostport[1]), workerType));
		}
		return wklist.toArray(new WorkerLocal[wklist.size()]);
	}*/
	/**
	 * 获取 parkhost:parkport 上类型为workerType的工人
	 * @param parkhost
	 * @param parkport
	 * @param workerType
	 * @return
	 */
	List<String[]> getWorkersServicePark(String parkhost, int parkport, String workerType)
	{
		List<ObjectBean> oblist = ParkPatternExector.getWorkerTypeList(parkhost, parkport, workerType);
		List<String[]> wslist = new ArrayList<String[]>();
		for(ObjectBean ob:oblist)
		{
			String[] hostport = ((String)ob.toObject()).split(":");
			if(!hostport[0].equals(null)||Integer.parseInt(hostport[1])!=0)
				wslist.add(new String[]{hostport[0], hostport[1], workerType});
			
		}
		return wslist;
	}
	/**
	 * 根据配置文件获取 协同服务上的工人，并且不在 host：port 协同服务上
	 * 获取 host：port服务器上 类型为workerType的工人
	 * @param host
	 * @param port
	 * @param workerType
	 * @return
	 */
	List<String[]> getWorkersService(String host, int port, String workerType)
	{
		//LogUtil.fine("", "", "getWorkersService:"+workerType);
		List<ObjectBean> oblist = ParkPatternExector.getWorkerTypeList(workerType);//getWorkerTypeList(String host, int port, String workerType)
		List<String[]> wslist = new ArrayList<String[]>();
		for(ObjectBean ob:oblist)
		{
			//System.out.println("ob.toObject():"+ob.toObject());
			String[] hostport = ((String)ob.toObject()).split(":");
			if(!hostport[0].equals(host)||Integer.parseInt(hostport[1])!=port)//&&
				wslist.add(new String[]{hostport[0], hostport[1], workerType});
			
		}
		return wslist;
	}
	/**
	 * 
	 * @param workerType
	 * @return
	 */
	List<String[]> getWorkersService(String workerType)
	{
		return getWorkersService(null,0,workerType);
	}
}