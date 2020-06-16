package com.example.hotelbooking.Model;

import android.widget.ImageView;

public class AgentRoom {
    public String roomimage,roomprice,roomtype,roomdiscription,noofrooms,roomrandomkey;

    public AgentRoom() {
    }

    public AgentRoom(String roomimage, String roomprice, String roomtype, String roomdiscription, String noofrooms, String roomrandomkey) {
        this.roomimage = roomimage;
        this.roomprice = roomprice;
        this.roomtype = roomtype;
        this.roomdiscription = roomdiscription;
        this.noofrooms = noofrooms;
        this.roomrandomkey = roomrandomkey;
    }

    public String getRoomimage() {
        return roomimage;
    }

    public void setRoomimage(String roomimage) {
        this.roomimage = roomimage;
    }

    public String getRoomprice() {
        return roomprice;
    }

    public void setRoomprice(String roomprice) {
        this.roomprice = roomprice;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public String getRoomdiscription() {
        return roomdiscription;
    }

    public void setRoomdiscription(String roomdiscription) {
        this.roomdiscription = roomdiscription;
    }

    public String getNoofrooms() {
        return noofrooms;
    }

    public void setNoofrooms(String noofrooms) {
        this.noofrooms = noofrooms;
    }

    public String getRoomrandomkey() {
        return roomrandomkey;
    }

    public void setRoomrandomkey(String roomrandomkey) {
        this.roomrandomkey = roomrandomkey;
    }
}
