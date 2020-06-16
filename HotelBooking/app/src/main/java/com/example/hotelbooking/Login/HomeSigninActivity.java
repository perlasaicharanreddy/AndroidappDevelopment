package com.example.hotelbooking.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.hotelbooking.R;

public class HomeSigninActivity extends AppCompatActivity {
    private Button phonelogin,emaillogin;
    private CheckBox agentbtn;
    private String userOragent="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_signin);
        phonelogin=findViewById(R.id.phone_login);
        emaillogin=findViewById(R.id.email_login);
        agentbtn=findViewById(R.id.check_box_agent);

        phonelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agentbtn.isChecked()){
                    userOragent="Agents";
                }
                sendUsertoPhoneloginActivity();
            }
        });
        emaillogin.setOnClickListener(new View.OnClickListener() {
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
        Intent intent=new Intent(this, EmailSignUpactivity.class);
        intent.putExtra("userOragent",userOragent);
        startActivity(intent);
    }

    private void sendUsertoPhoneloginActivity() {
        Intent intent=new Intent(this,PhoneSignUpActivity.class);
        intent.putExtra("userOragent",userOragent);
        startActivity(intent);
    }


}
