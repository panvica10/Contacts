package jackyman.sk.contacts.interfaces;

/**
 * Created by jakubcervenak on 03/05/18.
 */

public interface OnPhotoSavedInterface {

    void onPhotoSucces(String photoUri);

    void onPhotoFailure();
}
