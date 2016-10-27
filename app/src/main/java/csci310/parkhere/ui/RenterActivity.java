package csci310.parkhere.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import csci310.parkhere.R;

/**
 * Created by ivylinlaw on 10/17/16.
 */
public class RenterActivity extends FragmentActivity implements SearchFragment.OnFragmentInteractionListener {
    TextView _searchLink;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Fragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renter_ui);

        Toolbar renterToolbar = (Toolbar) findViewById(R.id.renterTabbar);
        setActionBar(renterToolbar);

        _searchLink = (TextView)findViewById(R.id.searchLink);

        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        searchFragment = new SearchFragment();

        // MOVE THESE INTO SEARCH LINK ON CLICK !!!
        fragmentTransaction.add(R.id.searchFragContainer, searchFragment);
        fragmentTransaction.commit();
        //

        _searchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
