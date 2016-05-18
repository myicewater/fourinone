package com.fourinone;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
/**
 * 代理实例调用处理程序
 * @author 朱素海
 *
 */
public class DelegateConsole implements InvocationHandler
{
	private Object[] bs;
	private DelegateConsole(Object[] bs){
		this.bs = bs;
	}
	/*
	public static Object bind(Object... abs)
	{
		List aslist = new ArrayList();
		List bslist = new ArrayList();
		for(Object obj:abs){
			if(obj instanceof Class)
				aslist.add(obj);
			else
				bslist.add(obj);
		}
		//System.out.println(as[0].getClass().getName());
		//System.out.println(bs[0].getClass().getName());
		return Proxy.newProxyInstance(aslist.get(0).getClassLoader(), aslist.toArray(), new DelegateConsole(bslist.toArray()));
	}
	*/
	/**
	 * 通过代理 建立as 和bs 的代理关系
	 * @param as
	 * @param bs
	 * @return
	 */
	public static Object bind(Class[] as, Object... bs)
	{
		//System.out.println(as[0].getClass().getName());
		//System.out.println(bs[0].getClass().getName());
		return Proxy.newProxyInstance(as[0].getClassLoader(), as, new DelegateConsole(bs));
	}
	/**
	 * 绑定代理关系
	 * @param a :代理源类
	 * @param bs ：代理实例
	 * @return
	 */
	public static <I> I bind(Class<I> a, Object... bs)
	{
		return (I)bind(new Class[]{a}, bs);
	}

	/**
	 * 方法调用处理程序
	 * 
	 * 根据注解方法名字调用代理类相应的方法
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		Object result = null;
		Object mbeginObj=null,mimplObj=null,mendObj=null;
		Method mbegin=null,mimpl=null,mend=null;
		
		for(int j=0;j<bs.length;j++)
		{
			Method[] bms = bs[j].getClass().getMethods();
			for(int i=0;i<bms.length;i++)
			{
				boolean anflag = bms[i].isAnnotationPresent(Delegate.class);
				if(anflag)
				{
					Delegate dl = bms[i].getAnnotation(Delegate.class);
					Class dlifl = Class.forName(dl.interfaceName());
					//System.out.println("method.getName():"+method.getName());
					//System.out.println("method.getParameterTypes():"+method.getParameterTypes());
					//System.out.println("method.getReturnType():"+method.getReturnType());
					
					if(dlifl.isAssignableFrom(proxy.getClass())&&dl.methodName().equals(method.getName())&&Arrays.equals(method.getParameterTypes(),bms[i].getParameterTypes())&&method.getReturnType().equals(bms[i].getReturnType())){
						DelegatePolicy dp = dl.policy();
						if(dp==DelegatePolicy.Begin){
							mbeginObj = bs[j];
							mbegin = bms[i];
						}
						else if(dp==DelegatePolicy.Implements){
							mimplObj = bs[j];
							mimpl = bms[i];
						}
						else if(dp==DelegatePolicy.End){
							mendObj = bs[j];
							mend = bms[i];
						}
					}
				}
			}
		}
		
		if(mimpl!=null)
		{
			if(mbegin!=null)
				mbegin.invoke(mbeginObj, args);
			result = mimpl.invoke(mimplObj, args);
			if(mend!=null)
				mend.invoke(mendObj, args);
		}
		return result;
	}
	
	static Object operate(Object instance, String methodname, Class[] argsType, Object[] argsValue){
		Class clazz = instance.getClass();
		Method m = null;
		while(m==null){
			try{
				//System.out.println(clazz);
				m = clazz.getDeclaredMethod(methodname, argsType);
				CoolHash.class.getDeclaredMethod(methodname, argsType);//must in CoolHash
				//System.out.println("m:"+m);
				break;
			}catch(Exception ex){//NoSuchMethodException
				//System.out.println(ex);
				m=null;
				if(clazz==Object.class){
					LogUtil.info("[CoolHashClient]","[operate]",ex);
					break;
				}
				clazz = clazz.getSuperclass();
			} 
		}
		
		Object obj=null;
		try{
			obj = m.invoke(instance, argsValue);
		}catch(Exception ex){//IllegalAccessException, IllegalArgumentException, InvocationTargetException
			LogUtil.info("[CoolHashClient]","[operate]",ex);
		}
		return obj;
	}
}