package com.example.myrecyclerview2.classes;

import java.time.LocalTime;

public class Reservation {
    int reservation_id, number_of_rooms,total_price, userId, destinationId;
    String check_in;
    String check_out;

    public Reservation(int number_of_rooms, int total_price, int userId, int destinationId, String check_in, String check_out) {
        this.number_of_rooms = number_of_rooms;
        this.total_price = total_price;
        this.userId = userId;
        this.destinationId = destinationId;
        this.check_in = check_in;
        this.check_out = check_out;
    }

    public Reservation(int reservation_id, int number_of_rooms, int total_price, int userId, int destinationId, String check_in, String check_out) {
        this.reservation_id = reservation_id;
        this.number_of_rooms = number_of_rooms;
        this.total_price = total_price;
        this.userId = userId;
        this.destinationId = destinationId;
        this.check_in = check_in;
        this.check_out = check_out;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    public int getNumber_of_rooms() {
        return number_of_rooms;
    }

    public void setNumber_of_rooms(int number_of_rooms) {
        this.number_of_rooms = number_of_rooms;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }
}
