package com.example.hotelbooking.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.hotelbooking.Model.AgentRoom;
import com.example.hotelbooking.Model.UserRoom;
import com.example.hotelbooking.R;
import com.example.hotelbooking.ViewHolders.AgentRoomsViewHolder;
import com.example.hotelbooking.ViewHolders.UserRoomViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class UserAllRoomsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference roomref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_all_rooms);

        roomref= FirebaseDatabase.getInstance().getReference().child("Rooms");
        recyclerView=findViewById(R.id.user_all_rooms_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<UserRoom> options=new FirebaseRecyclerOptions.Builder<UserRoom>()
                .setQuery(roomref,UserRoom.class)
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
                        Intent intent=new Intent(UserAllRoomsActivity.this,UserSelectedRoomPreviewActivity.class);
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
