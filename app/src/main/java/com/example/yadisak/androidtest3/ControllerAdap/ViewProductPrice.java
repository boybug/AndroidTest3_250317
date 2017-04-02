package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.yadisak.androidtest3.DTO.ProductPrice;
import com.example.yadisak.androidtest3.Globaldata;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._FBProvider.FirebaseCustomAdapter;
import com.example.yadisak.androidtest3._Interface.ICRUDAdap;
import com.example.yadisak.androidtest3._Interface.ICRUDResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ViewProductPrice implements ICRUDAdap<ProductPrice> {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB;

    FirebaseCustomAdapter<ProductPrice> adap;

    public FirebaseCustomAdapter<ProductPrice> getAdapter() {
        return adap;
    }

    public ViewProductPrice(Activity activity, String productKeyId) {

        refTB = refDB.child("product_"+ Globaldata.Branch.getId()).child(productKeyId).child("price");

        adap = new FirebaseCustomAdapter<ProductPrice>(activity
                , ProductPrice.class, R.layout._listrow_item_price, refTB.orderByChild("from")) {
            @Override
            protected void populateView(View v, ProductPrice model) {
//
                TextView lab_pro_from_to = (TextView) v.findViewById(R.id.lab_pro_from_to);
                lab_pro_from_to.setText(model.getFrom()+" - "+model.getTo());

                TextView lab_pro_price = (TextView) v.findViewById(R.id.lab_pro_price);
                lab_pro_price.setText(String.valueOf(model.getPrice()));

            }

            @Override
            protected List<ProductPrice> modifyArrayAdapter(List<ProductPrice> models) {
                return models;
            }
        };
    }

    @Override
    public void addItem(ProductPrice _item, ICRUDResult result) {


    }

    @Override
    public void updateItem(ProductPrice _item, ICRUDResult result) {

    }

    @Override
    public void removeItem(ProductPrice _item, ICRUDResult result) {


    }

    @Override
    public ProductPrice getItem(Object key) {

        int id = (int) key;
        ProductPrice ent = (ProductPrice) adap.getItem(id);

        return ent;
    }

    @Override
    public List<ProductPrice> getAllItems() {
        return adap.getAllItems();
    }

    @Override
    public int getCount() {
        return adap.getCount();
    }

}