package com.example.hotelbooking.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class UserBookRoomActivity extends AppCompatActivity {
    private Button mdatePicker;
    private TextView textView;
    private Button proceedToBookBtn,noOfRoomsIncrementBtn,noOfRoomsDecrementBtn,noOfPersonsIncrementBtn,noOfPersonsDecrementBtn;
    private TextView noOfRoomsTv,noOfPersonsTv,RoomPriceTV;
    private EditText guestNameTv,guestEmailTv,guestPhoneNoTv;
    private int roomscount=1,personscount=1;
    private String roomprice,roomid;
    private DatabaseReference bookingref,roomref,agentbookingsref;
    private FirebaseAuth mauth;
    private String currentuserid;
    String guestname,guestemail,guestphoneno,noofrooms,noofpersons,bookingdates,totalprice;
    private String saveCurrentDate,saveCurrentTime,BookingRandomKey;
    private String agentid,hotelname,hoteladdress,roomimage,hotelfacility1,hotelfacility2,hotelfacility3,roomdiscription,
                roomtype,hotelcity,hotelstate,hotelpincode,hotelimage,acornonac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_room);

        roomprice=getIntent().getExtras().get("roomprice").toString();
        roomid=getIntent().getExtras().get("roomid").toString();

        bookingref= FirebaseDatabase.getInstance().getReference().child("Bookings");
        roomref= FirebaseDatabase.getInstance().getReference().child("Rooms");
        agentbookingsref=FirebaseDatabase.getInstance().getReference().child("AgentBookings");

        roomref.child(roomid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    agentid=dataSnapshot.child("agentid").getValue().toString();
                    acornonac=dataSnapshot.child("acornonac").getValue().toString();
                    hotelname=dataSnapshot.child("hotelname").getValue().toString();
                    hoteladdress=dataSnapshot.child("hoteladdress").getValue().toString();
                    roomimage=dataSnapshot.child("roomimage").getValue().toString();
                    hotelfacility1=dataSnapshot.child("hotelfacility1").getValue().toString();
                    hotelfacility2=dataSnapshot.child("hotelfacility2").getValue().toString();
                    hotelfacility3=dataSnapshot.child("hotelfacility3").getValue().toString();
                    roomdiscription=dataSnapshot.child("roomdiscription").getValue().toString();
                    roomtype=dataSnapshot.child("roomtype").getValue().toString();
                    hotelcity=dataSnapshot.child("hotelcity").getValue().toString();
                    hotelstate=dataSnapshot.child("hotelstate").getValue().toString();
                    hotelpincode=dataSnapshot.child("hotelpincode").getValue().toString();
                    hotelimage=dataSnapshot.child("hotelimage").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mauth=FirebaseAuth.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();

        mdatePicker=findViewById(R.id.user_book_room_dates);
        textView=findViewById(R.id.user_book_room_no_of_date_text_view);
        Calendar calendar=Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        final long today= MaterialDatePicker.todayInUtcMilliseconds();
        calendar.setTimeInMillis(today);
        long month=MaterialDatePicker.thisMonthInUtcMilliseconds();
        CalendarConstraints.Builder constraintbuilder=new CalendarConstraints.Builder();
        constraintbuilder.setStart(month);
        constraintbuilder.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker.Builder<Pair<Long,Long>> builder=MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a date");
        builder.setCalendarConstraints(constraintbuilder.build());
        final MaterialDatePicker materialDatePicker=builder.build();
        mdatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");

            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                textView.setText("selected dates: "+materialDatePicker.getHeaderText());
                bookingdates=materialDatePicker.getHeaderText();
            }
        });


        InitializeFields();
        RoomPriceTV.setText("Price: "+Integer.toString(Integer.parseInt(roomprice)*roomscount));
        noOfRoomsDecrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roomscount!=1) {
                    roomscount -= 1;
                    noOfRoomsTv.setText(Integer.toString(roomscount));
                    RoomPriceTV.setText("Price: "+Integer.toString(Integer.parseInt(roomprice)*roomscount));
                }
            }
        });
        noOfRoomsIncrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roomscount<=3) {
                    roomscount += 1;
                    noOfRoomsTv.setText(Integer.toString(roomscount));
                    RoomPriceTV.setText("Price: "+Integer.toString(Integer.parseInt(roomprice)*roomscount));
                }
            }
        });
        noOfPersonsDecrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(personscount!=1) {
                    personscount -= 1;
                    noOfPersonsTv.setText(Integer.toString(personscount));
                }
            }
        });
        noOfPersonsIncrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(personscount<=2) {
                    personscount += 1;
                    noOfPersonsTv.setText(Integer.toString(personscount));
                }
            }
        });
        proceedToBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validatedata();
            }
        });


    }

    private void InitializeFields() {
        proceedToBookBtn=findViewById(R.id.user_book_room_proceed_to_book);
        noOfRoomsIncrementBtn=findViewById(R.id.user_book_room_increment_rooms_btn);
        noOfRoomsDecrementBtn=findViewById(R.id.user_book_room_decrement_rooms_btn);
        noOfPersonsIncrementBtn=findViewById(R.id.user_book_room_increment_persons_btn);
        noOfPersonsDecrementBtn=findViewById(R.id.user_book_room_decrement_persons_btn);
        noOfRoomsTv=findViewById(R.id.user_book_room_no_of_rooms_no_tv);
        noOfPersonsTv=findViewById(R.id.user_book_room_no_of_persons_no_tv);
        RoomPriceTV=findViewById(R.id.user_book_room_price);
        guestNameTv=findViewById(R.id.user_book_room_guest_name);
        guestEmailTv=findViewById(R.id.user_book_room_guest_email);
        guestPhoneNoTv=findViewById(R.id.user_book_room_guest_pno);

    }
    private void Validatedata() {
        guestname=guestNameTv.getText().toString();
        guestemail=guestEmailTv.getText().toString();
        guestphoneno=guestPhoneNoTv.getText().toString();
        if(TextUtils.isEmpty(guestname)){
            Toast.makeText(this,"enter guest name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(guestemail)){
            Toast.makeText(this,"enter guest email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(guestphoneno)){
            Toast.makeText(this,"enter guest phone no",Toast.LENGTH_SHORT).show();
        }
        else {
            uploadDtaToDatabase();
        }


    }


    private void uploadDtaToDatabase() {
        noofrooms=Integer.toString(roomscount);
        noofpersons=Integer.toString(personscount);
        totalprice=Integer.toString(Integer.parseInt(roomprice)*roomscount);



        final HashMap<String,Object> bookingMap=new HashMap<>();
        bookingMap.put("guestname",guestname);
        bookingMap.put("guestemail",guestemail);
        bookingMap.put("guestphone",guestphoneno);
        bookingMap.put("noofrooms",noofrooms);
        bookingMap.put("noofpersons",noofpersons);
        bookingMap.put("bookingdates",bookingdates);
        bookingMap.put("totalprice",totalprice);
        bookingMap.put("roomid",roomid);
        bookingMap.put("paymentstatus","incomplete");
        bookingMap.put("bookingstatus","notbooked");
        bookingMap.put("agentid",agentid);

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM  dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH;mm;ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());
        BookingRandomKey=saveCurrentDate+saveCurrentTime;
        bookingMap.put("bookingid",BookingRandomKey);
        bookingMap.put("hotelname",hotelname);
        bookingMap.put("hoteladdress",hoteladdress);
        bookingMap.put("roomimage",roomimage);
        bookingMap.put("hotelfacility1",hotelfacility1);
        bookingMap.put("hotelfacility2",hotelfacility2);
        bookingMap.put("hotelfacility3",hotelfacility3);
        bookingMap.put("roomdiscription",roomdiscription);
        bookingMap.put("roomtype",roomtype);
        bookingMap.put("hotelcity",hotelcity);
        bookingMap.put("hotelstate",hotelstate);
        bookingMap.put("hotelpincode",hotelimage);
        bookingMap.put("acornonac",acornonac);
        bookingMap.put("roomprice",roomprice);

        //put hotel details in bookings to display in bookings fragment

        bookingref.child(currentuserid).child(BookingRandomKey).updateChildren(bookingMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    agentbookingsref.child(agentid).child(BookingRandomKey).updateChildren(bookingMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                sendUserToPaymentOrPayAtHotelActivity();
                            }

                        }
                    });
                }
            }
        });


    }

    private void sendUserToPaymentOrPayAtHotelActivity() {
        Intent intent=new Intent(this,PaymentOrPayAtHotelActivity.class);
        intent.putExtra("totalprice",totalprice);
        intent.putExtra("bookingid",BookingRandomKey);
        intent.putExtra("agentid",agentid);
        startActivity(intent);
    }
}