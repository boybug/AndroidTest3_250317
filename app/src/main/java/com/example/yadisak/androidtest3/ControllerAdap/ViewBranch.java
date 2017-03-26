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

public class ViewBranch implements ICRUDAdap<Branch> {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB = refDB.child("branch");

    FirebaseCustomAdapter<Branch> adap;

    public FirebaseCustomAdapter<Branch> getAdapter() {
        return adap;
    }

    public ViewBranch(Activity activity) {

        adap = new FirebaseCustomAdapter<Branch>(activity, Branch.class, R.layout._listrow_branch, refTB.orderByKey()) {
            @Override
            protected void populateView(View v, Branch model) {
                TextView lab_name = (TextView) v.findViewById(R.id.lab_name);
                lab_name.setText(model.getName());

                TextView lab_code = (TextView) v.findViewById(R.id.lab_code);
                lab_code.setText(model.getCode());

                TextView lab_point = (TextView) v.findViewById(R.id.lab_taxid);
                lab_point.setText(String.valueOf(model.getTaxid()));
            }

            @Override
            protected List<Branch> modifyArrayAdapter(List<Branch> models) {
                return models;
            }
        };
    }

    @Override
    public void addItem(Branch _item, ICRUDResult result) {


    }

    @Override
    public void updateItem(Branch _item, ICRUDResult result) {

    }


    @Override
    public void removeItem(Branch _item, ICRUDResult result) {


    }

    @Override
    public Branch getItem(Object key) {

        int id = (int) key;
        Branch ent = (Branch) adap.getItem(id);

        return ent;
    }

    @Override
    public List<Branch> getAllItems() {
        return adap.getAllItems();
    }

    @Override
    public int getCount() {
        return adap.getCount();
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