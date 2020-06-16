package com.example.hotelbooking.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.R;

public class AgentBookingsViewHolder extends RecyclerView.ViewHolder {
    public TextView guestname,guestphone,guestemail,roomtype,noofrooms,noofpersons,bookingdates,totalprice,paymentstatus;
    public ImageView roomImage;
    public AgentBookingsViewHolder(@NonNull View itemView) {
        super(itemView);
        guestname=itemView.findViewById(R.id.agent_bookings_guest_name);
        guestphone=itemView.findViewById(R.id.agent_bookings_guest_phone);
        guestemail=itemView.findViewById(R.id.agent_bookings_guest_email);
        roomtype=itemView.findViewById(R.id.agent_bookings_room_type);
        noofrooms=itemView.findViewById(R.id.agent_bookings_no_of_rooms);
        noofpersons=itemView.findViewById(R.id.agent_bookings_no_of_persons);
        bookingdates=itemView.findViewById(R.id.agent_bookings_guest_booking_dates);
        totalprice=itemView.findViewById(R.id.agent_bookings_total_price);
        paymentstatus=itemView.findViewById(R.id.agent_bookings_payment_status);
        roomImage=itemView.findViewById(R.id.agent_bookings_card_room_image);

    }
}
