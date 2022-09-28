package com.example.user_manager_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user_manager_v1.helpers.StringHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    Button sign_in_btn;
    EditText et_email, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // hook editText
        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);

        //hook Button
        sign_in_btn = findViewById(R.id.sign_in_btn);

        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser();
            }
        });
    }
    // end of onCreate method

    public void backToHome(View view) {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }
    // end of backToHome

    public void goToSignUpAct(View view) {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        finish();
    }
    // end of goToSignUpAct

    public void authenticateUser(){
        // end for errors
        if(!validateEmail() || !validatePassword()){
            return;
        }// end of check for errors

        // instantiate the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(SignInActivity.this);

        // the url posting to
        String url = "http://192.168.98.210:9080/api/v1/user/login";

        // set parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", et_email.getText().toString());
        params.put("password", et_password.getText().toString());

        // set request object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // get values form response
                    String first_name = (String) response.get("first_name");
                    String last_name = (String) response.get("last_name");
                    String email = (String) response.get("email");

                    // set the intent actions
                    Intent goToProfile = new Intent(SignInActivity.this, ProfileActivity.class);

                    // pass value to profile activity
                    goToProfile.putExtra("first_name", first_name);
                    goToProfile.putExtra("last_name", last_name);
                    goToProfile.putExtra("email", email);

                    // start activity
                    startActivity(goToProfile);
                    finish();

                }catch (JSONException e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }// end of try block
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
            }
        });
        // End of request object

        requestQueue.add(jsonObjectRequest);
    }

    public boolean validateEmail() {
        String email_e = et_email.getText().toString();

        // check if email is empty
        if (email_e.isEmpty()) {
            et_email.setError("Email cannot be Empty");
            return false;
        }else if(!StringHelper.regexEmailValidationPattern(email_e)){
            et_email.setError("Please enter a valid email");
            return false;
        } else {
            et_email.setError(null);
            return true;
        }// check if email is empty
    }
    // end of validateEmail method

    public boolean validatePassword(){
        String password = et_password.getText().toString();

        // check if PasswordAndConfirm is empty
        if (password.isEmpty()) {
            et_password.setError("Password cannot be Empty");
            return false;
        }else {
            et_password.setError(null);
            return true;
        }// check if Password is empty
    }
    // end of validatePassword method
}
// End of signInActivity