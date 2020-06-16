package com.example.hotelbooking.Agent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hotelbooking.Admin.AdminHomeActivity;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewRoomActivity extends AppCompatActivity {
    private String roomtype;
    private DatabaseReference roomref,agentref;
    private StorageReference RoomImageRef;
    private ImageView roomimage;
    private EditText acornonacET,roomdiscriptionET,roompriceET,noofroomsET;
    private Button addRoomBtn;
    private Uri Imageuri;
    private int GALLARYPICK=1;
    private String acornonac,roomdiscription,roomprice,noofrooms;
    private String agentname,agentphno,agentemail,hotelname,hoteladdress,hotelcity,hotelstate,hotelpincode;
    private String hotelimage,hotelfacility1,hotelfacility2,hotelfacility3,hoteldescription;
    private String saveCurrentDate,saveCurrentTime;
    private String RoomRandomKey,downloadimgurl;
    private FirebaseAuth mauth;
    private String currentuserid;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_room);

        mauth=FirebaseAuth.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();

     roomtype=getIntent().getExtras().get("Roomtype").toString();

     agentref= FirebaseDatabase.getInstance().getReference().child("Agents");
     roomref=FirebaseDatabase.getInstance().getReference().child("Rooms");
     RoomImageRef= FirebaseStorage.getInstance().getReference().child("Room Images");

     InitializaionFields();

     getCurrentAgentDetails();

     roomimage.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             OpenGallery();
         }
     });

     addRoomBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             loadingBar.setTitle("Add new Room");
             loadingBar.setMessage("Please wait while we are adding new Room");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();
             validateroomInfo();
         }
     });


    }

    private void InitializaionFields() {
        roomimage=findViewById(R.id.select_room_img);
        acornonacET=findViewById(R.id.ac_or_nonac);
        roomdiscriptionET=findViewById(R.id.room_description);
        roompriceET=findViewById(R.id.room_price);
        noofroomsET=findViewById(R.id.no_of_rooms);
        addRoomBtn=findViewById(R.id.add_new_room);
        loadingBar = new ProgressDialog(this,R.style.MydialogTheme);

    }

    @Override
    protected void onStart() {
        super.onStart();
        agentref.child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("hotelfacility1")){
                    Toast.makeText(AddNewRoomActivity.this,"Add hotel details first",Toast.LENGTH_LONG).show();
                    sendUsertoAgentDetailsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendUsertoAgentDetailsActivity() {
            Intent intent=new Intent(this, HotelDetailsActivity.class);
            startActivity(intent);
    }

    private void getCurrentAgentDetails() {
        agentref.child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    agentname=dataSnapshot.child("name").getValue().toString();
                    agentphno=dataSnapshot.child("phone").getValue().toString();
                    agentemail=dataSnapshot.child("email").getValue().toString();
                    hotelname=dataSnapshot.child("hotelname").getValue().toString();
                    hoteladdress=dataSnapshot.child("hoteladdress").getValue().toString();
                    hotelcity=dataSnapshot.child("city").getValue().toString();
                    hotelstate=dataSnapshot.child("state").getValue().toString();
                    hotelpincode=dataSnapshot.child("pincode").getValue().toString();
                }
                if(dataSnapshot.exists()&&dataSnapshot.hasChild("hotelimage")){
                    hotelimage=dataSnapshot.child("hotelimage").getValue().toString();
                    hotelfacility1=dataSnapshot.child("hotelfacility1").getValue().toString();
                    hotelfacility2=dataSnapshot.child("hotelfacility2").getValue().toString();
                    hotelfacility3=dataSnapshot.child("hotelfacility3").getValue().toString();
                    hoteldescription=dataSnapshot.child("hoteldescription").getValue().toString();
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
            roomimage.setImageURI(Imageuri);
        }
    }

    private void validateroomInfo() {
        acornonac=acornonacET.getText().toString();
        roomdiscription=roomdiscriptionET.getText().toString();
        roomprice=roompriceET.getText().toString();
        noofrooms=noofroomsET.getText().toString();
        if(Imageuri==null){
            Toast.makeText(this,"Room Image is Mandatory",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(acornonac)){
            Toast.makeText(this,"please enter room has ac or not",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(roomdiscription)){
            Toast.makeText(this,"please enter room description",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(roomprice)){
            Toast.makeText(this,"please enter room price",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(noofrooms)){
            Toast.makeText(this,"please enter no of rooms you want to add",Toast.LENGTH_SHORT).show();
        }
        else {
            storeRoomInformation();
        }
    }

    private void storeRoomInformation() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM  dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH;mm;ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());
        RoomRandomKey=saveCurrentDate+saveCurrentTime;

        final StorageReference filePath=RoomImageRef.child(Imageuri.getLastPathSegment() + RoomRandomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(Imageuri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(AddNewRoomActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddNewRoomActivity.this,"Room Image Uploaded Successfully",Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadimgurl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadimgurl=task.getResult().toString();
                            Toast.makeText(AddNewRoomActivity.this,"got room image url Successfully",Toast.LENGTH_SHORT).show();
                            saveRoominfotodatabase();

                        }
                    }
                });

            }
        });
    }

    private void saveRoominfotodatabase() {
        HashMap<String,Object> roomMap=new HashMap<>();
        roomMap.put("roomrandomkey",RoomRandomKey);
        roomMap.put("acornonac",acornonac);
        roomMap.put("roomimage",downloadimgurl);
        roomMap.put("roomdiscription",roomdiscription);
        roomMap.put("roomprice",roomprice);
        roomMap.put("noofrooms",noofrooms);
        roomMap.put("acornonac",acornonac);
        roomMap.put("roomtype",roomtype);
        roomMap.put("agentid",currentuserid);

        roomMap.put("agentname",agentname);
        roomMap.put("agentphno",agentphno);
        roomMap.put("agentemail",agentemail);
        roomMap.put("hotelname",hotelname);
        roomMap.put("hoteladdress",hoteladdress);
        roomMap.put("hotelcity",hotelcity);
        roomMap.put("hotelstate",hotelstate);
        roomMap.put("hotelpincode",hotelpincode);
        if(!hotelimage.equals("")){
            roomMap.put("hotelimage",hotelimage);
            roomMap.put("hotelfacility1",hotelfacility1);
            roomMap.put("hotelfacility2",hotelfacility2);
            roomMap.put("hotelfacility3",hotelfacility3);
            roomMap.put("hoteldescription",hoteldescription);
        }




        roomref.child(RoomRandomKey).updateChildren(roomMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Intent intent=new Intent(AddNewRoomActivity.this, AgentHomeActivity.class);
//                    intent.putExtra("toOpen", "AddNewRoomCategoryFragment");
                    startActivity(intent);
                    loadingBar.dismiss();
                    Toast.makeText(AddNewRoomActivity.this, "room is added Successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingBar.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(AddNewRoomActivity.this,"Error"+message,Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
