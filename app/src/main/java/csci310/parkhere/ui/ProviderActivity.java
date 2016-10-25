package csci310.parkhere.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import csci310.parkhere.R;

/**
 * Created by ivylinlaw on 10/17/16.
 */
public class ProviderActivity extends AppCompatActivity implements SpacesFragment.OnFragmentInteractionListener, PrivateProfileFragment.OnFragmentInteractionListener {
    ImageView _profilePic;
    TextView _spaceLink;

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Fragment spacesFragment;
    Fragment privateProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_ui);

        Toolbar renterToolbar = (Toolbar) findViewById(R.id.providerTabbar);
        setSupportActionBar(renterToolbar);
//
        _spaceLink = (TextView)findViewById(R.id.spaceLink);
        _profilePic = (ImageView)findViewById(R.id.profilePic);
//
        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();
//        spacesFragment = fm.findFragmentById(R.id.fragment_spaces);
        spacesFragment = new SpacesFragment();
        privateProfileFragment = new PrivateProfileFragment();

//        fragmentTransaction.add(R.id.fragContainer, spacesFragment);
//        fragmentTransaction.commit();

        _spaceLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked on space tab item");
//                fragmentTransaction.replace(R.id.fragContainer, spacesFragment);
//                fragmentTransaction.commit();
            }
        });
        _profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked on profile tab item");
                try {
                    fragmentTransaction.add(R.id.fragContainer, privateProfileFragment);
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    System.out.println("Profile tab item exception");
                }
//                fragmentTransaction.replace(R.id.fragContainer, privateProfileFragment);
//                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.provider_menu_ui, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
//            case R.id.action_search:
//                openSearch();
//                return true;
//            case R.id.action_compose:
//                composeMessage();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }
}
