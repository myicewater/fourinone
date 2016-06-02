package com.fourinone;
/**
 * 包工头接口
 * @author 朱素海
 *
 */
public interface CtorLocal
{
	/**
	 * 发放任务
	 * <br>
	 * 参数和返回类型都是WareHouse，可以输入任意类型，返回任意类型
	 * <br>
	 * 做到了最大灵活性
	 * @param inhouse
	 * @return
	 */
	public WareHouse giveTask(WareHouse inhouse);
}