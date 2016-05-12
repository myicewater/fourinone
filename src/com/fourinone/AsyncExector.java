package com.fourinone;
/**
 * 异步执行器，回调继承此抽象类的类的task()
 * 
 * @author Administrator
 *
 */
public abstract class AsyncExector{
	public abstract void task();
	public void run(){
		try{
			new Thread(new Runnable(){
				public void run(){
					task();
				}
			}).start();
		}catch(Exception e){
			//e.printStackTrace();
			LogUtil.info("AsyncExector", "task", e);
		}
	}
}