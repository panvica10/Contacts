package jackyman.sk.contacts.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jackyman.sk.contacts.R;
import jackyman.sk.contacts.datamodel.Contact;
import jackyman.sk.contacts.datamodel.LetterRow;
import jackyman.sk.contacts.interfaces.RowType;
import jackyman.sk.contacts.utils.Logger;

/**
 * Created by jakubcervenak on 27/04/18.
 */

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static final int VIEW_TYPE_CONTACT = 0;
    private static final int VIEW_TYPE_LETTER = 1;
    private static final String TAG = "[ContactsAdapter]" ;

    private List<RowType> rowTypeList;
    private AdapterView.OnItemClickListener onItemClickListener;
    private Context context;

    private SparseBooleanArray selectedItemsId;

    public ContactsAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener,
                           List<RowType> contactList) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.rowTypeList = contactList;
        selectedItemsId = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CONTACT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
            return new ContactViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_letter, parent, false);
            return new LetterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ContactViewHolder) {
            if (rowTypeList.size() > 0 && position < rowTypeList.size()) {
                ((ContactViewHolder) holder).wholeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(null, null, holder.getAdapterPosition(), 0);
                    }
                });
                Contact contact = ((Contact) rowTypeList.get(position));
                ((ContactViewHolder) holder).contactName.setText(context.getString(R.string.contact_item_name_surname_string,
                        contact.getFirstName(),
                        contact.getSecondName()));
                ((ContactViewHolder) holder).textEmail.setText(contact.getMailAddress());
                ((ContactViewHolder) holder).textTelephone.setText(contact.getTelephoneNumber());
                //Logger.e(TAG, contact.getPhotoUri());
                Picasso.with(context).load(Uri.parse(contact.getPhotoUri()))
                        .placeholder(ContextCompat.getDrawable(context,R.drawable.ic_profile_picture))
                        .into(((ContactViewHolder) holder).profilePicture);
                ((ContactViewHolder) holder).wholeView.setBackgroundColor(selectedItemsId.get(position)? ContextCompat.getColor(context,R.color.blue_500)
                        : ContextCompat.getColor(context,R.color.white));
            }
        }
        if (holder instanceof LetterViewHolder){
            LetterRow letterRow = ((LetterRow) rowTypeList.get(position));
            ((LetterViewHolder) holder).letterText.setText(letterRow.getLetter());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (rowTypeList.size() > position) {
            if (rowTypeList.get(position) instanceof Contact) {
                return VIEW_TYPE_CONTACT;
            } else return VIEW_TYPE_LETTER;
        }
        return VIEW_TYPE_LETTER;
    }

    @Override
    public int getItemCount() {
        return rowTypeList.size();
    }



    private class ContactViewHolder extends RecyclerView.ViewHolder {
        private View wholeView;
        private CircleImageView profilePicture;
        private TextView textTelephone;
        private TextView textEmail;
        private TextView contactName;


        public ContactViewHolder(View itemView) {
            super(itemView);
            this.wholeView = itemView;
            this.profilePicture = itemView.findViewById(R.id.contact_item_profile_picture);
            this.textTelephone = itemView.findViewById(R.id.contact_item_telephone);
            this.textEmail = itemView.findViewById(R.id.contact_item_email);
            this.contactName = itemView.findViewById(R.id.contact_item_name);
        }
    }

    private class LetterViewHolder extends RecyclerView.ViewHolder {
        private TextView letterText;

        public LetterViewHolder(View itemView) {
            super(itemView);
            this.letterText = itemView.findViewById(R.id.contact_letter);
        }
    }

    public void toggleSelection(int position) {
        if(rowTypeList.get(position) instanceof Contact)
        selectView(position, !selectedItemsId.get(position));
    }


    public void removeSelection() {
        selectedItemsId = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    private void selectView(int position, boolean value) {
        if (value)
            selectedItemsId.put(position, true);
        else
            selectedItemsId.delete(position);

        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selectedItemsId.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return selectedItemsId;
    }




}
