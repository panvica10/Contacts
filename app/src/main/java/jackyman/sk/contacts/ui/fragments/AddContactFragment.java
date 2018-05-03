package jackyman.sk.contacts.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jackyman.sk.contacts.R;
import jackyman.sk.contacts.core.Constants;
import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.interfaces.OnPhotoSavedInterface;
import jackyman.sk.contacts.utils.DateFormatter;
import jackyman.sk.contacts.utils.ImageSaver;
import jackyman.sk.contacts.utils.Logger;
import jackyman.sk.contacts.utils.Utility;

/**
 * Created by jakubcervenak on 01/05/18.
 */

public class AddContactFragment extends BaseFragment implements Constants, View.OnClickListener, OnPhotoSavedInterface {
    @BindView(R.id.f_add_profile_picture)
    ImageView imageView;
    @BindView(R.id.f_add_first_name)
    EditText firstName;
    @BindView(R.id.f_add_last_name)
    EditText lastName;
    @BindView(R.id.f_add_phone_number)
    EditText phoneNumber;
    @BindView(R.id.f_add_email)
    EditText email;
    @BindView(R.id.f_add_birthday)
    EditText birthday;
    @BindView(R.id.f_add_gender)
    Spinner spinner;
    @BindView(R.id.f_add_save_button)
    Button button;

    private AlertDialog alertDialog;
    Unbinder unbinder;

    private String photoUri;
    private Contact contact;

    Calendar calendar = Calendar.getInstance();

    public static AddContactFragment newInstance(Bundle args) {
        AddContactFragment fragment = new AddContactFragment();
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
        rootView = inflater.inflate(R.layout.fragment_add_contact, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setActionBar();
        setViewContents();
        return rootView;
    }

    @Override
    protected void setActionBar() {
        getToolbarTitle().setText(R.string.add_contact_toolbat_text);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        hideAddContactMenuItem();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setViewContents() {
        contact = new Contact();
        button.setOnClickListener(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.checkPermission(getContext())) {
                    selectImage();
                }
            }
        });
        initializeDatePicker();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onClick(View view) {
        if(areAllFieldsFilled()) {

            createSavingDialog();
            contact.setFirstName(firstName.getText().toString());
            contact.setSecondName(lastName.getText().toString());

            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();
            ImageSaver.saveProfilePictureToFirebaseStorage(bitmap, getFirebaseStorage(),this);

            //photoUri = ImageSaver.saveProfilePictureToInternalStorage(bitmap, getContext());
            //contact.setPhotoName(photoUri.substring(photoUri.lastIndexOf("/") + 1));

            contact.setTelephoneNumber(phoneNumber.getText().toString());
            contact.setMailAddress(email.getText().toString());
            contact.setGender(spinner.getSelectedItem().toString().equalsIgnoreCase("Male"));
            Logger.e(birthday.getText().toString());
            contact.setDayOfBirth(DateFormatter.getLongFromString(birthday.getText().toString(),DateFormatter.Format_DD_MM_YY));
         //   getContactsViewModel().insert(contact);

        } else {
            Toast.makeText(getContext(),"Fill fields",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST)
                onSelectFromGalleryResult(data);
            else if (requestCode == TAKE_PHOTO_REQUEST)
                onCaptureImageResult(data);
        }
    }

    @Override
    public void onPhotoSucces(String photoUri) {
        contact.setPhotoUri(photoUri);
        getContentProvider().addContact(contact);
        createContactInFirebaseDatabase(contact);
        dismissSavingDialog();
        switchToFragmentAndClear(FRAGMENT_HOME, null);
    }

    @Override
    public void onPhotoFailure() {
        Toast.makeText(getContext(),"Save failed",Toast.LENGTH_SHORT).show();
    }

    private void initializeDatePicker() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBirthdayLabel();
            }
        };

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateBirthdayLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormatter.Format_DD_MM_YY, Locale.US);
        birthday.setText(sdf.format(calendar.getTime()));
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        //photoUri = ImageSaver.saveProfilePictureToInternalStorage(thumbnail, getContext());
        imageView.setImageBitmap(thumbnail);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bitmap = null;
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContext().getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.outHeight = 300;
            bitmapOptions.outWidth = 300;
            bitmap = (BitmapFactory.decodeFile(picturePath, bitmapOptions));
            //photoUri = ImageSaver.saveProfilePictureToInternalStorage(bitmap, getContext());
        }
        imageView.setImageBitmap(bitmap);
    }



    private void selectImage() {
        final CharSequence[] options = {getString(R.string.add_contact_take_photo_text), getString(R.string.add_contact_gallery_text), getString(R.string.add_contact_dialog_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.add_contact_image_dialog_title);
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.add_contact_take_photo_text))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals(getString(R.string.add_contact_gallery_text))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals(getString(R.string.add_contact_dialog_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private boolean areAllFieldsFilled() {
        boolean areFilled = true;
        if (firstName.getText().toString().equalsIgnoreCase("")) {
            areFilled = false;
        } else if (phoneNumber.getText().toString().equalsIgnoreCase("")){
            areFilled = false;
        } else if (email.getText().toString().equalsIgnoreCase("")){
            areFilled = false;
        }
        return areFilled;
    }

    private void createSavingDialog(){
        alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("PleaseWait");
        alertDialog.setMessage("Saving...");
        alertDialog.show();
    }

    private void dismissSavingDialog(){
        alertDialog.dismiss();
    }



}
