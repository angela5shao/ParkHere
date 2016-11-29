package csci310.parkhere.ui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.User;

/**
 * Created by ivylinlaw on 10/29/16.
 */
public class RegisterRenterActivity extends Activity {
    Button _nextButton;
    EditText _liscenseIdText, _liscensePlateNumText;
    String name, email, password, phonenum;
    ClientController clientController;
    private static final int REQUEST_SIGNUP = 0;

    ProgressDialog progressDialog;
    UserVerifyRegisterTask VeifyRegTask = null;
    UserRegisterTask RegTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_renter_ui);
        ClientController.getInstance().setCurrentActivity(this);

        _nextButton=(Button)findViewById(R.id.nextButton);
        _liscenseIdText=(EditText)findViewById(R.id.liscenseIdText); // license ID
        _liscensePlateNumText=(EditText)findViewById(R.id.liscensePlateNumText); // license plate number

        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        email = intent.getStringExtra("EMAIL");
        password = intent.getStringExtra("PASSWORD");
        phonenum = intent.getStringExtra("PHONE_NUM");

        //
        Log.v("@@@@@@@@@@@@ ", name);
        Log.v("@@@@@@@@@@@@ ", email);
        Log.v("@@@@@@@@@@@@ ", password);
        Log.v("@@@@@@@@@@@@ ", phonenum);
        //

//        clientController = (ClientController) intent.getSerializableExtra("CLIENT_CONTROLLER");
        clientController = ClientController.getInstance();
        clientController.setCurrentActivity(this);

        _nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });
    }

    private void register(View v) {
        String licenseID = _liscenseIdText.getText().toString();
        String licensePlate = _liscensePlateNumText.getText().toString();

        VeifyRegTask = new UserVerifyRegisterTask(email, password, phonenum, licenseID,_liscensePlateNumText.getText().toString(), "renter", name);
        VeifyRegTask.execute((Void) null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_SIGNUP) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(getBaseContext(), "Fragment Got it: " + requestCode + ", " + resultCode, Toast.LENGTH_SHORT).show();

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @Override
    protected void onPause() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private class UserVerifyRegisterTask extends AsyncTask<Void, Void, String> {
        private final String mUsername;
        private final String mPassword;
        private final String mlicenseID;
        private final String mphonenum;
        private final String mplatenum;
        private final String mcat;
        private final String mname;

        UserVerifyRegisterTask(String email, String password, String phonenum, String licenseID, String platenum, String cat, String name){
            mUsername = email;
            mPassword = password;
            mlicenseID = licenseID;
            mphonenum = phonenum ;
            mplatenum = platenum;
            mcat = cat;
            mname = name;

//            mVerificationCode = ""; // Undefined verification code
        }
        @Override
        protected void onPreExecute(){
            //Display a progress dialog
            progressDialog = new ProgressDialog(RegisterRenterActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Verifying...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params ){
            try {
                clientController.verifyRegister(mUsername);
                NetworkPackage NP = clientController.checkReceived();
                MyEntry<String, Serializable> entry = NP.getCommand();
                String key = entry.getKey();
                Object value = entry.getValue();
                if(key.equals("VERIFICATION")) {
                    return (String) value;
                }
                return null;
            } catch (IOException e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String code) {
            Context c = getBaseContext();
            if(code != null) {
                progressDialog.dismiss();

                // Create custom dialog object
                final String verifyCode = code;
                final Dialog reviewDialog = new Dialog(RegisterRenterActivity.this);
                reviewDialog.setContentView(R.layout.dialog_authen);
                reviewDialog.setTitle("Authentication");

                final EditText _codeDialog=(EditText)reviewDialog.findViewById(R.id.codeDialog);
                Button _btn_confirm=(Button)reviewDialog.findViewById(R.id.btn_confirm);
                _btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (verifyCode.equals(_codeDialog.getText().toString())) {
                            Log.d("Register", " Verify TRUE!!!");

                            RegTask = new UserRegisterTask(mUsername, mPassword, mphonenum, mlicenseID, mplatenum, mcat, mname);
                            RegTask.execute((Void) null);
                        } else {
                            Log.d("Register", " Verify FALSE!!!");

                            Intent intent = new Intent(getBaseContext(), RegisterMainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getBaseContext(), "Verification Error!", Toast.LENGTH_SHORT).show();
                        }

                        // Close dialog
                        reviewDialog.dismiss();
                    }
                });
                reviewDialog.show();

            } else{
                progressDialog.dismiss();
                Intent intent = new Intent(c, RegisterMainActivity.class);
                startActivityForResult(intent, 0);
                Toast.makeText(getBaseContext(), "Verification Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private final String mPassword;
        private final String mlicenseID;
        private final String mphonenum;
        private final String mplatenum;
        private final String mcat;
        private final String mname;

        UserRegisterTask(String email, String password, String phonenum, String licenseID, String platenum, String cat, String name){
            mUsername = email;
            mPassword = password;
            mlicenseID = licenseID;
            mphonenum = phonenum ;
            mplatenum = platenum;
            mcat = cat;
            mname = name;
        }
        @Override
        protected void onPreExecute(){
            //Display a progress dialog
            progressDialog = new ProgressDialog(RegisterRenterActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Registering...");
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params ){
            try {
                clientController.register(mUsername, mPassword, mphonenum, mlicenseID, mplatenum, mcat, mname);
                NetworkPackage NP = clientController.checkReceived();
                MyEntry<String, Serializable> entry = NP.getCommand();
                String key = entry.getKey();
                Object value = entry.getValue();

                if(key.equals("RF")){
                    return false;
                } else if(key.equals("REGISTER")){
                    User result = (User) value;
                    clientController.setUser(result);
                    return true;
                } else{
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean success) {
            Context c = getBaseContext();
            if(success) {
                progressDialog.dismiss();

                Intent intent = new Intent(getBaseContext(), RenterActivity.class);
                startActivityForResult(intent, 0);
            } else{
                progressDialog.dismiss();
                Intent intent = new Intent(c, HomeActivity.class);
                startActivityForResult(intent, 0);
                Toast.makeText(getBaseContext(), "Register Fail!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
