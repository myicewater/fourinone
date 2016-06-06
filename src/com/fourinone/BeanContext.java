package com.fourinone;

import java.rmi.RemoteException;
import java.rmi.Remote;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.io.File;
/**
 * ��Ⱥ������-��������
 * @author ���غ�
 *
 */
public class BeanContext extends ServiceContext
{
	public static void setConfigFile(String configFile){
		ConfigContext.configFile = configFile;
	}
	
	//get all config msg
	/**
	 * ���ص�ǰ��ְ����
	 * 
	 * 
	 * 
	 * @param host
	 * @param port
	 * @param sn
	 * @param servers
	 * @return
	 */
	public static ParkLocal getPark(String host, int port, String sn, String[][] servers){
		return DelegateConsole.bind(ParkLocal.class, new ParkProxy(host, port, servers, sn));
	}
	/**
	 * ����ְ������Э������
	 * @param host
	 * @param port
	 * @param servers
	 * @return
	 */
	public static ParkLocal getPark(String host, int port, String[][] servers){
		return getPark(host, port, ConfigContext.getParkService(), servers);
	}
	/**
	 * ���������ļ���ȡЭͬ����ְ���ߣ�
	 * @return
	 */
	public static ParkLocal getPark(){
		String[][] parkcfg = ConfigContext.getParkConfig();
		return getPark(parkcfg[0][0], Integer.parseInt(parkcfg[0][1]), parkcfg);//input serverconfiglist string[][]
	}
	
	
	/**
	 * ��ȡ ָ�� host:port �������ϵ�Эͬ����ְ������c
	 * @param host
	 * @param port
	 * @return
	 */
	public static ParkLocal getPark(String host, int port){
		String[][] parkcfg = ConfigContext.getParkConfig();
		parkcfg[0][0]=host!=null?host:parkcfg[0][0];
		parkcfg[0][1]=port!=0?(port+""):parkcfg[0][1];
		return getPark(host, port, parkcfg);
	}
	
	/**
	 * ����ְ��������Э������<br>
	 * rmi Զ�̵���<br>
	 * ��������,����InetServer
	 * <br>
	 * httpserver
	 * @param host localhost:1888,localhost:1889
	 * @param port
	 * @param sn servername: ParkService
	 * @param servers ���host:port
	 */
	public static void startPark(String host, int port, String sn, String[][] servers)
	{
		try{
			//DelegateConsole.bind(Park.class, new ParkService(host, port, servers, sn));
			startService(host, port, sn, new ParkService(host, port, servers, sn));
			//BeanService.putBean(host, true, port, sn, new ParkService(host, port, servers, sn));//input serverconfiglist string[][]
			if(Boolean.valueOf(ConfigContext.getConfig("PARK","STARTWEBAPP",null,"false")))
				startInetServer();
		}catch(RemoteException e){//new ParkService throw
			LogUtil.info("[BeanContext]", "[startPark]", e);
			//e.printStackTrace();
		}
	}
	/**
	 * 
	 * ��ȡְ���߷�������<br>
	 * �� host:port ������ְ���߷���
	 * 
	 * 
	 * @param host
	 * @param port
	 * @param servers
	 */
	public static void startPark(String host, int port, String[][] servers)
	{
		startPark(host, port, ConfigContext.getParkService(), servers);
	}
	
	public static void startPark(String host, int port){
		String[][] parkcfg = ConfigContext.getParkConfig();
		parkcfg[0][0]=host!=null?host:parkcfg[0][0];
		parkcfg[0][1]=port!=0?(port+""):parkcfg[0][1];
		startPark(host, port, parkcfg);
	}
	/**
	 * 
	 * ����Ĭ������<SERVERS>localhost:1888,localhost:1889</SERVERS>
	 * <br>
	 * ����ְ��������
	 * 
	 */
	public static void startPark()//String configfile
	{
		String[][] parkcfg = ConfigContext.getParkConfig();
		startPark(parkcfg[0][0], Integer.parseInt(parkcfg[0][1]), parkcfg);
	}
	/**
	 * ��ȡInet������Ϣ localhost:9080
	 * <br>
	 * ����web app ����
	 * 
	 */
	static void startInetServer()
	{
		String[] inetcfg = ConfigContext.getInetConfig();
		ParkInetServer.start(inetcfg[0], Integer.parseInt(inetcfg[1]), 0);
	}
	
	static void startWorker(String host, int port, String sn, MigrantWorker mwk)
	{
		/*try{
			//System.setProperty("responseTimeout", 1000+"");
			startService(host, port, sn, new WorkerService(mwk));
		}catch(RemoteException e){
			LogUtil.info("[BeanContext]", "[startWorker]", e);
		}*/
		startWorker(host, port, sn, mwk, false);
	}
	
