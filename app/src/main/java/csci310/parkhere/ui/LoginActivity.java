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
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;
import resource.MyEntry;
import resource.NetworkPackage;
import resource.User;

/**
 * Created by ivylinlaw on 10/15/16.
 */
public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";

    Button _loginButton;

    EditText _email, _password;
    String email, password;

    TextView _signupLink, _forgotPwLink;
    ClientController clientController;

    ProgressDialog progressDialog;
    UserLoginTask AuthTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);

        _loginButton=(Button)findViewById(R.id.loginButton);
        _signupLink=(TextView)findViewById(R.id.signupLink);
        _forgotPwLink=(TextView)findViewById(R.id.forgotPwLink);

        clientController = ClientController.getInstance();
        clientController.setCurrentActivity(this);

        // Log in
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });

        //Sign up
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        //Forgot password
        _forgotPwLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean>{
        private final String mUsername;
        private final String mPassword;
        private boolean authenticationStatus = true;

        UserLoginTask(String username, String password){
            mUsername = username;
            mPassword = password;
//            doInBackground((Void) null);
            System.out.println(mUsername);
            System.out.println(mPassword);
        }
        @Override
        protected void onPreExecute(){
            //Display a progress dialog
            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params ){
            try {

                Log.d("LOGIN", "ASYNTASK LOGIN");
                clientController.login(email, password);
                NetworkPackage NP = clientController.checkReceived();

                if(NP == null)
                {
                    Log.d("DOINBACKGROUND", "null");
                }
                MyEntry<String, Serializable> entry = NP.getCommand();




                String key = entry.getKey();
                Object value = entry.getValue();
                if(key.equals("LF")){
                    return false;
                } else if(key.equals("LOGIN")){
                    User result = (User) value;
                    Log.d("LOGIN", result.userName);
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
                finish();
                if (clientController.getUser().userType) {
                    Intent myIntent = new Intent(c, RenterActivity.class);
                    startActivityForResult(myIntent, 0);
                } else {
                    Intent myIntent = new Intent(c, ProviderActivity.class);
                    startActivityForResult(myIntent, 0);
                }
            } else{
                progressDialog.dismiss();
                Intent intent = new Intent(c, HomeActivity.class);
                startActivityForResult(intent, 0);
            }
        }

    }


    public void login(View v) {
        Log.d(TAG, "Login");

        _email=(EditText)findViewById(R.id.emailText);
        _password=(EditText)findViewById(R.id.passwordText);
        email = _email.getText().toString();
        password = _password.getText().toString();

        _loginButton.setEnabled(false);

        // TODO: Implement your own authentication logic here.

        AuthTask = new UserLoginTask(email, password);
        AuthTask.execute((Void) null);

    }


}

