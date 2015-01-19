package com.ykjndz.bt.tool;

import android.R.integer;

/**
 * 常量类
* @Description: 
* @author yougui_zheng 
* @date 2014-12-8 下午10:25:22 
*
 */
public class Constants {
	/**
	 * 打开开关
	 */
	public static String SWITCH_OPEN = "AT+PIOB1";
	/**
	 * 关闭开关
	 */
	public static String SWITCH_CLOSE = "AT+PIOB0";
	/**
	 * 打开小夜灯
	 */
	public static String LIGHT_OPEN = "AT+PIOA1";
	/**
	 * 关闭小夜灯
	 */
	public static String LIGHT_CLOSE = "AT+PIOA0";
	/**
	 * 打开告警
	 */
	public static String ALARM_OPEN = "AT+PIO91";
	/**
	 * 关闭告警
	 */
	public static String ALARM_CLOSE = "AT+PIO90";
	
	/**
	 * 开关状态:开
	 */
	public static int  OPEN = 1;
	/**
	 * 开关状态:关
	 */
	public static int  CLOSE = 0;
	
	/**
	 * 开关状态回复:开
	 */
	public static String RES_SWITCH_OPEN = "OK+Set:B1";
	/**
	 * 开关状态回复:关
	 */
	public static String RES_SWITCH_CLOSE = "OK+Set:B0";
	/**
	 * 小夜灯状态回复:开
	 */
	public static String RES_LIGHT_OPEN = "OK+Set:A1";
	/**
	 * 小夜灯状态回复:关
	 */
	public static String RES_LIGHT_CLOSE = "OK+Set:A0";
	/**
	 * 告警状态回复:开
	 */
	public static String RES_ALARM_OPEN = "OK+Set:91";
	/**
	 * 告警状态回复:关
	 */
	public static String RES_ALARM_CLOSE = "OK+Set:90";
	/**
	 * 警报
	 */
	public static String ALARM = "AT+PIO81";
	/**
	 * 查询状态指令
	 * B:开关灯
	 * A:小夜灯
	 * 9:警报
	 */
	public static String[] PORT_ARRAY = new String[]{"AT+PIO[B]?","AT+PIO[A]?","AT+PIO[9]?"};
	/**
	 * 连接
	 */
	public static String CONNECTED = "Connected";
	/**
	 * 断开:未连接
	 */
	public static String DISCONNECTED = "Disconnected";
	/**
	 * 声音标签
	 */
	public static String SOUND = "sound";
}
