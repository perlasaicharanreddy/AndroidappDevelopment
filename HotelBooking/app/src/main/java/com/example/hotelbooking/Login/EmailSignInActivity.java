package com.example.hotelbooking.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hotelbooking.Agent.AgentHomeActivity;
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

public class EmailSignInActivity extends AppCompatActivity {
        private Button loginBtn;
        private EditText emailEt,passwordEt;
        private FirebaseAuth mauth;
        private ProgressDialog loadingbar;
        private DatabaseReference rootref;
        private String currentuserid;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_email_sign_in);

            mauth=FirebaseAuth.getInstance();
            // currentuser=mauth.getCurrentUser();
            rootref=FirebaseDatabase.getInstance().getReference();

            InitializeFields();

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allowUserToLogin();
                }
            });
        }


        private void InitializeFields() {
            loginBtn=findViewById(R.id.login_Btn);
            emailEt=findViewById(R.id.login_email);
            passwordEt=findViewById(R.id.login_password);
            loadingbar = new ProgressDialog(this,R.style.MydialogTheme);
        }


        private void allowUserToLogin(){
            String useremail = emailEt.getText().toString();
            String userpassword = passwordEt.getText().toString();
            if (TextUtils.isEmpty(useremail)) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(userpassword)) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            }
            else {
                loadingbar.setTitle("Sign In");
                loadingbar.setMessage("Please wait ...");
                loadingbar.setCanceledOnTouchOutside(true);
                loadingbar.show();
                mauth.signInWithEmailAndPassword(useremail,userpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(EmailSignInActivity.this,"Logged In Successful",Toast.LENGTH_SHORT).show();
                                    currentuserid=mauth.getCurrentUser().getUid();
                                    rootref.child("Users").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(currentuserid)){
                                                sendUserToUserHomeActivity();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    rootref.child("Agents").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(currentuserid)){
                                                sendUserToAgentHomeActivity();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    loadingbar.dismiss();
                                }
                                else {
                                    String message = task.getException().toString();
                                    Toast.makeText(EmailSignInActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                    loadingbar.dismiss();
                                }
                            }
                        });
            }

        }
    private void sendUserToUserHomeActivity() {
        Intent intent=new Intent(this, UserHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void sendUserToAgentHomeActivity() {
        Intent intent=new Intent(this, AgentHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
