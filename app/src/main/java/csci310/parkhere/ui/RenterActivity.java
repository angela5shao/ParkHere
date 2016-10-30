package csci310.parkhere.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import csci310.parkhere.R;

/**
 * Created by ivylinlaw on 10/17/16.
 */
public class RenterActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener,
        PrivateProfileFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener {
    LinearLayout _resLink, _searchLink;
    ImageView _profilePic;
    ImageView _editLogo;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Fragment searchFragment, privateProfileFragment, editProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renter_ui);

        Toolbar renterToolbar = (Toolbar) findViewById(R.id.renterTabbar);
        setSupportActionBar(renterToolbar);

        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        searchFragment = new SearchFragment();
        privateProfileFragment = new PrivateProfileFragment();
        editProfileFragment = new EditProfileFragment();

        _resLink = (LinearLayout)findViewById(R.id.resLink);
        _searchLink = (LinearLayout)findViewById(R.id.searchLink);
        _profilePic = (ImageView) findViewById(R.id.profilePic);

        fragmentTransaction.add(R.id.fragContainer, searchFragment);
        fragmentTransaction.commit();

        _resLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragContainer, );
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });

        _searchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, searchFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        _profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, privateProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        if(privateProfileFragment.getView() != null) {
            Log.d("", "privateProfileFragment not null!!!!!!!!!!!!!!!!!");
            _editLogo = (ImageView) privateProfileFragment.getView().findViewById(R.id.editLogo);
            _editLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentTransaction.replace(R.id.fragContainer, editProfileFragment);
                    fragmentTransaction.commit();
                }
            });
        }else {
            Log.d("", "privateProfileFragment null!!!!!!!!!!!!!!!!!");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.renter_menu_ui, menu);

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
