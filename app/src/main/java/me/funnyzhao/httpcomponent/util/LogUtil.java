package me.funnyzhao.httpcomponent.util;


import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/*
 
 * -----------------------------------------------------------------
 
 * Copyright (C) by funnyzhao, All rights reserved.
 
 * -----------------------------------------------------------------
 
 * Author: funnyzhao
 
 * Create: 2018/6/26 11:05
 
 * Changes (from 2018/6/26)
 *
 * 币果log
 * 封装logger
 
 * -----------------------------------------------------------------
 
 */
public class LogUtil {
	
	public static void init(final boolean isDebug){
		Logger.addLogAdapter(new AndroidLogAdapter(){
			@Override
			public boolean isLoggable(int priority, @Nullable String tag) {
				return isDebug;
			}
		});
	}
	
	public static void info(String tag,String s){
		Logger.d(s);
	}
	public static void netInfo(String s){
		Logger.d(s);
	}
	
	public static void jsonInfo(String json) {
		Logger.json(json);
	}
}
