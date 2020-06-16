package com.example.hotelbooking.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.R;

public class UserRoomViewHolder extends RecyclerView.ViewHolder {
    public ImageView roomImage;
    public TextView hotelname,hoteladdress,roomprice,hotelfacility1,hotelfacility2,hotelfacility3,roomdiscription;

    public UserRoomViewHolder(@NonNull View itemView) {
        super(itemView);
        roomImage=itemView.findViewById(R.id.user_card_room_image);
        hotelname=itemView.findViewById(R.id.user_card_hotel_name);
        hoteladdress=itemView.findViewById(R.id.user_card_hotel_address);
        roomprice=itemView.findViewById(R.id.user_card_room_price);
        hotelfacility1=itemView.findViewById(R.id.user_card_hotel_facility1);
        hotelfacility2=itemView.findViewById(R.id.user_card_hotel_facility2);
        hotelfacility3=itemView.findViewById(R.id.user_card_hotel_facility3);
        roomdiscription=itemView.findViewById(R.id.user_card_room_description);


    }


}
