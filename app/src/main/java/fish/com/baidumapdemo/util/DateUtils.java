package fish.com.baidumapdemo.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 项目名称：utils.app
 * 类描述：
 * 创建人：Darker
 * 邮箱：1522651962@qq.com
 * 创建时间：16/5/10 下午1:45
 * 修改备注：
 *
 * @version 1.0
 */
public class DateUtils {


    /**
     * 日期类型 *
     */
    public static final String yyyyMMDD = "yyyy-MM-dd";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String HHmmss = "HH:mm:ss";
    public static final String hhmmss = "HH:mm:ss";
    public static final String LOCALE_DATE_FORMAT = "yyyy年M月d日 HH:mm:ss";
    public static final String DB_DATA_FORMAT = "yyyy-MM-DD HH:mm:ss";
    public static final String NEWS_ITEM_DATE_FORMAT = "hh:mm M月d日 yyyy";


    public static String dateToString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date stringToDate(String dateStr, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Date类型转换为日期字符串
     *
     * @param date Date对象
     * @param type 需要的日期格式
     * @return 按照需求格式的日期字符串
     */
    public static String formatDate(Date date, String type) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(type);
            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将日期字符串转换为Date类型
     *
     * @param dateStr 日期字符串
     * @param type    日期字符串格式
     * @return Date对象
     */
    public static Date parseDate(String dateStr, String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    /**
     * 得到年
     *
     * @param date Date对象
     * @return 年
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 得到月
     *
     * @param date Date对象
     * @return 月
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;

    }

    /**
     * 得到日
     *
     * @param date Date对象
     * @return 日
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 转换日期 将日期转为今天, 昨天, 前天, XXXX-XX-XX, ...
     *
     * @param time 时间
     * @return 当前日期转换为更容易理解的方式
     */
    public static String translateDate(Long time) {
        long oneDay = 24 * 60 * 60 * 1000;
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        long todayStartTime = today.getTimeInMillis();

        if (time >= todayStartTime && time < todayStartTime + oneDay) { // today
            return "今天";
        } else if (time >= todayStartTime - oneDay && time < todayStartTime) { // yesterday
            return "昨天";
        } else if (time >= todayStartTime - oneDay * 2 && time < todayStartTime - oneDay) { // the day before yesterday
            return "前天";
        } else if (time > todayStartTime + oneDay) { // future
            return "将来某一天";
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(time);
            return dateFormat.format(date);
        }
    }

    /**
     * 转换日期 转换为更为人性化的时间
     *
     * @param time 时间
     * @return
     */
    private String translateDate(long time, long curTime) {
        long oneDay = 24 * 60 * 60;
        Calendar today = Calendar.getInstance();    //今天
        today.setTimeInMillis(curTime * 1000);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayStartTime = today.getTimeInMillis() / 1000;
        if (time >= todayStartTime) {
            long d = curTime - time;
            if (d <= 60) {
                return "1分钟前";
            } else if (d <= 60 * 60) {
                long m = d / 60;
                if (m <= 0) {
                    m = 1;
                }
                return m + "分钟前";
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("今天 HH:mm");
                Date date = new Date(time * 1000);
                String dateStr = dateFormat.format(date);
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }
                return dateStr;
            }
        } else {
            if (time < todayStartTime && time > todayStartTime - oneDay) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("昨天 HH:mm");
                Date date = new Date(time * 1000);
                String dateStr = dateFormat.format(date);
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {

                    dateStr = dateStr.replace(" 0", " ");
                }
                return dateStr;
            } else if (time < todayStartTime - oneDay && time > todayStartTime - 2 * oneDay) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("前天 HH:mm");
                Date date = new Date(time * 1000);
                String dateStr = dateFormat.format(date);
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }
                return dateStr;
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = new Date(time * 1000);
                String dateStr = dateFormat.format(date);
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }
                return dateStr;
            }
        }
    }

    /**
     * 获取当前的时间戳
     *
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取指定时间的时间戳
     *
     * @param date
     * @return
     */
    public static long getTimestampByDate(Date date) {
        return date.getTime() / 1000;
    }

    public static String timestampToString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd");
        String date = sdf.format(new Date(time * 1000));
        return date;
    }

    /**
     * 根据String获取星期
     *
     * @param date
     * @return
     */
    public static int getDayofweek(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(date));
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    //加日期
    @Nullable
    public static String addDay(String s, int n, String type) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(type);
            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(Calendar.DATE, n);//增加一天
            return sdf.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static String getDayofweekCN(String yyyyMMDD) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(yyyyMMDD);
            SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
            String week = dateFm.format(date);
            return week;
//            switch (week) {
//                case "Monday":
//                    return "星期一";
//                case "Tuesday":
//                    return "星期二";
//                case "Wednesday":
//                    return "星期三";
//                case "Thursday":
//                    return "星期四";
//                case "Friday":
//                    return "星期五";
//                case "Saturday":
//                    return "星期六";
//                case "Sunday":
//                    return "星期日";
//            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * 日期格式字符串转换成时间戳
     * @param date_str 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String date_str, String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime()/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}


