package com.fourinone;

import java.text.DecimalFormat;
/**
 * 
 * 将要溢出异常
 * 
 * @author 朱素海
 *
 */
public class ClosetoOverException extends ServiceException {
	/**
	 * 总内存、可用内存、最大内存
	 */
	private double tm,fm,mm;
	/**
	 * 安全内存系数
	 */
	private static double safeMemoryPer = Double.parseDouble(ConfigContext.getConfig("PARK","SAFEMEMORYPER",null,"0.95"));
	
	public ClosetoOverException(double tm, double fm, double mm){
		super("The capacity close to out of memory, please clear out some data!");
		this.tm = tm;
		this.fm = fm;
		this.mm = mm;
	}
	
	public double getTm(){
		return tm;
	}
	
	public double getFm(){
		return fm;
	}
	
	public double getMm(){
		return mm;
	}
	
	public String print(){
		return print(tm,fm,mm);
	}
	
	/**
	 * 打印内存情况
	 * @param tm
	 * @param fm
	 * @param mm
	 * @return
	 */
	public static String print(double tm, double fm, double mm){
		DecimalFormat df = new DecimalFormat("0.00");
		return "tm:"+df.format(tm/(1024*1024))+"m,fm:"+df.format(fm/(1024*1024))+"m,mm:"+df.format(mm/(1024*1024))+"m";
	}
	/**
	 * 检查当前内存使用情况，超出则抛出 接近溢出异常
	 * @return
	 * @throws ClosetoOverException
	 */
	public static boolean checkMemCapacity() throws ClosetoOverException
	{
		double tm = new Long(Runtime.getRuntime().totalMemory()).doubleValue();
		double fm = new Long(Runtime.getRuntime().freeMemory()).doubleValue();
		double mm = new Long(Runtime.getRuntime().maxMemory()).doubleValue();
		
		LogUtil.fine("[checkMemCapacity]", "[MemCapacityInfo]", print(tm, fm, mm));
		
		if(tm/mm>safeMemoryPer&&fm/mm<1-safeMemoryPer)
			throw new ClosetoOverException(tm,fm,mm);
		
		return true;
		
		//return (((double)totalMemory/(double)maxMemory)>0.95&&((double)freeMemory/(double)maxMemory)<0.05)?true:false;
	}
}