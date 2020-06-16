package com.example.hotelbooking.Users;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hotelbooking.Admin.AdminCheckUsersBookingsActivity;
import com.example.hotelbooking.Model.UserRoom;
import com.example.hotelbooking.R;
import com.example.hotelbooking.ViewHolders.UserRoomViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSavedFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference roomref,savedref,selectedroomref;
    private FirebaseAuth mauth;
    private String currentuserid;
    private TextView heading;

    private  String agentid;

    public UserSavedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user_saved, container, false);

        savedref= FirebaseDatabase.getInstance().getReference().child("Savedlist");
        roomref= FirebaseDatabase.getInstance().getReference().child("Rooms");

        recyclerView=view.findViewById(R.id.user_saved_recyclerview);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        heading=view.findViewById(R.id.user_saved_rooms_heading);
        mauth= FirebaseAuth.getInstance();
        currentuserid=mauth.getCurrentUser().getUid();




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<UserRoom> options=new FirebaseRecyclerOptions.Builder<UserRoom>()
                .setQuery(savedref.child(currentuserid),UserRoom.class)
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
                        Intent intent=new Intent(getContext(),UserSelectedRoomPreviewActivity.class);
                        intent.putExtra("visituserid",visituserId);
                        startActivity(intent);

                    }
                });

                userRoomViewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add("Remove from save list").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                selectedroomref=getRef(i);
                                selectedroomref.removeValue();
                                return true;
                            }
                        });


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
