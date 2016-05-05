package com.fourinone;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * ½Ó¿ÚWorker
 * @author ÖìËØº£
 *
 */
public interface Worker extends ParkActive
{
	public void setMigrantWorker(MigrantWorker mw) throws RemoteException;
	public WareHouse doTask(WareHouse inhouse) throws RemoteException;
	public void stopTask() throws RemoteException,InterruptedException;
	public boolean receiveMaterials(WareHouse inhouse) throws RemoteException;
}