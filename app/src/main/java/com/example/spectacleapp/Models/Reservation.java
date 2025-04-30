package com.example.spectacleapp.Models;

import java.util.List;

public class Reservation {
    private String name;
    private String username;
    private String spectacleTitle;
    private String poster;
    private double totalPrice;
    private String date;
    private String time;

    private List<String> seats;

    // Constructor
    public Reservation(){}
    public Reservation(String name, String username, String spectacleTitle, String posterLink,
                       double totalPrice, String date, String time, List<String> seats) {
        this.name = name;
        this.username = username;
        this.spectacleTitle = spectacleTitle;
        this.poster = posterLink;
        this.totalPrice = totalPrice;
        this.date = date;
        this.time = time;
        this.seats = seats;
    }

    // Getters
    public String getName() {
        return name;
    }

    public List<String> getSeats(){
        return seats;
    }

    public String getUsername() {
        return username;
    }

    public String getSpectacleTitle() {
        return spectacleTitle;
    }

    public String getPoster() {
        return poster;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSeats(List<String> seats){
        this.seats = seats;
    }

    public void setSpectacleTitle(String spectacleTitle) {
        this.spectacleTitle = spectacleTitle;
    }

    public void setPoster(String posterLink) {
        this.poster = posterLink;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
