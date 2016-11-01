package csci310.parkhere.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

    ProgressDialog progressDialog;

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
            onLoginFailed(getApplicationContext());
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
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

    }

    public boolean validate() {
        //
        return true;
    }

    public void onLoginSuccess(Context c) {
        progressDialog.dismiss();

        finish();


        if(clientController.getUser().userType)
        {
            Intent myIntent = new Intent(c, RenterActivity.class);
            startActivityForResult(myIntent, 0);
        }
        else
        {
            Intent myIntent = new Intent(c, ProviderActivity.class);
            startActivityForResult(myIntent, 0);
        }
    }

    public void onLoginFailed(Context c) {
        progressDialog.dismiss();


        Intent intent = new Intent(c, HomeActivity.class);
        startActivityForResult(intent, 0);
    }
}

