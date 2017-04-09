package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.yadisak.androidtest3.DTO.Customer;
import com.example.yadisak.androidtest3.DTO.Order;
import com.example.yadisak.androidtest3.Globaldata;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._Extension.CRUDMessage;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._FBProvider.FirebaseCustomAdapter;
import com.example.yadisak.androidtest3._Interface.ICRUDAdap;
import com.example.yadisak.androidtest3._Interface.ICRUDResult;
import com.example.yadisak.androidtest3._Interface.ICustomResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ViewOrder implements ICRUDAdap<Order> {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB = refDB.child("order_" + Globaldata.Branch.getId());

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

                TextView lab_order_cus = (TextView) v.findViewById(R.id.lab_order_cus);
                lab_order_cus.setText(model.getCus_name());

                TextView lab_order_total = (TextView) v.findViewById(R.id.lab_order_total);
                lab_order_total.setText(String.valueOf(model.getTotal()));

                if (model.getStat().equals("new")) {
                    v.setBackgroundColor(Color.parseColor("#71c7b3"));
                }
                else
                    v.setBackgroundColor(Color.parseColor("#F49144"));
            }

            @Override
            protected List<Order> modifyArrayAdapter(List<Order> models) {
                return models;
            }
        };
    }


    public void getCustomer(Order _item, ICustomResult result) {
        refDB.child("customer")
                .orderByChild("code").equalTo(_item.getCus_code())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() == true) {

                            DataSnapshot value = dataSnapshot.getChildren().iterator().next();
                            Customer cus = (Customer) value.getValue(Customer.class);

                            result.onReturn(DAOState.SUCCESS, "", cus);
                        } else {
                            result.onReturn(DAOState.CONDITION, "!Not found Customer.", null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result.onReturn(DAOState.ERROR, databaseError.getMessage(), null);
                    }
                });
    }

    @Override
    public void addItem(Order _item, ICRUDResult result) {

        refTB.orderByChild("no").equalTo(_item.getNo())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() == false) {

                            String keyId = refTB.push().getKey();
                            refTB.child(keyId).setValue(_item);
                            _item.setFirebaseId(keyId);

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

    public void updateItemtotal(Order _item, float total) {
        refTB.orderByChild("no").equalTo(_item.getNo())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot value = dataSnapshot.getChildren().iterator().next();
                            value.getRef().child("total").setValue(total); // Set value by some field
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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


    public void getlastorder(ICustomResult result) {

        refTB.orderByChild("no").limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot value = dataSnapshot.getChildren().iterator().next();
                            refTB.child(value.getKey()).removeValue();
                            result.onReturn(DAOState.SUCCESS, "", null);
                        } else {
                        result.onReturn(DAOState.CONDITION, "!Not found Order.", null);
                    }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result.onReturn(DAOState.ERROR, databaseError.getMessage(), null);
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