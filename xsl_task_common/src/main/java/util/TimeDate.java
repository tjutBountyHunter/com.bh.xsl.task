package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDate {
    public static String timedate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String str = simpleDateFormat.format(date);
        return str;
    }
}
