package com.example.foodplaner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ingredient implements Parcelable, Serializable {

    @SerializedName("idIngredient")
    private String idIngredient;

    @SerializedName("strIngredient")
    private String strIngredient;

    @SerializedName("strDescription")
    private String strDescription;

    @SerializedName("strThumb")
    private String strThumb;

    @SerializedName("strType")
    private String strType;

    // Empty constructor (Required for Gson)
    public Ingredient() {
    }

    public Ingredient(String idIngredient, String strIngredient,
                      String strDescription, String strThumb, String strType) {
        this.idIngredient = idIngredient;
        this.strIngredient = strIngredient;
        this.strDescription = strDescription;
        this.strThumb = strThumb;
        this.strType = strType;
    }

    // Parcelable constructor
    protected Ingredient(Parcel in) {
        idIngredient = in.readString();
        strIngredient = in.readString();
        strDescription = in.readString();
        strThumb = in.readString();
        strType = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    // Getters and Setters

    public String getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(String idIngredient) {
        this.idIngredient = idIngredient;
    }

    public String getStrIngredient() {
        return strIngredient;
    }

    public void setStrIngredient(String strIngredient) {
        this.strIngredient = strIngredient;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public String getStrThumb() {
        return strThumb;
    }

    public void setStrThumb(String strThumb) {
        this.strThumb = strThumb;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    // Parcelable methods

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idIngredient);
        dest.writeString(strIngredient);
        dest.writeString(strDescription);
        dest.writeString(strThumb);
        dest.writeString(strType);
    }
}
