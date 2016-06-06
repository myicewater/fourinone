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
	 * 
	 * @implement WorkerServiceProxy-64 具体实现通过代理实现的
	 */
	public WareHouse giveTask(WareHouse inhouse);
}