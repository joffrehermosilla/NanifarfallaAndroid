package nanifarfalla.app.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utilidades para fechas y horas
 */

public class DateTimeUtils {

    public static final String DATE_ONLY_PATTERN = "dd/MM/yyyy";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String getDateTime(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return df.format(Calendar.getInstance().getTime());
    }

    public static String formatDate(Date date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return df.format(date);
    }

    public static Date getTime() {
        return Calendar.getInstance().getTime();
    }

    public static Date parseDate(String dateString, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
