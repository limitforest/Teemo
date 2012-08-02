package com.ciotc.teemo.action;


import java.lang.reflect.Constructor;

import javax.swing.Action;
import javax.swing.Icon;

import com.ciotc.teemo.file.controller.DisplayController;

public class ActionConstructor {
	
	public static Action constructAction(String key,DisplayController displayController,String name,Icon icon) throws Exception{
//		StringBuilder sb = new StringBuilder();
//		sb.append("com.ciotc.teemo.action.");
//		sb.append(key.toUpperCase().substring(0, 1));
//		sb.append(key.toLowerCase().substring(1));
//		sb.append("Action");
		String s = "com.ciotc.teemo.action."+key;
		Class<?> c = Class.forName(s);
		Constructor<?> con = c.getConstructor(new Class[]{DisplayController.class,String.class,Icon.class});
		return (Action) con.newInstance(displayController,name,icon);
	}
}
