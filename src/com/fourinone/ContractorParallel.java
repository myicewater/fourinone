package com.fourinone;
/**
 * 包工头：具有并行属性
 * @author 朱素海
 *
 */
public abstract class ContractorParallel extends ParallelService
{
	private int parallelPatternFlag = ConfigContext.getParallelPattern();
	
	/**
	 * 根据工人类型获取工人
	 * @param workerType
	 * @return
	 */
	//from workservice or park: WorkerServiceProxy/WorkerParkProxy(static ParkProxy/ParkLocal; workerType/nodename doTask)
	protected WorkerLocal[] getWaitingWorkers(String workerType)
	{
		//return parallelPatternFlag==0?getWaitingWorkersFromService(workerType):getWaitingWorkersFromPark(workerType);
		return getWaitingWorkers(workerType,null);
	}
	
	WorkerLocal[] getWaitingWorkers(String workerType, MigrantWorker mw)//protected
	{
		return parallelPatternFlag==0?getWaitingWorkersFromService(workerType, mw):getWaitingWorkersFromPark(workerType);
	}
	
	public void waitWorking(String host, int port, String workerType){
		ContractorService ctorsv = new ContractorService(this);
		ctorsv.waitWorking(host,port,workerType);
	}
	
	public void waitWorking(String workerType){
		ContractorService ctorsv = new ContractorService(this);
		ctorsv.waitWorking(workerType);
	}
	/**
	 * 关闭 线程池执行器
	 */
	public void exit(){
		PoolExector.close();
	}
	
	abstract WorkerLocal[] getWaitingWorkersFromService(String workerType, MigrantWorker mw);
	
	abstract WorkerLocal[] getWaitingWorkersFromService(String workerType);
	
	abstract WorkerLocal[] getWaitingWorkersFromPark(String workerType);
	
	abstract WareHouse[] doTaskBatch(WareHouse wh);
	
	abstract WareHouse[] doTaskBatch(WorkerLocal[] wks, WareHouse wh);
	
	abstract WareHouse[] doTaskCompete(WorkerLocal[] wks, WareHouse[] tasks);
	
	abstract WorkerLocal[] getLocalWorkers(int num);
		
	public abstract WareHouse giveTask(WareHouse inhouse);
	
	//2015.7.15
	//abstract Object operateAsyn(String methodname, Class[] argsType, Object[] argsValue);
}