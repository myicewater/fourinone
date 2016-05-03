package com.fourinone;

public class Result<E> extends WareHouse{
	private E res;

	public Result(){
		super();
	}
	
	public Result(boolean ready)
	{
		super(ready);
	}
	
	void setResult(E res)
	{
		this.res = res;
	}
	
	public E getResult()//throw
	{
		//if(getException()!=null)throws new FileExcpetion()
		return res;
	}
	
	public Object getResultWait(){
		return getResultWait(new Result[]{this})[0];
	}
	
	public static Object[] getResultWait(Result[] rts){
		if(rts==null||rts.length==0)
			return null;
		
		Object[] rtobj = new Object[rts.length];
		int j=0;
		while(j<rts.length){
			for(int i=0;i<rts.length;i++){
				if(rts[i].getMark()&&(rts[i].getStatus()==Result.READY||rts[i].getStatus()==Result.EXCEPTION)){
					rts[i].setMark(false);
					rtobj[i]=rts[i].getResult();
					j++;
				}
			}
			try{Thread.sleep(1);}catch(Exception ex){}
		}
		
		return rtobj;
	}
}