package utils;
import play.data.binding.*;

import org.joda.time.format.*;

import java.lang.annotation.*;
import java.sql.Time;
import java.text.ParseException;
import java.util.regex.*;

@Global
public class SqlTimeBinder implements TypeBinder<Time> {
    private static final Pattern TWELVE_HOUR = Pattern.compile("^\\d{1,2}:\\d{2}[ap][m]?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern TWELVE_HOUR_SHORT = Pattern.compile("^\\d{1,2}[ap][m]?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern TWENTYFOUR_HOUR = Pattern.compile("^\\d{1,2}:\\d{2}$");

    private DateTimeFormatter twelve_hour = DateTimeFormat.forPattern("h:ma");
    private DateTimeFormatter twelve_hour_no_minutes = DateTimeFormat.forPattern("ha");
    private DateTimeFormatter twenty_four_hour = DateTimeFormat.forPattern("H:m");

    public Object bind(String name, Annotation[] annotations, String value, Class clazz, java.lang.reflect.Type genericType) {
        if (value == null || value.length() == 0) {
            return null;
        }

        Matcher m = TWELVE_HOUR.matcher(value);
        if (m.matches()) {
            return new java.sql.Time(twelve_hour.parseDateTime(value).getMillis());
        }
        m = TWELVE_HOUR_SHORT.matcher(value);
        if (m.matches()) {
            return new java.sql.Time(twelve_hour_no_minutes.parseDateTime(value).getMillis());
        }

        m = TWENTYFOUR_HOUR.matcher(value);
        if (m.matches()) {
            return new java.sql.Time(twenty_four_hour.parseDateTime(value).getMillis());
        }

        throw new IllegalArgumentException("Invalid time");
    }
}