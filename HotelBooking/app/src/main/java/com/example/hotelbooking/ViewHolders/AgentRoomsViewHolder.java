package com.example.hotelbooking.ViewHolders;

import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.R;

public class AgentRoomsViewHolder extends RecyclerView.ViewHolder  {
    public TextView roomtype,noofrooms,roomprice,roomdiscription;
    public ImageView roomimage;
    public CardView cardView;

    public AgentRoomsViewHolder(@NonNull View itemView) {
        super(itemView);
        roomimage=itemView.findViewById(R.id.agent_card_room_image);
        noofrooms=itemView.findViewById(R.id.agent_card_no_of_rooms);
        roomprice=itemView.findViewById(R.id.agent_card_room_price);
        roomdiscription=itemView.findViewById(R.id.agent_card_room_description);
        roomtype=itemView.findViewById(R.id.agent_card_room_type);
        cardView=itemView.findViewById(R.id.agent_room_card_view);
    }



}
