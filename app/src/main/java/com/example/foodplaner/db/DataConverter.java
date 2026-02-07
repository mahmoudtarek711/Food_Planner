package com.example.foodplaner.db;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {
    @TypeConverter
    public String fromList(List<String> list) {
        if (list == null) return null;
        return new Gson().toJson(list);
    }

    @TypeConverter
    public List<String> toList(String data) {
        if (data == null) return null;
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(data, listType);
    }
}
