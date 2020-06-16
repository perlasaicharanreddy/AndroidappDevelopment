package com.example.hotelbooking.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.R;

public class UserBookingsViewHolder extends RecyclerView.ViewHolder {
    public TextView hotelnameTv,hoteladdressTv,roomtypeTv,noofroomsTv,noofpersonsTv,bookingdatesTv,totalpriceTv,paymentstatus;
    public ImageView roomimage;
    public UserBookingsViewHolder(@NonNull View itemView) {
        super(itemView);

        roomimage=itemView.findViewById(R.id.user_bookings_card_room_image);
        hotelnameTv=itemView.findViewById(R.id.user_bookings_card_hotel_name);
        hoteladdressTv=itemView.findViewById(R.id.user_bookings_card_hotel_address);
        roomtypeTv=itemView.findViewById(R.id.user_bookings_room_type);
        noofroomsTv=itemView.findViewById(R.id.user_bookings_no_of_rooms);
        noofpersonsTv=itemView.findViewById(R.id.user_bookings_no_of_persons);
        bookingdatesTv=itemView.findViewById(R.id.user_bookings_dates);
        totalpriceTv=itemView.findViewById(R.id.user_bookings_card_total_price);
        paymentstatus=itemView.findViewById(R.id.user_bookings_payment_status);


    }
}
