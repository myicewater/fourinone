package com.fourinone;
/**
 * ����Bean �࣬�̳�Serializable ���л��ӿ�
 * @author ���غ�
 *
 */
public interface ObjectBean extends java.io.Serializable{
	/**
	 * ת���ɶ���
	 * @return
	 */
	public Object toObject();
	/**
	 * ��������
	 * @return
	 */
	public String getName();
	/**
	 * ����������
	 * @return
	 */
	public String getDomain();
	
	/**
	 * ���ؽڵ�
	 * @return
	 */
	public String getNode();
}