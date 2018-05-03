package jackyman.sk.contacts.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.datamodel.dao.ContactDao;

/**
 * Created by jakubcervenak on 02/05/18.
 */

public class ContactsRepository {

    private ContactDao contactDao;
    private LiveData<List<Contact>> contactsList;

    ContactsRepository(Application application) {
        ContactsRoomDatabase db = ContactsRoomDatabase.getDatabase(application);
        contactDao = db.contactDao();
        contactsList = contactDao.getAllContacts();
    }

    LiveData<List<Contact>> getAllContacts() {
        return contactsList;
    }

    public void insert (Contact contact) {
        new insertAsyncTask(contactDao).execute(contact);
    }

    public void delete (Contact contact) {new deleteAsyncTask(contactDao).execute(contact);}

    private static class insertAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao asyncTaskDao;

        insertAsyncTask(ContactDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Contact... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Contact, Void, Void> {

        private ContactDao asyncTaskDao;

        deleteAsyncTask(ContactDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Contact... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
