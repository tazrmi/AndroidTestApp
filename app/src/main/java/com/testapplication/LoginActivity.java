package com.testapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.testapplication.Helpers.ConnectionHelper;
import com.testapplication.Helpers.WebServiceController;
import com.testapplication.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking if the user token is already saved. If so, the login screen is skipped
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        if (sharedPref.contains("token")) {

            User.getInstance().setToken(sharedPref.getString("token", " "));

            Intent blogListIntent = new Intent(LoginActivity.this, BlogList.class);
            startActivity(blogListIntent);

        } else {
            setContentView(R.layout.activity_login);
            mEmailView = (EditText) findViewById(R.id.email);

            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
        }
    }

    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;

        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (!cancel){
            // Check for an Internet connection
            if (ConnectionHelper.isNetworkAvailable(getApplicationContext())){
                WebServiceController.loginUser(getApplicationContext(), this);
            }
            else {
                Toast.makeText(getApplicationContext(), "There was no Internet connection detected. Please turn on the Inetrnet connection and try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Recieving the data from the web service, saving the user token and starting the next activity
    public void tokenRecieved(JSONObject object) throws JSONException {
        if (object.isNull("token")){
            Toast.makeText(getApplicationContext(), "There has been a mistake. Please try again.", Toast.LENGTH_SHORT).show();
        }
        else {
            User.getInstance().setToken(object.getString("token"));

            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("token", object.getString("token"));
            editor.commit();

            Intent blogListIntent = new Intent(LoginActivity.this, BlogList.class);
            startActivity(blogListIntent);
        }
    }

    private boolean isEmailValid(String email) {
        final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
}

