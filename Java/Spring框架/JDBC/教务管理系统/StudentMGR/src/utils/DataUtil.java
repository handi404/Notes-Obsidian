package utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {
    //    将日期转为字符串
    public static String format(Date date, String pattern){
        return new SimpleDateFormat(pattern).format(date);
    }
    //    将字符串转为日期对象
    public static Date parse(String date,String pattern){
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
