package jackyman.sk.contacts.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;

import jackyman.sk.contacts.R;
import jackyman.sk.contacts.database.ContactsViewModel;
import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.interfaces.FragmentSwitcherInterface;
import jackyman.sk.contacts.ui.fragments.AddContactFragment;
import jackyman.sk.contacts.ui.fragments.ContactDetailFragment;
import jackyman.sk.contacts.ui.fragments.HomeFragment;
import jackyman.sk.contacts.ui.fragments.SplashFragment;
import jackyman.sk.contacts.utils.Logger;
import jackyman.sk.contacts.utils.Utility;

public class MainActivity extends BaseActivity implements FragmentSwitcherInterface {


    private static final String TAG = "[MainActivity]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.initialize(true);
        setContentView(R.layout.activity_main);
        setToolbar();

     //   setUpViewModel();

            getFragmentsManager().clearAndInit(this, true);
           switchToFragmentAndClear(FRAGMENT_SPLASH, getIntent().getExtras());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        addContactMenuItem = menu.findItem(R.id.add_contact_item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_contact_item:
                switchToFragment(FRAGMENT_ADD_CONTACT, null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        getContentProvider().releaseLoadedData();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void switchToFragment(int fragmentID, Bundle args) {
        switchToFragment(fragmentID, args, false);
    }

    @Override
    public void switchToFragmentAndClear(int fragmentID, Bundle args) {
        switchToFragment(fragmentID, args, true);
    }

    private void switchToFragment(int fragmentID, Bundle args, boolean clear) {
        switch (fragmentID) {
            case FRAGMENT_SPLASH:
                getFragmentsManager().switchFragmentInMainBackStack(SplashFragment.newInstance(args), clear, true);
                break;
            case FRAGMENT_HOME:
                getFragmentsManager().switchFragmentInMainBackStack(HomeFragment.newInstance(args), clear, true);
                break;
            case FRAGMENT_ADD_CONTACT:
                getFragmentsManager().switchFragmentInMainBackStack(AddContactFragment.newInstance(args), clear, true);
                break;
            case FRAGMENT_CONTACT_DETAIL:
                getFragmentsManager().switchFragmentInMainBackStack(ContactDetailFragment.newInstance(args), clear, true);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentsManager().getMainBackStackSize() == 1) {
            if (getFragmentsManager().getTopFragmentInMainBackstack().customOnBackPressed())
                finish();
            finish();
        } else {
            getFragmentsManager().getTopFragmentInMainBackstack().customOnBackPressed();
            showTopFragmentInMainBS();
        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void setUpViewModel() {
        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);
        contactsViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable final List<Contact> contactList) {
                getContentProvider().setContactList(contactList);
                getContentProvider().createRowList();
                onDataChangeListener.onDataChange();
            }
        });
    }

    public void setUpFirebase() {
        storageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference("contacts");

    }

    public void uploadImageToFirebaseStorage(final Contact contact) {
        Uri file = Uri.fromFile(new File(contact.getPhotoUri()));
        StorageReference storageReference = storageRef.child(contact.getPhotoUri());

        storageReference.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        contact.setPhotoUri(taskSnapshot.getDownloadUrl().toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(MainActivity.this,"Image save failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void createContactInFirebaseDatabase(Contact contact) {
        String key = database.push().getKey();
        contact.setfKey(key);
        database.child(key).setValue(contact);
    }

    public void removeContactFromFirebaseDatabase(Contact contact) {
        database.child(contact.getfKey()).removeValue();
    }


}
