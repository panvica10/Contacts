package jackyman.sk.contacts.datamodel;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


import java.sql.Date;

import jackyman.sk.contacts.interfaces.RowType;

/**
 * Created by jakubcervenak on 30/04/18.
 */

@Entity(tableName = "contact_table")
public class Contact implements RowType {

    @PrimaryKey(autoGenerate = true)
    private Long ID;
    @Ignore
    private String fKey;
    private String firstName;
    private String secondName;
    private boolean gender;
    private Long dayOfBirth;
    private String telephoneNumber;
    private String mailAddress;
    private String photoUri;

    public Contact() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public Long getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(Long dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getfKey() {
        return fKey;
    }

    public void setfKey(String fKey) {
        this.fKey = fKey;
    }

}
