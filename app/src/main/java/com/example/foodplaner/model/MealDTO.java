package com.example.foodplaner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MealDTO implements Parcelable {

    @SerializedName("idMeal")
    private String idMeal;

    @SerializedName("strMeal")
    private String strMeal;

    @SerializedName("strMealThumb")
    private String strMealThumb;

    @SerializedName("strArea")
    private String strArea;

    @SerializedName("strInstructions")
    private String strInstructions;

    @SerializedName("strYoutube")
    private String strYoutube;

    @SerializedName("strCategory")
    private String strCategory;

    // Ingredients
    @SerializedName("strIngredient1") private String strIngredient1;
    @SerializedName("strIngredient2") private String strIngredient2;
    @SerializedName("strIngredient3") private String strIngredient3;
    @SerializedName("strIngredient4") private String strIngredient4;
    @SerializedName("strIngredient5") private String strIngredient5;
    @SerializedName("strIngredient6") private String strIngredient6;
    @SerializedName("strIngredient7") private String strIngredient7;
    @SerializedName("strIngredient8") private String strIngredient8;
    @SerializedName("strIngredient9") private String strIngredient9;
    @SerializedName("strIngredient10") private String strIngredient10;
    @SerializedName("strIngredient11") private String strIngredient11;
    @SerializedName("strIngredient12") private String strIngredient12;
    @SerializedName("strIngredient13") private String strIngredient13;
    @SerializedName("strIngredient14") private String strIngredient14;
    @SerializedName("strIngredient15") private String strIngredient15;
    @SerializedName("strIngredient16") private String strIngredient16;
    @SerializedName("strIngredient17") private String strIngredient17;
    @SerializedName("strIngredient18") private String strIngredient18;
    @SerializedName("strIngredient19") private String strIngredient19;
    @SerializedName("strIngredient20") private String strIngredient20;

    // Measures
    @SerializedName("strMeasure1") private String strMeasure1;
    @SerializedName("strMeasure2") private String strMeasure2;
    @SerializedName("strMeasure3") private String strMeasure3;
    @SerializedName("strMeasure4") private String strMeasure4;
    @SerializedName("strMeasure5") private String strMeasure5;
    @SerializedName("strMeasure6") private String strMeasure6;
    @SerializedName("strMeasure7") private String strMeasure7;
    @SerializedName("strMeasure8") private String strMeasure8;
    @SerializedName("strMeasure9") private String strMeasure9;
    @SerializedName("strMeasure10") private String strMeasure10;
    @SerializedName("strMeasure11") private String strMeasure11;
    @SerializedName("strMeasure12") private String strMeasure12;
    @SerializedName("strMeasure13") private String strMeasure13;
    @SerializedName("strMeasure14") private String strMeasure14;
    @SerializedName("strMeasure15") private String strMeasure15;
    @SerializedName("strMeasure16") private String strMeasure16;
    @SerializedName("strMeasure17") private String strMeasure17;
    @SerializedName("strMeasure18") private String strMeasure18;
    @SerializedName("strMeasure19") private String strMeasure19;
    @SerializedName("strMeasure20") private String strMeasure20;

    // Lists (lazy populated)
    private List<String> ingredients;
    private List<String> measures;

    // ---------------- EMPTY CONSTRUCTOR (Gson needs this) ----------------
    public MealDTO() {
        // DO NOT populate here
    }

    // ---------------- LAZY BUILD ----------------
    private void buildIngredientListsIfNeeded() {
        if (ingredients != null && measures != null) return;

        ingredients = new ArrayList<>();
        measures = new ArrayList<>();

        addIfNotEmpty(strIngredient1, strMeasure1);
        addIfNotEmpty(strIngredient2, strMeasure2);
        addIfNotEmpty(strIngredient3, strMeasure3);
        addIfNotEmpty(strIngredient4, strMeasure4);
        addIfNotEmpty(strIngredient5, strMeasure5);
        addIfNotEmpty(strIngredient6, strMeasure6);
        addIfNotEmpty(strIngredient7, strMeasure7);
        addIfNotEmpty(strIngredient8, strMeasure8);
        addIfNotEmpty(strIngredient9, strMeasure9);
        addIfNotEmpty(strIngredient10, strMeasure10);
        addIfNotEmpty(strIngredient11, strMeasure11);
        addIfNotEmpty(strIngredient12, strMeasure12);
        addIfNotEmpty(strIngredient13, strMeasure13);
        addIfNotEmpty(strIngredient14, strMeasure14);
        addIfNotEmpty(strIngredient15, strMeasure15);
        addIfNotEmpty(strIngredient16, strMeasure16);
        addIfNotEmpty(strIngredient17, strMeasure17);
        addIfNotEmpty(strIngredient18, strMeasure18);
        addIfNotEmpty(strIngredient19, strMeasure19);
        addIfNotEmpty(strIngredient20, strMeasure20);
    }

    private void addIfNotEmpty(String ingredient, String measure) {
        if (ingredient != null && !ingredient.trim().isEmpty()) {
            ingredients.add(ingredient.trim());
            measures.add(measure != null ? measure.trim() : "");
        }
    }

    // ---------------- Parcelable ----------------
    protected MealDTO(Parcel in) {
        idMeal = in.readString();
        strMeal = in.readString();
        strMealThumb = in.readString();
        strArea = in.readString();
        strInstructions = in.readString();
        strYoutube = in.readString();
        strCategory = in.readString();
        ingredients = in.createStringArrayList();
        measures = in.createStringArrayList();
    }

    public static final Creator<MealDTO> CREATOR = new Creator<MealDTO>() {
        @Override
        public MealDTO createFromParcel(Parcel in) {
            return new MealDTO(in);
        }

        @Override
        public MealDTO[] newArray(int size) {
            return new MealDTO[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        buildIngredientListsIfNeeded();

        dest.writeString(idMeal);
        dest.writeString(strMeal);
        dest.writeString(strMealThumb);
        dest.writeString(strArea);
        dest.writeString(strInstructions);
        dest.writeString(strYoutube);
        dest.writeString(strCategory);
        dest.writeStringList(ingredients);
        dest.writeStringList(measures);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // ---------------- Getters ----------------
    public String getStrIdMeal() { return idMeal; }
    public String getStrMeal() { return strMeal; }
    public String getStrMealThumb() { return strMealThumb; }
    public String getStrArea() { return strArea; }
    public String getStrInstructions() { return strInstructions; }
    public String getStrYoutube() { return strYoutube; }
    public String getStrCategory() { return strCategory; }

    public List<String> getIngredients() {
        buildIngredientListsIfNeeded();
        return ingredients;
    }

    public List<String> getMeasures() {
        buildIngredientListsIfNeeded();
        return measures;
    }
    public MealRoomDTO toRoomDTO(String userEmail) {
        MealRoomDTO room = new MealRoomDTO();
        room.setIdMeal(this.idMeal);
        room.setUserEmail(userEmail);
        room.setStrMeal(this.strMeal);
        room.setStrMealThumb(this.strMealThumb);
        room.setStrArea(this.strArea);
        room.setStrInstructions(this.strInstructions);
        room.setStrYoutube(this.strYoutube);
        room.setIngredients(this.getIngredients());
        room.setMeasures(this.getMeasures());
        return room;
    }
    public static MealDTO fromRoomDTO(MealRoomDTO roomDto) {
        MealDTO dto = new MealDTO();
        dto.idMeal = roomDto.getIdMeal();
        dto.strMeal = roomDto.getStrMeal();
        dto.strMealThumb = roomDto.getStrMealThumb();
        dto.strArea = roomDto.getStrArea();
        dto.strCategory = roomDto.getStrCategory();
        dto.strInstructions = roomDto.getStrInstructions();
        dto.strYoutube = roomDto.getStrYoutube();

        // Convert the lists back
        dto.ingredients = roomDto.getIngredients();
        dto.measures = roomDto.getMeasures();

        return dto;
    }
}
