package com.example.hotelbooking.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.StringSearch;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.hotelbooking.Model.UserRoom;
import com.example.hotelbooking.R;
import com.example.hotelbooking.ViewHolders.UserRoomViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private EditText searchet;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference roomref;
    private ImageButton searchbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        roomref= FirebaseDatabase.getInstance().getReference().child("Rooms");
        recyclerView=findViewById(R.id.search_recyclear_view);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(recyclerView);

        searchet=findViewById(R.id.search_et);

        searchet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onStart();

            }
        });
        searchbtn=findViewById(R.id.search_btn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<UserRoom> options=new FirebaseRecyclerOptions.Builder<UserRoom>()
                .setQuery(roomref.orderByChild("hotelcity").startAt(searchet.getText().toString()),UserRoom.class)
                .build();
        FirebaseRecyclerAdapter<UserRoom, UserRoomViewHolder> adapter=new FirebaseRecyclerAdapter<UserRoom, UserRoomViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserRoomViewHolder userRoomViewHolder, final int i, @NonNull UserRoom userRoom) {
                userRoomViewHolder.hotelname.setText(userRoom.getHotelname());
                userRoomViewHolder.hoteladdress.setText(userRoom.getHoteladdress());
                userRoomViewHolder.roomprice.setText(userRoom.getRoomprice());
                userRoomViewHolder.hotelfacility1.setText(userRoom.getHotelfacility1());
                userRoomViewHolder.hotelfacility2.setText(userRoom.getHotelfacility2());
                userRoomViewHolder.hotelfacility3.setText(userRoom.getHotelfacility3());
                userRoomViewHolder.roomdiscription.setText(userRoom.getRoomdiscription());
                Picasso.get().load(userRoom.getRoomimage()).into(userRoomViewHolder.roomImage);

                userRoomViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visituserId=getRef(i).getKey();
                        Intent intent=new Intent(SearchActivity.this,UserSelectedRoomPreviewActivity.class);
                        intent.putExtra("visituserid",visituserId);
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public UserRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_room_card_view,parent,false);
                final UserRoomViewHolder holder=new UserRoomViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
