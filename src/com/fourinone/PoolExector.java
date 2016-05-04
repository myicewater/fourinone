package com.fourinone;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;

class PoolExector //java.io.Closeable
{
	/**
	 * 使用可能的几个池线程之一执行每个提交的任务
	 */
	private static ThreadPoolExecutor tpe;//rm static, new everytime
	private static ScheduledThreadPoolExecutor stpe;//rm static, new everytime
	
	/**
	 * 初始化线程池执行器
	 * @return
	 */
	static synchronized ThreadPoolExecutor tpe()//
	{
		if(tpe==null)
		{
			//System.out.println(Thread.currentThread()+":new ThreadPoolExecutor...");
			int corePoolSize = ConfigContext.getInitServices();
			int maximumPoolSize = ConfigContext.getMaxServices();
			long keepAliveTime = 3000;
			TimeUnit unit = TimeUnit.MILLISECONDS;
			BlockingQueue<Runnable> waitQueue = new ArrayBlockingQueue<Runnable>(2000);
			RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();//ThreadPoolExecutor.CallerRunsPolicy();
			tpe =new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, waitQueue, handler);
			//System.out.println(Thread.currentThread()+":new done.");
		}
		return tpe;
	}
	
	static ScheduledThreadPoolExecutor stpe()
	{
		if(stpe==null)
		{
			int corePoolSize = ConfigContext.getInitServices();
			stpe =new ScheduledThreadPoolExecutor(corePoolSize);
		}
		return stpe;
	}
	
	static void execute(Runnable d, Runnable i, long t){
		tpe().execute(d);
		if(t>0)
			stpe().schedule(i,t,TimeUnit.SECONDS);
	}
	
	static synchronized void close(){//
		if(tpe!=null){
			try{
				//System.out.println(Thread.currentThread()+"：shutdown ThreadPoolExecutor...");
				while(tpe.getActiveCount()!=0);//2015.8.13
				tpe.shutdown();
				//tpe.awaitTermination(1, TimeUnit.DAYS);
				tpe=null;
			}catch(SecurityException se){
				LogUtil.info("[tpe]", "[close]", "[Error Exception:]", se);
			}
		}
		if(stpe!=null){
			try{
				stpe.shutdown();
				stpe=null;
			}catch(SecurityException se){
				LogUtil.info("[stpe]", "[close]", "[Error Exception:]", se);
			}
		}
	}
}