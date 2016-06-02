package com.fourinone;
import java.rmi.Remote;
/**
 * 特性:远程调用、序列化<br>
 * 接口支持rmi远程调用<br>
 * 支持序列化<br>
 * @author 朱素海
 *
 */
interface ParkActive extends ParkStatg,Remote{
}