package com.example.hotelbooking.Agent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AgentProfileActivity extends AppCompatActivity {
    private TextView agentnameTV;
    private EditText agentPhonenoET,agentemailET,hotelnameET,hoteladdressET,hotelCityET,hotelratingET;
    private Button updatebtn;
    private ImageView agentprofileimg;
    private DatabaseReference agentref;
    private String saveCurrentDate, saveCurrentTime;
    private String AgentRandomKey, downloadimgurl;
    private FirebaseAuth mauth;
    private StorageReference AgentImageRef;
    private ProgressDialog loadingBar;
    private String agentname,agentPhoneno,agentemail,hotelname,hoteladdress,hotelCity,hotelrating,agentimage;
    private Uri Imageuri;
    private int GALLARYPICK=1;
    private String currentuserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile);

        agentref= FirebaseDatabase.getInstance().getReference().child("Agents");
        AgentImageRef= FirebaseStorage.getInstance().getReference().child("Agent Images");
        mauth=FirebaseAuth.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();

        InitializationFields();
        getdetailsfromdatabaseAndDisplay();
        agentprofileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAgentInfo();
            }
        });
    }

    private void InitializationFields() {
        agentnameTV=findViewById(R.id.agent_name);
        agentPhonenoET=findViewById(R.id.agent_phone_no);
        agentemailET=findViewById(R.id.agent_email);
        hotelnameET=findViewById(R.id.agent_hotel_name);
        hoteladdressET=findViewById(R.id.agent_hotel_address);
        hotelCityET=findViewById(R.id.agent_hotel_star_City);
        hotelratingET=findViewById(R.id.agent_hotel_rating);
        updatebtn=findViewById(R.id.agent_details_update_btn);
        agentprofileimg=findViewById(R.id.agent_profile_image);
        loadingBar = new ProgressDialog(this,R.style.MydialogTheme);

    }
    private void getdetailsfromdatabaseAndDisplay() {
        agentref.child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    agentname = dataSnapshot.child("name").getValue().toString();
                    agentPhoneno = dataSnapshot.child("phone").getValue().toString();
                    agentemail = dataSnapshot.child("email").getValue().toString();
                    hotelname = dataSnapshot.child("hotelname").getValue().toString();
                    hoteladdress = dataSnapshot.child("hoteladdress").getValue().toString();
                    hotelCity = dataSnapshot.child("city").getValue().toString();
                    agentnameTV.setText(agentname);
                    agentPhonenoET.setText(agentPhoneno);
                    agentemailET.setText(agentemail);
                    hotelnameET.setText(hotelname);
                    hoteladdressET.setText(hoteladdress);
                    hotelCityET.setText(hotelCity);
                }
                if(dataSnapshot.hasChild("agentimage")){
                    agentimage = dataSnapshot.child("agentimage").getValue().toString();
                    hotelrating = dataSnapshot.child("hotelrating").getValue().toString();
                    Picasso.get().load(agentimage).into(agentprofileimg);
                    hotelratingET.setText(hotelrating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void OpenGallery(){
        Intent gallaryintent=new Intent();
        gallaryintent.setAction(Intent.ACTION_GET_CONTENT);
        gallaryintent.setType("image/*");
        startActivityForResult(gallaryintent,GALLARYPICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLARYPICK && resultCode==RESULT_OK && data!=null){
            Imageuri=data.getData();
            agentprofileimg.setImageURI(Imageuri);
        }
    }

    private void validateAgentInfo() {
        agentPhoneno = agentPhonenoET.getText().toString();
        agentemail = agentemailET.getText().toString();
        hotelname = hotelnameET.getText().toString();
        hoteladdress = hoteladdressET.getText().toString();
        hotelCity = hotelCityET.getText().toString();
        hotelrating = hotelratingET.getText().toString();

        if (Imageuri == null) {
            Toast.makeText(this, "Agent Image is Mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(agentPhoneno)) {
            Toast.makeText(this, "please enter agent Phoneno", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(agentemail)) {
            Toast.makeText(this, "please enter agent email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(hotelname)) {
            Toast.makeText(this, "please enter hotel name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(hoteladdress)) {
            Toast.makeText(this, "please enter hotel address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(hotelCity)) {
            Toast.makeText(this, "please enter hotel city", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(hotelrating)) {
            Toast.makeText(this, "please enter hotel rating", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Adding Hotel Info");
            loadingBar.setMessage("Please wait while we are updating Hotel information");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            storeagentimage();
        }
    }

    private void storeagentimage() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM  dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH;mm;ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        AgentRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = AgentImageRef.child(Imageuri.getLastPathSegment() + AgentRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(Imageuri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AgentProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AgentProfileActivity.this, "Agent Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadimgurl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadimgurl = task.getResult().toString();
                            Toast.makeText(AgentProfileActivity.this, "got agent image url Successfully", Toast.LENGTH_SHORT).show();
                            saveagentinfotodatabase();

                        }
                    }
                });

            }
        });
    }

    private void saveagentinfotodatabase() {
        HashMap<String,Object> agentMap=new HashMap<>();
        agentMap.put("name",agentname);
        agentMap.put("phone",agentPhoneno);
        agentMap.put("email",agentemail);
        agentMap.put("hotelname",hotelname);
        agentMap.put("hoteladdress",hoteladdress);
        agentMap.put("city",hotelCity);
        agentMap.put("hotelrating",hotelrating);
        agentMap.put("agentimage",downloadimgurl);


        agentref.child(currentuserid).updateChildren(agentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(AgentProfileActivity.this, AgentHomeActivity.class);
                    startActivity(intent);
                    loadingBar.dismiss();
                    Toast.makeText(AgentProfileActivity.this, "Hotel details added Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AgentProfileActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }




}
