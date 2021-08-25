package com.nivacreation.login;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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


public class MainActivity extends AppCompatActivity {

    private EditText txtEmailSign, txtPass, txtComPass , txtFirstName, txtlastName;
    private TextView txtButton; //,txtSaPass, txtSaConPass;
    private Button btnSignUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmailSign = findViewById(R.id.emailSignup);
        txtPass = findViewById(R.id.passwordSignup);
        txtComPass = findViewById(R.id.confirmPassword);
        txtButton = findViewById(R.id.txtButtonSignUp);
        btnSignUp = findViewById(R.id.btnSignup);
        //txtSaPass = findViewById(R.id.sample_password);
        //txtSaConPass = findViewById(R.id.sample_confirmPas);
        txtFirstName = findViewById(R.id.firstName);
        txtlastName = findViewById(R.id.lastName);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


        try{

        }catch (Exception e){
            Log.i("12345", "get instance error "+e);
        }
//        String sPass = txtPass.getText().toString();
//        String sCoPass = txtComPass.getText().toString();
//        txtSaPass.setText(sPass);
//        txtSaConPass.setText(sCoPass);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(MainActivity.this, sPass,Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, sCoPass,Toast.LENGTH_SHORT).show();
                createUser();
            }
        });

        txtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
            }
        });
    }


    private void createUser()
    {

        String email = txtEmailSign.getText().toString();
        String password = txtPass.getText().toString();
        String comPassword = txtComPass.getText().toString();
        String firstName = txtFirstName.getText().toString();
        String lastName = txtlastName.getText().toString();

        if (!firstName.isEmpty())
        {
            if(!lastName.isEmpty())
            {
                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    if(!password.isEmpty())
                    {
                        if (!(password.length() < 7))
                        {
                            if(!comPassword.isEmpty())
                            {
                                if (password.equals(comPassword))
                                {
                                    mAuth.createUserWithEmailAndPassword(email,password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NotNull Task<AuthResult> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(MainActivity.this, "Registered Successfully !!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                                                        finish();
                                                    }else
                                                        {
                                                            Toast.makeText(MainActivity.this, "Registration Error !!!", Toast.LENGTH_SHORT).show();
                                                        }

                                                }
                                            });

//                                    mAuth.createUserWithEmailAndPassword(email,password)
//                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                                @Override
//                                                public void onComplete(@NotNull Task<AuthResult> task) {
//                                                    Toast.makeText(MainActivity.this, "Registered Successfully !!", Toast.LENGTH_SHORT).show();
//                                                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
//                                                    finish();
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NotNull Exception e) {
//                                            Toast.makeText(MainActivity.this, "Registration Error !!!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
                                }else
                                {
                                    txtComPass.setError("Passwords are not matched");
                                }

                            }else
                            {
                                txtComPass.setError("Empty Fields Are Not Allowed");
                            }
                        }else
                            {
                                txtPass.setError("Passwords length should be >=7 !");
                            }


                    }else
                    {
                        txtPass.setError("Empty Fields Are Not Allowed");
                    }
                }else if (email.isEmpty())
                {
                    txtEmailSign.setError("Empty Fields Are Not Allowed");
                }else
                {
                    txtEmailSign.setError("Please Enter Correct Email");
                }
            }else
            {
                txtlastName.setError("Empty Fields Are Not Allowed");
            }

        }else
            {
                txtFirstName.setError("Empty Fields Are Not Allowed");
            }

    }
    private void userDetailsAdd(Task<AuthResult> task) {
        /*DatabaseReference userReference;
        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid());

        userReference.child("user_id").setValue(task.getResult().getUser().getUid());
        userReference.child("email").setValue(task.getResult().getUser().getEmail());
        userReference.child("role").setValue(spinner.getSelectedItem()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "Successfully added", Toast.LENGTH_LONG).show();
                }else{
                    Log.i("1234", "user added failed" );
                }
            }
        });*/
    }

}