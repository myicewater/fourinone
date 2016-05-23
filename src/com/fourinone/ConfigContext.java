package com.fourinone;

import java.util.ArrayList;
import java.util.Properties;
import java.text.DateFormat;
import java.util.Date;
import java.io.File;
/**
 * 配置上下文
 * @author 朱素海
 *
 */
public class ConfigContext
{
	/**
	 * 配置文件读取类
	 */
	private static MulBean mb = null;
	/**
	 * 初始化值
	 */
	private static String QSXYSJ=null,YMMZ=null,RZDY=null,YCDYXY=null,DMY=null,AQCL=null,POLICY=null,LSML=null,SERVICEONWORKER=null;
	private static long TMOT=-1;
	static String configFile = new File("").getAbsolutePath()+File.separator+"config.xml";
	private static ObjValue USERS = null;
	private static long KEYLENTH=-1,VALUELENGTH=-1,REGIONLENGTH=-1,LOADLENGTH=-1;
	private static int HASHCAPACITY=-1;
	private static String DATAROOT;
	
	/**
	 * 获取存储键值对 键 最大长度，单位M
	 * @return
	 */
	static long getKEYLENTH(){
		if(KEYLENTH==-1)
			KEYLENTH = new Long(getConfig("COOLHASH","KEYLENTH","B","256"));
		return KEYLENTH;
	}
	/**
	 * 获取存储键值对 值 最大长度，单位M
	 * @return
	 */
	static long getVALUELENGTH(){
		if(VALUELENGTH==-1)
			VALUELENGTH = new Long(getConfig("COOLHASH","VALUELENGTH","M","2"));
		return VALUELENGTH;
	}
	/**
	 * 获取键值对 区域最大长度？？
	 * @return
	 */
	static long getREGIONLENGTH(){
		if(REGIONLENGTH==-1)
			REGIONLENGTH = new Long(getConfig("COOLHASH","REGIONLENGTH","M","2"));
		return REGIONLENGTH;
	}
	/**
	 * 获取键值对加载最大长度 ，单位兆
	 * @return
	 */
	static long getLOADLENGTH(){
		if(LOADLENGTH==-1)
			LOADLENGTH = new Long(getConfig("COOLHASH","LOADLENGTH","M","64"));
		return LOADLENGTH;
	}
	/**
	 * 获取coolhash 最大容量
	 * @return
	 */
	static int getHASHCAPACITY(){
		if(HASHCAPACITY==-1)
			HASHCAPACITY = new Integer(getConfig("COOLHASH","HASHCAPACITY",null,"1000000"));
		return HASHCAPACITY;
	}
	/**
	 * 获取coolhash 数据跟路径
	 * @return
	 */
	static String getDATAROOT(){
		if(DATAROOT==null)
			DATAROOT = getConfig("COOLHASH","DATAROOT",null,"data");
		return DATAROOT;
	}
	/**
	 * 读取配置文件，返回工具类
	 * @return
	 */
	static MulBean getMulBean(){
		return mb!=null?mb:new MulBean("ISO-8859-1");
	}
	/**
	 * 根据key获取值
	 * @return
	 */
	static String getQSXYSJ(){
		if(QSXYSJ==null)
			QSXYSJ = getMulBean().getString("QSXYSJ");
		return QSXYSJ;
	}
	/**
	 * 根据key获取值
	 * @return
	 */
	static String getYMMZ(){
		if(YMMZ==null)
			YMMZ = getMulBean().getString("YMMZ");
		return YMMZ;
	}
	
	static String getRZDY(){
		if(RZDY==null)
			RZDY = getMulBean().getString("RZDY");
		return RZDY;
	}
	
	static String getYCDYXY(){
		if(YCDYXY==null)
			YCDYXY = getMulBean().getString("YCDYXY");
		return YCDYXY;
	}
	
	static String getDMY(){
		if(DMY==null)
			DMY = getMulBean().getString("DMY");
		return DMY;
	}
	
	static String getAQCL(){
		if(AQCL==null)
			AQCL = getMulBean().getString("AQCL");
		return AQCL;
	}
	
	static String getPOLICY(){
		if(POLICY==null)
			POLICY = getMulBean().getString("POLICY");
		return POLICY;
	}
	
