package com.example.hotelbooking.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UserSelectedRoomPreviewActivity extends AppCompatActivity {
    String visituserid;
    private TextView hotelNameET,hotelAddressET,hotelCityET,hotelStateEt,hotelPincodeET,hotelDescriptionET,
    roomPriceEt,acOrnonAcET,facility1ET,facility2ET,facility3ET,roomtypeET,roomDescriptionET,agentContactET;
    private Button booknowBtn,saveBtn;
    private ImageView hotelImage,roomImage;
    private DatabaseReference roomref,savedlistref;
    private FirebaseAuth mauth;
    private String currentuserid;
    private String roompriceintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selected_room_preview);
        visituserid=getIntent().getExtras().get("visituserid").toString();
        roomref= FirebaseDatabase.getInstance().getReference().child("Rooms");
        savedlistref= FirebaseDatabase.getInstance().getReference().child("Savedlist");
        mauth=FirebaseAuth.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();


        InitializeFielda();
        booknowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToBookRoomActivity();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRoomtoSavedlist();
            }
        });
    }


    private void InitializeFielda() {
        hotelImage=findViewById(R.id.user_preview_hotel_image);
        roomImage=findViewById(R.id.user_preview_room_image);
        hotelNameET=findViewById(R.id.user_preview_card_hotel_name);
        hotelAddressET=findViewById(R.id.user_preview_card_hotel_address);
        hotelCityET=findViewById(R.id.user_preview_card_hotel_city);
        hotelStateEt=findViewById(R.id.user_preview_card_hotel_state);
        hotelPincodeET=findViewById(R.id.user_preview_card_hotel_pincode);
        hotelDescriptionET=findViewById(R.id.user_preview_card_hotel_description);
        roomPriceEt=findViewById(R.id.user_preview_card_room_price);
        acOrnonAcET=findViewById(R.id.user_preview_card_ac_or_nonac);
        facility1ET=findViewById(R.id.user_preview_card_hotel_facility1);
        facility2ET=findViewById(R.id.user_preview_card_hotel_facility2);
        facility3ET=findViewById(R.id.user_preview_card_hotel_facility3);
        roomtypeET=findViewById(R.id.user_preview_card_room_type);
        roomDescriptionET=findViewById(R.id.user_preview_card_room_description);
        agentContactET=findViewById(R.id.user_preview_card_contact_no);
        booknowBtn=findViewById(R.id.book_now_btn);
        saveBtn=findViewById(R.id.add_to_saved_list_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        roomref.child(visituserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Picasso.get().load(dataSnapshot.child("hotelimage").getValue().toString()).into(hotelImage);
                    Picasso.get().load(dataSnapshot.child("roomimage").getValue().toString()).into(roomImage);
                    hotelNameET.setText(dataSnapshot.child("hotelname").getValue().toString());
                    hotelAddressET.setText(dataSnapshot.child("hoteladdress").getValue().toString());
                    hotelCityET.setText("City: "+dataSnapshot.child("hotelcity").getValue().toString());
                    hotelStateEt.setText("State: "+dataSnapshot.child("hotelstate").getValue().toString());
                    hotelPincodeET.setText("Pincode: "+dataSnapshot.child("hotelpincode").getValue().toString());
                    hotelDescriptionET.setText(dataSnapshot.child("hoteldescription").getValue().toString());
                    roomPriceEt.setText("Price "+dataSnapshot.child("roomprice").getValue().toString());
                    acOrnonAcET.setText("Ac/NonAc: "+dataSnapshot.child("acornonac").getValue().toString());
                    facility1ET.setText(dataSnapshot.child("hotelfacility1").getValue().toString());
                    facility2ET.setText(dataSnapshot.child("hotelfacility2").getValue().toString());
                    facility3ET.setText(dataSnapshot.child("hotelfacility3").getValue().toString());
                    roomtypeET.setText("Room type: "+dataSnapshot.child("roomtype").getValue().toString());
                    roomDescriptionET.setText(dataSnapshot.child("roomdiscription").getValue().toString());
                    agentContactET.setText("Contact no: "+dataSnapshot.child("agentphno").getValue().toString());


                    roompriceintent=dataSnapshot.child("roomprice").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendUserToBookRoomActivity() {
        Intent intent=new Intent(this,UserBookRoomActivity.class);
        intent.putExtra("roomprice",roompriceintent);
        intent.putExtra("roomid",visituserid);
        startActivity(intent);
    }

    private void saveRoomtoSavedlist() {
        final String[] roomimage = new String[1];
        final String hotelname;
        final String hoteladdress = null;
        final String roomprice;
        final String hotelfacility1;
        final String hotelfacility2;
        final String hotelfacility3;
        final String roomdiscription;
        roomref.child(visituserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    HashMap<String,Object> savelist=new HashMap<>();
                    savelist.put("roomid",visituserid);//visituserid is roomid
                    savelist.put("roomimage",dataSnapshot.child("roomimage").getValue().toString());
                    savelist.put("hotelname",dataSnapshot.child("hotelname").getValue().toString());
                    savelist.put("hoteladdress",dataSnapshot.child("hoteladdress").getValue().toString());
                    savelist.put("roomprice",dataSnapshot.child("roomprice").getValue().toString());
                    savelist.put("hotelfacility1",dataSnapshot.child("hotelfacility1").getValue().toString());
                    savelist.put("hotelfacility2",dataSnapshot.child("hotelfacility2").getValue().toString());
                    savelist.put("hotelfacility3",dataSnapshot.child("hotelfacility3").getValue().toString());
                    savelist.put("roomdiscription",dataSnapshot.child("roomdiscription").getValue().toString());
                    savedlistref.child(currentuserid).child(visituserid).updateChildren(savelist).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                sendUsertoUserHomeActivity();
                            }
                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendUsertoUserHomeActivity() {
        Intent intent=new Intent(this,UserHomeActivity.class);
        startActivity(intent);
    }

}
