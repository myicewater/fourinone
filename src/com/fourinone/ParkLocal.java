package com.fourinone;

import java.util.List;
import java.io.Serializable;

public interface ParkLocal
{
	/**
	 * ������
	 * @param domain ����
	 * @param obj ����ֵ
	 * @return
	 */
	public ObjectBean create(String domain, Serializable obj);
	/**
	 * ������
	 * @param domain ����
	 * @param node �ڵ���
	 * @param obj ����
	 * @return
	 */
	public ObjectBean create(String domain, String node, Serializable obj);
	/**
	 * ������Ȩ�޵���
	 * @param domain
	 * @param node
	 * @param obj
	 * @param auth
	 * @return
	 */
	public ObjectBean create(String domain, String node, Serializable obj, AuthPolicy auth);
	public ObjectBean create(String domain, String node, Serializable obj, boolean heartbeat);
	public ObjectBean create(String domain, String node, Serializable obj, AuthPolicy auth, boolean heartbeat);
	/**
	 * ����node
	 * @param domain
	 * @param node
	 * @param obj
	 * @return
	 */
	public ObjectBean update(String domain, String node, Serializable obj);
	public ObjectBean get(String domain, String node);
	public ObjectBean getLastest(String domain, String node, ObjectBean ob);
	/**
	 * ��ȡ����domain
	 * @param domain
	 * @return
	 */
	public List<ObjectBean> get(String domain);
	/**
	 * ��ȡ����domain������node����Ҫ����ɵ����м��϶���
	 * @param domain
	 * @param oblist
	 * @return
	 */
	public List<ObjectBean> getLastest(String domain, List<ObjectBean> oblist);
	/***
	 * ɾ��node
	 * @param domain
	 * @param node
	 * @return
	 */
	public ObjectBean delete(String domain, String node);
	/**
	 * ǿ������domain��ɾ��
	 * @param domain
	 * @return
	 */
	public boolean setDeletable(String domain);
	/**
	 * ɾ��domain����������node
	 * @param domain
	 * @return
	 */
	public List<ObjectBean> delete(String domain);
	/**
	 * ���node �ļ����¼�
	 * @param domain
	 * @param node
	 * @param ob
	 * @param liser
	 */
	public void addLastestListener(String domain, String node, ObjectBean ob, LastestListener liser);
	public void addLastestListener(String domain, List<ObjectBean> oblist, LastestListener liser);
}