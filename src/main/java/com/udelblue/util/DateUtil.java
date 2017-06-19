package com.udelblue.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {

	public static String DateNow() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String DateNowPlusDay(int days) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		// Date day = dateFormat.parse(string);
		Date day = new Date();
		Date dayAfter = new Date(day.getTime() + TimeUnit.DAYS.toMillis(days));
		return dateFormat.format(dayAfter);
	}

	public static String DateNowPlusOne() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		// Date day = dateFormat.parse(string);
		Date day = new Date();
		Date dayAfter = new Date(day.getTime() + TimeUnit.DAYS.toMillis(1));
		return dateFormat.format(dayAfter);
	}

	public static String DateTimeNow() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
