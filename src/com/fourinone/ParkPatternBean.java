package com.fourinone;
/**
 * 职介者式样 模范 bean，职介者信息存储bean
 * 
 * <br>
 * 通过此bean可以创建一个职介者实例
 * 
 * @author Administrator
 *
 */
public class ParkPatternBean
{
	String domain,node;
	WareHouse inhouse,outhouse;
	ObjectBean thisversion;
	RecallException rx;
	ParkPatternBean(String domain, String node, WareHouse inhouse, WareHouse outhouse, RecallException rx)
	{
		this.domain = domain;
		this.node = node;
		this.inhouse = inhouse;
		this.outhouse = outhouse;
		this.rx = rx;
	}
}