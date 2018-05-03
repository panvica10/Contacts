package jackyman.sk.contacts.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jackyman.sk.contacts.R;
import jackyman.sk.contacts.adapters.ContactsAdapter;
import jackyman.sk.contacts.core.Constants;
import jackyman.sk.contacts.custom.RecyclerClickListener;
import jackyman.sk.contacts.custom.RecyclerTouchListener;
import jackyman.sk.contacts.custom.ToolbarActionModeCallback;
import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.datamodel.LetterRow;
import jackyman.sk.contacts.interfaces.OnDataChangeListener;
import jackyman.sk.contacts.interfaces.RowType;
import jackyman.sk.contacts.utils.ContactSorter;
import jackyman.sk.contacts.utils.ImageSaver;
import jackyman.sk.contacts.utils.Logger;

/**
 * Created by jakubcervenak on 27/04/18.
 */

public class HomeFragment extends BaseFragment implements Constants, AdapterView.OnItemClickListener,OnDataChangeListener{

    private static final String TAG = "[HomeFragment]";
    @BindView(R.id.f_home_recycler_view)
    RecyclerView recyclerView;
    private ActionMode actionMode;
    private Unbinder unbinder;


    private RecyclerView.LayoutManager layoutManager;
    private ContactsAdapter contactsAdapter;

    public static HomeFragment newInstance(Bundle args) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setActionBar();
        setViewContents();

        return rootView;
    }

    @Override
    protected void setActionBar() {
        showActionBar();
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        showAddContactMenuItem();
        getToolbarTitle().setText(R.string.home_fragment_toolbar_text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    protected void setViewContents() {
      //  createDummyContacts();

        layoutManager = new LinearLayoutManager(getContext());
        contactsAdapter = new ContactsAdapter(getContext(), this, getContentProvider().createRowList());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactsAdapter);
        setOnDataChangeListener(this);
        implementRecyclerViewClickListeners();
    }




    private void implementRecyclerViewClickListeners() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (actionMode != null)
                    onListItemSelect(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                onListItemSelect(position);
            }
        }));
    }

    private void onListItemSelect(int position) {
        contactsAdapter.toggleSelection(position);

        boolean hasCheckedItems = contactsAdapter.getSelectedCount() > 0;

        if (hasCheckedItems && actionMode == null)
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ToolbarActionModeCallback(HomeFragment.this, contactsAdapter));
        else if (!hasCheckedItems && actionMode != null)
            actionMode.finish();

        if (actionMode != null)
            actionMode.setTitle(String.valueOf(contactsAdapter
                    .getSelectedCount()) + " selected");


    }

    public void setNullToActionMode() {
        if (actionMode != null)
            actionMode = null;
    }

    public void deleteRows() {
        SparseBooleanArray selected = contactsAdapter.getSelectedIds();//Get selected ids

        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                Contact contact = ((Contact) getContentProvider().getRowList().get(selected.keyAt(i)));
             //   getContactsViewModel().delete(contact);
                removeContactFromFirebaseDatabase(contact);
                getContentProvider().getContactList().remove(contact);
                getContentProvider().getRowList().remove(selected.keyAt(i));
                Logger.e(TAG, String.valueOf(selected.keyAt(i)));
                contactsAdapter.notifyDataSetChanged();//notify adapter

            }
        }
        deleteEmptyLetterRows();
        contactsAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
        actionMode.finish();

    }

    public void deleteEmptyLetterRows(){
        List<RowType> rowTypeList = getContentProvider().getRowList();
        for(int i = (rowTypeList.size() -1); i>0; i-- ){
            if(rowTypeList.get(i) instanceof LetterRow && rowTypeList.get(i-1) instanceof LetterRow){
                rowTypeList.remove(i-1);
            }
        }
        if(rowTypeList.get(rowTypeList.size()-1) instanceof LetterRow){
            rowTypeList.remove(rowTypeList.size()-1);
        }
    }

    @Override
    public void onDataChange() {
        contactsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle args = new Bundle();
        args.putInt(ROW_NUMBER,i);
        switchToFragment(FRAGMENT_CONTACT_DETAIL,args);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
