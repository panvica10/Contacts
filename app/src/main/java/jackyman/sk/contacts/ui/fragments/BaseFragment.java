package jackyman.sk.contacts.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;

import jackyman.sk.contacts.core.ContentProvider;
import jackyman.sk.contacts.core.FragmentsManager;
import jackyman.sk.contacts.database.ContactsViewModel;
import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.interfaces.FragmentSwitcherInterface;
import jackyman.sk.contacts.interfaces.OnDataChangeListener;
import jackyman.sk.contacts.ui.MainActivity;

/**
 * Created by jakubcervenak on 27/04/18.
 */

public abstract class BaseFragment extends Fragment implements FragmentSwitcherInterface {


    protected View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View getRootView() {
        return rootView;
    }

    protected abstract void setActionBar();

    protected abstract void setViewContents();


    protected void hideKeyboard() {
        ((MainActivity) getActivity()).hideKeyboard();
    }

    public void switchToFragment(int fragmentID, Bundle args) {
        ((MainActivity) getActivity()).switchToFragment(fragmentID, args);
    }

    public void switchToFragmentAndClear(int fragmentID, Bundle args) {
        ((MainActivity) getActivity()).switchToFragmentAndClear(fragmentID, args);
    }

    public ContentProvider getContentProvider() {
        return ((MainActivity) getActivity()).getContentProvider();
    }

    public TextView getToolbarTitle() {
        return ((MainActivity) getActivity()).getToolbarTitle();
    }

    public void hideAddContactMenuItem() {
        ((MainActivity) getActivity()).hideAddContactMenuItem();
    }

    public void showAddContactMenuItem() {
        ((MainActivity) getActivity()).showAddContactMenuItem();
    }



    public ActionBar getSupportActionBar() {
        return ((MainActivity) getActivity()).getSupportActionBar();
    }

    public void hideActionBar() {
        ((MainActivity) getActivity()).hideActionBar();
    }

    public void showActionBar() {
        ((MainActivity) getActivity()).showActionBar();
    }

    public void hideStatusBar() {
        ((MainActivity) getActivity()).hideStatusBar();
    }

    public void showStatusBar() {
        ((MainActivity) getActivity()).showStatusBar();
    }


    public boolean customOnBackPressed() {
        return true;
    }

    public void onBackPressed() {
        ((MainActivity) getActivity()).onBackPressed();
    }

    public ContactsViewModel getContactsViewModel() {
        return ((MainActivity) getActivity()).getContactsViewModel();
    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        ((MainActivity) getActivity()).setOnDataChangeListener(onDataChangeListener);
    }

    public void createContactInFirebaseDatabase(Contact contact){
        ((MainActivity) getActivity()).createContactInFirebaseDatabase(contact);
    }

    public void removeContactFromFirebaseDatabase(Contact contact){
        ((MainActivity) getActivity()).removeContactFromFirebaseDatabase(contact);
    }


    public void uploadImageToFirebaseStorage(Contact contact){
        ((MainActivity) getActivity()).uploadImageToFirebaseStorage(contact);
    }

    public StorageReference getFirebaseStorage(){
        return ((MainActivity) getActivity()).getFirebaseStorage();
    }

    public DatabaseReference getFirebaseDatabase(){
        return ((MainActivity) getActivity()).getFirebaseDatabase();
    }

    public void setUpFirebase(){
        ((MainActivity) getActivity()).setUpFirebase();
    }

    public void showErrorDialog(int message){
        ((MainActivity) getActivity()).showErrorDialog(message);
    }

}
