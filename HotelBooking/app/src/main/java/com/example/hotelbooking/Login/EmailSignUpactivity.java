package com.example.hotelbooking.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hotelbooking.Agent.AgentHomeActivity;
import com.example.hotelbooking.Agent.AgentRegistrationActivity;
import com.example.hotelbooking.R;
import com.example.hotelbooking.Users.UserHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmailSignUpactivity extends AppCompatActivity {
    private Button emailsigninBtn;
    private EditText emailEt, passwordEt;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;
    private DatabaseReference rootref;
    private String currentuserid;
    private String emailid;
    private String userOragent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signupactivity);

        mAuth = FirebaseAuth.getInstance();
        rootref= FirebaseDatabase.getInstance().getReference();

        userOragent=getIntent().getExtras().get("userOragent").toString();

        InitializeFields();
        emailsigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

    }

    private void InitializeFields() {
        emailsigninBtn = findViewById(R.id.email_signin);
        emailEt = findViewById(R.id.register_email);
        passwordEt = findViewById(R.id.register_password);
        loadingbar = new ProgressDialog(this,R.style.MydialogTheme);
    }
    private void sendusertoUserRegistrationActivity() {
        Intent intent=new Intent(this,UserRegistrationActivity.class);
        intent.putExtra("check","email");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void sendUserToUserHomeActivity() {
        Intent intent=new Intent(this, UserHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void createNewAccount() {
        String useremail = emailEt.getText().toString();
        String userpassword = passwordEt.getText().toString();
        if (TextUtils.isEmpty(useremail)) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userpassword)) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        } else {
            loadingbar.setTitle("Sign in");
            loadingbar.setMessage("Please wait While we are sign in for you");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            mAuth.createUserWithEmailAndPassword(useremail, userpassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String currentUserId=mAuth.getCurrentUser().getUid();
                                rootref.child(userOragent).child(currentUserId).setValue("");
                                emailid=mAuth.getCurrentUser().getEmail();
                                checkuserExistance();
                                Toast.makeText(EmailSignUpactivity.this, "signed in  Successfully", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(EmailSignUpactivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
                        }
                    });
        }
    }

    private void checkuserExistance() {
        currentuserid=mAuth.getCurrentUser().getUid();
        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentuserid)){
                    if(userOragent.equals("Users"))
                    sendUserToUserHomeActivity();
                    else sendUserToAgentHomeActivity();
                }
                else {
                    rootref.child(userOragent).child(currentuserid).child("email").setValue(emailid);
                    if(userOragent.equals("Users"))
                    sendusertoUserRegistrationActivity();
                    else sendUserToAgentRegistrationActivity();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendUserToAgentHomeActivity() {
        Intent intent=new Intent(this, AgentHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendUserToAgentRegistrationActivity() {
        Intent intent=new Intent(this, AgentRegistrationActivity.class);
        intent.putExtra("check","email");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
