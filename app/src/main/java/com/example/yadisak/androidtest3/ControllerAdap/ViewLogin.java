package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.yadisak.androidtest3._Extension.CRUDMessage;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._FBProvider.*;
import com.example.yadisak.androidtest3._Interface.*;
import com.example.yadisak.androidtest3.DTO.*;
import com.example.yadisak.androidtest3.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewLogin  {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB = refDB.child("user");


    public ViewLogin(Activity activity) {

    }

    public void login(User _item, ICustomResult result) {

        refTB.orderByChild("username").equalTo(_item.getUsername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() == true) {
                            result.onReturn(DAOState.SUCCESS,"",null);
                        } else {
                            result.onReturn(DAOState.CONDITION, "!User Login faild.",null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result.onReturn(DAOState.ERROR, databaseError.getMessage(),null);
                    }
                });
    }



}