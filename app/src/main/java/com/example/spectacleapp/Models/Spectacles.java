package com.example.spectacleapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.spectacleapp.Adapter.Actor;

import java.util.ArrayList;
import java.util.List;

public class Spectacles implements Parcelable {
    private String title;
    private String description;
    private String poster;
    private String time;
    private int rat;
    private int year;
    private double[] price = new double[3];
    private List<String> genre;
    private ArrayList<Actor> cast;
    private String googleMapsLink;

    private List<DaySchedule> daySchedules; // NEW LINE

    // Default constructor
    public Spectacles() {}

    // Constructor
    public Spectacles(String title, String description, String poster, String time,
                      int rat, int year, double[] price,
                      List<String> genre, ArrayList<Actor> casts,
                      String googleMapsLink, List<DaySchedule> daySchedules) { // ADD DAY SCHEDULES HERE
        this.title = title;
        this.description = description;
        this.poster = poster;
        this.time = time;
        this.rat = rat;
        this.year = year;
        this.price = price;
        this.genre = genre;
        this.cast = casts;
        this.googleMapsLink = googleMapsLink;
        this.daySchedules = daySchedules;
    }

    // Getters and Setters
    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRat() {
        return rat;
    }

    public void setRat(int rat) {
        this.rat = rat;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double[] getPrice() {
        return price;
    }

    public void setPrice(double[] price) {
        this.price = price;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public ArrayList<Actor> getCasts() {
        return cast;
    }

    public void setCasts(ArrayList<Actor> casts) {
        this.cast = casts;
    }
    public String getGoogleMapsLink() {
        return googleMapsLink;
    }

    public void setGoogleMapsLink(String googleMapsLink) {
        this.googleMapsLink = googleMapsLink;
    }

    public List<DaySchedule> getDaySchedules() {
        return daySchedules;
    }

    public void setDaySchedules(List<DaySchedule> daySchedules) {
        this.daySchedules = daySchedules;
    }

    // Parcelable implementation
    protected Spectacles(Parcel in) {
        title = in.readString();
        description = in.readString();
        poster = in.readString();
        time = in.readString();
        rat = in.readInt();
        year = in.readInt();
        price = in.createDoubleArray();
        genre = in.createStringArrayList();
        cast = in.createTypedArrayList(Actor.CREATOR);
        googleMapsLink = in.readString();
        daySchedules = in.createTypedArrayList(DaySchedule.CREATOR); // READ DAY SCHEDULES
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(poster);
        dest.writeString(time);
        dest.writeInt(rat);
        dest.writeInt(year);
        dest.writeDoubleArray(price);
        dest.writeStringList(genre);
        dest.writeTypedList(cast);
        dest.writeString(googleMapsLink);
        dest.writeTypedList(daySchedules); // WRITE DAY SCHEDULES
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Spectacles> CREATOR = new Creator<Spectacles>() {
        @Override
        public Spectacles createFromParcel(Parcel in) {
            return new Spectacles(in);
        }

        @Override
        public Spectacles[] newArray(int size) {
            return new Spectacles[size];
        }
    };
}
