package com.fourinone;

public interface WorkerLocal extends WorkerProxy
{
	/**
	 * ������
	 * @param inhouse
	 * @return
	 */
	public WareHouse doTask(WareHouse inhouse);
	public WareHouse doTask(WareHouse inhouse, long timeoutseconds);
	public void interrupt();
	public String getHost();
	public int getPort();
}