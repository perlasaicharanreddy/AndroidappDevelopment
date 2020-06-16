package com.example.hotelbooking.Admin;

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

import com.example.hotelbooking.Model.AdminManageAgents;
import com.example.hotelbooking.R;
import com.example.hotelbooking.ViewHolders.AdminManageAgentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminManageAgentsFragment extends Fragment {
    private String userid,username;
    private DatabaseReference agentsref,selectedagentref;
    private RecyclerView recyclerView;
    private TextView heading;
    private RecyclerView.LayoutManager layoutManager;
    public AdminManageAgentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_manage_agents, container, false);
        recyclerView=view.findViewById(R.id.admin_manage_agents_recyclear_view);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        agentsref= FirebaseDatabase.getInstance().getReference().child("Agents");
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminManageAgents> options=new FirebaseRecyclerOptions.Builder<AdminManageAgents>()
                .setQuery(agentsref.orderByChild("admin").equalTo("access"),AdminManageAgents.class)
                .build();
        FirebaseRecyclerAdapter<AdminManageAgents, AdminManageAgentViewHolder> adapter=new FirebaseRecyclerAdapter<AdminManageAgents, AdminManageAgentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminManageAgentViewHolder adminManageAgentViewHolder, final int i, @NonNull final AdminManageAgents adminManageAgents) {
                adminManageAgentViewHolder.agentname.setText(adminManageAgents.getName());
                adminManageAgentViewHolder.agentphone.setText("Phone no: "+adminManageAgents.getPhone());
                adminManageAgentViewHolder.agentemail.setText("email: "+adminManageAgents.getEmail());
                adminManageAgentViewHolder.hotelname.setText("hotel name: "+adminManageAgents.getHotelname());
                adminManageAgentViewHolder.hotelrating.setText("star rating: "+adminManageAgents.getHotelrating());
                adminManageAgentViewHolder.agentcity.setText("City: "+adminManageAgents.getCity());
                adminManageAgentViewHolder.agentstate.setText("State: "+adminManageAgents.getState());
                adminManageAgentViewHolder.address.setText("Address: "+adminManageAgents.getHoteladdress());
                Picasso.get().load(adminManageAgents.agentimage).placeholder(R.drawable.profile).into(adminManageAgentViewHolder.agentimage);
                adminManageAgentViewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add("check Rooms").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                selectedagentref=getRef(i).getRef();
                                String adentid;
                                Intent intent=new Intent(getContext(), AdminCheckAgentRoomsActivity.class);
                                intent.putExtra("agentid",selectedagentref.getKey());
                                intent.putExtra("agentname",adminManageAgents.getName());
                                startActivity(intent);
                                return true;
                            }
                        });
                        menu.add("remove Agent").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                selectedagentref=getRef(i);
                                selectedagentref.removeValue();
                                return true;
                            }
                        });


                    }
                });
            }

            @NonNull
            @Override
            public AdminManageAgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_manage_agents_card_view,parent,false);
                final AdminManageAgentViewHolder holder=new AdminManageAgentViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
