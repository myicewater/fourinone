package com.fourinone;
/**
 * ְ����ʽ�� ģ�� bean��ְ������Ϣ�洢bean
 * 
 * <br>
 * ͨ����bean���Դ���һ��ְ����ʵ��
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