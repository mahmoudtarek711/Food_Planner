package com.example.foodplaner.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public class AuthRepository {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public Completable register(String email, String password, String username) {
        return Completable.create(emitter -> {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        // Once account is created, update the Display Name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();

                        authResult.getUser().updateProfile(profileUpdates)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) emitter.onComplete();
                                    else emitter.onError(task.getException());
                                });
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }

    public Observable<FirebaseUser> login(String email, String password) {
        return Observable.create(emitter -> {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (!emitter.isDisposed()) {
                            if (task.isSuccessful()) {
                                emitter.onNext(mAuth.getCurrentUser());
                                emitter.onComplete();
                            } else {
                                emitter.onError(task.getException());
                            }
                        }
                    });
        });
    }

    public Observable<FirebaseUser> loginGuest() {
        return Observable.create(emitter -> {
            mAuth.signInAnonymously().addOnCompleteListener(task -> {
                if (!emitter.isDisposed()) {
                    if (task.isSuccessful()) {
                        emitter.onNext(mAuth.getCurrentUser());
                        emitter.onComplete();
                    } else {
                        emitter.onError(task.getException());
                    }
                }
            });
        });
    }
    public String getCurrentUserEmail()
    {
       return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }
    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}