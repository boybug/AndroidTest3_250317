package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.yadisak.androidtest3.DTO.Product;
import com.example.yadisak.androidtest3.Globaldata;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._Extension.CRUDMessage;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._FBProvider.FirebaseCustomAdapter;
import com.example.yadisak.androidtest3._Interface.ICRUDAdap;
import com.example.yadisak.androidtest3._Interface.ICRUDResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ViewProduct implements ICRUDAdap<Product> {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB = refDB.child("product_"+ Globaldata.Branch.getId());

    FirebaseCustomAdapter<Product> adap;

    List<Product> modelsCustom;

    public FirebaseCustomAdapter<Product> getAdapter() {
        return adap;
    }

    public ViewProduct(Activity activity) {

        adap = new FirebaseCustomAdapter<Product>(activity, Product.class, R.layout._listrow_item, refTB.orderByKey()) {
            @Override
            protected void populateView(View v, Product model) {

                TextView lab_name = (TextView) v.findViewById(R.id.lab_name);
                lab_name.setText(model.getName());

                TextView lab_code = (TextView) v.findViewById(R.id.lab_code);
                lab_code.setText(model.getCode());
            }

            @Override
            protected List<Product> modifyArrayAdapter(List<Product> models) {
                return models;
            }
        };
    }

    @Override
    public void addItem(Product _item, ICRUDResult result) {

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
                            result.onReturn(DAOState.CONDITION, "!Product Code has used.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result.onReturn(DAOState.ERROR, databaseError.getMessage());
                    }
                });
    }

    @Override
    public void updateItem(Product _item, ICRUDResult result) {
        refTB.orderByChild("code").equalTo(_item.getCode())
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
    public void removeItem(Product _item, ICRUDResult result) {

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

    @Override
    public Product getItem(Object key) {

        int id = (int) key;
        Product ent = (Product) adap.getItem(id);

        return ent;
    }

    @Override
    public List<Product> getAllItems() {
        return adap.getAllItems();
    }

    @Override
    public int getCount() {
        return adap.getCount();
    }

    public void filter() {
        try {


        } catch (Exception ex) {

        }
    }
}