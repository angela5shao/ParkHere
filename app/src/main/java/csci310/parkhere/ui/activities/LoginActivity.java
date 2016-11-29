package csci310.parkhere.ui.activities;

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
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    String encodedPic;
    ProgressDialog progressDialog;
    UserLoginTask AuthTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);

        _loginButton=(Button)findViewById(R.id.loginButton);
        _signupLink=(TextView)findViewById(R.id.signupLink);
        _forgotPwLink=(TextView)findViewById(R.id.forgotPwLink);

        _email=(EditText)findViewById(R.id.emailText);
        _password=(EditText)findViewById(R.id.passwordText);

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
                Intent registerIntent = new Intent(v.getContext(), RegisterMainActivity.class);
                startActivity(registerIntent);
            }
        });

        //Forgot password
        _forgotPwLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = _email.getText().toString();

                if(email != null && isEmailValid(email) == true) {
                    ForgotPWTask FPWT = new ForgotPWTask(email);
                    FPWT.execute((Void) null);
                }
                else {
                    Toast.makeText(LoginActivity.this.getBaseContext(), "Please Enter a Valid Email", Toast.LENGTH_LONG).show();
                }
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
//                clientController.cancelReceived();
                NetworkPackage NP = clientController.checkReceived();

                if(NP == null)
                {
                    Log.d("DOINBACKGROUND", "null");
                    return false;
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

                    Log.d("Login", result.userPlate);


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
                getProfilePic gpp = new getProfilePic(mUsername);
                gpp.execute((Void)null);
                if (clientController.getUser().userType) {
                    Intent myIntent = new Intent(c, RenterActivity.class);
                    startActivityForResult(myIntent, 0);
                } else {
                    Intent myIntent = new Intent(c, ProviderActivity.class);
                    startActivityForResult(myIntent, 0);
                }
            } else{
                progressDialog.dismiss();
                if(clientController.receiving){
                    Toast.makeText(getBaseContext(), "Lost Connection to Server", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(c, HomeActivity.class);
                startActivityForResult(intent, 0);
            }
        }

    }

    private class ForgotPWTask extends AsyncTask<Void, Void, Boolean>{
        private final String mUsername;

        ForgotPWTask(String username){
            mUsername = username;
        }
        @Override
        protected void onPreExecute(){

//            if(mUsername != null && isEmailValid(mUsername) == true) clientController.forgotPW(mUsername);
//            else {
//                Toast.makeText(getBaseContext(), "Please Enter a Valid Email", Toast.LENGTH_SHORT).show();
//            }

            if(mUsername == null || mUsername.isEmpty() || !isEmailValid(mUsername))
                Toast.makeText(getBaseContext(), "Please Enter a Valid Email", Toast.LENGTH_SHORT).show();


                //Display a progress dialog
            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Checking...");
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params ){
//<<<<<<< HEAD
//            if(mUsername != null && isEmailValid(mUsername) == true) clientController.forgotPW(mUsername);
//            else {
//                return false;
//            }
//=======
//
            clientController.forgotPW(mUsername);
//>>>>>>> eeecd23a971f4bc854e626a7ca549bfefc6ba317


            NetworkPackage NP = clientController.checkReceived();
            if(NP == null)
            {
                return false;
            }
            MyEntry<String, Serializable> entry = NP.getCommand();

            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("RESETPASSWORD")){
                if((Boolean) value) {
                    Log.d("LOGIN FORGOT PW ", "--- RESETPASSWORD == true");
                    return true;
                }
            }

            return false;
        }
        @Override
        protected void onPostExecute(Boolean success) {
            progressDialog.dismiss();
            Log.d("LOGIN FORGOT PW ", "--- onPostExecute");
            if(success) {
                Toast.makeText(LoginActivity.this.getBaseContext(), "New temporary password has sent to your email!", Toast.LENGTH_SHORT).show();
            } else{
                if(clientController.receiving){
                    Toast.makeText(getBaseContext(), "Lost Connection to Server", Toast.LENGTH_SHORT).show();
                }
                if(mUsername != null && isEmailValid(mUsername) == true){
                    Toast.makeText(getBaseContext(), "Reset password failed!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getBaseContext(), "Please Enter a Valid Email", Toast.LENGTH_SHORT).show();
                }
            }
            finish();
        }

    }

    private class getProfilePic extends AsyncTask<Void, Void, String>{
        private final String mUsername;

        getProfilePic(String username){
            mUsername = username;
        }
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected String doInBackground(Void... params ){

//            clientController.getProfilePic(mUsername);
            clientController.getProfilePic();

            NetworkPackage NP = clientController.checkReceived();
            MyEntry<String, Serializable> entry = NP.getCommand();

            String key = entry.getKey();
            Object value = entry.getValue();
            String encodedPic = null;
            if(key.equals("RESETPASSWORD")){
                encodedPic = (String)value;
            }
            return encodedPic;
        }
        @Override
        protected void onPostExecute(String profilePic) {
            
        }

    }

    public void login(View v) {
        Log.d(TAG, "Login");

        email = _email.getText().toString();
        password = _password.getText().toString();

        _loginButton.setEnabled(false);

        // TODO: Implement your own authentication logic here.

        AuthTask = new UserLoginTask(email, password);
        AuthTask.execute((Void) null);

    }

    public boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}

