package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import com.example.yadisak.androidtest3.Globaldata;
import com.example.yadisak.androidtest3._Extension.*;
import com.example.yadisak.androidtest3._FBProvider.*;
import com.example.yadisak.androidtest3._Interface.*;
import com.example.yadisak.androidtest3.DTO.*;
import com.example.yadisak.androidtest3.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ViewOrder implements ICRUDAdap<Order> {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB = refDB.child("order_"+ Globaldata.Branch.getCode());

    FirebaseCustomAdapter<Order> adap;

    public FirebaseCustomAdapter<Order> getAdapter() {
        return adap;
    }

    public ViewOrder(Activity activity) {
        adap = new FirebaseCustomAdapter<Order>(activity, Order.class, R.layout._listrow_order, refTB.orderByKey()) {
            @Override
            protected void populateView(View v, Order model) {
                TextView lab_order_no = (TextView) v.findViewById(R.id.lab_order_no);
                lab_order_no.setText(model.getNo());

                TextView lab_order_date = (TextView) v.findViewById(R.id.lab_order_date);
                lab_order_date.setText(Utility.DATE_FORMAT.format(model.getDate()));
            }

            @Override
            protected List<Order> modifyArrayAdapter(List<Order> models) {
                return models;
            }
        };
    }

    @Override
    public void addItem(Order _item, ICRUDResult result) {

        refTB.orderByChild("no").equalTo(_item.getNo())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() == false) {
                            refTB.push().setValue(_item);
                            result.onReturn(DAOState.SUCCESS, CRUDMessage.MSG_ADDED);
                        } else {
                            result.onReturn(DAOState.CONDITION, "!Order No has used.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result.onReturn(DAOState.ERROR, databaseError.getMessage());
                    }
                });
    }

    @Override
    public void updateItem(Order _item, ICRUDResult result) {
        refTB.orderByChild("no").equalTo(_item.getNo())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot value = dataSnapshot.getChildren().iterator().next();
                            refTB.child(value.getKey()).setValue(_item);
                            result.onReturn(DAOState.SUCCESS, CRUDMessage.MSG_UPDATED);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result.onReturn(DAOState.ERROR, databaseError.getMessage());
                    }
                });
    }


    @Override
    public void removeItem(Order _item, ICRUDResult result) {

        refTB.orderByChild("no").equalTo(_item.getNo())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot value = dataSnapshot.getChildren().iterator().next();
                            refTB.child(value.getKey()).removeValue();
                            result.onReturn(DAOState.SUCCESS, CRUDMessage.MSG_DELETED);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result.onReturn(DAOState.CONDITION, databaseError.getMessage());
                    }
                });
    }

    @Override
    public Order getItem(Object key) {

        int id = (int) key;
        Order ent = (Order) adap.getItem(id);

        return ent;
    }

    @Override
    public List<Order> getAllItems() {
        return adap.getAllItems();
    }

    @Override
    public int getCount() {
        return adap.getCount();
    }


    public void filter(String charText) {
        try {


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}