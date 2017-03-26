package com.example.yadisak.androidtest3.DTO;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class _FirebaseAttribute implements Serializable {

    @Exclude
    private String firebaseId;
    @Exclude
    public String getFirebaseId() {
        return firebaseId;
    }
    @Exclude
    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }
}
