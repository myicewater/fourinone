package com.fourinone;

public interface CoolHashClient extends CoolHash{
	public void begin();
	public void rollback();
	public void commit();
	public void exit();
	public Result operateAsyn(String methodname, Class[] argsType, Object[] argsValue);//2015.07.15
}