package abir.lmtd.yodemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import abir.lmtd.yodemo.fragment.ContactListFragment;

public class MainActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    ContactListFragment contactListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactListFragment = new ContactListFragment();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_container, contactListFragment);
        fragmentTransaction.commit();

    }
}