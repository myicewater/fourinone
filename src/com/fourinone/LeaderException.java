package com.fourinone;
/**
 * �쵼�쳣
 * @author Administrator
 *
 */
public class LeaderException extends Exception {
	private String[] leaderServer;
	public LeaderException(){}
	/**
	 * �ܾ���Ϊmaster(�쵼)�쳣
	 * @param thisserver
	 * @param leaderServer
	 */
	public LeaderException(String[] thisserver, String[] leaderServer){
		super(thisserver[0]+":"+thisserver[1]+" Refuse Service, Leader of group is "+leaderServer[0]+":"+leaderServer[1]);
		this.leaderServer = leaderServer;
	}
	public String[] getLeaderServer(){
		return leaderServer;
	}
}