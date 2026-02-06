package com.example.foodplaner.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AreaResponse implements Serializable {

    @SerializedName("meals")
    private List<Area> areas;

    public List<Area> getAreas() {
        return areas;
    }

    public void setMeals(List<Area> areas) {
        this.areas = areas;
    }
}
