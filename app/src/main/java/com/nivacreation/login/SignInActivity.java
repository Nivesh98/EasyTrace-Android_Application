package com.nivacreation.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;

public class SignInActivity extends AppCompatActivity {

    private TextView txtSignUp, txtForgetPass;
    private EditText edtEmailSignIn, edtPasswordSignIn;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    String userId;
    public String verifyUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtSignUp = findViewById(R.id.txtButtonSignIn);
        txtForgetPass = findViewById(R.id.txtForgetpassword);
        edtEmailSignIn = findViewById(R.id.emailSignIn);
        edtPasswordSignIn = findViewById(R.id.passwordSignIn);
        btnLogin = findViewById(R.id.btnLogIn);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

       userId = mAuth.getCurrentUser().getUid();

        txtForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link");
                passwordResetDialog.setView(resetMail);


                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail = resetMail.getText().toString();

                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SignInActivity.this, "Reset Link Sent To Your Email. ",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(SignInActivity.this, " Error ! Reset Link is Not Sent. " +e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });

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

    private void findUserType()
        {

            DocumentReference documentReference = fStore.collection("Users").document(userId);
            documentReference.addSnapshotListener(SignInActivity.this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                    verifyUserType = value.getString("User Type");
                    switch (verifyUserType)
                    {
                        case "Passenger":
                            Toast.makeText(SignInActivity.this, "Login Successfully !!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                            finish();
                            break;
                        case "Driver":
                            Toast.makeText(SignInActivity.this, "Login Successfully !!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this, Driver.class));
                            finish();
                            break;
                        case "Admin":
                            Toast.makeText(SignInActivity.this, "Login Successfully !!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this, Admin.class));
                            finish();
                            break;

                    }
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
                                findUserType();
                            }else
                                {
                                    Toast.makeText(SignInActivity.this, "Login Error !!!", Toast.LENGTH_SHORT).show();
                                }
                        }
                    });

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