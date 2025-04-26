package com.example.spectacleapp.Models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class TimeSlot implements Parcelable {
    private String time;
    private List<String> seats;

    public TimeSlot(String time, List<String> seats) {
        this.time = time;
        this.seats = seats;
    }

    protected TimeSlot(Parcel in) {
        time = in.readString();
        seats = in.createStringArrayList();
    }

    public static final Creator<TimeSlot> CREATOR = new Creator<TimeSlot>() {
        @Override
        public TimeSlot createFromParcel(Parcel in) {
            return new TimeSlot(in);
        }

        @Override
        public TimeSlot[] newArray(int size) {
            return new TimeSlot[size];
        }
    };

    public String getTime() {
        return time;
    }

    public List<String> getSeats() {
        return seats;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeStringList(seats);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
