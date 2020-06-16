package com.example.hotelbooking.Model;

public class UserRoom {
    public String hotelname, hoteladdress, roomprice, roomdiscription, hotelfacility1, hotelfacility2, hotelfacility3, roomimage, agentid,
            roomtype,noofrooms,noofpersons,bookingdates,totalprice,paymentstatus;

    public UserRoom() {
    }

    public UserRoom(String hotelname, String hoteladdress, String roomprice, String roomdiscription, String hotelfacility1, String hotelfacility2, String hotelfacility3, String roomimage, String agentid, String roomtype, String noofrooms, String noofpersons, String bookingdates, String totalprice, String paymentstatus) {
        this.hotelname = hotelname;
        this.roomprice = roomprice;
        this.hoteladdress = hoteladdress;
        this.roomdiscription = roomdiscription;
        this.hotelfacility1 = hotelfacility1;
        this.hotelfacility2 = hotelfacility2;
        this.hotelfacility3 = hotelfacility3;
        this.roomimage = roomimage;
        this.agentid = agentid;
        this.roomtype = roomtype;
        this.noofrooms = noofrooms;
        this.noofpersons = noofpersons;
        this.bookingdates = bookingdates;
        this.totalprice = totalprice;
        this.paymentstatus = paymentstatus;
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

    public String getRoomprice() {
        return roomprice;
    }

    public void setRoomprice(String roomprice) {
        this.roomprice = roomprice;
    }

    public String getRoomdiscription() {
        return roomdiscription;
    }

    public void setRoomdiscription(String roomdiscription) {
        this.roomdiscription = roomdiscription;
    }

    public String getHotelfacility1() {
        return hotelfacility1;
    }

    public void setHotelfacility1(String hotelfacility1) {
        this.hotelfacility1 = hotelfacility1;
    }

    public String getHotelfacility2() {
        return hotelfacility2;
    }

    public void setHotelfacility2(String hotelfacility2) {
        this.hotelfacility2 = hotelfacility2;
    }

    public String getHotelfacility3() {
        return hotelfacility3;
    }

    public void setHotelfacility3(String hotelfacility3) {
        this.hotelfacility3 = hotelfacility3;
    }

    public String getRoomimage() {
        return roomimage;
    }

    public void setRoomimage(String roomimage) {
        this.roomimage = roomimage;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public String getNoofrooms() {
        return noofrooms;
    }

    public void setNoofrooms(String noofrooms) {
        this.noofrooms = noofrooms;
    }

    public String getNoofpersons() {
        return noofpersons;
    }

    public void setNoofpersons(String noofpersons) {
        this.noofpersons = noofpersons;
    }

    public String getBookingdates() {
        return bookingdates;
    }

    public void setBookingdates(String bookingdates) {
        this.bookingdates = bookingdates;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }
}