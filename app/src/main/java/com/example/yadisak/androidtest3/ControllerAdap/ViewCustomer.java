package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.yadisak.androidtest3.DTO.Customer;
import com.example.yadisak.androidtest3.DTO._SelectionProperty;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._Extension.CRUDMessage;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._FBProvider.FirebaseConstant;
import com.example.yadisak.androidtest3._FBProvider.FirebaseCustomAdapter;
import com.example.yadisak.androidtest3._Interface.ICRUDAdap;
import com.example.yadisak.androidtest3._Interface.ICRUDResult;
import com.example.yadisak.androidtest3._Interface.ICustomResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewCustomer implements ICRUDAdap<Customer> {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB = refDB.child("customer");

    FirebaseCustomAdapter<Customer> adap;

    public FirebaseCustomAdapter<Customer> getAdapter() {
        return adap;
    }

    public ViewCustomer(Activity activity) {

        adap = new FirebaseCustomAdapter<Customer>(activity, Customer.class, R.layout._listrow_customer, refTB.orderByKey()) {
            @Override
            protected void populateView(View v, Customer model) {
                TextView lab_name = (TextView) v.findViewById(R.id.lab_name);
                lab_name.setText(model.getName());

                TextView lab_code = (TextView) v.findViewById(R.id.lab_code);
                lab_code.setText(model.getCode());

                TextView lab_tel = (TextView) v.findViewById(R.id.lab_tel);
                lab_tel.setText(String.valueOf(model.getTel()));
            }

            @Override
            protected List<Customer> modifyArrayAdapter(List<Customer> models) {
                return models;
            }
        };
    }

    @Override
    public void addItem(Customer _item, ICRUDResult result) {

        refTB.orderByChild("code").equalTo(_item.getCode())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() == false) {

                            String keyId = refTB.push().getKey();
                            refTB.child(keyId).setValue(_item);
                            _item.setFirebaseId(keyId);

                            result.onReturn(DAOState.SUCCESS, CRUDMessage.MSG_ADDED);
                        } else {
                            result.onReturn(DAOState.CONDITION, "!Customer Code has used.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result.onReturn(DAOState.ERROR, databaseError.getMessage());
                    }
                });
    }

    @Override
    public void updateItem(Customer _item, ICRUDResult result) {
        refDB.orderByChild("code").equalTo(_item.getCode())
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
    public void removeItem(Customer _item, ICRUDResult result) {

        refDB.child(FirebaseConstant.TABLE_ORDER).orderByChild("cus_code").equalTo(_item.getCode())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            result.onReturn(DAOState.CONDITION, "This Customer hs used.");
                        } else {

                            refTB.orderByChild("code").equalTo(_item.getCode())
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
                                            result.onReturn(DAOState.ERROR, databaseError.getMessage());
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result.onReturn(DAOState.ERROR, databaseError.getMessage());
                    }
                });
    }

    @Override
    public Customer getItem(Object key) {

        int id = (int) key;
        Customer ent = (Customer) adap.getItem(id);

        return ent;
    }

    @Override
    public List<Customer> getAllItems() {
        return adap.getAllItems();
    }

    @Override
    public int getCount() {
        return adap.getCount();
    }

    public void getSelectionList(ICustomResult result) {

        List<_SelectionProperty> list = new ArrayList<>();

        refTB.orderByChild("code")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot value : dataSnapshot.getChildren()) {
                                Customer data = value.getValue(Customer.class);
                                list.add(new _SelectionProperty(data.getCode(), data.getName(), data.getPoint()));
                            }
                        }
                        result.onReturn(DAOState.SUCCESS, "", list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result.onReturn(DAOState.ERROR, databaseError.getMessage(), list);
                    }
                });

    }

    public void filter(String charText) {
        try {

//            charText = charText.toLowerCase(Locale.getDefault());
//            items.clear();
//
//            if (charText.length() == 0) {
//                items.addAll(items_all);
//
//            } else {
//
//                for (Customer item : items_all) {
//                    if (item.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                        items.add(item);
//                    }
//                }
//            }

        } catch (Exception ex) {
            throw ex;
        }
    }
}