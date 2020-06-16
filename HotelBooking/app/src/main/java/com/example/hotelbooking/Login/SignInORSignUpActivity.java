package com.example.hotelbooking.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hotelbooking.R;

public class SignInORSignUpActivity extends AppCompatActivity {
    Button signin,signup;
    private TextView adminbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_o_r_sign_up);
        signup=findViewById(R.id.signup);
        signin=findViewById(R.id.signin);
        adminbtn=findViewById(R.id.admin);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             senduserToHomeSignInActivity();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senduserToSignInActivity();
            }
        });
        adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senduserToAdminLoginactivity();
            }
        });

    }


    private void senduserToHomeSignInActivity() {
        Intent intent=new Intent(this, HomeSigninActivity.class);
        startActivity(intent);
    }
    private void senduserToSignInActivity() {
        Intent intent=new Intent(this, PhoneOrEmailLoginActivity.class);
        startActivity(intent);
    }

    private void senduserToAdminLoginactivity() {
        Intent intent=new Intent(this, AdminLoginActivity.class);
        startActivity(intent);
    }


}
