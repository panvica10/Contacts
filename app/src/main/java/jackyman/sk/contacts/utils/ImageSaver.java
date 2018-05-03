package jackyman.sk.contacts.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.interfaces.OnPhotoSavedInterface;

/**
 * Created by jakubcervenak on 30/04/18.
 */

public class ImageSaver {


    public static String saveProfilePictureToInternalStorage(Bitmap bitmap , Context context){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Logger.e(mypath.getAbsolutePath());
        return mypath.getAbsolutePath();
    }

    public static void saveProfilePictureToFirebaseStorage(Bitmap bitmap, StorageReference storageReference, final OnPhotoSavedInterface onPhotoSavedInterface){
        StorageReference imageReference = storageReference.child("images/profile_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Logger.e("[ImageSaver]", taskSnapshot.getDownloadUrl().toString());
                onPhotoSavedInterface.onPhotoSucces(taskSnapshot.getDownloadUrl().toString());
            }
        });
    }
}
