package jackyman.sk.contacts.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import jackyman.sk.contacts.R;
import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.utils.Logger;
import jackyman.sk.contacts.utils.Utility;

/**
 * Created by jakubcervenak on 27/04/18.
 */

public class SplashFragment extends BaseFragment {


    public static SplashFragment newInstance(Bundle args){
        SplashFragment fragment = new SplashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_splash,container,false);
        setActionBar();
        setViewContents();
        return rootView;
    }

    @Override
    protected void setActionBar() {
        hideActionBar();

    }


    @Override
    protected void setViewContents() {
        if(Utility.isOnline(getContext())) {
            setUpFirebase();
            downloadDataFromFirebase();
        } else showErrorDialog(R.string.alert_no_connection_text);

    }

    private void downloadDataFromFirebase(){
        getFirebaseDatabase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Contact contact = dataSnapshot.getValue(Contact.class);
                    getContentProvider().addContact(contact);

                }
                getContentProvider().createRowList();
                switchToFragmentAndClear(FRAGMENT_HOME,null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
