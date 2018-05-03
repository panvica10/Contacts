package jackyman.sk.contacts.custom;

/**
 * Created by jakubcervenak on 01/05/18.
 */

import android.view.View;


public interface RecyclerClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
