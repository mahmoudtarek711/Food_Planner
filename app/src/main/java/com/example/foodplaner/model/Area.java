package com.example.foodplaner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Area implements Parcelable, Serializable {

    @SerializedName("strArea")
    private String strArea;

    // Empty constructor (Required for Gson)
    public Area() {
    }

    public Area(String strArea) {
        this.strArea = strArea;
    }

    protected Area(Parcel in) {
        strArea = in.readString();
    }

    public static final Creator<Area> CREATOR = new Creator<Area>() {
        @Override
        public Area createFromParcel(Parcel in) {
            return new Area(in);
        }

        @Override
        public Area[] newArray(int size) {
            return new Area[size];
        }
    };

    // Getter and Setter

    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    // Parcelable methods

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(strArea);
    }
}
