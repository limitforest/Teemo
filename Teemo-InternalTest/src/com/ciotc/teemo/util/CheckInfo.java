package com.ciotc.teemo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class CheckInfo {
public static boolean isBirthday(String birth) {
	boolean flag = false;
	StringBuffer sb = new StringBuffer();
	/*sb.append("^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))")
			.append("|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))")
			.append("|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-")
			.append("(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])")
			.append("|((16|[2468][048]|[3579][26])00))-0?2-29))$");*/
	
	sb.append("^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$");
	
	
	
//	sb.append("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))");
	Pattern pt = Pattern.compile(sb.toString());
	if (pt.matcher(birth).matches()) {
		flag = true;
	}
	return flag;
}

// 填入的日期和当前日期进行比较。
public static boolean compareDateToNow(String birthday) {
	// Date now = new Date();
	SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd");
	Date now = new Date();
	String nowF = dateF.format(now);
	// 生日要比当前日期小。
	if (nowF.compareTo(birthday) < 0) {
		JOptionPane.showMessageDialog(null, "请输入合法出生日期。");
		return false;
	} else {
		return true;
	}
}
}
