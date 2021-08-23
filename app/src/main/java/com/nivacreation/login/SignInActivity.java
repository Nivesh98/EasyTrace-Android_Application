package com.nivacreation.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    private TextView txtSignUp;
    private EditText edtEmailSignIn, edtPasswordSignIn;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtSignUp = findViewById(R.id.txtButtonSignIn);
        edtEmailSignIn = findViewById(R.id.emailSignIn);
        edtPasswordSignIn = findViewById(R.id.passwordSignIn);
        btnLogin = findViewById(R.id.btnLogIn);

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation()
    {
        String email = edtEmailSignIn.getText().toString();
        String password = edtPasswordSignIn.getText().toString();

        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            if (!password.isEmpty())
            {
                startActivity(new Intent(SignInActivity.this,HomeActivity.class));
            }else
                {
                    edtPasswordSignIn.setError("Empty Fields Are Not Allowed !");
                }
        }else if (email.isEmpty())
        {
            edtEmailSignIn.setError("Empty Fields Are Not Allowed !");
        }else
            {
                edtEmailSignIn.setError("Please Enter Correct Email !");
            }
    }
}