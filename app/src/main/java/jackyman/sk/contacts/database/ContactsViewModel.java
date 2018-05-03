package jackyman.sk.contacts.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import jackyman.sk.contacts.datamodel.Contact;

/**
 * Created by jakubcervenak on 02/05/18.
 */

public class ContactsViewModel extends AndroidViewModel {

    private ContactsRepository mRepository;

    private LiveData<List<Contact>> contactsList;

    public ContactsViewModel (Application application) {
        super(application);
        mRepository = new ContactsRepository(application);
        contactsList = mRepository.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() { return contactsList; }

    public void insert(Contact contact) { mRepository.insert(contact); }

    public void delete(Contact contact) {mRepository.delete(contact);}
}
