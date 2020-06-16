package com.example.hotelbooking.Agent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hotelbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AgentRegistrationActivity extends AppCompatActivity {
    private EditText name,phoneno,email,hotelname,hotelAddress,city,state,pincode;
    private Button createAccountBtn;
    private String check;
    private DatabaseReference rootref;
    private FirebaseAuth mAuth;
    private String currentuerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_registration);

        mAuth=FirebaseAuth.getInstance();
        currentuerid=mAuth.getCurrentUser().getUid();
        rootref= FirebaseDatabase.getInstance().getReference();

        Toast.makeText(this,"AgentRegistrationActivity",Toast.LENGTH_SHORT).show();
        Log.i("AgentRegistrationActivity","AgentRegistrationActivity");


        check=getIntent().getExtras().get("check").toString();
        Toast.makeText(this,check,Toast.LENGTH_SHORT).show();
        InitializeFields();


        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }




    private void InitializeFields() {
        name=findViewById(R.id.agent_name);
        phoneno=findViewById(R.id.agent_mobile_no);
        email=findViewById(R.id.agent_email);
        hotelname=findViewById(R.id.hotel_name);
        hotelAddress=findViewById(R.id.hotel_address);
        city=findViewById(R.id.hotel_city);
        state=findViewById(R.id.hotel_state);
        pincode=findViewById(R.id.hotel_pincode);
        createAccountBtn=findViewById(R.id.create_agent_acc_btn);

        if(check.equals("email")){
            email.setFocusable(false);
            phoneno.setFocusable(true);
        }
        if(check.equals("phone")){
            email.setFocusable(true);
            phoneno.setFocusable(false);
        }
    }
    private void createAccount() {
        HashMap<String,Object> agentMap=new HashMap<>();

        agentMap.put("name",name.getText().toString());
        if(check.equals("email")){
            agentMap.put("phone",phoneno.getText().toString());
        }
        if(check.equals("phone")){
            agentMap.put("email",email.getText().toString());
        }
        agentMap.put("hotelname",hotelname.getText().toString());
        agentMap.put("hoteladdress",hotelAddress.getText().toString());
        agentMap.put("city",city.getText().toString());
        agentMap.put("state",state.getText().toString());
        agentMap.put("pincode",pincode.getText().toString());
        agentMap.put("admin","access");
        rootref.child("Agents").child(currentuerid).updateChildren(agentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AgentRegistrationActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                    sendusertoAgentHomeActivity();
                }
                else {
                    Toast.makeText(AgentRegistrationActivity.this,"error",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void sendusertoAgentHomeActivity() {
        Intent intent=new Intent(this,AgentHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
