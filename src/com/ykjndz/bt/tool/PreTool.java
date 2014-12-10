package com.ykjndz.bt.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
/**
 * SharedPreferences操作工具类
* @Description: 
* @author yougui_zheng 
* @date 2014-12-8 下午10:20:26 
*
 */
public abstract class PreTool {
	/**
	 * 获取一个boolean键值
	 * 
	 * @param key
	 *            键值名
	 * @param defaultValue
	 *            默认值
	 * @param proferenceName
	 *            proference标识名称
	 * @param context
	 *            当前操作context对象
	 * @return 需要查找的值
	 * @author lqq
	 * @since 1.0
	 */
	public static synchronized boolean getBoolean(String key,
			boolean defaultValue, String proferenceName, Context context) {
		boolean booleanValue = false;
		try {
			SharedPreferences sharedPre = context.getSharedPreferences(
					proferenceName, 0);
			booleanValue = sharedPre.getBoolean(key, defaultValue);
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
		}
		return booleanValue;
	}

	/**
	 * 获取一个float键值
	 * 
	 * @param key
	 *            键值名
	 * @param defaultValue
	 *            默认值
	 * @param proferenceName
	 *            proference标识名称
	 * @param context
	 *            当前操作context对象
	 * @return 需要查找的值
	 * @author lqq
	 * @since 1.2.0
	 */
	public static synchronized float getFloat(String key, float defaultValue,
			String proferenceName, Context context) {
		float floatValue = 0f;
		try {
			SharedPreferences sharedPre = context.getSharedPreferences(
					proferenceName, 0);
			floatValue = sharedPre.getFloat(key, defaultValue);
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
		}
		return floatValue;
	}

	/**
	 * 获取一个int键值
	 * 
	 * @param key
	 *            键值名
	 * @param defaultValue
	 *            默认值
	 * @param proferenceName
	 *            proference标识名称
	 * @param context
	 *            当前操作context对象
	 * @return 需要查找的值
	 * @author lqq
	 * @since 1.0
	 */
	public static synchronized int getInt(String key, int defaultValue,
			String proferenceName, Context context) {
		int intValue = 0;
		try {
			SharedPreferences sharedPre = context.getSharedPreferences(
					proferenceName, 0);
			intValue = sharedPre.getInt(key, defaultValue);
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
		}
		return intValue;
	}
	
	/**
	 * 获取一个long键值
	 * 
	 * @param key
	 *            键值名
	 * @param defaultValue
	 *            默认值
	 * @param proferenceName
	 *            proference标识名称
	 * @param context
	 *            当前操作context对象
	 * @return 需要查找的值
	 * @author lqq
	 * @since 1.2.0
	 */
	public static synchronized long getLong(String key, long defaultValue,
			String proferenceName, Context context) {
		long longValue = 0l;
		try {
			SharedPreferences sharedPre = context.getSharedPreferences(
					proferenceName, 0);
			longValue = sharedPre.getLong(key, defaultValue);
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
		}
		return longValue;
	}

	/**
	 * 获取一个String键值
	 * 
	 * @param key
	 *            键值名
	 * @param defaultValue
	 *            默认值
	 * @param proferenceName
	 *            proference标识名称
	 * @param context
	 *            当前操作context对象
	 * @return 需要查找的值
	 * @author lqq
	 * @since 1.0
	 */
	public static synchronized String getString(String key,
			String defaultValue, String proferenceName, Context context) {
		String strValue = null;
		try {
			SharedPreferences sharedPre = context.getSharedPreferences(
					proferenceName, 0);
			strValue = sharedPre.getString(key, defaultValue);
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
		}
		return strValue;
	}

	/**
	 * 保存一个boolean值
	 * 
	 * @param key
	 *            键值名
	 * @param value
	 *            值
	 * @param proferenceName
	 *            proference标识名称
	 * @param context
	 *            当前操作context对象
	 * @return 是否成功
	 * @author lqq
	 * @since 1.0
	 */
	public static synchronized boolean putBoolean(String key, boolean value,
			String proferenceName, Context context) {
		try {
			SharedPreferences sharedPre = context.getSharedPreferences(
					proferenceName, 0);
			SharedPreferences.Editor e = sharedPre.edit();
			e.putBoolean(key, value);
			e.commit();
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 保存一个float值
	 * 
	 * @param key
	 *            键值名
	 * @param value
	 *            值
	 * @param proferenceName
	 *            proference标识名称
	 * @param context
	 *            当前操作context对象
	 * @return 是否成功
	 * @author lqq
	 * @since 1.2.0
	 */
	public static synchronized boolean putFloat(String key, float value,
			String proferenceName, Context context) {
		try {
			SharedPreferences sharedPre = context.getSharedPreferences(
					proferenceName, 0);
			SharedPreferences.Editor e = sharedPre.edit();
			e.putFloat(key, value);
			e.commit();
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 保存一个int值
	 * 
	 * @param key
	 *            键值名
	 * @param value
	 *            值
	 * @param proferenceName
	 *            proference标识名称
	 * @param context
	 *            当前操作context对象
	 * @return 是否成功
	 * @author lqq
	 * @since 1.0
	 */
	public static synchronized boolean putInt(String key, int value,
			String proferenceName, Context context) {
		try {
			SharedPreferences sharedPre = context.getSharedPreferences(
					proferenceName, 0);
			SharedPreferences.Editor e = sharedPre.edit();
			e.putInt(key, value);
			e.commit();
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 保存一个long值
	 * 
	 * @param key
	 *            键值名
	 * @param value
	 *            值
	 * @param proferenceName
	 *            proference标识名称
	 * @param context
	 *            当前操作context对象
	 * @return 是否成功
	 * @author lqq
	 * @since 1.2.0
	 */
	public static synchronized boolean putLong(String key, long value,
			String proferenceName, Context context) {
		try {
			SharedPreferences sharedPre = context.getSharedPreferences(
					proferenceName, 0);
			SharedPreferences.Editor e = sharedPre.edit();
			e.putLong(key, value);
			e.commit();
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 保存一个string值
	 * 
	 * @param key
	 *            键值名
	 * @param value
	 *            值
	 * @param proferenceName
	 *            proference标识名称
	 * @param context
	 *            当前操作context对象
	 * @return 是否成功
	 * @author lqq
	 * @since 1.0
	 */
	public static synchronized boolean putString(String key, String value,
			String proferenceName, Context context) {
		try {
			SharedPreferences sharedPre = context.getSharedPreferences(
					proferenceName, 0);
			SharedPreferences.Editor e = sharedPre.edit();
			e.putString(key, value);
			e.commit();
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
			return false;
		}
		return true;
	}

}