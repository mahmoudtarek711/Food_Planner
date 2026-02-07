package com.example.foodplaner.repository;

import com.google.firebase.auth.FirebaseUser;

public interface OnAuthListener {
    void onSuccess(FirebaseUser user);
    void onFailure(String error);
}
