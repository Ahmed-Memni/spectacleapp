package com.example.spectacleapp.Models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class DaySchedule implements Parcelable {
    public String date;
    public List<TimeSlot> timeSlots;
    // No-argument constructor
    public DaySchedule() {
        // Firebase requires a no-argument constructor
    }
    public DaySchedule(String date, List<TimeSlot> timeSlots) {
        this.date = date;
        this.timeSlots = timeSlots;
    }

    protected DaySchedule(Parcel in) {
        date = in.readString();
        timeSlots = in.createTypedArrayList(TimeSlot.CREATOR);
    }

    public static final Creator<DaySchedule> CREATOR = new Creator<DaySchedule>() {
        @Override
        public DaySchedule createFromParcel(Parcel in) {
            return new DaySchedule(in);
        }

        @Override
        public DaySchedule[] newArray(int size) {
            return new DaySchedule[size];
        }
    };

    public String getDate() {
        return date;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeTypedList(timeSlots);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
