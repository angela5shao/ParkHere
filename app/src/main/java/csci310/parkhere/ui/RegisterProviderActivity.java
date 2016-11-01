package csci310.parkhere.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;

/**
 * Created by ivylinlaw on 10/29/16.
 */
public class RegisterProviderActivity extends Activity {
    Button _nextButton;
    EditText _liscenseIdText;
    String name, email, password, phonenum;
    ClientController clientController;

    ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(RegisterProviderActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.
        try {
            clientController.register(email, password, phonenum, licenseID, "#######", "provider", name);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void onRegisterSuccess(Context c) {
        progressDialog.dismiss();
        Intent intent = new Intent(c, ProviderActivity.class);
        startActivityForResult(intent, 0);
    }


    public void onRegisterFailed(Context c) {
//        Toast.makeText(getBaseContext(), "register fail", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        Intent intent = new Intent(c, HomeActivity.class);
        startActivityForResult(intent, 0);
    }
}
