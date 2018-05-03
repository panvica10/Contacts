package jackyman.sk.contacts.interfaces;

import android.os.Bundle;

/**
 * Created by jakubcervenak on 27/04/18.
 */

public interface FragmentSwitcherInterface {

    int FRAGMENT_SPLASH = 0;
    int FRAGMENT_HOME = 1;
    int FRAGMENT_ADD_CONTACT = 2;
    int FRAGMENT_CONTACT_DETAIL = 3;

    public void switchToFragment(int fragmentID, Bundle args);

    public void switchToFragmentAndClear(int fragmentID, Bundle args);
}