	static String getLSML(){
		if(LSML==null)
			LSML = getMulBean().getString("LSML");
		return LSML;
	}
	/**
	 * 根据属性名获取属性值
	 * @param propstr
	 * @return
	 */
	static String getProp(String propstr){
		return getMulBean().getString(propstr);
	}
	/**
	 * 获取rmi调用地址
	 * @param ym
	 * @param dk
	 * @param mc
	 * @return
	 */
	static String getProtocolInfo(String ym, int dk, String mc){
		return getYCDYXY()+ym+":"+dk+"/"+mc;
	}
	/**
	 * 获取工人超时时间
	 * @return
	 */
	static long getTMOT(){
		if(TMOT==-1)
			TMOT = getSecTime(new Double(getConfig("WORKER","TIMEOUT","TRUE","0")));
		return TMOT;
	}
	/**
	 * 获取服务标志
	 * @return
	 */
	static boolean getServiceFlag(){
		if(SERVICEONWORKER==null)
			SERVICEONWORKER = getConfig("WORKER","SERVICE", null, "false");
		return Boolean.parseBoolean(SERVICEONWORKER);
	}	
	/**
	 * 小时转化成秒	
	 * @param hours
	 * @return
	 */
	static long getSecTime(Double hours)
	{
		Double t = hours*3600*1000;
		return t.longValue();
	}
	/**
	 * 获取职介所（Park） 服务器（SERVERS）地址信息
	 * @return
	 */
	static String[][] getParkConfig()
	{
		String servers = getConfig("PARK","SERVERS",null);
		return getServerFromStr(servers);
	}
	
	static String getParkService()
	{
		return getConfig("PARK","SERVICE",null);
	}
	
	static String[] getCtorService()
	{
		return getConfig("CTOR","CTORSERVERS",null).split(":");
	}
	
	/**
	 * 获取fttp配置信息
	 * @return
	 */
	static String[] getFttpConfig()
	{
		return getConfig("FTTP","SERVERS",null).split(":");
	}
	/**
	 * 获取webapp 配置信息
	 * @return
	 */
	static String[] getInetConfig()
	{
		return getConfig("WEBAPP","SERVERS",null).split(":");
	}
	
	static ObjValue getUsersConfig()
	{
		if(USERS==null){
			String userstr = getConfig("WEBAPP","USERS",null);
			USERS = getObjFromStr(userstr);
		}
		return USERS;
	}
	
	static String getInetStrConfig(String wkjn)
	{
		String inetstr = "http://"+getConfig("WEBAPP","SERVERS",null)+"/res/";
		return wkjn!=null?inetstr+wkjn:inetstr;
	}
	
	static String getPolicyConfig(){
		String tdir = System.getProperty(getLSML());
		File fl = new File(tdir, "a.pl");
		if(!fl.exists()){
			FileAdapter fa = new FileAdapter(fl.getPath());
			fa.getWriter().write(getPOLICY().getBytes());
			fa.close();
		}
		return fl.getPath();
	}
	
	/**
	 * 获取工人 服务地址 配置信息
	 * @return
	 */
	static String[] getWorkerConfig()
	{
		return getConfig("WORKER","SERVERS",null).split(":");
	}
	
	static String[][] getCacheConfig()
	{
		String servers = getConfig("CACHE","SERVERS",null);
		return getServerFromStr(servers);
	}
	/**
	 * 获取缓存配置信息
	 * @return
	 */
	static String getCacheService()
	{
		return getConfig("CACHE","SERVICE",null);
	}
	
	static String[] getCacheFacadeConfig()
	{
		return getConfig("CACHEFACADE","SERVERS",null).split(":");
	}
	
	public static String getCacheFacadeService()
	{
		return getConfig("CACHEFACADE","SERVICE",null);
	}
	
	static int getInitServices()
	{
		int initnum = 10;
		try{
			initnum = Integer.parseInt(getConfig("CTOR","INITSERVICES",null,"10"));
		}catch(Exception e){}
		return initnum;
	}
	
	static int getMaxServices()
	{
		int maxnum = 100;
		try{
			maxnum = Integer.parseInt(getConfig("CTOR","MAXSERVICES",null,"100"));
		}catch(Exception e){}
		return maxnum;
	}
	/**
	 * 获取并行模式
	 * @return
	 */
	static int getParallelPattern()
	{
		return Integer.parseInt(getConfig("COMPUTEMODE","MODE","DEFAULT"));
	}
	/**
	 * 根据配置组（PROPSROW）名称，属性名称获取配置信息
	 * @param cfgname
	 * @param cfgprop
	 * @param cfgdesc
	 * @return
	 */
	static String getConfig(String cfgname, String cfgprop, String cfgdesc)
	{
		return getConfig(cfgname, cfgprop, cfgdesc, null);
	}
	