	static void startWorker(String sn, MigrantWorker mwk)
	{
		String[] wkcfg = ConfigContext.getWorkerConfig();
		startWorker(wkcfg[0], Integer.parseInt(wkcfg[1]), sn, mwk);
	}
	
	static void startWorker(String host, int port, String sn, MigrantWorker mwk, boolean issup)
	{
		try{
			ParkActive pa = DelegateConsole.bind(Worker.class, new WorkerService(mwk));//Worker.class
			if(issup)
				startService(host, port, sn, pa, ConfigContext.getInetStrConfig(mwk.getWorkerJar()), ConfigContext.getPolicyConfig());
			else
				startService(host, port, sn, pa);
		}catch(RemoteException e){
			LogUtil.info("[BeanContext]", "[startWorker]", e);
		}
	}
	
	static void startFttpWorker(String host, int port, String sn, MigrantWorker mwk)
	{
		try{
			ParkActive pa = (ParkActive)DelegateConsole.bind(new Class[]{Worker.class, FttpWorker.class}, new FttpWorkerService(mwk));//Worker.class
			//ConfigContext.getInetStrConfig(mwk.getWorkerJar())
			startService(host, port, sn, pa);
		}catch(RemoteException e){
			LogUtil.info("[BeanContext]", "[startFttpWorker]", e);
		}
	}
	
	/**
	 * ��ȡfttp���ã�����fttp�ļ�����
	 */
	public static void startFttpServer(){//root
		String[] fttpcfg = ConfigContext.getFttpConfig();
		startFttpServer(fttpcfg[0], Integer.parseInt(fttpcfg[1]));
	}
	
	public static void startFttpServer(String host){
		startFttpServer(host, FttpMigrantWorker.FTTPPORT);
	}
	
	/**
	 * ����fttp����
	 * @param host
	 * @param port
	 */
	public static void startFttpServer(String host, int port){
		new FttpMigrantWorker().waitWorking(host,port,FttpMigrantWorker.FTTPSN);
	}
	
	static Worker getWorker(String host, int port, String sn){
		return getService(Worker.class, host, port, sn);
	}
	
	static WorkerLocal getWorkerLocal(String host, int port, String sn){
		return (WorkerLocal)DelegateConsole.bind(new Class[]{WorkerLocal.class,CtorLocal.class}, new WorkerServiceProxy(host, port, sn));
	}
	
	static WorkerLocal getWorkerLocal(String domainnodekey){
		return DelegateConsole.bind(WorkerLocal.class, new WorkerParkProxy(domainnodekey));
	}
	
	static WorkerLocal getWorkerLocal(){
		return (WorkerLocal)DelegateConsole.bind(new Class[]{WorkerLocal.class}, new WorkerLocalProxy());
	}
	
	static WorkerLocal getFttpLocal(String host, int port, String sn){
		return (WorkerLocal)DelegateConsole.bind(new Class[]{WorkerLocal.class, CtorLocal.class, FttpLocal.class}, new FttpWorkerProxy(host, port, sn));
	}
	
	static Workman getWorkman(String host, int port, String sn){
		return DelegateConsole.bind(Workman.class, new WorkerServiceProxy(host, port, sn));
	}
	
	/*public static <I extends Remote> void start(String host, int port, String sn, I i)
	{
		try{
			BeanService.putBean(host,true,port,sn,i);
		}catch(RemoteException e){
			e.printStackTrace();
		}
	}
	
	public static <I extends Remote> I get(Class<I> a, String host, int port, String sn){
		I i=null;
		try{
			i=(I)BeanService.getBean(host,port,sn);
		}catch(RemoteException e){
			System.out.println(e);
		}
		return i;
	}*/
	
	public static void startCacheFacade(String host, int port)
	{
		try{
			startService(host, port, ConfigContext.getCacheFacadeService(), DelegateConsole.bind(Cache.class, new CacheFacade(ConfigContext.getCacheService(),ConfigContext.getCacheGroupConfig())));
		}catch(RemoteException e){
			LogUtil.info("[BeanContext]", "[startCacheFacade]", e);
			//e.printStackTrace();
		}
		/*try{
			BeanService.putBean(host,true,port,"CacheService",new CacheFacade());//input Groups
		}catch(RemoteException e){
			e.printStackTrace();
		}*/
	}
	
	public static void startCacheFacade()
	{
		String[] cachecfg = ConfigContext.getCacheFacadeConfig();
		startCacheFacade(cachecfg[0], Integer.parseInt(cachecfg[1]));
	}
	
	public static Cache getCacheFacade(String host, int port)
	{
		return getService(Cache.class, host, port, ConfigContext.getCacheFacadeService());
	}
	
