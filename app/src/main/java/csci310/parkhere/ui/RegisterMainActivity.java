package csci310.parkhere.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import csci310.parkhere.R;

/**
 * Created by ivylinlaw on 10/16/16.
 */
public class RegisterMainActivity extends Activity {
    Button _nextButton;
    EditText _nameText, _emailText, _passwordText, _phoneText;
    Spinner _usertypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main_ui);

        _nextButton=(Button)findViewById(R.id.nextButton);
        _nameText=(EditText)findViewById(R.id.nameText); // name
        _emailText=(EditText)findViewById(R.id.emailText); // email
        _passwordText=(EditText)findViewById(R.id.passwordText); // password
        _phoneText=(EditText)findViewById(R.id.phoneText); // phone number

        _usertypeSpinner = (Spinner) findViewById(R.id.usertypeSpinner);

        _nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check input here?
                String name = _nameText.getText().toString();
                String email = _emailText.getText().toString();
                String password = _passwordText.getText().toString();
                String phonenum = _phoneText.getText().toString();
                //

                String usertype = _usertypeSpinner.getSelectedItem().toString();
                if(usertype.equals("Renter")) {
                    Intent intent = new Intent(getBaseContext(), RegisterRenterActivity.class);
                    intent.putExtra("NAME", name);
                    intent.putExtra("EMAIL", email);
                    intent.putExtra("PASSWORD", password);
                    intent.putExtra("PHONE_NUM", phonenum);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getBaseContext(), RegisterProviderActivity.class);
                    intent.putExtra("NAME", name);
                    intent.putExtra("EMAIL", email);
                    intent.putExtra("PASSWORD", password);
                    intent.putExtra("PHONE_NUM", phonenum);
                    startActivity(intent);
                }
            }
        });

    }


}
