package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getDateToString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getDateTimeToString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getDateTimeToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 将string类型的时间转化为Date类型的
     * @param stringDate
     * @return
     */
    public static Date stringToDate(String stringDate){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(stringDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String dateTimeToString(String dateTime){
        /**
         *
         * 功能描述: 将dateTime类型转换为String。
         *
         * @param: [dateTime]
         * @return: java.lang.String
         * @auther: 11432_000
         * @date: 2018/10/6 11:19
         */
        String format = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(dateTime);
            format = sdf.format(date);
        } catch (ParseException e) {
            System.out.println("异常");
        }
        return format;
    }
}
