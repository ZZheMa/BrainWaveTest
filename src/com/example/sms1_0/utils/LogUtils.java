package com.example.sms1_0.utils;

import android.util.Log;

/**
 * ��logcat��ӡlog�Ĺ�����
 * @author ����
 */
public class LogUtils {
	
	public static boolean isDebug = true;
	
	public static void i(String tag, String msg) {
		if (isDebug) {
			Log.i(tag, msg);
		}
	}
	
	public static void i(Object tag, String msg) {
		if (isDebug) {
			Log.i(tag.getClass().getSimpleName(), msg);
		}
	}
}
