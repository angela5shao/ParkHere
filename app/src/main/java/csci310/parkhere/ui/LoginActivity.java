package csci310.parkhere.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);

        _loginButton=(Button)findViewById(R.id.loginButton);
        _signupLink=(TextView)findViewById(R.id.signupLink);
        _forgotPwLink=(TextView)findViewById(R.id.forgotPwLink);
        clientController = ClientController.getInstance();

        // Log in
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
//                if(email.getText().toString().equals("admin") &&
//                        password.getText().toString().equals("admin")) {
//                    Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
//                }
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

    public void login(View v) {
        Log.d(TAG, "Login");

        _email=(EditText)findViewById(R.id.emailText);
        _password=(EditText)findViewById(R.id.passwordText);
        email = _email.getText().toString();
        password = _password.getText().toString();

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        try {
            clientController.login(email, password);
        } catch(IOException e){
            e.printStackTrace();
        }
        final View curr_v = v;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess(curr_v);
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public boolean validate() {
        //
        return true;
    }

    public void onLoginSuccess(View v) {
        _loginButton.setEnabled(true);
        finish();

        Intent myIntent = new Intent(v.getContext(), RenterActivity.class);
        startActivityForResult(myIntent, 0);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }
}

