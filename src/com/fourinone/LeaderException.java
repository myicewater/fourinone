package com.fourinone;
/**
 * 领导异常
 * @author Administrator
 *
 */
public class LeaderException extends Exception {
	private String[] leaderServer;
	public LeaderException(){}
	/**
	 * 拒绝成为master(领导)异常
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