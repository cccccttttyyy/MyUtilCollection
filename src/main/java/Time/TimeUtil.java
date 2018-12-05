package Time;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeUtil {
	/**
	 * 获取当前系统时间
	 * @return
	 */
	public static  String getCurrentTime(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String date = df.format(new Date(System.currentTimeMillis())).toString();		
		return date;
	}	
	/**
	 * 获取当前系统时间
	 * @return
	 */
	public static  String date2String(java.util.Date date){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dateStr = df.format(date).toString();		
		return dateStr;
	}	
	/**
	 * 获取当前系统时间
	 * @return
	 */
	public static String getNextDayTime(){
		Calendar today = getCurrentCalendarTime();
		Calendar tomorrow = Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		int month = today.get(Calendar.MONTH);
		int day = today.get(Calendar.DAY_OF_MONTH);
		int hour = today.get(Calendar.HOUR_OF_DAY);
		int minute = today.get(Calendar.MINUTE);
		int second = today.get(Calendar.SECOND);
		int millisecond = today.get(Calendar.MILLISECOND);
		tomorrow.set(Calendar.YEAR, year);
		tomorrow.set(Calendar.MONTH, month);
		tomorrow.set(Calendar.DAY_OF_MONTH, day+1);
		tomorrow.set(Calendar.HOUR_OF_DAY, hour);
		tomorrow.set(Calendar.MINUTE, minute);
		tomorrow.set(Calendar.SECOND, second);
		tomorrow.set(Calendar.MILLISECOND, millisecond);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(tomorrow.getTime()).toString();	
		//System.out.println("过期时间:"+date);
		return date;
	}
	public static String getCurrentMonth(){
		Calendar nowdate = Calendar.getInstance();
		int month = nowdate.get(Calendar.MONTH);
		return String.valueOf(month+1);
	}
	
	public static String getCurrentDay(){
		Calendar nowdate = Calendar.getInstance();
		int day = nowdate.get(Calendar.DAY_OF_MONTH);
		return String.valueOf(day);
	}
	/**
	 * 获取当前系统日期
	 * @return
	 */
	public static String getCurrentDate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String date = df.format(new Date(System.currentTimeMillis())).toString();		
		return date;
	}
	public static String getCurrentDate(String format){
		SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
		String date = df.format(new Date(System.currentTimeMillis())).toString();
		return date;
	}
	/**
	 * 获取当前系统日期
	 * @return
	 */
	public static Calendar getCurrentCalendarTime(){
		Calendar nowdate = Calendar.getInstance();
		int hour = nowdate.get(Calendar.HOUR_OF_DAY);
		int minute = nowdate.get(Calendar.MINUTE);
		int second = nowdate.get(Calendar.SECOND);
		int millisecond = nowdate.get(Calendar.MILLISECOND);
		Calendar date = Calendar.getInstance();
	    date.set(Calendar.HOUR_OF_DAY, hour);
	    date.set(Calendar.MINUTE, minute);
	    date.set(Calendar.SECOND, second);
	    date.set(Calendar.MILLISECOND, millisecond);
		return date;
	}
	/**
	 * 设置定时(hour,minute,second,millisecond不能为空)
	 * @return
	 */
	public static Calendar setCalendarDate(int hour,int minute,int second,int millisecond){
		Calendar date = Calendar.getInstance();
	    date.set(Calendar.HOUR_OF_DAY, hour);
	    date.set(Calendar.MINUTE, minute);
	    date.set(Calendar.SECOND, second);
	    date.set(Calendar.MILLISECOND, millisecond);
		Calendar date1 = getCurrentCalendarTime();
		if(date.before(date1)){
			Calendar nowdate = Calendar.getInstance();
			int year = nowdate.get(Calendar.YEAR);
			int month = nowdate.get(Calendar.MONTH);
			int day = nowdate.get(Calendar.DAY_OF_MONTH);
			date.set(Calendar.YEAR, year);
			date.set(Calendar.MONTH, month);
			date.set(Calendar.DAY_OF_MONTH, day+1);
		}
	    System.out.println("定时:"+date.getTime());
		return date;
	}
	public static long getDelay(){
		long delay = 24*60*60*1000;
		Calendar today = Calendar.getInstance();
		int days = today.getActualMaximum(Calendar.DAY_OF_MONTH);
		delay = days*delay;
		return delay;
	}
	public static long getWeekDelay(){
		long delay = 24*60*60*1000;
		int days = 7;
		delay = days*delay;
		return delay;
	}
	/**
	 * 设置定时(pushTime)
	 * @return
	 * @throws ParseException 
	 */
	public static Calendar setCalendarDateByMonth(String pushTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		java.util.Date pushDate;
		try {
			pushDate = sdf.parse(pushTime);
			Calendar date = Calendar.getInstance();
			date.setTime(pushDate);
			Calendar date1 = getCurrentCalendarTime();
			if(!date.after(date1)){
				date1.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
				date1.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
				date1.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
				date1.set(Calendar.SECOND, date.get(Calendar.SECOND));
				date1.set(Calendar.MILLISECOND, date.get(Calendar.MILLISECOND));
				if(!date1.after(getCurrentCalendarTime())){
					date1.set(Calendar.MONTH, date1.get(Calendar.MONTH)+1);
				}
				date.setTime(date1.getTime());
			}
		    System.out.println("月任务定时:"+date.getTime());
		    return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return null;
	}
	/**
	 * 设置定时(pushTime)
	 * @return
	 * @throws ParseException 
	 */
	public static Calendar setCalendarDateByWeek(String pushTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		java.util.Date pushDate;
		try {
			pushDate = sdf.parse(pushTime);
			Calendar date = Calendar.getInstance();
			date.setTime(pushDate);
			Calendar date1 = getCurrentCalendarTime();
			if(!date.after(date1)){
				date1.set(Calendar.DAY_OF_WEEK, date.get(Calendar.DAY_OF_WEEK));
				date1.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
				date1.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
				date1.set(Calendar.SECOND, date.get(Calendar.SECOND));
				date1.set(Calendar.MILLISECOND, date.get(Calendar.MILLISECOND));
				if(!date1.after(getCurrentCalendarTime())){
					date1.set(Calendar.WEEK_OF_MONTH, date.get(Calendar.WEEK_OF_MONTH)+1);
				}
				date.setTime(date1.getTime());
			}			
		    System.out.println("周任务定时:"+date.getTime());
		    return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return null;
	}
	/**
	 * 设置定时(pushTime)
	 * @return
	 * @throws ParseException 
	 */
	public static Calendar setCalendarDateByDay(String pushTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		java.util.Date pushDate;
		try {
			pushDate = sdf.parse(pushTime);
			Calendar date = Calendar.getInstance();
			date.setTime(pushDate);
			Calendar date1 = getCurrentCalendarTime();
			if(compareTime(date,date1)){
				int year = date1.get(Calendar.YEAR);
				int month = date1.get(Calendar.MONTH);
				int day = date1.get(Calendar.DAY_OF_MONTH);
				date.set(Calendar.YEAR, year);
				date.set(Calendar.MONTH, month);
				date.set(Calendar.DAY_OF_MONTH, day+1);
			}else{
				int year = date1.get(Calendar.YEAR);
				int month = date1.get(Calendar.MONTH);
				int day = date1.get(Calendar.DAY_OF_MONTH);
				date.set(Calendar.YEAR, year);
				date.set(Calendar.MONTH, month);
				date.set(Calendar.DAY_OF_MONTH, day);
			}
		    System.out.println("日任务定时:"+date.getTime());
		    return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return null;
	}
	/**
	 * 比较日期
	 * @param date
	 * @param date1
	 * @return
	 */
	public static boolean compareDate(Calendar date,Calendar date1){	
		if(date.after(date1)){
			return true;
		}else{
			return false;	
		}					
	}	
	/**
	 * 比较时分秒
	 * @param date
	 * @param date1
	 * @return
	 */
	public static boolean compareTime(Calendar date,Calendar date1){
		int hour = date.get(Calendar.HOUR_OF_DAY);
		int minute = date.get(Calendar.MINUTE);
		int mill = date.get(Calendar.MILLISECOND);
		int hour1 = date1.get(Calendar.HOUR_OF_DAY);
		int minute1 = date1.get(Calendar.MINUTE);
		int mill1 = date1.get(Calendar.MILLISECOND);
		int mdate = hour*60*60*1000+minute*60*1000+mill;
		int mdate1 = hour1*60*60*1000+minute1*60*1000+mill1;
		if(mdate<=mdate1){
			return true;
		}else{
			return false;	
		}				
	}	
	/**
	 * 获取当前前几分钟或后几分钟的时间
	 * @param minute
	 * @return
	 */
	public static String getTimeByMinute(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }
	
	public static void main(String args[]) throws ParseException{
		setCalendarDateByMonth("2015-9-15 6:30:00");
		setCalendarDateByMonth("2016-4-15 6:30:00");
		setCalendarDateByMonth("2015-9-25 6:30:00");
		setCalendarDateByMonth("2016-4-25 6:30:00");
	}
}

