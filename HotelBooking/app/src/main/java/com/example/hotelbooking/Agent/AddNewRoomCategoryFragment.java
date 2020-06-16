package com.example.hotelbooking.Agent;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.hotelbooking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewRoomCategoryFragment extends Fragment {
    private RelativeLayout singleroom,doubleroom,tripleroom,twinroom,suiteroom,connectingroom,villa,juniorsuiteroom,apartmentroom;

    public AddNewRoomCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_new_room_category, container, false);
        singleroom=view.findViewById(R.id.single_room);
        doubleroom=view.findViewById(R.id.double_room);
        tripleroom=view.findViewById(R.id.triple_room);
        twinroom=view.findViewById(R.id.twin_room);
        suiteroom=view.findViewById(R.id.suite_room);
        connectingroom=view.findViewById(R.id.connecting_room);
        villa=view.findViewById(R.id.villa_room);
        juniorsuiteroom=view.findViewById(R.id.junior_suite_room);
        apartmentroom=view.findViewById(R.id.apartment_room);

        singleroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddNewRoomActivity.class);
                intent.putExtra("Roomtype","singleroom");
                startActivity(intent);
            }
        });
        doubleroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddNewRoomActivity.class);
                intent.putExtra("Roomtype","doubleroom");
                startActivity(intent);
            }
        });
        tripleroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddNewRoomActivity.class);
                intent.putExtra("Roomtype","tripleroom");
                startActivity(intent);
            }
        });
        twinroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddNewRoomActivity.class);
                intent.putExtra("Roomtype","twinroom");
                startActivity(intent);
            }
        });
        suiteroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddNewRoomActivity.class);
                intent.putExtra("Roomtype","suiteroom");
                startActivity(intent);
            }
        });
        connectingroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddNewRoomActivity.class);
                intent.putExtra("Roomtype","connectingroom");
                startActivity(intent);
            }
        });
        villa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddNewRoomActivity.class);
                intent.putExtra("Roomtype","villa");
                startActivity(intent);
            }
        });
        juniorsuiteroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddNewRoomActivity.class);
                intent.putExtra("Roomtype","junior suiteroom");
                startActivity(intent);
            }
        });
        apartmentroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddNewRoomActivity.class);
                intent.putExtra("Roomtype","apartmentroom");
                startActivity(intent);
            }
        });




        return view;
    }
}
