package com.nivacreation.login;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText txtEmailSign, txtPass, txtComPass , txtFirstName, txtlastName;
    private TextView txtButton; //,txtSaPass, txtSaConPass;
    private Button btnSignUp;
    private ProgressBar progressBar;

    private RadioGroup radioGroupUsers;
    private RadioButton radioButtonUsers;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;

    String UserNameRadio;
    String userID, userIdLogin;
    public String verifyUserType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmailSign = findViewById(R.id.emailSignup);
        txtPass = findViewById(R.id.passwordSignup);
        txtComPass = findViewById(R.id.confirmPassword);
        txtButton = findViewById(R.id.txtButtonSignUp);
        btnSignUp = findViewById(R.id.btnSignup);
        txtFirstName = findViewById(R.id.firstName);
        txtlastName = findViewById(R.id.lastName);
        progressBar = findViewById(R.id.progressBar);

        radioGroupUsers = findViewById(R.id.rgUsers);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //This is a newly added. this is an example.
        if (mAuth.getCurrentUser() != null){
            findUserType();
            finish();
        }



        radioGroupUsers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButtonUsers = findViewById(checkedId);
                Toast.makeText(MainActivity.this, radioButtonUsers.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
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
//                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                Intent goPassengerActivity2 = new Intent(MainActivity.this, SignInActivity.class);
                goPassengerActivity2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                goPassengerActivity2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(goPassengerActivity2);
                finish();
            }
        });
    }

    private void findRadioChecked(int checkIdRadio) {
        switch (checkIdRadio)
        {
            case  R.id.rdPassenger :
                radioButtonUsers = findViewById(R.id.rdPassenger);
                UserNameRadio = radioButtonUsers.getText().toString();
                break;
//            case R.id.rdAdmin:
//                radioButtonUsers = findViewById(R.id.rdAdmin);
//                UserNameRadio = radioButtonUsers.getText().toString();
//                break;
            case R.id.rdDriver:
                radioButtonUsers = findViewById(R.id.rdDriver);
                UserNameRadio = radioButtonUsers.getText().toString();
                break;
        }


    }

    private void userTypeGoActivity(String userType){

        switch (userType)
        {
            case "Passenger":
                startActivity(new Intent(MainActivity.this, Passenger_Navigation.class));
                finish();
                break;
            case "Driver":
                startActivity(new Intent(MainActivity.this, Driver_Navigation.class));
                finish();
                break;
            case "Admin":
                startActivity(new Intent(MainActivity.this, Admin.class));
                finish();
                break;
        }
    }

    private void createUser()
    {
        int checkIdRadio = radioGroupUsers.getCheckedRadioButtonId();

        findRadioChecked(checkIdRadio);

        String email = txtEmailSign.getText().toString().trim();
        String password = txtPass.getText().toString().trim();
        String comPassword = txtComPass.getText().toString().trim();
        String firstName = txtFirstName.getText().toString();
        String lastName = txtlastName.getText().toString();
        String userType = UserNameRadio;

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
                                    progressBar.setVisibility(View.VISIBLE);
                                    mAuth.createUserWithEmailAndPassword(email,password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NotNull Task<AuthResult> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(MainActivity.this, "Registered Successfully !!", Toast.LENGTH_SHORT).show();
                                                        userID = mAuth.getCurrentUser().getUid();
                                                        DocumentReference documentReference = fStore.collection("Users").document(userID);
                                                        Map<String,Object> user = new HashMap<>();
                                                        user.put("First Name",firstName);
                                                        user.put("Last Name",lastName);
                                                        user.put("email",email);
                                                        user.put("User Type", userType);

                                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d(TAG,"onSuccess: User profile is created for "+ userID);
                                                            }
                                                        });
                                                        // check whether what type of user is
                                                        userTypeGoActivity(userType);
                                                        finish();
                                                    }else
                                                        {
                                                            Toast.makeText(MainActivity.this, "Registration Error !!!", Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
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

    private void findUserType() {
        userIdLogin = mAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("Users").document(userIdLogin);
        documentReference.addSnapshotListener(MainActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                verifyUserType = value.getString("User Type");
                switch (verifyUserType) {
                    case "Passenger":
                        //Toast.makeText(SignInActivity.this, "Login Successfully !!", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                        Intent goPassengerActivity = new Intent(MainActivity.this,HomeActivity.class);
                        goPassengerActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        goPassengerActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(goPassengerActivity);
                        finish();
                        break;
                    case "Driver":
                        //Toast.makeText(SignInActivity.this, "Login Successfully !!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, Driver.class));
                        finish();
                        break;
                    case "Admin":
                        //Toast.makeText(SignInActivity.this, "Login Successfully !!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, Admin.class));
                        finish();
                        break;

                }
            }
        });
    }

}