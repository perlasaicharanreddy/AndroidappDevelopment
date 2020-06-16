package com.example.hotelbooking.Admin;

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

import com.example.hotelbooking.Model.AgentRoom;
import com.example.hotelbooking.R;
import com.example.hotelbooking.ViewHolders.AgentRoomsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminManageRoomsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference roomref,selectedroomref;

    public AdminManageRoomsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_admin_manage_rooms, container, false);

        roomref= FirebaseDatabase.getInstance().getReference().child("Rooms");
        recyclerView=view.findViewById(R.id.admin_manage_rooms_recyclear_view);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(recyclerView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AgentRoom> options=new FirebaseRecyclerOptions.Builder<AgentRoom>()
                .setQuery(roomref,AgentRoom.class)
                .build();

        FirebaseRecyclerAdapter<AgentRoom, AgentRoomsViewHolder> adapter=new FirebaseRecyclerAdapter<AgentRoom, AgentRoomsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AgentRoomsViewHolder agentRoomsViewHolder, final int i, @NonNull AgentRoom agentRoom) {
                agentRoomsViewHolder.roomtype.setText("Room type: "+agentRoom.getRoomtype());
                agentRoomsViewHolder.roomprice.setText("Room price: "+agentRoom.getRoomprice());
                agentRoomsViewHolder.roomdiscription.setText(agentRoom.getRoomdiscription());
                agentRoomsViewHolder.noofrooms.setText("No of Rooms: "+agentRoom.getNoofrooms());
                Picasso.get().load(agentRoom.getRoomimage()).into(agentRoomsViewHolder.roomimage);
                agentRoomsViewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add("delete room").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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
            public AgentRoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_room_card_view,parent,false);
                final AgentRoomsViewHolder holder=new AgentRoomsViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
}
