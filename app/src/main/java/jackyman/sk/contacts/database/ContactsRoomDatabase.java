package jackyman.sk.contacts.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import jackyman.sk.contacts.R;
import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.datamodel.dao.ContactDao;
import jackyman.sk.contacts.utils.ImageSaver;
import jackyman.sk.contacts.utils.Logger;

import static jackyman.sk.contacts.core.Constants.FEMALE;
import static jackyman.sk.contacts.core.Constants.MALE;

/**
 * Created by jakubcervenak on 02/05/18.
 */

@Database(entities = {Contact.class}, version = 1)
@TypeConverters({DateTransformer.class})
public abstract class ContactsRoomDatabase extends RoomDatabase{
    public abstract ContactDao contactDao();

    private static ContactsRoomDatabase instance;
    private static Context mContext;

    public static ContactsRoomDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (ContactsRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            ContactsRoomDatabase.class, "contacts_database")
                            .build();
                    mContext = context;
                }
            }
        }
        return instance;
    }


}
