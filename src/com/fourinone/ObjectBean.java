package com.fourinone;
/**
 * 对象Bean 接口<br>
 * 序列化
 * @author 朱素海
 *
 */
public interface ObjectBean extends java.io.Serializable{
	/**
	 * 转换成对象
	 * @return
	 */
	public Object toObject();
	/**
	 * 返回名字
	 * @return
	 */
	public String getName();
	/**
	 * 返回所属域
	 * @return
	 */
	public String getDomain();
	
	/**
	 * 返回节点
	 * @return
	 */
	public String getNode();
}