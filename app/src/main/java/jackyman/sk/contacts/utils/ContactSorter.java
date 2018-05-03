package jackyman.sk.contacts.utils;

import android.content.Context;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jackyman.sk.contacts.datamodel.Contact;


/**
 * Created by jakubcervenak on 30/04/18.
 */

public class ContactSorter {

    private static ContactComparator contactComparator = new ContactComparator();

    public static boolean isFirstLettersInContactsEquals(Contact firstContact, Contact secondContact){
       return (firstContact.getFirstName().substring(0,1).equalsIgnoreCase(secondContact.getFirstName().substring(0,1)));
    }

    public static String getFirstLetterOfFirstName(Contact contact){
        return contact.getFirstName().substring(0,1);
    }

    public static List<Contact> sortContactsAlphabetically(List<Contact> contacts){
        Collections.sort( contacts, contactComparator);
        return contacts;
    }

    private static class ContactComparator implements Comparator<Contact>{

        @Override
        public int compare(Contact contact, Contact t1) {
            return contact.getFirstName().compareTo(t1.getFirstName());
        }
    }

}
