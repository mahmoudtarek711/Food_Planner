package com.example.foodplaner.searchscreen.view;

import java.util.HashMap;
import java.util.Map;

public class CountryMapper {

    private static final Map<String, String> areaToCode = new HashMap<>();

    static {

        areaToCode.put("Algerian", "DZ");
        areaToCode.put("American", "US");
        areaToCode.put("Argentinian", "AR");
        areaToCode.put("Australian", "AU");
        areaToCode.put("British", "GB");
        areaToCode.put("Canadian", "CA");
        areaToCode.put("Chinese", "CN");
        areaToCode.put("Croatian", "HR");
        areaToCode.put("Dutch", "NL");
        areaToCode.put("Egyptian", "EG");
        areaToCode.put("Filipino", "PH");
        areaToCode.put("French", "FR");
        areaToCode.put("Greek", "GR");
        areaToCode.put("Indian", "IN");
        areaToCode.put("Irish", "IE");
        areaToCode.put("Italian", "IT");
        areaToCode.put("Jamaican", "JM");
        areaToCode.put("Japanese", "JP");
        areaToCode.put("Kenyan", "KE");
        areaToCode.put("Malaysian", "MY");
        areaToCode.put("Mexican", "MX");
        areaToCode.put("Moroccan", "MA");
        areaToCode.put("Norwegian", "NO");
        areaToCode.put("Polish", "PL");
        areaToCode.put("Portuguese", "PT");
        areaToCode.put("Russian", "RU");
        areaToCode.put("Saudi Arabian", "SA");
        areaToCode.put("Slovakian", "SK");
        areaToCode.put("Spanish", "ES");
        areaToCode.put("Syrian", "SY");
        areaToCode.put("Thai", "TH");
        areaToCode.put("Tunisian", "TN");
        areaToCode.put("Turkish", "TR");
        areaToCode.put("Ukrainian", "UA");
        areaToCode.put("Uruguayan", "UY");
        areaToCode.put("Venezulan", "VE");
        areaToCode.put("Vietnamese", "VN");

        // Optional additional safety mappings
        areaToCode.put("Yemeni", "YE");
        areaToCode.put("Yemen", "YE");
    }

    public static String getCode(String areaName) {

        if (areaName == null) return "";

        String code = areaToCode.get(areaName);

        return code != null ? code : "";
    }
}