	//<T> Class BeanContextBase
	public static Cache getCacheFacade(){//loadbalance server host and port
		String[] cachecfg = ConfigContext.getCacheFacadeConfig();
		return getCacheFacade(cachecfg[0], Integer.parseInt(cachecfg[1]));
		//return DelegateConsole.bind(Cache.class, new CacheFacade());
		/*
		Cache ca=null;
		try{
			ca=(Cache)BeanService.getBean(host,port,"CacheService");
		}catch(RemoteException e){
			//e.printStackTrace();
			System.out.println(e);
		}
		return ca;
		*/
	}
	
	public static CacheLocal getCache(){
		String[] cachecfg = ConfigContext.getCacheFacadeConfig();
		return getCache(cachecfg[0], Integer.parseInt(cachecfg[1]));
	}
	
	public static CacheLocal getCache(String host, int port){
		return DelegateConsole.bind(CacheLocal.class, new CacheProxy(host, port));
		//return DelegateHandle.bind(CacheLocal.class, CacheProxy.class);
	}
	/**
	 * �����������
	 * @param host
	 * @param port
	 * @param servers
	 */
	public static void startCache(String host, int port, String[][] servers)
	{
		startPark(host, port, ConfigContext.getCacheService(), servers);
	}
	
	public static void startCache()
	{
		String[][] cachecfg = ConfigContext.getCacheConfig();
		startCache(cachecfg[0][0], Integer.parseInt(cachecfg[0][1]), cachecfg);
	}
	
	public static void exit(){
		close();
	}
	
	public static int start(String... params){
		return start(null, null, params);
	}
	
	public static int start(FileAdapter fa, String... params){
		return start(null, fa, params);
	}
	
	public static int start(Map env, String... params){
		return start(env, null, params);
	}
	
	/**
	 * ��������������
	 * @param env
	 * @param fa
	 * @param params
	 * @return
	 */
	public static int start(Map env, FileAdapter fa, String... params){
		ProcessBuilder pb = new ProcessBuilder(params);
		pb.redirectErrorStream(true);
		int exitcode = -1;
		if(env!=null)
			pb.environment().putAll(env);
		if(fa!=null)
			pb.directory(fa);
		try{
			Process p = pb.start();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line="";
			while((line=stdout.readLine())!=null){System.out.println(line);}
			stdout.close();
			//System.out.println("waitFor:"+p.waitFor());
			//System.out.println("exitValue:"+p.exitValue());
			exitcode = p.waitFor();
		}catch(Exception ex){
			LogUtil.info("[BeanContext]", "[start]", ex);
		}
		return exitcode;
	}
	/*
	public static Result<Integer> tryStart(String... params){
		return tryStart(null, params);
	}
	
	public static Result<Integer> tryStart(final FileAdapter fa, final String... params){
		final Result<Integer> fr = new Result<Integer>(false);
		tpe().execute(new Runnable(){
			public void run(){
				try{
					int r = start(fa, params);
					fr.setResult(new Integer(r));
					fr.setReady(FileResult.READY);
				}catch(Throwable e){
					LogUtil.info("[BeanContext]", "[tryStart]", e);
					fr.setReady(FileResult.EXCEPTION);
				}
			}
		});
		return fr;
	}
	*/
	public static StartResult<Integer> tryStart(String... params){
		return tryStart(null, null, params);
	}
	
	public static StartResult<Integer> tryStart(FileAdapter fa, String... params){
		return tryStart(null, fa, params);
	}
	
	public static StartResult<Integer> tryStart(Map env, String... params){
		return tryStart(env, null, params);
	}
	
	public static StartResult<Integer> tryStart(Map env, FileAdapter fa, String... params){
		ProcessBuilder pb = new ProcessBuilder(params);
		pb.redirectErrorStream(true);
		if(env!=null)
			pb.environment().putAll(env);
		if(fa!=null)
			pb.directory(fa);
		try{
			return new StartResult(pb.start(), false);
		}catch(Exception ex){
			LogUtil.info("[BeanContext]", "[startup]", ex);
		}
		return null;
	}

	public static void killOnExit(StartResult<Integer>[] srarr){
		killOnExit(srarr, "application will exit...");
	}
	
