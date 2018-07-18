package com.anji.tmsexpand.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static void main(String[] args) {
		String [] dates = getWeekDays(2018, 27);
		for(int i=0;i<dates.length;i++) {
			System.out.println(dates[i]);
		}
	}

	/**
	 * 根据具体年份周数获取日期范围
	 * 
	 * @param year
	 * @param week
	 * @param targetNum
	 * @return
	 */
	public static String[] getWeekDays(int year, int week) {
		String[] dates = new String[7];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year); 
		cal.set(Calendar.WEEK_OF_YEAR, week); 
		cal.set(Calendar.DAY_OF_WEEK, 2); // 1表示周日，2表示周一，7表示周六
		Date firstDay = cal.getTime();
		dates[0] = sdf.format(firstDay.getTime());
		
		cal.set(Calendar.DAY_OF_WEEK, 3); // 1表示周日，2表示周一，7表示周六
		Date secondDay = cal.getTime();
		dates[1] = sdf.format(secondDay.getTime());
		
		cal.set(Calendar.DAY_OF_WEEK, 4); // 1表示周日，2表示周一，7表示周六
		Date thirdDay = cal.getTime();
		dates[2] = sdf.format(thirdDay.getTime());
		
		cal.set(Calendar.DAY_OF_WEEK, 5); // 1表示周日，2表示周一，7表示周六
		Date fourthDay = cal.getTime();
		dates[3] = sdf.format(fourthDay.getTime());
		
		cal.set(Calendar.DAY_OF_WEEK, 6); // 1表示周日，2表示周一，7表示周六
		Date fifthDay = cal.getTime();
		dates[4] = sdf.format(fifthDay.getTime());
		
		cal.set(Calendar.DAY_OF_WEEK, 7); // 1表示周日，2表示周一，7表示周六
		Date sixthDay = cal.getTime();
		dates[5] = sdf.format(sixthDay.getTime());
		
		cal.set(Calendar.WEEK_OF_YEAR, week + 1); 
		cal.set(Calendar.DAY_OF_WEEK, 1); // 1表示周日，2表示周一，7表示周六
		Date lastDay = cal.getTime();
		dates[6] = sdf.format(lastDay.getTime());
		
		return dates;
	}
	
	/**
	 * 获取当前日期是第几周
	 * @param date
	 * @return
	 */
	public static int getWeekNum(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		int weekNum = calendar.get(Calendar.WEEK_OF_YEAR);
		return weekNum;

	}
	
	/** 
    * 获取过去第几天的日期 
    * 
    * @param past 
    * @return 
    */  
   public static String getPastDate(int past) {  
       Calendar calendar = Calendar.getInstance();  
       calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);  
       Date today = calendar.getTime();  
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
       String result = format.format(today);  
       return result;  
   } 
}
