package Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/** 
 * <Description> 日期处理工具类<br> 
 *  
 * @author wang.yong<br>
 * @version 1.0<br>
 * @CreateDate 2018年11月16日 <br>
 * @see com.inspur.datafactory.common <br>
 */
public class DateUtils {
	
	public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE = "yyyy-MM-dd";
	public static final String SHORTDATE = "yy-MM-dd";
	public static final String TIME = "HH:mm:dd";
	public static final String YEAR = "yyyy";
	public static final String MONTH = "MM";
	public static final String DAY = "dd";
	public static final String HOUR = "HH";
	public static final String MINUTE = "mm";
    public static final String SEC = "ss";
    public static final String DATETIMECHINESE = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String DATECHINESE = "yyyy年MM月dd日";
    public static final String SIMPLEDATECHINESE = "MM月dd日";
   
	/**
	 * Description: 取得自定义格式的当前时间后再过几分钟的时间，如12:05以后5分钟的时间<br> 
	 *  
	 * @author wang.yong<br>
	 * @param timeNum
	 *  往后几分钟
	 * @param type
	 * @return 自定义类型的时间字符串<br>
	 */ 
	public static String crateTimeFromNowTimeByTime(int timeNum, String type) {
        Calendar calendar = new GregorianCalendar(Integer.parseInt(getYear()),
                Integer.parseInt(getMonth()) - 1, Integer.parseInt(getDay()),
                Integer.parseInt(getHour()), Integer.parseInt(getMinute()),
                Integer.parseInt(getSec()));
        calendar.add(Calendar.MINUTE, timeNum);
        return new SimpleDateFormat(type).format(calendar.getTime());
    }
    /**
     * Description: 判断一个字符串日期是否过期<br> 
     *  
     * @author wang.yong<br>
     * @param dateTime
     * @return int 
     *    过期返回1，不过期返回0
     * @throws ParseException <br>
     */ 
    public static int isOutOfDate(String dateTime){
        long nowTimeLong = System.currentTimeMillis();
        long ckTimeLong = 0L;
        try {
            ckTimeLong = new SimpleDateFormat(DATETIME).parse(dateTime)
                    .getTime();
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (nowTimeLong - ckTimeLong > 0) {// 过期
            return 1;
        }
        return 0;
    }
    /**
     * Description:判断是否在一个起止日期内 <br> 
     *  
     * @author wang.yong<br>
     * @param start_time
     *   开始时间
     * @param over_time
     *   结束时间
     * @return int
     *   在这个时间段内返回1，不在返回0
     * @throws ParseException <br>
     */ 
    public static int isOutOfDate(String start_time, String over_time)
            throws ParseException {
        long nowTimeLong = System.currentTimeMillis();
        long ckStartTimeLong = new SimpleDateFormat(DATETIME).parse(start_time)
                .getTime();
        long ckOverTimeLong = new SimpleDateFormat(DATETIME).parse(over_time)
                .getTime();
        if (nowTimeLong > ckStartTimeLong && nowTimeLong < ckOverTimeLong) {
            return 1;
        }
        return 0;
    }
    /**
     * Description: 判断一个自定义日期是否在一个起止日期内<br> 
     *  例如:判断2012-01-05 00:00:00是否在2012-04-05 00:00:00~2012-04-15 00:00:00
     * @author wang.yong<br>
     * @param time
     *   自定义的时间
     * @param start_time
     *   开始时间
     * @param over_time
     *   结束时间
     * @return int
     *   在这个时间段内返回1，不在返回0
     * @throws ParseException <br>
     */ 
    public static int isOutOfDate(String time, String start_time,
            String over_time) throws ParseException {
        long timeLong = new SimpleDateFormat(DATETIME).parse(time).getTime();
        long ckStartTimeLong = new SimpleDateFormat(DATETIME).parse(start_time)
                .getTime();
        long ckOverTimeLong = new SimpleDateFormat(DATETIME).parse(over_time)
                .getTime();
        if (timeLong > ckStartTimeLong && timeLong < ckOverTimeLong) {
            return 1;
        }
        return 0;
    }
    /**
     * Description:判断当前时间是否在一个时间段内 <br> 
     *  
     * @author wang.yong<br>
     * @param time_limit_start 开始时间
     * @param time_limit_over 结束时间
     * @return (int) 1在这个时间段内，0不在这个时间段内
     * @throws ParseException <br>
     */ 
    public static int isInTime(String time_limit_start, String time_limit_over)
            throws ParseException {
        // 获取当前日期
        String nowDate = new SimpleDateFormat(DATE).format(System.currentTimeMillis());
        return isOutOfDate(nowDate + " " + time_limit_start, nowDate + " "
                + time_limit_over);
    }
    /**
     * Description: 判断一个自定义时间是否在一个时间段内<br> 
     *  例如:判断02:00是否在08:00~10:00时间段内
     * @author wang.yong<br>
     * @param time 自定义时间
     * @param time_limit_start 开始时间
     * @param time_limit_over 结束时间
     * @return (int) 1在这个时间段内，0不在这个时间段内
     * @throws ParseException <br>
     */ 
    public static int isInTime(String time, String time_limit_start,
            String time_limit_over) throws ParseException {
        String nowDate = new SimpleDateFormat(DATE).format(System.currentTimeMillis());
        return isOutOfDate(nowDate + " " + time, nowDate + " "
                + time_limit_start, nowDate + " " + time_limit_over);
    }
    /**
     * Description:取得自定义月份后的日期，如13个月以后的时间 <br> 
     *  
     * @author wang.yong<br>
     * @param monthNum
     *  往后几个月
     * @return yyyy-MM-dd HH:mm:ss类型时间字符串<br>
     */ 
    public static String crateTimeFromNowTimeByMonth(int monthNum) {
        Calendar calendar = new GregorianCalendar(Integer.parseInt(getYear()),
                Integer.parseInt(getMonth()) - 1, Integer.parseInt(getDay()),
                Integer.parseInt(getHour()), Integer.parseInt(getMinute()),
                Integer.parseInt(getSec()));
        calendar.add(Calendar.MONTH, monthNum);
        return new SimpleDateFormat(DATETIME).format(calendar.getTime());
    }
    /**
     * Description: 取得自定义天数后的日期，如13天以后的时间<br> 
     *  
     * @author wang.yong<br>
     * @param dayNum
     *   往后几天
     * @return yyyy-MM-dd HH:mm:ss类型时间字符串<br>
     */ 
    public static String crateTimeFromNowTimeByDay(int dayNum) {
        Calendar calendar = new GregorianCalendar(Integer.parseInt(getYear()),
                Integer.parseInt(getMonth()) - 1, Integer.parseInt(getDay()),
                Integer.parseInt(getHour()), Integer.parseInt(getMinute()),
                Integer.parseInt(getSec()));
        calendar.add(Calendar.DATE, dayNum);
        return new SimpleDateFormat(DATETIME).format(calendar.getTime());
    }
                              
    /**
     * Description: 取得自定义天数后的日期，如13天以后的时间<br> 
     *  
     * @author wang.yong<br>
     * @param dayNum
     *  往后几天
     * @return yyyy-MM-dd类型时间字符串<br>
     */ 
    public static String crateTimeFromNowDayByDay(int dayNum) {
        Calendar calendar = new GregorianCalendar(Integer.parseInt(getYear()),
                Integer.parseInt(getMonth()) - 1, Integer.parseInt(getDay()),
                Integer.parseInt(getHour()), Integer.parseInt(getMinute()),
                Integer.parseInt(getSec()));
        calendar.add(Calendar.DATE, dayNum);
        return new SimpleDateFormat(DATE).format(calendar.getTime());
    }  
    /**
     * Description:计算两个时间间隔(精确到分钟) <br> 
     *  
     * @author wang.yong<br>
     * @param startDay
     *  开始日(整型):0表示当日，1表示明日
     * @param startTime
     *  开始时间(24h):00:00
     * @param endDay
     *  结束日(整型):0表示当日，1表示明日，限制：大于等于 startDay
     * @param endTime
     * 结束时间(24h):23:50
     * @return String格式化的日期格式：DD天HH小时mm分<br>
     */ 
    public static String calculateIntervalTime(int startDay, String startTime,
            int endDay, String endTime) {
        int day = endDay - startDay;
        int hour = 0;
        int mm = 0;
        if (day < 0) {
            return null;
        } else {
            int sh = Integer.valueOf(startTime.split(":")[0]);
            int eh = Integer.valueOf(endTime.split(":")[0]);
            int sm = Integer.valueOf(startTime.split(":")[1]);
            int em = Integer.valueOf(endTime.split(":")[1]);
            hour = eh - sh;
            if (hour > 0) {
                mm = em - sm;
                if (mm < 0) {
                    hour--;
                    mm = 60 + mm;
                }
            } else {
                day = day - 1;
                hour = 24 + hour;
                mm = em - sm;
                if (mm < 0) {
                    hour--;
                    mm = 60 + mm;
                }
            }
        }
        if (hour == 24) {
            day++;
            hour = 0;
        }
        if (day != 0) {
            return day + "天" + hour + "小时" + mm + "分";
        } else {
            return hour + "小时" + mm + "分";
        }
    }
    /**
     * Description: 计算两个时间差,返回毫秒数<br> 
     *  
     * @author wang.yong<br>
     * @param startTime
     * @param endTime
     * @return long<br>
     */ 
    public static long calculateIntervalTime(String startTime, String endTime){
        return parseDateTime(endTime).getTime()
                - parseDateTime(startTime).getTime();
    }
    /**
     * Description: string类型时间转换成Date类型<br> 
     *  
     * @author wang.yong<br>
     * @param datetime
     * @return Date<br>
     */ 
    public static Date parseDateTime(String datetime){
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME);
        Date parseDateTime = null;
        try {
            parseDateTime = sdf.parse(datetime);
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return parseDateTime;
    }
    /**
     * Description: 毫秒数转换为yyyy-MM-dd HH:mm:ss格式时间<br> 
     *  
     * @author wang.yong<br>
     * @param milliseconds
     * @return String<br>
     */ 
    public static String parseMillisecondsToDateTime(long milliseconds) {
    	Date date = new Date(milliseconds);
    	SimpleDateFormat sd = new SimpleDateFormat(DATETIME);
    	return sd.format(date);
    }
    /**
     * Description: 日期类型字符串转换为毫秒数<br> 
     *  
     * @author wang.yong<br>
     * @param date
     * @return long<br>
     */ 
    public static long parseStringDateToMilliseconds(String date){
    	Date dateTime = parseDateTime(date);
    	return dateTime.getTime();
    }
    /**
     * Description: yyyy-MM-dd HH:mm:ss格式当前时间<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getCurrentDateTime() {
        return new SimpleDateFormat(DATETIME).format(System.currentTimeMillis());
    }
    /**
     * Description: yyyy年MM月dd日 HH时mm分ss秒格式当前时间<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getCurrentChineseDateTime() {
        return new SimpleDateFormat(DATETIMECHINESE).format(System.currentTimeMillis());
    }
    /**
     * Description: yyyy年MM月dd日格式当前时间<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getCurrentChineseDate() {
        return new SimpleDateFormat(DATECHINESE).format(System.currentTimeMillis());
    }
    /**
     * Description: MM月dd日格式当前时间<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getCurrentSimpleChineseDate() {
        return new SimpleDateFormat(SIMPLEDATECHINESE).format(System.currentTimeMillis());
    }
    /**
     * Description:MM月dd日格式的中文时间 如果num为-1表示前一天 1为后一天 0为当天 <br> 
     *  
     * @author wang.yong<br>
     * @param num
     * @return String<br>
     */ 
    public static String getSimpleChineseDate(int num) {
        Date d = parseDateTime(crateTimeFromNowTimeByDay(num));
        return new SimpleDateFormat(SIMPLEDATECHINESE).format(d);
    }
    /**
     * Description: HH:mm:dd格式当前时间<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getTime() {
        return new SimpleDateFormat(TIME).format(System.currentTimeMillis());
    }
    /**
     * Description: yyyy格式当前年份<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getYear() {
        return new SimpleDateFormat(YEAR).format(System.currentTimeMillis());
    }
    /**
     * Description: 当前月份<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getMonth() {
        return new SimpleDateFormat(MONTH).format(System.currentTimeMillis());
    }
    /**
     * Description: 当前日<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getDay() {
        return new SimpleDateFormat(DAY).format(System.currentTimeMillis());
    }
    /**
     * Description: 当前小时<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getHour() {
        return new SimpleDateFormat(HOUR).format(System.currentTimeMillis());
    }
    /**
     * Description:当前分钟 <br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getMinute() {
        return new SimpleDateFormat(MINUTE).format(System.currentTimeMillis());
    }
    /**
     * Description: 当前秒<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getSec() {
        return new SimpleDateFormat(SEC).format(System.currentTimeMillis());
    }
    
    /**
     * Description:yyyy-MM-dd格式昨天时间 <br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getYestday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date d = cal.getTime();
        return new SimpleDateFormat(DATE).format(d);// 获取昨天日期
    }
    /**
     * Description: yy-MM-dd格式昨天时间<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getShortYestday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date d = cal.getTime();
        return new SimpleDateFormat(SHORTDATE).format(d);// 获取昨天日期
    }
    
    /**
     * Description: yy-MM-dd格式当天时间<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getShortToday() {
    	return new SimpleDateFormat(SHORTDATE).format(System.currentTimeMillis());
    }
    /**
     * Description: 本周一yyyy-MM-dd HH:mm:ss格式时间<br> 
     *  
     * @author wang.yong<br>
     * @return String<br>
     */ 
    public static String getMonday() {
        Calendar calendar = new GregorianCalendar();
        // 取得本周一
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return new SimpleDateFormat(DATETIME).format(calendar.getTime());// 获取昨天日期
    }
}
