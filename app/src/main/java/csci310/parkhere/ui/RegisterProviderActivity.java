package csci310.parkhere.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
public class RegisterProviderActivity extends Activity {
    Button _nextButton;
    EditText _liscenseIdText;
    String name, email, password, phonenum;
    ClientController clientController;

    ProgressDialog progressDialog;
    UserRegisterTask RegTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_provider_ui);

        _nextButton=(Button)findViewById(R.id.nextButton);
        _liscenseIdText=(EditText)findViewById(R.id.liscenseIdText); // license ID

        Intent intent = getIntent();
        name = intent.getStringExtra("NAME");
        email = intent.getStringExtra("EMAIL");
        password = intent.getStringExtra("PASSWORD");
        phonenum = intent.getStringExtra("PHONE_NUM");

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

        // TODO: Implement your own authentication logic here.
        RegTask = new UserRegisterTask(email, password, phonenum, licenseID,"#######", "provider", name);
        RegTask.execute((Void) null);
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
//            doInBackground((Void) null);
        }
        @Override
        protected void onPreExecute(){
            //Display a progress dialog
            progressDialog = new ProgressDialog(RegisterProviderActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Registering...");
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params ){
            try {
                clientController.register(mUsername, mPassword, phonenum, mlicenseID, mplatenum, mcat, mname);
                NetworkPackage NP = clientController.checkReceived();
                MyEntry<String, Serializable> entry = NP.getCommand();
                String key = entry.getKey();
                Object value = entry.getValue();
                if(key.equals("RF")){
                    return false;
                } else if(key.equals("REGISTER")){
                    User result = (User) value;
                    Log.d("REGISTER", result.userName);
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
                Log.d("LOGIN TEST 1", "yeah");
                progressDialog.dismiss();
                Intent intent = new Intent(c, ProviderActivity.class);
                startActivityForResult(intent, 0);
            } else{
                progressDialog.dismiss();
                Intent intent = new Intent(c, HomeActivity.class);
                startActivityForResult(intent, 0);
            }
        }

    }
}
