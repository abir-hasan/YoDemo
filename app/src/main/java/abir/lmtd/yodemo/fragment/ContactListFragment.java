package abir.lmtd.yodemo.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import abir.lmtd.yodemo.R;
import abir.lmtd.yodemo.adapter.ContactListAdapter;
import abir.lmtd.yodemo.model.ContactDetails;

/**
 * Created by Abir on 17-Dec-15.
 */
public class ContactListFragment extends Fragment {

    public static final int CONTACT_REQ_CODE = 1;

    ListView lvContactList;
    ContactListAdapter adapter;
    List<ContactDetails> contactDetailsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);
        initializeUI(rootView);
        return rootView;
    }

    private void initializeUI(View view) {
        lvContactList = (ListView) view.findViewById(R.id.lvContactList);

        MyTask task = new MyTask();
        task.execute();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            contactDetailsList = new ArrayList<>();

            ContentResolver resolver = getActivity().getContentResolver();
            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                ContactDetails details = new ContactDetails();
                details.contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                details.contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                details.contactPhotoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));

                Cursor pNumberCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{details.contactId}, null);
                while (pNumberCursor.moveToNext()) {
                    String phoneNumber = pNumberCursor.getString(pNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    details.contactNumbers.add(phoneNumber);
                }

                Cursor emailCursor = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{details.contactId}, null);
                while (emailCursor.moveToNext()) {
                    String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    details.contactEmails.add(email);
                }
                contactDetailsList.add(details);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new ContactListAdapter(getActivity(), contactDetailsList);
            lvContactList.setAdapter(adapter);


            for (int i = 0; i < contactDetailsList.size(); i++) {
                Log.e("PHOTO URI", "URI " + i + " " + contactDetailsList.get(i).contactPhotoUri);
            }
        }
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        *//*Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        ContactListFragment.this.startActivityForResult(intent, CONTACT_REQ_CODE);*//*
    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.e("FRAGMENT", "" + requestCode);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CONTACT_REQ_CODE:
                    //Toast.makeText(getActivity(), "Got the result", Toast.LENGTH_SHORT).show();
                    Uri uri = data.getData();
                    Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.));

                        Log.e("DATA", name);
                    }
                    break;
            }
        }
    }*/
}
