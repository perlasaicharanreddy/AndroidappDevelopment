package com.example.hotelbooking.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.R;

public class AdminManageAgentViewHolder extends RecyclerView.ViewHolder {
    public ImageView agentimage;
    public TextView agentname,agentphone,agentcity,agentstate,hotelrating,hotelstate,address,agentemail,hotelname;

    public AdminManageAgentViewHolder(@NonNull View itemView) {
        super(itemView);
        agentname=itemView.findViewById(R.id.admin_manage_agent_name);
        agentphone=itemView.findViewById(R.id.admin_manage_agent_phone_no);
        agentcity=itemView.findViewById(R.id.admin_manage_agent_city);
        agentstate=itemView.findViewById(R.id.admin_manage_agent_state);
        hotelrating=itemView.findViewById(R.id.admin_manage_agent_hotel_rating);
        hotelname=itemView.findViewById(R.id.admin_manage_agent_hotel_name);
        hotelstate=itemView.findViewById(R.id.admin_manage_agent_state);
        address=itemView.findViewById(R.id.admin_manage_agent_address);
        agentimage=itemView.findViewById(R.id.admin_manage_agent_image);
        agentemail=itemView.findViewById(R.id.admin_manage_agent_email);

    }
}
