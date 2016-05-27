package com.fourinone;
/**
 * 数组适配器
 * @author 朱素海
 *
 */
public class ArrayAdapter{
	/**
	 * 对象数组容量
	 */
	private int objinit = 0x40;
	/**
	 * 存放整数数组的 对象数组
	 */
	Object[] objarr;
	int objindex = 0;
	
	ArrayAdapter(){
		objarr = new Object[objinit];
	}
	
	public interface ListInt extends ParkStatg{
		public void add(int i);
		public void add(int[] initarr);
		public int size();
		public void set(int index, int i);
		public int get(int index);
		public void sort();
		public int[] sort(int[] arr);
		public int[] toArray();
		public void order(int[] arr);
	}
	
	/** 
	 * 如果达到最大容量 0x40,则进行扩容，每次增加 0x40个
	 */
	void auto(){
		if(objindex==objarr.length){
			Object[] objarrnew = new Object[objarr.length+objinit];
			System.arraycopy(objarr,0,objarrnew,0,objarr.length);
			objarr = objarrnew;
		}
	}
	
	/**
	 * 创建整形数组
	 * @return
	 */
	public static ListInt getListInt(){
		return new ArrayInt();
	}
	
	/*public static ListInt getListInt(int[] initarr){
		return initarr!=null?new ArrayInt(initarr):new ArrayInt();
	}*/
	
	public static void main(String[] args){
		ListInt ai = getListInt();//new int[]{25,19,1,0,23,3,18,19,5,4,15,0,3,11,48,2,7,24,2,3,4,22,14,1,34,6,9,21,65,8}
		java.util.Random rad = new java.util.Random();
		for(int i=0;i<40000000;i++)
			ai.add(rad.nextInt(10000000));
		
		System.out.println(ai.size());
		long begin = (new java.util.Date()).getTime();
		ai.sort();
		long end = (new java.util.Date()).getTime();
		System.out.println("time:"+(end-begin)/1000+"s");
		/*for(int i=0;i<ai.size();i++)
			System.out.print(ai.get(i)+",");*/
	}
}