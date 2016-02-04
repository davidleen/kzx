package com.android.yijiang.kzx.sdk;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 封装日期处理相关方法 增加ORACLE中的ADD_MONTH ,SYSDATE 等类似功能的方法
 * 
 * @replain
 * @author linwenbin
 * @date 2013-5-31
 * @file DateUtil.java
 */
public class DateUtil {

	/** 采用Singleton设计模式而具有的唯一实例 */
	private static DateUtil instance = new DateUtil();
	private static String defaultPattern = "yyyy-MM-dd";
	/** 格式化器存储器 */
	private static Map<String, SimpleDateFormat> formats;

	private DateUtil() {
		resetFormats();
	}

	/**
	 * 通过缺省日期格式得到的工具类实例
	 * 
	 * @return <code>DateUtilities</code>
	 */
	public static DateUtil getInstance() {
		return instance;
	}

	/** Reset the supported formats to the default set. */
	public void resetFormats() {
		formats = new HashMap<String, SimpleDateFormat>();

		// alternative formats
		formats.put("yyyy-MM-dd HH:mm:ss", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		formats.put("yyyyMMddHHmmssms", new SimpleDateFormat("yyyyMMddHHmmssms"));

		// alternative formats
		formats.put("yyyy-MM-dd", new SimpleDateFormat("yyyy-MM-dd"));

		// XPDL examples format
		formats.put("MM/dd/yyyy HH:mm:ss a", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a"));

		// alternative formats
		formats.put("yyyy-MM-dd HH:mm:ss a", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a"));

		// ISO formats
		formats.put("yyyy-MM-dd'T'HH:mm:ss'Z'", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
		formats.put("yyyy-MM-dd'T'HH:mm:ssZ", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
		formats.put("yyyy-MM-dd'T'HH:mm:ssz", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz"));
	}

	/**
	 * 格式化日期字符串
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if (!formats.containsKey(pattern))
			pattern = defaultPattern;
		DateFormat format = formats.get(pattern);
		return format.format(date);
	}

	/**
	 * 自动判断格式化类型
	 * 
	 * @param date
	 *            需格式化的日期
	 * @return 格式化后的字符串
	 */
	public static String format(Date date) {
		Iterator<SimpleDateFormat> iter = formats.values().iterator();
		while (iter.hasNext()) {
			return iter.next().format(date);
		}
		return null;
	}

	/**
	 * 解析日期
	 * 
	 * @param date
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static Date parse(String date, String pattern) {
		Date resultDate = null;
		try {
			if (!formats.containsKey(pattern))
				pattern = defaultPattern;
			resultDate = formats.get(pattern).parse(date);
		} catch (ParseException e) {
		}
		return resultDate;
	}

	/**
	 * 解析字符串到日期型
	 * 
	 * @param dateString
	 *            日期字符串
	 * @return 返回日期型对象
	 */
	public static Date parse(String dateString) {
		Iterator<SimpleDateFormat> iter = formats.values().iterator();
		while (iter.hasNext()) {
			try {
				return iter.next().parse(dateString);
			} catch (ParseException e) {
			}
		}
		return null;
	}

	/**
	 * 取得当前日期
	 * 
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @param offsetYear
	 * @return 当前时间 + offsetYear
	 */
	public static Timestamp getTimestampExpiredYear(int offsetYear) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, offsetYear);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offsetMonth
	 * @return 当前时间 + offsetMonth
	 */
	public static Timestamp getCurrentTimestampExpiredMonth(int offsetMonth) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, offsetMonth);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offsetDay
	 * @return 当前时间 + offsetDay
	 */
	public static Timestamp getCurrentTimestampExpiredDay(int offsetDay) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, offsetDay);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offsetSecond
	 * @return 当前时间 + offsetSecond
	 */
	public static Timestamp getCurrentTimestampExpiredSecond(int offsetSecond) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.SECOND, offsetSecond);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * @param offsetDay
	 * @return 指定时间 + offsetDay
	 */
	public static Timestamp getTimestampExpiredDay(Date givenDate, int offsetDay) {
		Calendar date = Calendar.getInstance();
		date.setTime(givenDate);
		date.add(Calendar.DATE, offsetDay);
		return new Timestamp(date.getTime().getTime());
	}

	/**
	 * 实现ORACLE中ADD_MONTHS函数功能
	 * 
	 * @param offsetMonth
	 * @return 指定时间 + offsetMonth
	 */
	public static Timestamp getTimestampExpiredMonth(Date givenDate, int offsetMonth) {
		Calendar date = Calendar.getInstance();
		date.setTime(givenDate);
		date.add(Calendar.MONTH, offsetMonth);
		return new Timestamp(date.getTime().getTime());
	}

	/**
	 * @param offsetSecond
	 * @return 指定时间 + offsetSecond
	 */
	public static Timestamp getTimestampExpiredSecond(Date givenDate, int offsetSecond) {
		Calendar date = Calendar.getInstance();
		date.setTime(givenDate);
		date.add(Calendar.SECOND, offsetSecond);
		return new Timestamp(date.getTime().getTime());
	}

	/**
	 * @param offsetSecond
	 * @return 指定时间 + offsetSecond
	 */
	public static Timestamp getTimestampExpiredHour(Date givenDate, int offsetHour) {
		Calendar date = Calendar.getInstance();
		date.setTime(givenDate);
		date.add(Calendar.HOUR, offsetHour);
		return new Timestamp(date.getTime().getTime());
	}

	/**
	 * @return 当前日期 yyyy-MM-dd
	 */
	public static String getCurrentDay() {
		return format(new Date(), defaultPattern);
	}
	
	public static String getCurrentDay(String format) {
		return format(new Date(), format);
	}

	/**
	 * @return 当前的时间戳 yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowTime() {
		return format(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * @return 给出指定日期的月份的第一天
	 */
	public static Date getMonthFirstDay(Date givenDate) {
		Date date = DateUtil.parse(DateUtil.format(givenDate, "yyyy-MM"), "yyyy-MM");
		return date;
	}

	/**
	 * @return 给出指定日期的月份的最后一天
	 */
	public static Date getMonthLastDay(Date givenDate) {
		Date firstDay = getMonthFirstDay(givenDate);
		Date lastMonthFirstDay = DateUtil.getTimestampExpiredMonth(firstDay, 1);
		Date lastDay = DateUtil.getTimestampExpiredDay(lastMonthFirstDay, -1);
		return lastDay;
	}

	/**
	 * method 将字符串类型的日期转换为一个timestamp（时间戳记java.sql.Timestamp）
	 * 
	 * @param dateString
	 *            需要转换为timestamp的字符串
	 * @return dataTime timestamp
	 */
	public final static java.sql.Timestamp string2Time(String dateString) throws java.text.ParseException {
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS", Locale.ENGLISH);// 设定格式
		// dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",
		// Locale.ENGLISH);
		dateFormat.setLenient(false);
		java.util.Date timeDate = dateFormat.parse(dateString);// util类型
		java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());// Timestamp类型,timeDate.getTime()返回一个long型
		return dateTime;
	}

	/**
	 * method 将字符串类型的日期转换为一个Date（java.sql.Date）
	 * 
	 * @param dateString
	 *            需要转换为Date的字符串
	 * @return dataTime Date
	 */
	public final static java.sql.Date string2Date(String dateString) {
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		dateFormat.setLenient(false);
		java.util.Date timeDate = null;
		try {
			timeDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// util类型
		java.sql.Date dateTime = new java.sql.Date(timeDate.getTime());// sql类型
		return dateTime;
	}

	public static void main(String[] args) {
		Date da = new Date();
		// 注意：这个地方da.getTime()得到的是一个long型的值
		System.out.println(da.getTime());

		// 由日期date转换为timestamp

		// 第一种方法：使用new Timestamp(long)
		Timestamp t = new Timestamp(new Date().getTime());
		System.out.println(t);

		// 第二种方法：使用Timestamp(int year,int month,int date,int hour,int minute,int
		// second,int nano)
		Timestamp tt = new Timestamp(Calendar.getInstance().get(Calendar.YEAR) - 1900, Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE), Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(
				Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND), 0);
		System.out.println(tt);

		try {
			String sToDate = "2005-8-18";// 用于转换成java.sql.Date的字符串
			String sToTimestamp = "2005-8-18 14:21:12.123";// 用于转换成java.sql.Timestamp的字符串
			Date date1 = string2Date(sToDate);
			Timestamp date2 = string2Time(sToTimestamp);
			System.out.println("Date:" + date1.toString());// 结果显示
			System.out.println("Timestamp:" + date2.toString());// 结果显示
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getDaysBetween(String beginDate, String endDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date bDate = format.parse(beginDate);
			Date eDate = format.parse(endDate);
			Calendar d1 = new GregorianCalendar();
			d1.setTime(bDate);
			Calendar d2 = new GregorianCalendar();
			d2.setTime(eDate);
			int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
			int y2 = d2.get(Calendar.YEAR);
			if (d1.get(Calendar.YEAR) != y2) {
				d1 = (Calendar) d1.clone();
				do {
					days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天数
					d1.add(Calendar.YEAR, 1);
				} while (d1.get(Calendar.YEAR) != y2);
			}
			return days;
		} catch (ParseException e) {
			// TODO: handle exception
		}
		return 1;
	}
	
//获取指定日期的年份
	public static int getYearOfDate( java.util.Date p_date ) {
	   java.util.Calendar c = java.util.Calendar.getInstance();
	   c.setTime( p_date );
	   return c.get( java.util.Calendar.YEAR );
	}
	 
//获取指定日期的月份
	public static int getMonthOfDate( java.util.Date p_date ) {
	   java.util.Calendar c = java.util.Calendar.getInstance();
	   c.setTime( p_date );
	   return c.get( java.util.Calendar.MONTH ) + 1;
	}

//获取指定日期的日份
	public static int getDayOfDate( java.util.Date p_date ) {
	   java.util.Calendar c = java.util.Calendar.getInstance();
	   c.setTime( p_date );
	   return c.get( java.util.Calendar.DAY_OF_MONTH );
	}

	// 获取指定日期的小时
	public static int getHourOfDate( java.util.Date p_date ) {
	   java.util.Calendar c = java.util.Calendar.getInstance();
	   c.setTime( p_date );
	   return c.get( java.util.Calendar.HOUR_OF_DAY );
	}
	 
//获取指定日期的分钟
	public static int getMinuteOfDate( java.util.Date p_date ) {
	   java.util.Calendar c = java.util.Calendar.getInstance();
	   c.setTime( p_date );
	   return c.get( java.util.Calendar.MINUTE );
	}
	 
	// 获取指定日期的秒钟
	public static int getSecondOfDate( java.util.Date p_date ) {
	   java.util.Calendar c = java.util.Calendar.getInstance();
	   c.setTime( p_date );
	   return c.get( java.util.Calendar.SECOND );
	}
	 
	// 获取指定日期的毫秒  
	public static long getMillisOfDate( java.util.Date p_date ) {
	   java.util.Calendar c = java.util.Calendar.getInstance();
	   c.setTime( p_date );
	   return c.getTimeInMillis();
	}

	public static int dateDiff(String startTime, String endTime, String format) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long diff = 0;
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		day = diff / nd;// 计算差多少天
		hour = diff % nd / nh + day * 24;// 计算差多少小时
		min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
		sec = diff % nd % nh % nm / ns;// 计算差多少秒
		return Integer.parseInt(String.valueOf(day));
	}

	// 一.日期转换为时间戳
	public static long getTimestamp(String unixDate) throws ParseException {
		Date date1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(unixDate);
		Date date2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("1970/01/01 08:00:00");
		long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime() - date2.getTime() : date2.getTime() - date1.getTime();
		long rand = (int) (Math.random() * 1000);

		return rand;
	}
	public static long getTimestamp2(String unixDate) throws ParseException {
		Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(unixDate);
		Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 08:00:00");
		long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime() - date2.getTime() : date2.getTime() - date1.getTime();
		long rand = (int) (Math.random() * 1000);

		return rand;
	}
	/**
	 * 字符串转为时间戳
	 * 
	 * @param timeStr
	 * @return
	 */
	public static String getTime(String timeStr) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d;
		try {
			d = sdf.parse(timeStr);
			long l = d.getTime();
			String str = String.valueOf(l);
			re_time = str.substring(0, 10);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re_time;
	}

	// 二.时间戳转换为date 型
	public static String getDate(String unixDate) {
		SimpleDateFormat fm1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat fm2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		long unixLong = 0;
		String date = "";
		try {
			unixLong = Long.parseLong(unixDate) * 1000;
		} catch (Exception ex) {
			System.out.println("String转换Long错误，请确认数据可以转换！");
		}
		try {
			date = fm1.format(unixLong);
			date = fm2.format(new Date(date));
		} catch (Exception ex) {
			System.out.println("String转换Date错误，请确认数据可以转换！");
		}
		return date;
	}

	public static String getUnixDate(String dateStr) {
		String date = "";
		if (dateStr != null) {
			String math = dateStr.substring(6, dateStr.indexOf("0800)") - 1);
			long long_math = Long.parseLong(math.substring(0, math.length() - 3));
			date = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm").format(new java.util.Date(long_math * 1000));
		} else {
			date = getNowTime();
		}
		return date;
	}

	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	// private final static SimpleDateFormat dateFormater = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// private final static SimpleDateFormat dateFormater2 = new
	// SimpleDateFormat("yyyy-MM-dd");

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	
	private final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm");
		}
	};

	// 把日期转为字符串
	public static String dataToString(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return df.format(date);
	}

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/* 日志 */
	private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
	private static final SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private static final SimpleDateFormat sdfDate2 = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sdfDateTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 将传入Date值转换为想要显示的字符串
	 * 
	 * @param value
	 *            只允许java.util.Date 或 String 类型,其他类型不处理
	 * @return 几秒前，几分钟前，几小时前，几天前，几个月前，几年前，很久以前（10年前）
	 */
	public static String convertToShowStr(Object value) {
		String result = null;
		if (value instanceof Date) {
			result = convertDateToShowStr((Date) value);
		} else if (value instanceof String) {
			result = convertDateToShowStr(convertToDate((String) value));
		}
		return result;
	}

	/**
	 * 将传入Date值转换为想要显示的字符串
	 * 
	 * @param value
	 *            只允许java.util.Date 或 String 类型,其他类型不处理
	 * @return 几月几号
	 */
	public static String convertToShowStr2(Object value) {
		String result = null;
		if (value instanceof Date) {
			result = convertDateToShowStr2((Date) value);
		} else if (value instanceof String) {
			result = convertDateToShowStr2(convertToDate((String) value));
		}
		return result;
	}

	private static String convertDateToShowStr2(Date value) {
		// TODO Auto-generated method stub
		String result = null;
		result = value.getMonth() + 1 + "月" + value.getDate() + "号";
		return result;
	}

	/**
	 * 将传入的时间字符串转换为Date
	 * 
	 * @param value
	 * @return
	 */
	public static Date convertToDate(String value) {
		Date result = null;
		try {
			if (value.indexOf(":") == -1) {
				if (value.indexOf("/") != -1) {
					result = sdfDate.parse(value);
				} else {
					result = sdfDate2.parse(value);
				}
			} else {
				if (value.indexOf("/") != -1) {
					result = sdfDateTime.parse(value);
				} else {
					result = sdfDateTime2.parse(value);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 转换日期到指定格式方便查看的描述说明
	 * 
	 * @param date
	 * @return 几秒前，几分钟前，几小时前，几天前，几个月前，几年前，很久以前（10年前）,如果出现之后的时间，则提示：未知
	 */
	public static String convertDateToShowStr(Date date) {
		String showStr = "";
		long yearSeconds = 31536000L;// 365 * 24 * 60 * 60;
		long monthSeconds = 2592000L;// 30 * 24 * 60 * 60;
		long daySeconds = 86400L;// 24 * 60 * 60;
		long hourSeconds = 3600L;// 60 * 60;
		long minuteSeconds = 60L;

		long time = (new Date().getTime() - date.getTime()) / 1000;
		if (time <= 0) {
			showStr = "刚刚";
			return showStr;
		}
		if (time / yearSeconds > 0) {
			int year = (int) (time / yearSeconds);
			if (year > 10)
				showStr = "很久前";
			else {
				showStr = year + "年前";
			}
		} else if (time / monthSeconds > 0) {
			showStr = time / monthSeconds + "个月前";
		} else if (time / daySeconds > 0) {
			showStr = time / daySeconds + "天前";
		} else if (time / hourSeconds > 0) {
			showStr = time / hourSeconds + "小时前";
		} else if (time / minuteSeconds > 0) {
			showStr = time / minuteSeconds + "分钟前";
		} else if (time > 0) {
			showStr = time + "秒前";
		}

		return showStr;
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		String paramTime=dateFormater3.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天 "+paramTime;
		} else if (days == 2) {
			ftime = "前天 "+paramTime;
		} else if (days > 2 && days <= 10) {
//			ftime = days + "天前";
			ftime=dateFormater.get().format(time);
		} else if (days > 10) {
//			ftime = dateFormater2.get().format(time);
			ftime=dateFormater.get().format(time);
		}
		return ftime;
	}

	/**
	 * 判断给定时间在否在给定两个时间之前
	 */
	public static int do7(String star, String end) {
		SimpleDateFormat localTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date sdate = localTime.parse(star);
			Date edate = localTime.parse(end);
			long time = System.currentTimeMillis();
			if (time >= sdate.getTime() && time <= edate.getTime()) {
				return 1;
			} else if (time >= edate.getTime()) {
				return 2;
			} else {
				return 3;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 距离当前多少个小时
	 * 
	 * @param dateStr
	 * @return
	 */
	public static int getExpiredHour(String dateStr) {
		int ret = -1;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Date date;
		try {
			date = sdf.parse(dateStr);
			Date dateNow = new Date();

			long times = date.getTime() - dateNow.getTime();
			if (times > 0) {
				ret = ((int) (times / ONE_HOUR_MILLISECONDS));
			} else {
				ret = -1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	/** one day millisecond count */
	public static final long ONE_DAY_MILLISECONDS = 1000 * 3600 * 24;

	public static final long ONE_HOUR_MILLISECONDS = 1000 * 3600;

	public static final long ONE_MIN_MILLISECONDS = 1000 * 60;

	public static String convert(int val) {
		String retStr = "";
		switch (val) {
		case 0:
			return "周日";
		case 1:
			return "周一";
		case 2:
			return "周二";
		case 3:
			return "周三";
		case 4:
			return "周四";
		case 5:
			return "周五";
		case 6:
			return "周六";
		default:
			break;
		}
		return retStr;
	}
}
