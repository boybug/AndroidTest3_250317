package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewProduct implements ICRUDAdap<Product> {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB = refDB.child("product_"+ Globaldata.Branch.getId());

    FirebaseCustomAdapter<Product> adap;
    String searchString = "";


    public FirebaseCustomAdapter<Product> getAdapter() {
        return adap;
    }

    public ViewProduct(Activity activity) {

        adap = new FirebaseCustomAdapter<Product>(activity, Product.class, R.layout._listrow_item, refTB.orderByChild("rownum")) {

            @Override
            protected void populateView(View v, Product model) {

                TextView lab_name = (TextView) v.findViewById(R.id.lab_name);
                lab_name.setText(model.getName());

                TextView lab_code = (TextView) v.findViewById(R.id.lab_code);
                lab_code.setText(model.getCode());

                v.setBackgroundColor(Color.parseColor(model.getBgcolor()));

                if(!model.getBgcolor().equals("#ffffff")) {
                    lab_name.setTextColor(Color.BLACK);
                }
                else {
                    lab_name.setTextColor(Color.parseColor("#0070a2"));
                }

                TextView lab_price = (TextView) v.findViewById(R.id.lab_price);
                lab_price.setText(model.getTopprice());

                if(model.getStep().equals("Y")) {
                    lab_price.setTextColor(Color.RED);
                }else{
                    lab_price.setTextColor(Color.BLACK);
                }

                if(searchString != null)
                {
                    setspantext(searchString,model.getName(),lab_name);
                    setspantext(searchString,model.getCode(),lab_code);
                }

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

    public List<Product> getAllItemsold() {
        return adap.getAllItemsold();
    }

    @Override
    public int getCount() {
        return adap.getCount();
    }


    public void filter(String text) {
        this.searchString = text.toLowerCase(Locale.getDefault());
        if(adap.getAllItemsold().size() == 0){
            adap.setOldmodels(adap.getAllItems());
        }

        text = text.toLowerCase(Locale.getDefault());
        List<Product> tempProduct = new ArrayList<>();
        tempProduct.clear();
        if (text.length() == 0)
        {
            adap.setModels(adap.getAllItemsold());
        }
        else{
            for (Product p : adap.getAllItemsold()) {
                if (p.getName().toLowerCase(Locale.getDefault()).contains(text) || p.getCode().toLowerCase(Locale.getDefault()).contains(text))
                    tempProduct.add(p);
            }
            adap.setModels(tempProduct);
        }
        adap.notifyChanged();
    }

    public void setspantext(String search, String name, TextView textView )
    {
        int firstIndex = name.toLowerCase(Locale.getDefault()).indexOf(search,0);
        Spannable span = new SpannableString(name);
        for(int i = 0; i < name.length() && firstIndex != -1; i = firstIndex +1){
            firstIndex = name.toLowerCase(Locale.getDefault()).indexOf(search, i);
            if(firstIndex == -1)
                break;
            else{
                span.setSpan(new BackgroundColorSpan(0xFFFFFF00),firstIndex,firstIndex + search.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(span,TextView.BufferType.SPANNABLE);
            }

        }

    }

}

