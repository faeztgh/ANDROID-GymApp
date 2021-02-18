package ir.faez.gymapp.data.model;

import androidx.room.TypeConverter;

import java.util.Date;

public class TypeConverters {

    @TypeConverter
    public static Date fromTimeStamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
