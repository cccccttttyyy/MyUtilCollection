package String;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * 所属模块: 工具包
 * 说明：常用字符串及数组操作
 */
public class StringUtils {
    private static Log log = LogFactory.getLog(StringUtils.class);//日志
    private static StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

    public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i ++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }

    public static String getInitialUpperCase(String str) {
        if(str != null && str.length() > 0) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return str;
    }

    private final static String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化小数保持最少和最多小数点.
     *
     *  @param  num
     *  @param  minFractionDigits
     *  @param  maxFractionDigits
     *  @return
     */
    public   static  String formatFraction( double  num,  int  minFractionDigits,  int  maxFractionDigits) {
        //  输出固定小数点位数
        java.text.NumberFormat nb  =  java.text.NumberFormat.getInstance();
        nb.setMaximumFractionDigits(maxFractionDigits);
        nb.setMinimumFractionDigits(minFractionDigits);
        nb.setGroupingUsed( false );
        String rate  =  nb.format(num);
        return  rate;
    }

    /**
     * 得到当前日期前后的日期
     * @return 返回日期字符串
     */
    public static String getBefDateString(int day_i){
        Calendar day=Calendar.getInstance();
        day.add(Calendar.DATE,day_i);
        return new SimpleDateFormat(dateFormat).format(day.getTime());
    }

    /**
     * 获取当前日期的字符串长度
     * @return
     */
    public static int getDateTimeLen(){
        return dateFormat.length();
    }

    /**
     * 获取当前日期
     * @return
     */
    public static String getDateTime(){
        return new   SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
    }

    /**
     * 获取当前日期
     * @return
     */
    public static String getDate(){
        return new   SimpleDateFormat( "yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    /**
     * 获取当前日期（下划线分隔）
     * @return
     */
    public static String getDateBottom(){
        return new   SimpleDateFormat( "yyyy_MM_dd").format(Calendar.getInstance().getTime());
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getTime(){
        return new   SimpleDateFormat( "HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    /**
     * 字符串转换为毫秒
     * @return
     */
    public static long parseLongDate(String str){
        try {
            return new SimpleDateFormat(dateFormat).parse(str).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 计算两个字符串转换为日期后相减得到的分钟
     * @param str1
     * @param str2
     * @return
     */
    public static int getSubTime(String str1,String str2){
        return (int)(parseLongDate(str2) - parseLongDate(str1))/(1000*60);
    }

    /**
     * 计算两个字符串转换为日期后相减得到的天数
     * @param str1
     * @param str2
     * @return
     */
    public static long getSubDay(String str1,String str2){
        return (parseLongDate(str2) - parseLongDate(str1))/(1000*60*24*60);
    }
    /**
     * Change the null string value to "", if not null, then return it self, use
     * this to avoid display a null string to "null".
     *
     *  @param  input
     *            the string to clear
     *  @return  the result
     */
    public   static  String clearNull(String input) {
        return  isEmpty(input)  ?   ""  : input;
    }

    /**
     * 转换由表单读取的数据的内码(从 ISO8859 转换到 gb2312).
     *
     *  @param  input
     *            输入的字符串
     *  @return  转换结果, 如果有错误发生, 则返回原来的值
     */
    public   static  String toChi(String input) {
        try  {
            byte [] bytes  =  input.getBytes( " ISO8859-1 " );
            return   new  String(bytes,  " GBK " );
        }  catch  (Exception ex) {
            log.error(ex);
        }
        return  input;
    }

    /**
     * 转换由表单读取的数据的内码到 ISO(从 GBK 转换到ISO8859-1).
     *
     *  @param  input
     *            输入的字符串
     *  @return  转换结果, 如果有错误发生, 则返回原来的值
     */
    public   static  String toISO(String input) {
        return  changeEncoding(input,  " GBK " ,  " ISO8859-1 " );
    }

    /**
     * 转换字符串的内码.
     *
     *  @param  input
     *            输入的字符串
     *  @param  sourceEncoding
     *            源字符集名称
     *  @param  targetEncoding
     *            目标字符集名称
     *  @return  转换结果, 如果有错误发生, 则返回原来的值
     */
    public   static  String changeEncoding(String input, String sourceEncoding,
                                           String targetEncoding) {
        if  (input  ==   null   ||  input.equals( "" )) {
            return  input;
        }

        try  {
            byte [] bytes  =  input.getBytes(sourceEncoding);
            return   new  String(bytes, targetEncoding);
        }  catch  (Exception ex) {
            log.error(ex);
        }
        return  input;
    }


    /**
     * 对给定字符进行 URL 编码
     */
    public   static  String encode(String value) {
        if  (isEmpty(value)) {
            return   "" ;
        }

        try  {
            value  =  java.net.URLEncoder.encode(value,  " GB2312 " );
        }  catch  (Exception ex) {
            log.error(ex);
        }

        return  value;
    }

    /**
     * 对给定字符进行 URL 解码
     *
     *  @param  value
     *            解码前的字符串
     *  @return  解码后的字符串
     */
    public   static  String decode(String value) {
        if  (isEmpty(value)) {
            return   "" ;
        }

        try  {
            return  java.net.URLDecoder.decode(value,  " GB2312 " );
        }  catch  (Exception ex) {
            log.error(ex);
        }

        return  value;
    }

    /**
     * 判断map是否未空, 如果不为 null 或者长度大于0, 均返回 true.
     */
    public   static   boolean  isNotEmptyMap(Map<?,?> input) {
        return  (input  !=   null   &&  input.size()  >   0 );
    }

    /**
     * 判断list是否未空, 如果不为 null 或者长度大于0, 均返回 true.
     */
    public   static   boolean  isNotEmptyList(List<?> input) {
        return  (input  !=   null   &&  input.size()  >   0 );
    }

    /**
     * 判断字符串数组是否未空, 如果不为 null 或者长度大于0, 均返回 .
     */
    public   static   boolean  isNotEmptyArray(String[] input) {
        return  (input  !=   null   &&  input.length  >   0 );
    }


    /**
     * 判断字符串是否未空, 如果为 null 或者长度为0, 均返回 true.
     */
    public   static   boolean  isEmpty(String input) {
        return  (input  ==   null   ||  input.length()  ==   0 );
    }

    /**
     * 判断字符串是否未空, 如果为 null 或者长度为0, 均返回 true.
     */
    public   static   boolean  isEmptyObject(Object input) {
        return  (input  ==   null   );
    }

    /**
     * 判断字符串是否未空, 如果为 null 或者长度为0, 均返回 true.
     */
    public   static   boolean  isNotEmpty(String input) {
        return  !isEmpty(input);
    }

    /**
     * 将字符串转换为整型数字
     * @param str
     * @return
     */
    public static int parseInt(String str){
        if(isNotEmpty(str)){
            return Integer.parseInt(str);
        }else{
            return 0;
        }
    }

    /**
     * 获得输入字符串的字节长度(即二进制字节数), 用于发送短信时判断是否超出长度.
     *
     *  @param  input
     *            输入字符串
     *  @return  字符串的字节长度(不是 Unicode 长度)
     */
    public   static   int  getBytesLength(String input) {
        if  (input  ==   null ) {
            return   0 ;
        }
        int  bytesLength  =  input.getBytes().length;
        return  bytesLength;
    }

    /**
     * 将日期转换为中文表示方式的字符串(格式为 yyyy年MM月dd日 HH:mm:ss).
     */
    public   static  String dateToChineseString(Date date) {
        if  (date  ==   null ) {
            return   "" ;
        }

        java.text.SimpleDateFormat dateFormat  =   new  java.text.SimpleDateFormat(
                " yyyy年MM月dd日 HH:mm:ss " );

        return  dateFormat.format(date);
    }

    /**
     * 将日期转换为 14 位的字符串(格式为yyyyMMddHHmmss).
     */
    public   static  String dateTo14String(Date date) {
        if  (date  ==   null ) {
            return   null ;
        }

        java.text.SimpleDateFormat dateFormat  =   new  java.text.SimpleDateFormat(
                " yyyyMMddHHmmss " );

        return  dateFormat.format(date);
    }

    /**
     * 将 14 位的字符串(格式为yyyyMMddHHmmss)转换为日期.
     */
    public   static  Date string14ToDate(String input) {
        if  (isEmpty(input)) {
            return   null ;
        }

        if  (input.length()  !=   14 ) {
            return   null ;
        }

        java.text.SimpleDateFormat dateFormat  =   new  java.text.SimpleDateFormat(
                " yyyyMMddHHmmss " );

        try  {
            return  dateFormat.parse(input);
        }  catch  (Exception ex) {
            log.error(ex);
        }

        return   null ;
    }

    public   static  String replaceChar(String s,  char  c,  char  c1) {
        if  (s  ==   null ) {
            return   "" ;
        }
        return  s.replace(c, c1);
    }

    public   static  String replaceString(String s, String s1, String s2) {
        if  (s  ==   null   ||  s1  ==   null   ||  s2  ==   null ) {
            return   "" ;
        }
        return  s.replaceAll(s1, s2);
    }

    public   static  String toHtml(String s) {
        s  =  replaceString(s,  " < " ,  " &#60; " );
        s  =  replaceString(s,  " > " ,  " &#62; " );
        return  s;
    }

    public   static  String toBR(String s) {
        s  =  replaceString(s,  " \n " ,  " <br>\n " );
        s  =  replaceString(s,  " \t " ,  " &nbsp;&nbsp;&nbsp;&nbsp; " );
        s  =  replaceString(s,  "    " ,  " &nbsp;&nbsp; " );
        return  s;
    }

    public   static  String toSQL(String s) {
        s  =  replaceString(s,  " \r\n " ,  " \n " );
        return  s;
    }

    public   static  String replaceEnter(String s)  throws  NullPointerException {
        return  s.replaceAll( " \n " ,  " <br> " );
    }

    public   static  String replacebr(String s)  throws  NullPointerException {
        return  s.replaceAll( " <br> " ,  " \n " );
    }

    public   static  String replaceQuote(String s)  throws  NullPointerException {
        return  s.replaceAll( " ' " ,  " '' " );
    }

    public static int toInteger(Object str){
        return Integer.parseInt(str.toString());
    }

    public static String subString(String str,int count){
        if(isEmpty(str))return null;
        if(str.length() > count){
            return str.substring(0,count) + "...";
        }else{
            return str;
        }
    }

    public static boolean isNum(String str){
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
    /**
     * 1：首先遍历需要替换的字符串，生成字符串数组以#分割
     * 2：找传入的参数,有此参数，有则替换，没有则直接返回
     * @param rowkey
     * @param str
     * @param params
     * @return
     */
    public static boolean replaceParam(StringBuffer paramRule,String str,Map<String,Object> params){
        boolean b = true;
        String[] rowkeys = str.split("#");
        //
        for(int i=0;i<rowkeys.length;i++){
            String rowItem = rowkeys[i];
            //只检查#中间的字符串
            if((i+1)%2==0){
                if(null!=params.get(rowItem)){
                    rowItem = params.get(rowItem).toString();
                }else{
                    b = false;
                    break;
                }
            }
            paramRule.append(rowItem);
        }
        return b;
    }
    /**
     * 1：用前台传来的参数替换rowkey
     * @param rowkey
     * @param str
     * @param params
     * @return
     */
    public static String replaceRowkeyParam(String str,String split,List<Map<String,Object>> rule){
        StringBuffer paramRule = new StringBuffer("");
        String[] rowkeys = str.split(split);
        for(int i=0;i<rowkeys.length;i++){
            String rowItem = rowkeys[i];
            boolean h = false;
            int k = rule.size();
            for (int r = 0; r < k;r++) {
                Map<String,Object> ruleMap = rule.get(r);
                String name = ruleMap.get("name").toString();
                if(name.equals(rowItem)){
                    //存在rowkey中的参数
                    h = true;
                    paramRule.append(ruleMap.get("value"));
                    if(i<rowkeys.length-1){
                        paramRule.append(split);
                    }
                }
            }
            if(!h){
                break;
            }
        }
        return paramRule.toString();
    }
    /**
     * 数据加密方法
     * @param rule
     * @param str
     * @return
     */
    public static String getHideString(String rule,String str){
        String rules[] = rule.split("-");
        int start = Integer.parseInt(rules[0]);
        int end = Integer.parseInt(rules[1]);
        if(null==str)
            return str;
//		String v = str;
//		if(start<=str.length()){
//			v = str.substring(0,start-1) +"**";
//		}else{
//			return v;
//		}
//		if(end<=str.length()&&(start+end)<=str.length()+1){
//			v = v + str.substring(str.length()-end+1, str.length());
//		}
//		v = str.substring(0,start)+"**"+str.substring(start+end);
        StringBuffer v = new StringBuffer();
        if(str.length()>start){
            v.append(str.substring(0,start-1));
        }else{
            v.append(str);
        }
        int l = str.length();
        if(l>start){
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<end&&i<l-start;i++){
                sb.append("*");
            }
            v.append(sb.toString());
            if(l>start+end){
                v.append(str.substring(start+end-1));
            }
        }
        return v.toString();
    }

    /**
     * 判断字符串数组是否未空, 如果不为 null 或者长度大于0, 均返回 .
     */
    public   static   boolean  isNotEmptyObject(Object obj) {
        return  (obj != null && isNotEmpty(obj.toString()));
    }

    /**
     * 获取YYYYMMDD hh:mm:ss格式
     * @return
     */
    public static String getTime19() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * list转化成字符串
     * @param list
     * @param separator 分割符
     * @return
     */
    public static String listToString(List list, char separator) {
        if(list != null && list.size() > 0){
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                str.append(list.get(i)).append(separator);
            }
            str.deleteCharAt(str.length()-1);
            return str.toString();
        }
        return null;
    }

    public static void listSort(List<Map<String, Object>> resultList){
        // resultList是需要排序的list，其内放的是Map返回的结果集
        Collections.sort(resultList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                int num1 =Integer.parseInt(String.valueOf(o1.get("num")));
                int num2 = Integer.parseInt(String.valueOf(o2.get("num")));
                if (num1<num2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if ((cs == null) || ((strLen = cs.length()) == 0))
            return true;
        for (int i = 0; i < strLen; ++i) {
            if (!(Character.isWhitespace(cs.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return (!(isBlank(cs)));
    }
    /**
     * 字符串数组去重
     */
    public static String[] stringListToHeavy(String[] strList){
        List<String> list = Arrays.asList(strList);
        Set<String> set = new HashSet<String>(list);
        String [] rid=(String [])set.toArray(new String[0]);
        return rid;

    }
}
