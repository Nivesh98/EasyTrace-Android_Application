package com.nivacreation.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class SignInActivity extends AppCompatActivity {

    private TextView txtSignUp;
    private EditText edtEmailSignIn, edtPasswordSignIn;
    private Button btnLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtSignUp = findViewById(R.id.txtButtonSignIn);
        edtEmailSignIn = findViewById(R.id.emailSignIn);
        edtPasswordSignIn = findViewById(R.id.passwordSignIn);
        btnLogin = findViewById(R.id.btnLogIn);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

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
        String email = edtEmailSignIn.getText().toString().trim();
        String password = edtPasswordSignIn.getText().toString().trim();

        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            if (!password.isEmpty())
            {
                if (!(password.length() < 7))
                {
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(SignInActivity.this, "Login Successfully !!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                finish();
                            }else
                                {
                                    Toast.makeText(SignInActivity.this, "Login Error !!!", Toast.LENGTH_SHORT).show();
                                }

                        }
                    });

//                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
//                            Toast.makeText(SignInActivity.this, "Login Successfully !!", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
//                            finish();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NotNull Exception e) {
//                            Toast.makeText(SignInActivity.this, "Login Error !!!", Toast.LENGTH_SHORT).show();
//                        }
//                    });

                }else
                    {
                        edtPasswordSignIn.setError("tPasswords length should be >=7 !");
                    }

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