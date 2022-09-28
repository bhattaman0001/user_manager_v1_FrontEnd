package com.example.user_manager_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user_manager_v1.helpers.StringHelper;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText first_name, last_name, email, password, confirm;
    Button sign_up_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // hook edittext fields
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);

        // hook button field
        sign_up_btn = findViewById(R.id.sign_up_btn);
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processFormFields();
            }
        });
    }
    // end of onCreate method

    public void backToHome(View view) {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }
    // end of backToHome method

    public void goToSignInAct(View view) {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }
    // end of goToSignInAct method


    public void processFormFields(){
        // end for errors
        if(!validateFirstName() || !validateLastName() || !validateEmail() || !validatePasswordAndConfirm()){
            return;
        }// end of check for errors

        // instantiate the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);

        // the url posting to
        String url = "http://192.168.98.210:9080/api/v1/user/register";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                System.out.println("Response is: "+response);

                if(response.equalsIgnoreCase("success")){
                    first_name.setText(null);
                    last_name.setText(null);
                    email.setText(null);
                    password.setText(null);
                    confirm.setText(null);
                    Toast.makeText(SignUpActivity.this, "Registration is Successful", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                 // print error
//                error.printStackTrace();
//                System.out.println(error.getMessage());

                Toast.makeText(SignUpActivity.this, "Registration is UnSuccessful", Toast.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", first_name.getText().toString());
                params.put("last_name", last_name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        }; // end of string request object

        requestQueue.add(stringRequest);
    }
    // end of processFormFields method


    public boolean validateFirstName() {
        String firstName = first_name.getText().toString();

        // check if first name is empty
        if (firstName.isEmpty()) {
            first_name.setError("First name cannot be Empty");
            return false;
        } else {
            first_name.setError(null);
            return true;
        }// check if first name is empty
    }
    // end of validateFirstName method


    public boolean validateLastName() {
        String lastName = last_name.getText().toString();

        // check if last name is empty
        if (lastName.isEmpty()) {
            last_name.setError("Last name cannot be Empty");
            return false;
        } else {
            last_name.setError(null);
            return true;
        }// check if last name is empty
    }
    // end of validateLastName method


    public boolean validateEmail() {
        String email_e = email.getText().toString();

        // check if email is empty
        if (email_e.isEmpty()) {
            email.setError("Email cannot be Empty");
            return false;
        }else if(!StringHelper.regexEmailValidationPattern(email_e)){
            email.setError("Please enter a valid email");
            return false;
        } else {
            email.setError(null);
            return true;
        }// check if email is empty
    }
    // end of validateEmail method

    public boolean validatePasswordAndConfirm() {
        String password_p = password.getText().toString();
        String confirm_p = confirm.getText().toString();

        // check if PasswordAndConfirm is empty
        if (password_p.isEmpty() || confirm_p.isEmpty()) {
            password.setError("Password cannot be Empty");
            confirm.setError("Confirm field cannot be Empty");
            return false;
        }else if(!password_p.equals(confirm_p)){
            confirm.setError("Confirm password must be same as password");
            return false;
        } else {
            password.setError(null);
            confirm.setError(null);
            return true;
        }// check if PasswordAndConfirm is empty
    }
    // end of validatePasswordAndConfirm method

}
// end of SignUpActivity class