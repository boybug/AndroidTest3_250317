package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import com.example.yadisak.androidtest3.DTO.*;
import com.example.yadisak.androidtest3.Globaldata;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._FBProvider.FirebaseCustomAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ViewProductOrdPick {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB = refDB.child("product_" + Globaldata.Branch.getCode());

    FirebaseCustomAdapter<Product> adap;

    List<Product> modelsCustom;

    public FirebaseCustomAdapter<Product> getAdapter() {
        return adap;
    }

    public ViewProductOrdPick(Activity activity) {

        adap = new FirebaseCustomAdapter<Product>(activity, Product.class, R.layout._listrow_item_order_pick, refTB.orderByKey()) {
            @Override
            protected void populateView(View v, Product model) {

//                LinearLayout xxx =  (LinearLayout) v.findViewById(R.id.lin_lay_name);
//                xxx.setBackgroundColor(0xFF000000);

                TextView lab_pro_name = (TextView) v.findViewById(R.id.lab_pro_name);
                lab_pro_name.setText(model.getName());

                TextView lab_pro_qty = (TextView) v.findViewById(R.id.lab_pro_qty);
                lab_pro_qty.setText("[" + String.valueOf(model.getStock()) + "]");

//                TextView lab_pro_price = (TextView) v.findViewById(R.id.lab_pro_price);
//                lab_pro_price.setText("(" + String.valueOf(model.getPrice()) + ")");
            }

            @Override
            protected List<Product> modifyArrayAdapter(List<Product> models) {
                return models;
            }
        };
    }

    public Product getItem(int id) {

        Product ent = (Product) adap.getItem(id);
        return ent;
    }

    public void filter() {
        try {


        } catch (Exception ex) {

        }
    }
}
