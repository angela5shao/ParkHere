package csci310.parkhere.ui;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.LinearLayout;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.models.PaymentMethodNonce;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;

/**
 * Created by ivylinlaw on 10/17/16.
 */
public class RenterActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener,
        PrivateProfileFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener,
        SearchSpaceDetailFragment.OnFragmentInteractionListener {
    LinearLayout _resLink, _searchLink;
    ImageView _profilePic;
    ImageView _editLogo;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    Fragment searchFragment, privateProfileFragment, editProfileFragment, searchSpaceDetailFragment;
    Fragment mBraintreeFragment;
    ClientController clientController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renter_ui);

//        clientController = ClientController.getInstance();
//        clientController.setCurrentActivity(this);

        Toolbar renterToolbar = (Toolbar) findViewById(R.id.renterTabbar);
        setSupportActionBar(renterToolbar);

        fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();

        searchFragment = new SearchFragment();
        privateProfileFragment = new PrivateProfileFragment();
        editProfileFragment = new EditProfileFragment();
        searchSpaceDetailFragment = new SearchSpaceDetailFragment();
        mBraintreeFragment = BraintreeFragment.newInstance(this, mAuthorization);

        _resLink = (LinearLayout)findViewById(R.id.resLink);
        _searchLink = (LinearLayout)findViewById(R.id.searchLink);
        _profilePic = (ImageView) findViewById(R.id.profilePic);

        fragmentTransaction.add(R.id.fragContainer, searchSpaceDetailFragment);
        fragmentTransaction.commit();

//        fragmentTransaction.add(R.id.fragContainer, searchFragment);
//        fragmentTransaction.commit();

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
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, searchFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        _profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, privateProfileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                            BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                    );
                    String nonce = paymentMethodNonce.getNonce();
                    break;
                case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
                    // handle errors here, a throwable may be available in
                    // data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE)
                    break;
                default:
                    break;
            }
        }
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public void switchToEditProfileFrag() {
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, editProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
