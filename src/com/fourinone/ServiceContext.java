package com.fourinone;

import java.rmi.RemoteException;
/**
 * 集群上下文服务
 * @author 朱素海
 *
 */
class ServiceContext extends BeanService
{
	/**
	 * 启动host:port 上服务
	 * <br>
	 * 创建rmi
	 * @param host
	 * @param port 
	 * @param sn 职介者服务名称
	 * @param i
	 */
	static <I extends ParkActive> void startService(String host, int port, String sn, I i)
	{
		try{
			putBean(host,false,port,sn,i);
		}catch(Exception e){
			LogUtil.info("[ObjectService]", "[startService]", e);
		}
	}
	/**
	 * 启动服务
	 * @param host
	 * @param port
	 * @param sn
	 * @param i
	 * @param cb
	 * @param pl
	 */
	static <I extends ParkActive> void startService(String host, int port, String sn, I i, String cb, String pl)//
	{
		try{
			putBean(host,false,port,sn,i,cb,pl,new ParkManager());
			//pm.checkPermission(new ParkPermission("park","all"));
		}catch(Exception e){
			LogUtil.info("[ObjectService]", "[startService]", e);
		}
	}
	
	static <I extends ParkActive> I getService(Class<I> a, String host, int port, String sn){
		I i=null;
		try{
			i=(I)getBean(host,port,sn);
		}catch(RemoteException e){
			LogUtil.info("[ObjectService]", "[getService]", e);
		}
		return i;
	}
}