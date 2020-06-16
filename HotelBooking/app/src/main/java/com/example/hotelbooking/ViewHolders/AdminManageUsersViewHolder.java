package com.example.hotelbooking.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbooking.R;

public class AdminManageUsersViewHolder extends RecyclerView.ViewHolder {
    public ImageView userimage;
    public TextView name,idcard,birthady,phone,email,address,city;

    public AdminManageUsersViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.admin_manage_user_name);
        idcard=itemView.findViewById(R.id.admin_manage_user_id_card);
        email=itemView.findViewById(R.id.admin_manage_user_email);
        phone=itemView.findViewById(R.id.admin_manage_user_phone_no);
        address=itemView.findViewById(R.id.admin_manage_user_address);
        city=itemView.findViewById(R.id.admin_manage_user_city);
        birthady=itemView.findViewById(R.id.admin_manage_user_birthday);
        userimage=itemView.findViewById(R.id.admin_manage_user_image);

    }
}
