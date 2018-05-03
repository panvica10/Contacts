package jackyman.sk.contacts.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import jackyman.sk.contacts.R;
import jackyman.sk.contacts.core.Constants;
import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.interfaces.RowType;
import jackyman.sk.contacts.utils.DateFormatter;
import jackyman.sk.contacts.utils.Logger;

/**
 * Created by jakubcervenak on 03/05/18.
 */

public class ContactDetailFragment extends BaseFragment implements Constants {

    @BindView(R.id.detail_picture)
    CircleImageView profilePicture;
    @BindView(R.id.detail_contact_name)
    TextView name;
    @BindView(R.id.detail_item_telephone)
    TextView telephone;
    @BindView(R.id.detail_item_email)
    TextView email;
    @BindView(R.id.detail_item_birthday)
    TextView birthday;
    @BindView(R.id.detail_item_gender)
    TextView gender;

    private Contact contact;
    private int rowNumber;

    private Unbinder unbinder;

    public static ContactDetailFragment newInstance(Bundle args) {
        ContactDetailFragment fragment = new ContactDetailFragment();
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
        rootView = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        unbinder = ButterKnife.bind(this,rootView);
        setActionBar();
        setViewContents();
        return rootView;
    }

    @Override
    protected void setActionBar() {
        hideAddContactMenuItem();

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        hideAddContactMenuItem();
        getToolbarTitle().setText(R.string.contact_detail_toolbar_text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setViewContents() {
        rowNumber = getArguments().getInt(ROW_NUMBER);
        contact = ((Contact) getContentProvider().getRowList().get(rowNumber));
        Logger.e(contact.getPhotoUri());
        Picasso.with(getContext()).load(Uri.parse(contact.getPhotoUri()))
                .placeholder(ContextCompat.getDrawable(getContext(),R.drawable.ic_profile_picture))
                .into(profilePicture);
        if(contact.getSecondName() != null) {
            String firstAndLastNameString = contact.getFirstName() + " " + contact.getSecondName();
            name.setText(firstAndLastNameString);
        } else name.setText(contact.getFirstName());

        telephone.setText(contact.getTelephoneNumber());
        email.setText(contact.getMailAddress());

        if(contact.getDayOfBirth()!= null) {
            birthday.setText(DateFormatter.getStringFromLong(contact.getDayOfBirth(),DateFormatter.Format_DD_MM_YY));
        }
        gender.setText(contact.isGender() ? getString(R.string.male) : getString(R.string.female));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
