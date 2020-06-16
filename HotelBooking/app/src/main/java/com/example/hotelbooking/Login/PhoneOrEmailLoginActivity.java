package com.example.hotelbooking.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.hotelbooking.R;

public class PhoneOrEmailLoginActivity extends AppCompatActivity {
    private Button phonebtn,emailbtn;
    private CheckBox agentbtn;
    private String userOragent="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_or_email_login);
        phonebtn=findViewById(R.id.home_login_phone_login);
        emailbtn=findViewById(R.id.home_login_email_login);
        agentbtn=findViewById(R.id.home_login_check_box_agent);

        phonebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agentbtn.isChecked()){
                    userOragent="Agents";
                }
                sendUsertoPhoneloginActivity();
            }
        });
        emailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agentbtn.isChecked()){
                    userOragent="Agents";
                }
                sendUsertoEmailloginActivity();
            }
        });
    }

    private void sendUsertoEmailloginActivity() {
        Intent intent=new Intent(this, EmailSignInActivity.class);
        startActivity(intent);
    }

    private void sendUsertoPhoneloginActivity() {
        Intent intent=new Intent(this,PhoneSignUpActivity.class);
        intent.putExtra("userOragent",userOragent);
        startActivity(intent);
    }

}
