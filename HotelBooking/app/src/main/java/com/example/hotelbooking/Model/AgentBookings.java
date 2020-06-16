package com.example.hotelbooking.Model;

public class AgentBookings {
    public String guestname,guestphone,guestemail,roomtype,noofrooms,noofpersons,bookingdates,totalprice,paymentstatus,roomimage;

    public AgentBookings() {
    }

    public AgentBookings(String guestname, String guestphone, String guestemail, String roomtype, String noofrooms, String noofpersons, String bookingdates, String totalprice, String paymentstatus, String roomimage) {
        this.guestname = guestname;
        this.guestphone = guestphone;
        this.guestemail = guestemail;
        this.roomtype = roomtype;
        this.noofrooms = noofrooms;
        this.noofpersons = noofpersons;
        this.bookingdates = bookingdates;
        this.totalprice = totalprice;
        this.paymentstatus = paymentstatus;
        this.roomimage = roomimage;
    }

    public String getGuestname() {
        return guestname;
    }

    public void setGuestname(String guestname) {
        this.guestname = guestname;
    }

    public String getGuestphone() {
        return guestphone;
    }

    public void setGuestphone(String guestphone) {
        this.guestphone = guestphone;
    }

    public String getGuestemail() {
        return guestemail;
    }

    public void setGuestemail(String guestemail) {
        this.guestemail = guestemail;
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

    public String getRoomimage() {
        return roomimage;
    }

    public void setRoomimage(String roomimage) {
        this.roomimage = roomimage;
    }
}
