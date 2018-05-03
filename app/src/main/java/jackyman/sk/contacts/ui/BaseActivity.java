package jackyman.sk.contacts.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.Visibility;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import jackyman.sk.contacts.R;
import jackyman.sk.contacts.core.ContentProvider;
import jackyman.sk.contacts.core.FragmentsManager;
import jackyman.sk.contacts.database.ContactsViewModel;
import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.interfaces.OnDataChangeListener;

/**
 * Created by jakubcervenak on 27/04/18.
 */

public class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected RelativeLayout content;
    protected TextView toolbarTitle;
    protected MenuItem addContactMenuItem;
    protected ContactsViewModel contactsViewModel;
    protected OnDataChangeListener onDataChangeListener;
    protected DatabaseReference database;
    protected StorageReference storageRef;
    protected AlertDialog alertDialog;


    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public ContentProvider getContentProvider() {
        return ContentProvider.getInstance(this);
    }


    public FragmentsManager getFragmentsManager() {
        return FragmentsManager.getInstance(this);
    }

    public void showTopFragmentInMainBS() {
        getFragmentsManager().showTopFragmentInMainBS();
    }


    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideActionBar() {
        toolbar.setVisibility(View.GONE);
        toolbar.animate().translationY(-getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material)).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    public void showActionBar() {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    public void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void showStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Show Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void showErrorDialog(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }


    public TextView getToolbarTitle() {
        return toolbarTitle;
    }

    public void hideAddContactMenuItem(){
        addContactMenuItem.setVisible(false);
    }

    public void showAddContactMenuItem(){
        addContactMenuItem.setVisible(true);
    }

    public ContactsViewModel getContactsViewModel() {
        return contactsViewModel;
    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
    }

    public DatabaseReference getFirebaseDatabase(){
        return database;
    }

    public StorageReference getFirebaseStorage(){
        return storageRef;
    }

}
