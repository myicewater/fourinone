package com.fourinone;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.List;
/**
 * 协同服务（职介所）执行器
 * @author 朱素海
 *
 */
public class ParkPatternExector
{
	static boolean newParkFlag=false;
	private static ParkLocal pl;
	private static LinkedBlockingQueue<ParkPatternBean> bq = new LinkedBlockingQueue<ParkPatternBean>();
	private static AsyncExector aeLastest=null;
	
	/**
	 * 根据配置文件获取协同服务（职介所）
	 * @return
	 */
	static ParkLocal getParkLocal()
	{
		if(pl==null)
		{
			/*String[][] servers = {{"localhost","1888"},{"localhost","1889"}};//get from config.xml
			pl = BeanContext.getPark(servers[0][0], Integer.parseInt(servers[0][1]), servers);*/
			pl = BeanContext.getPark();
		}
		return pl;
	}
	
	/**
	 * 获取 指定 host:port 服务器上的协同服务（职介所）
	 * @param host
	 * @param port
	 * @return
	 */
	static ParkLocal getParkLocal(String host, int port){
		if(newParkFlag)
			return BeanContext.getPark(host, port);//new pl everytime 14.10.16
		else
			return pl=pl==null?BeanContext.getPark(host, port):pl;
	}
	
	//static void resetParkLocal(){pl=null;}//14.10.17
	/**
	 * 根据配置文件获取协同服务上类型为workerType的工人
	 * @param workerType
	 * @return
	 */
	static List<ObjectBean> getWorkerTypeList(String workerType)
	{
		return getParkLocal().get("_worker_"+workerType);
	}
	/**
	 * 获取 parkhost:parkport 协同服务上上类型为workerType 的 工人
	 * @param parkhost
	 * @param parkport
	 * @param workerType
	 * @return
	 */
	static List<ObjectBean> getWorkerTypeList(String parkhost, int parkport, String workerType)
	{
		return getParkLocal(parkhost, parkport).get("_worker_"+workerType);
	}
	
	/**
	 * 创建节点
	 * @param workerType
	 * @param nodevalue
	 * @return
	 */
	static ObjectBean createWorkerTypeNode(String workerType, String nodevalue)
	{
		return getParkLocal().create("_worker_"+workerType, ParkGroup.getKeyId(), nodevalue, AuthPolicy.OP_ALL, true);
	}
	/**
	 * 创建类型为workerType 的节点
	 * @param parkhost
	 * @param parkport
	 * @param workerType
	 * @param nodevalue
	 * @return
	 */
	static ObjectBean createWorkerTypeNode(String parkhost, int parkport, String workerType, String nodevalue)
	{
		return getParkLocal(parkhost, parkport).create("_worker_"+workerType, ParkGroup.getKeyId(), nodevalue, AuthPolicy.OP_ALL, true);
	}
	
	static ObjectBean getLastestObjectBean(ObjectBean ob)
	{
		String[] keyarr = ParkObjValue.getDomainNode(ob.getName());
		while(true)
		{
			ObjectBean curob = getParkLocal().getLastest(keyarr[0], keyarr[1], ob);
			if(curob!=null)
				return curob;
		}
			
	}
	
	static ObjectBean updateObjectBean(ObjectBean ob, WareHouse wh)
	{
		String[] keyarr = ParkObjValue.getDomainNode(ob.getName());
		return getParkLocal().update(keyarr[0], keyarr[1], wh);
	}
	
	static void append(ParkPatternBean ppb)
	{
		try{
			ObjectBean ob = getParkLocal().update(ppb.domain, ppb.node, ppb.inhouse);
			ppb.thisversion = ob;
			bq.put(ppb);
			
			if(aeLastest==null){
				LogUtil.fine("", "", "AsyncExector aeLastest:");
				(aeLastest = new AsyncExector(){
					public void task(){
						try{
							while(true){
								ParkPatternBean curPpb = bq.take();
								//System.out.println("ParkPatternExector bq.size():"+bq.size());
								ObjectBean curversion = getParkLocal().getLastest(curPpb.domain, curPpb.node, curPpb.thisversion);
								if(curversion!=null)
								{
									curPpb.thisversion = curversion;
									curPpb.rx.setRecall(false);
									curPpb.outhouse.putAll((WareHouse)curversion.toObject());
									//curPpb.outhouse.setReady(true);
									curPpb.outhouse.setReady(FileResult.READY);
								}
								else
									bq.put(curPpb);
							}
						}catch(Exception e){
							LogUtil.info("AsyncExector", "append aeLastest", e);
							//e.printStackTrace();
						}
					}
				}).run();
			}
		}catch(Exception e){
			LogUtil.info("ParkPatternExector", "append", e);
		}
	}
}