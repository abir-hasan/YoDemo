package abir.lmtd.yodemo.model;

import java.util.ArrayList;

/**
 * Created by Abir on 17-Dec-15.
 */
public class ContactDetails {
    public String contactId;
    public String contactName;
    public String contactPhotoUri;
    public ArrayList<String> contactNumbers = new ArrayList<>();
    public ArrayList<String> contactEmails = new ArrayList<>();
}
