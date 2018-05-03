package jackyman.sk.contacts.custom;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import jackyman.sk.contacts.R;
import jackyman.sk.contacts.adapters.ContactsAdapter;
import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.interfaces.RowType;
import jackyman.sk.contacts.ui.fragments.HomeFragment;

/**
 * Created by jakubcervenak on 01/05/18.
 */

public class ToolbarActionModeCallback implements ActionMode.Callback {

    private ContactsAdapter contactsAdapter;
    private HomeFragment homeFragment;


    public ToolbarActionModeCallback(HomeFragment homeFragment, ContactsAdapter contactsAdapter) {
        this.contactsAdapter = contactsAdapter;
        this.homeFragment = homeFragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.action_menu, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                    homeFragment.deleteRows();
                break;

        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {

        //When action mode destroyed remove selected selections and set action mode to null
        //First check current fragment action mode
            contactsAdapter.removeSelection();  // remove selection
            homeFragment.setNullToActionMode();//Set action mode null
    }
}
