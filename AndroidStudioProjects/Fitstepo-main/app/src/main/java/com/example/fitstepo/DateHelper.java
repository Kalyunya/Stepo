package com.example.fitstepo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    public static String getFormattedDate() {
        return new SimpleDateFormat("EEEE, dd MMMM", Locale.ENGLISH).format(new Date());
    }
    public static String getDayOfWeek() {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        return dayFormat.format(new Date());
    }
    public static String getDayAndMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMMM", Locale.ENGLISH);
        return dateFormat.format(new Date());
    }
}

