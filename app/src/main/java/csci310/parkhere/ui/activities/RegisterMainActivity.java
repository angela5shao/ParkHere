package csci310.parkhere.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import csci310.parkhere.R;
import csci310.parkhere.controller.ClientController;

/**
 * Created by ivylinlaw on 10/16/16.
 */
public class RegisterMainActivity extends Activity {
    Button _nextButton;
    EditText _nameText, _emailText, _passwordText, _phoneText;
    Spinner _usertypeSpinner;
    ClientController clientController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main_ui);

        clientController = ClientController.getInstance();
        clientController.setCurrentActivity(this);

        _nextButton=(Button)findViewById(R.id.registerNextButton);
        _nameText=(EditText)findViewById(R.id.nameText); // name
        _nameText.setHint("FirstName LastName");

        _emailText=(EditText)findViewById(R.id.emailText); // email
        _emailText.setHint("ttrojan@usc.edu");

        _passwordText=(EditText)findViewById(R.id.passwordText); // password
        _passwordText.setHint("***********");

        _phoneText=(EditText)findViewById(R.id.phoneText); // phone number
        _phoneText.setHint("888-888-8888");

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

                String[] nameWord = name.split(" ");
                if(nameWord.length != 2)
                {
                    Toast.makeText(getBaseContext(), "Please input firstname and lastname", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isEmailValid(email))
                {
                    Toast.makeText(getBaseContext(), "Please input valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length() < 10)
                {
                    Toast.makeText(getBaseContext(), "Please input password longer than 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(password);
                boolean hasSpecialChar = m.find();

                if (!hasSpecialChar) {
                    Toast.makeText(getBaseContext(), "Please input a password with a special character (ex: @#$%)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isNumeric(phonenum) || phonenum.length() != 10)
                {
                    Toast.makeText(getBaseContext(), "Please input valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }


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

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

}
