package abir.lmtd.yodemo.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);
        initializeUI(rootView);
        return rootView;
    }

    /**
     * Initializing the components of the view
     *
     * @param view- the inflated view of the fragment
     */
    private void initializeUI(View view) {
        lvContactList = (ListView) view.findViewById(R.id.lvContactList);
        lvContactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int size = contactDetailsList.get(position).contactNumbers.size();
                Log.e("NUMBER", "LONG CLICKED-" + position + " size " + size);
                for (int i = 0; i < size; i++) {
                    Log.e("NUMBER", i + " " + contactDetailsList.get(position).contactNumbers.get(i));
                }
                smsDialog(position);
                return true;
            }
        });

        MyTask task = new MyTask();
        task.execute();
    }

    /**
     * Opens a dialog to send sms to the particular contact
     *
     * @param: pos- is the contacts adapter position
     */
    private void smsDialog(final int pos) {
        final Dialog smsDialogBuilder = new Dialog(getActivity(), R.style.CustomDialog);
        smsDialogBuilder.setContentView(R.layout.dialog_input_sms_text);

        TextView tvDialogContactName, tvDialogContactNumber;
        final EditText etDialogTextBody;
        FloatingActionButton fabDialogSendSms;

        tvDialogContactName = (TextView) smsDialogBuilder.findViewById(R.id.tvDialogContactName);
        tvDialogContactNumber = (TextView) smsDialogBuilder.findViewById(R.id.tvDialogContactNumber);
        etDialogTextBody = (EditText) smsDialogBuilder.findViewById(R.id.etDialogTextBody);
        fabDialogSendSms = (FloatingActionButton) smsDialogBuilder.findViewById(R.id.fabDialogSendSms);

        tvDialogContactName.setText("Name: " + contactDetailsList.get(pos).contactName);
        if (contactDetailsList.get(pos).contactNumbers.size() != 0) {
            tvDialogContactNumber.setText("Number: " + contactDetailsList.get(pos).contactNumbers.get(0));
        } else {
            tvDialogContactNumber.setText("Name: XXX");
        }

        fabDialogSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String text = etDialogTextBody.getText().toString();
                    String phoneNumber = contactDetailsList.get(pos).contactNumbers.get(0);

                    SmsManager manager = SmsManager.getDefault();
                    manager.sendTextMessage(phoneNumber, null, text, null, null);
                    Toast.makeText(getActivity(), "Messege Sent Successfully.", Toast.LENGTH_SHORT).show();
                    smsDialogBuilder.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    smsDialogBuilder.dismiss();
                    Toast.makeText(getActivity(), "Messege Couldn't Be Sent!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        smsDialogBuilder.show();
    }

    /**
     * Class to load contacts on the background
     */
    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            contactDetailsList = new ArrayList<>();

            ContentResolver resolver = getActivity().getContentResolver();
            Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                ContactDetails details = new ContactDetails();
                details.contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                details.contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //details.contactPhotoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));

                Cursor pNumberCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{details.contactId}, null);
                while (pNumberCursor.moveToNext()) {
                    String phoneNumber = pNumberCursor.getString(pNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    details.contactNumbers.add(phoneNumber);
                }
                pNumberCursor.close();

                /*Cursor emailCursor = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{details.contactId}, null);
                while (emailCursor.moveToNext()) {
                    String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    details.contactEmails.add(email);
                }
                emailCursor.close();
                */
                contactDetailsList.add(details);
            }
            cursor.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new ContactListAdapter(getActivity(), contactDetailsList);
            lvContactList.setAdapter(adapter);
            progressDialog.dismiss();
            /*for (int i = 0; i < contactDetailsList.size(); i++) {
                Log.e("PHOTO URI", "URI " + i + " " + contactDetailsList.get(i).contactPhotoUri);
            }*/
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
