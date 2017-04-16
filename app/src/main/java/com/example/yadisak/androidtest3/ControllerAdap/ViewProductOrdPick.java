package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.yadisak.androidtest3.DTO.Product;
import com.example.yadisak.androidtest3.Globaldata;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._FBProvider.FirebaseCustomAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class ViewProductOrdPick {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB ;

    FirebaseCustomAdapter<Product> adap;

    public FirebaseCustomAdapter<Product> getAdapter() {
        return adap;
    }

    public ViewProductOrdPick(Activity activity) {

        refTB = refDB.child("product_" + Globaldata.Branch.getId());

        adap = new FirebaseCustomAdapter<Product>(activity
                , Product.class, R.layout._listrow_item_order_pick, refTB) {
            @Override
            protected void populateView(View v, Product model) {

                    v.setBackgroundColor(Color.parseColor(model.getBgcolor()));

                    TextView lab_pro_name = (TextView) v.findViewById(R.id.lab_pro_name);
                    lab_pro_name.setText(model.getName());

                    TextView lab_pro_qty = (TextView) v.findViewById(R.id.lab_pro_qty);
                    lab_pro_qty.setText("[" + String.valueOf(model.getStock()) + "]");

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
