package jackyman.sk.contacts.database;

import android.arch.persistence.room.TypeConverter;

import java.sql.Date;

/**
 * Created by jakubcervenak on 02/05/18.
 */

public class DateTransformer {

    @TypeConverter
    public static Long fromDate(Date date) {
        if (date == null)
            return(null);

        return(date.getTime());
    }

    @TypeConverter
    public static Date toDate(Long millisSinceEpoch) {
        if (millisSinceEpoch == null)
            return(null);

        return(new Date(millisSinceEpoch));
    }
}