	/**
	 * 通过配置名称（<PROPSROW DESC="PARK">），属性名，配置描述从配置文件中获取相应的值
	 * 如果值为空则返回传入的 默认值（defvalue）
	 * @param cfgname 配置名称
	 * @param cfgprop  属性名
	 * @param cfgdesc 属性名描述
	 * @param defvalue 默认值
	 * @return
	 */
	static String getConfig(String cfgname, String cfgprop, String cfgdesc, String defvalue)
	{
		XmlUtil xu = new XmlUtil();
		ArrayList al = xu.getXmlObjectByFile(configFile,cfgname,cfgdesc);
		String v = null;
		if(al!=null&&al.size()>0)
		{
			ObjValue cfgProps = (ObjValue)al.get(0);
			v = cfgProps.getString(cfgprop);
		}
		if(v==null)
			v=defvalue;
		//LogUtil.fine("[ConfigContext]", "[getConfig]", v);
		return v;
	}
	
	static String getLogLevel(String deflevel)
	{
		XmlUtil xu = new XmlUtil();
		ArrayList al = xu.getXmlPropsByFile(configFile,"LOG","LOGLEVEL");
		Properties dbProps = (Properties)al.get(0);
		String levelName = dbProps.getProperty("LEVELNAME");
		
		return levelName!=null?levelName:deflevel;
	}
	
	static ObjValue getCacheGroupConfig()
	{
		XmlUtil xu = new XmlUtil();
		ArrayList al = xu.getXmlObjectByFile(configFile,"CACHEGROUP");
		ObjValue groups = new ObjValue();
		
		for(int i=0;i<al.size();i++)
		{
			ObjValue cacheProps = (ObjValue)al.get(i);
			ObjValue gp = new ObjValue();
			String gpcfgstr = cacheProps.getString("GROUP");
			for(String perstr:gpcfgstr.split(";"))
			{
				String[] perstrarr = perstr.split("@");
				gp.setObj(perstrarr[0],new Long(getDateLong(perstrarr[1])));
			}
			groups.put(gp, new Long(getDateLong(cacheProps.getString("STARTTIME"))));
		}
		
		LogUtil.fine("[ConfigContext]", "[getCacheConfig]", groups);
		return groups;
	}
	
	static String getDateLong(String dateStr)
	{
		if(dateStr!=null&&!dateStr.equals(""))
		{
			try
			{
				DateFormat dateFormat = DateFormat.getDateInstance();
				Date d = dateFormat.parse(dateStr);
				dateStr = d.getTime()+"";
				if(dateStr.length()==12)
					dateStr = "0"+dateStr;
			}catch(Exception e){
				System.out.println(e);
			}
		}
		return  dateStr;
	}
	
	static String[][] getServerFromStr(String servers)
	{
		String[] serverarr = servers.split(",");
		String[][] sarr = new String[serverarr.length][];
		for(int n=0;n<serverarr.length;n++)
		{
			String[] hostport=serverarr[n].split(":");
			sarr[n]=hostport;
		}
		
		return sarr;
	}
	
	private static ObjValue getObjFromStr(String strs)
	{
		String[] strarr = strs.split(",");
		ObjValue ov = new ObjValue();
		for(String thestr:strarr)
		{
			String[] str=thestr.split(":");
			ov.setString(str[0],str[1]);
		}
		
		return ov;
	}
	/**
	 * 返回request
	 * @param requestUrl
	 * @return
	 */
	static String getRequest(String requestUrl){
		return getMulBean().getFileString(getMulBean().getString(requestUrl));
	}
	
	public static void main(String args[])
	{
		BeanContext.setConfigFile("D:\\demo\\comutil\\test\\config.xml");
		System.out.println(getParkConfig()[0][0]);
		LogUtil.fine(getCacheConfig());
		LogUtil.fine("getParallelPattern:"+getParallelPattern());
		System.out.println(getConfig("CACHEFACADE","TRYKEYSNUM",null,"500"));
	}
}