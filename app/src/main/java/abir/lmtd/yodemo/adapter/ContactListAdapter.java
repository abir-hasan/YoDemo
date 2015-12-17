package abir.lmtd.yodemo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import abir.lmtd.yodemo.R;
import abir.lmtd.yodemo.model.ContactDetails;

/**
 * Created by Abir on 17-Dec-15.
 */
public class ContactListAdapter extends BaseAdapter {

    Context context;
    List<ContactDetails> contactDetailsList;

    /**
     * View Holder Class
     * To Bind the components
     */
    private class ContactViewHolder {
        ImageView ivContactPhoto;
        TextView tvContactId, tvContactName;
    }

    public ContactListAdapter(Context con, List<ContactDetails> contactDetailsList) {
        this.context = con;
        this.contactDetailsList = contactDetailsList;
    }

    @Override
    public int getCount() {
        return contactDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_contact_list, null);
            viewHolder = new ContactViewHolder();
            viewHolder.ivContactPhoto = (ImageView) convertView.findViewById(R.id.ivContactPhoto);
            viewHolder.tvContactId = (TextView) convertView.findViewById(R.id.tvContactId);
            viewHolder.tvContactName = (TextView) convertView.findViewById(R.id.tvContactName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ContactViewHolder) convertView.getTag();
        }


        if (contactDetailsList.get(position).contactPhotoUri != null) {
            viewHolder.ivContactPhoto.setImageURI(Uri.parse(contactDetailsList.get(position).contactPhotoUri));
        }
        viewHolder.tvContactId.setText(contactDetailsList.get(position).contactId);
        viewHolder.tvContactName.setText(contactDetailsList.get(position).contactName);

        return convertView;
    }
}
