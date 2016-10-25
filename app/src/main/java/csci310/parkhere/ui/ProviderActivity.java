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
public class ProviderActivity extends FragmentActivity implements SpacesFragment.OnFragmentInteractionListener {
    TextView _spaceLink;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Fragment spacesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_ui);

        Toolbar renterToolbar = (Toolbar) findViewById(R.id.providerTabbar);
        setActionBar(renterToolbar);
//
        _spaceLink = (TextView)findViewById(R.id.spaceLink);
//
        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();
//        spacesFragment = fm.findFragmentById(R.id.fragment_spaces);
        spacesFragment = new SpacesFragment();

        fragmentTransaction.add(R.id.spaceFragContainer, spacesFragment);
        fragmentTransaction.commit();

        _spaceLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
