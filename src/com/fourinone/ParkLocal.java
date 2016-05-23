package com.fourinone;

import java.util.List;
import java.io.Serializable;

public interface ParkLocal
{
	/**
	 * 创建域
	 * @param domain 域名
	 * @param obj 序列值
	 * @return
	 */
	public ObjectBean create(String domain, Serializable obj);
	/**
	 * 创建域
	 * @param domain 域名
	 * @param node 节点名
	 * @param obj 序列
	 * @return
	 */
	public ObjectBean create(String domain, String node, Serializable obj);
	/**
	 * 创带有权限的域
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
	 * 更新node
	 * @param domain
	 * @param node
	 * @param obj
	 * @return
	 */
	public ObjectBean update(String domain, String node, Serializable obj);
	public ObjectBean get(String domain, String node);
	public ObjectBean getLastest(String domain, String node, ObjectBean ob);
	/**
	 * 获取最新domain
	 * @param domain
	 * @return
	 */
	public List<ObjectBean> get(String domain);
	/**
	 * 获取最新domain下所有node，需要传入旧的所有集合对照
	 * @param domain
	 * @param oblist
	 * @return
	 */
	public List<ObjectBean> getLastest(String domain, List<ObjectBean> oblist);
	/***
	 * 删除node
	 * @param domain
	 * @param node
	 * @return
	 */
	public ObjectBean delete(String domain, String node);
	/**
	 * 强行设置domain可删除
	 * @param domain
	 * @return
	 */
	public boolean setDeletable(String domain);
	/**
	 * 删除domain及其下所有node
	 * @param domain
	 * @return
	 */
	public List<ObjectBean> delete(String domain);
	/**
	 * 添加node 的监听事件
	 * @param domain
	 * @param node
	 * @param ob
	 * @param liser
	 */
	public void addLastestListener(String domain, String node, ObjectBean ob, LastestListener liser);
	public void addLastestListener(String domain, List<ObjectBean> oblist, LastestListener liser);
}