	public static void killOnExit(final StartResult<Integer>[] srarr, final String info){
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				LogUtil.info("[StartResult]","[Exit]",info);
				for(StartResult<Integer> sr:srarr)
					sr.kill();
			}
		});
	}
	
	public static ObjectBean getLock(){
		return getLock("_lock","_lock");
	}
	
	public static ObjectBean getLock(String locknode, String lockname){
		ParkLocal pl = ParkPatternExector.getParkLocal();//String parkhost, int parkport
		ObjectBean ob = pl.create(locknode, lockname);
		String nodename = ob.getNode();
		while(true){
			List<ObjectBean> oblist = pl.get(locknode);
			String curnode = (String)oblist.get(0).getNode();
			if(curnode.equals(nodename))
				break;
		}
		return ob;
		//return getLock(locknode, lockname, lockname);
	}
	
	/*private static ObjectBean getLock(String locknode, String lockname, Object lockvalue){//lockvalue=w,r
		ParkLocal pl = ParkPatternExector.getParkLocal();//getPark()
		ObjectBean ob = lockname!=null?pl.create(locknode, lockname, lockvalue):pl.create(locknode, lockvalue);
		String lockname = ob.getNode();
		while(true){
			List<ObjectBean> oblist = pl.get(locknode);
			String curnode = (String)oblist.get(0).getNode();
			if(curnode.equals(lockname))
				break;
		}
		return ob;
	}*/
	
	public static void unLock(ObjectBean ob){
		ParkPatternExector.getParkLocal().delete(ob.getDomain(),ob.getNode());
	}
	/**
	 * ͨ��UUID��������ַ����������� -
	 * @return
	 */
	public static String getNumber(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	static boolean getWindows(boolean defaul){
		return defaul?defaul:System.getProperty("os.name").contains("Windows");
	}
	
	private static boolean getLinux(){
		return System.getProperty("os.name").contains("Linux");
	}
	
	public static void startCoolHashServer(String parkhost, int parkport, int worknum){
		startCoolHashServer("com.fourinone.CoolHashWorker", parkhost, parkport, worknum, "fourinone.jar"+File.pathSeparator, "log", null);
	}
	
	public static void startCoolHashServer(String parkhost, int parkport, int worknum, String jarclasspath, String logdir, String langCode){
		startCoolHashServer("com.fourinone.CoolHashWorker", parkhost, parkport, worknum, jarclasspath, logdir, langCode);
	}
	
	public static void startCoolHashServer(String parkhost, int parkport, int worknum, String langCode){
		startCoolHashServer("com.fourinone.CoolHashWorker", parkhost, parkport, worknum, "fourinone.jar"+File.pathSeparator, "log", langCode);
	}
	
	static void startCoolHashServerLocal(String parkhost, int parkport, int worknum){
		startCoolHashServer("com.fourinone.DumpWorker", parkhost, parkport, worknum,"fourinone.jar"+File.pathSeparator, "log", null);
	}
	
	static void startCoolHashServerLocal(String parkhost, int parkport, int worknum, String jarclasspath, String logdir){
		startCoolHashServer("com.fourinone.DumpWorker", parkhost, parkport, worknum, jarclasspath, logdir, null);
	}
	
	private static void startCoolHashServer(String service, String parkhost, int parkport, int worknum, String jarclasspath, String logdir, String langCode)
	{
		try{
			final StartResult[] starts = new StartResult[worknum+1];
			System.out.println("       0000");
			System.out.println("     00000000");
			System.out.println("    00000000000");
			System.out.println("   0000     000");
			System.out.println("   00  0  0  00");
			System.out.println("   00        00     Start ParkService and waiting few seconds...");
			starts[0]=BeanContext.tryStart("java","-cp",jarclasspath,"com.fourinone.BeanContext",parkhost,parkport+"");
			starts[0].print(logdir+"/park.log");
			Thread.sleep(4000);
			System.out.println("   0000     0000    Start CoolHashService and waiting few seconds...");
			int port=parkport+5;
			for(int i=1;i<starts.length;i++){
				if(langCode!=null)
					starts[i]=BeanContext.tryStart("java","-Dfile.encoding="+langCode,"-cp",jarclasspath,service,parkhost,parkport+"",parkhost,(port++)+"");
				else starts[i]=BeanContext.tryStart("java","-cp",jarclasspath,service,parkhost,parkport+"",parkhost,(port++)+"");
				starts[i].print(logdir+"/worker"+i+".log");
			}
			Thread.sleep(5000);
			System.out.println("  0000000 0000000   Please try connect to CoolHashServer now.");
			System.out.println(" 00000  00 0000000  Host: "+parkhost);
			System.out.println("0      000000    0  Port: "+parkport);
			System.out.println("0   0 00000  00  0  Version: Fourinone 4.0");
			System.out.println("0   00  0 000    0");
			System.out.println(" 0     0  0000   0");
			System.out.println(" 00   000       0");
			System.out.println("  00          000");
			System.out.println("   0000000000000");
			System.out.println("    0CoolHash00");
			BeanContext.killOnExit(starts,"CoolHashServer will exit...");
		}catch(Exception ex){
			LogUtil.info("[Start]","[CoolHashServer]",ex);
		}
	}
	
	public static CoolHashClient getCoolHashClient(String parkhost, int parkport){
		return new CoolHashCtor(parkhost, parkport);
	}
	
	static CoolHashClient getCoolHashClientLocal(String parkhost, int parkport){
		return new DumpCtor(parkhost, parkport);
	}
	
	public static void main(String[] args)
	{
		if(args!=null&&args.length==2)
			startPark(args[0],Integer.parseInt(args[1]));
		else startPark();
	}
}