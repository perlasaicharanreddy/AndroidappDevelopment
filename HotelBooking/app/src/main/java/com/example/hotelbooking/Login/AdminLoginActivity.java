package com.example.hotelbooking.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.drm.ProcessedData;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hotelbooking.Admin.AdminHomeActivity;
import com.example.hotelbooking.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText usernameET,passwordET;
    private Button loginbtn;
    private String username,password;
    private DatabaseReference ref;
    private  String adminname,adminpassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

       usernameET=findViewById(R.id.admin_user_name);
        passwordET=findViewById(R.id.admin_password);
        loginbtn=findViewById(R.id.admin_login_Btn);
        progressDialog=new ProgressDialog(this,R.style.MydialogTheme);

        ref=FirebaseDatabase.getInstance().getReference().child("Admin");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    adminname=dataSnapshot.child("name").getValue().toString();
                    adminpassword=dataSnapshot.child("password").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("login in");
                progressDialog.setMessage("Please wait while logging in");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                username=usernameET.getText().toString();
                password=passwordET.getText().toString();
                if(username.equals(adminname)&&password.equals(adminpassword)){
                    progressDialog.dismiss();
                    sendusertoAdminHomeActivity();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(AdminLoginActivity.this,"login unsuccessful",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void sendusertoAdminHomeActivity() {
        Intent intent=new Intent(this, AdminHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
