package com.fourinone;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Remote;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
/**
 * 节点服务<br>
 * 所谓的节点都是rmi远程调用
 * <br>
 * rmi操作
 * 
 * @author 朱素海
 *
 */
class BeanService extends PoolExector
{	

	/**
	 * 集群添加节点  
	 * 
	 * park调用实例 putBean(host,false,port,sn,i)
	 * 
	 * 创建远程rmi注册服务
	 * 
	 * @param TPYFWYM host
	 * @param TPYRZDY 
	 * @param TPYDK port
	 * @param rmname 服务名称
	 * @param paobj 
	 */
	final static void putBean(String TPYFWYM, boolean TPYRZDY, int TPYDK, String rmname, ParkActive paobj)
	{
		if(TPYFWYM!=null)
			System.setProperty(ConfigContext.getYMMZ(), TPYFWYM);//设置远程服务名称java.rmi.server.hostname
		if(TPYRZDY)
			System.setProperty(ConfigContext.getRZDY(),"true");//设置java.rmi.server.logCalls
		try{
			LocateRegistry.getRegistry(TPYDK).list();//返回本地主机在指定 port 上对远程对象 Registry 的引用。 返回在此注册表中绑定的名称的数组
		}catch(Exception ex){
			try{
				UnicastRemoteObject.exportObject(paobj, 0);//使用指定的端口导出远程对象，以便能够接收传入的调用
				Registry rgsty = LocateRegistry.createRegistry(TPYDK);//getRegistry(TPYDK);创建并导出接受指定 port 请求的本地主机上的 Registry 实例。 
				rgsty.rebind(rmname,paobj);//用提供的远程引用替换此注册表中指定的 name 绑定
			}
			catch(Exception e){
				LogUtil.info("[ObjectService]", "[regObject]", "[Error Exception:]", e);
			}
		}
	}
	
	final static void putBean(String TPYFWYM, boolean TPYRZDY, int TPYDK, String rmname, ParkActive paobj, String cb, String pl, SecurityManager sm){
		setSecurity(cb, pl, sm);
		putBean(TPYFWYM, TPYRZDY, TPYDK, rmname, paobj);
		/*if(TPYFWYM!=null)
			System.setProperty(ConfigContext.getYMMZ(), TPYFWYM);
		if(TPYRZDY)
			System.setProperty(ConfigContext.getRZDY(),"true");
		try{
			LocateRegistry.getRegistry(TPYDK).list();
		}catch(Exception ex){
			try{
				//UnicastRemoteObject.exportObject(paobj, 0);
				Registry rgsty = LocateRegistry.createRegistry(TPYDK);//getRegistry(TPYDK);
				rgsty.rebind(rmname,paobj);
			}
			catch(Exception e){
				LogUtil.info("[BeanService]", "[regObject]", "[Error Exception:]", e);
			}
		}*/
	}
	/**
	 * 获取Bean
	 * <br>
	 * 获取远程绑定
	 * @param TPYYM host
	 * @param TPYDK port
	 * @param rmname 绑定名称
	 * @return
	 * @throws RemoteException
	 */
	final static ParkActive getBean(String TPYYM,  int TPYDK, String rmname) throws RemoteException
	{
		try{
			if(ConfigContext.getTMOT()>0l)
				System.setProperty(ConfigContext.getQSXYSJ(), ConfigContext.getTMOT()+"");
			return (ParkActive)Naming.lookup(ConfigContext.getProtocolInfo(TPYYM,TPYDK,rmname));
		}catch(Exception e){
			//LogUtil.info("[BeanService]", "[getBean]", "getBean:"+e.getClass());
			if(e instanceof RemoteException)
				throw (RemoteException)e;
			else{
				//e.printStackTrace();
				LogUtil.info("[ObjectService]", "[getBean]", "[Error Exception:]", e);
			}
		}
		return null;
	}
	/**
	 * 设置安全管理
	 * @param cb codebase
	 * @param pl policy
	 * @param sm 安全管理类
	 */
	private static void setSecurity(String cb, String pl, SecurityManager sm){
		if(cb!=null&&pl!=null){
			System.setProperty(ConfigContext.getDMY(),cb);
			System.setProperty(ConfigContext.getAQCL(),pl);
		 	if(System.getSecurityManager()==null&&sm!=null){
		    	System.setSecurityManager(sm);//ParkManager()
		   	}
		}
	}
}