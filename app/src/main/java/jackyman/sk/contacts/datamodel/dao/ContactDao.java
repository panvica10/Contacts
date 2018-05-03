package jackyman.sk.contacts.datamodel.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import jackyman.sk.contacts.datamodel.Contact;

/**
 * Created by jakubcervenak on 02/05/18.
 */

@Dao
public interface ContactDao  {

    @Insert
    void insert(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("DELETE FROM contact_table")
    void deleteAll();

    @Query("SELECT * FROM contact_table")
    LiveData<List<Contact>> getAllContacts();
}
