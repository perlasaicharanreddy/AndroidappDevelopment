package com.example.hotelbooking.Model;

public class AdminManageAgents {
    public String name,phone,state,email,city,agentimage,hotelrating,hotelname,hoteladdress,agentid;

    public AdminManageAgents() {
    }

    public AdminManageAgents(String name, String phone, String state, String email, String city, String agentimage, String hotelrating, String hotelname, String hoteladdress, String agentid) {
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.email = email;
        this.city = city;
        this.agentimage = agentimage;
        this.hotelrating = hotelrating;
        this.hotelname = hotelname;
        this.hoteladdress = hoteladdress;
        this.agentid = agentid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAgentimage() {
        return agentimage;
    }

    public void setAgentimage(String agentimage) {
        this.agentimage = agentimage;
    }

    public String getHotelrating() {
        return hotelrating;
    }

    public void setHotelrating(String hotelrating) {
        this.hotelrating = hotelrating;
    }

    public String getHotelname() {
        return hotelname;
    }

    public void setHotelname(String hotelname) {
        this.hotelname = hotelname;
    }

    public String getHoteladdress() {
        return hoteladdress;
    }

    public void setHoteladdress(String hoteladdress) {
        this.hoteladdress = hoteladdress;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }
}
