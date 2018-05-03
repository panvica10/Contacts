package jackyman.sk.contacts.core;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.datamodel.LetterRow;
import jackyman.sk.contacts.interfaces.RowType;
import jackyman.sk.contacts.utils.ContactSorter;

/**
 * Created by jakubcervenak on 27/04/18.
 */

public class ContentProvider {
    private static ContentProvider instance;
    private static Context context;

    private List<Contact> contactList;

    private List<RowType> rowList;

    private ContentProvider(Context context) {
        ContentProvider.context = context;
    }

    public static ContentProvider getInstance(Context context) {
        if (instance == null)
            instance = new ContentProvider(context);
        return instance;
    }

    public void destroy() {
        instance = null;
    }

    public void releaseLoadedData() {
        contactList = null;
        rowList = null;
    }

    public List<Contact> getContactList() {
        if (contactList == null)
            contactList = new ArrayList<>();
        return contactList;
    }


    public void addContact(Contact contact) {
        if (contactList == null)
            contactList = new ArrayList<>();
        contactList.add(contact);
    }

    public List<RowType> getRowList() {
        if (rowList == null)
            rowList = new ArrayList<>();
        return rowList;
    }


    public void deleteRowList(){
        if(rowList != null) {
            rowList.clear();
        }
    }

    public void setContactList(List<Contact> contactList) {
        getContactList().clear();
        getContactList().addAll(contactList);
    }

    public List<RowType> createRowList() {
        deleteRowList();
        if (getContactList().size() != 0) {
            ContactSorter.sortContactsAlphabetically(contactList);
            getRowList().add(new LetterRow(ContactSorter.getFirstLetterOfFirstName(contactList.get(0)).toUpperCase()));
            getRowList().add(contactList.get(0));
            for (int i = 0; i < contactList.size() - 1; i++) {
                if (!ContactSorter.isFirstLettersInContactsEquals(contactList.get(i), contactList.get(i + 1))) {
                    getRowList().add(new LetterRow(ContactSorter.getFirstLetterOfFirstName(contactList.get(i + 1)).toUpperCase()));
                }
                getRowList().add(contactList.get(i + 1));
            }
        }
        return getRowList();
    }

}
