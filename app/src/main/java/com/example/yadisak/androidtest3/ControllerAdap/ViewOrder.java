package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ViewOrder implements ICRUDAdap<Order> {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB = refDB.child("order_" + Globaldata.Branch.getId());

    FirebaseCustomAdapter<Order> adap;
    String searchString = "";

    public FirebaseCustomAdapter<Order> getAdapter() {
        return adap;
    }

    public ViewOrder(Activity activity) {
        adap = new FirebaseCustomAdapter<Order>(activity, Order.class, R.layout._listrow_order, refTB.orderByKey()) {
            @Override
            protected void populateView(View v, Order model) {

                TextView lab_order_no = (TextView) v.findViewById(R.id.firstLine);
                lab_order_no.setText(model.getNo());

                TextView lab_order_cus = (TextView) v.findViewById(R.id.secondLine);
                lab_order_cus.setText("ลูกค้า : "+model.getCus_name());

                TextView lab_order_total = (TextView) v.findViewById(R.id.icon);
                lab_order_total.setText(String.valueOf(model.getTotal()));

                TextView lab_order_ship = (TextView) v.findViewById(R.id.txt_ship);
                lab_order_ship.setText(model.getShip());

                TextView lab_order_user = (TextView) v.findViewById(R.id.tridLine);
                lab_order_user.setText("ผู้เปิดบิล : "+String.valueOf(model.getUser()));

                if (model.getStat().equals("new")) {
                    v.setBackgroundColor(Color.parseColor("#abdacf"));
                    lab_order_total.setTextColor(Color.parseColor("#ff0000"));
                    lab_order_no.setTextColor(Color.parseColor("#0070a2"));
                }
                else  if (model.getStat().equals("confirm")){
                    v.setBackgroundColor(Color.parseColor("#F49144"));
                    lab_order_total.setTextColor(Color.parseColor("#000000"));
                    lab_order_no.setTextColor(Color.parseColor("#000000"));
                }

                if(searchString != null)
                {
                    setspantext(searchString,model.getNo(),lab_order_no);
                    setspantext(searchString,model.getCus_name(),lab_order_cus);
                }
            }

            @Override
            protected List<Order> modifyArrayAdapter(List<Order> models) {
                return models;
            }
        };
    }


    public void getCustomer(Order _item, ICustomResult result) {
        refDB.child("customer_" + Globaldata.Branch.getId())
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

    public void updatestatus(Order _item, String stat, String pay, String ship, String remark) {
        refTB.orderByChild("no").equalTo(_item.getNo())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot value = dataSnapshot.getChildren().iterator().next();
                            value.getRef().child("stat").setValue(stat);
                            value.getRef().child("pay").setValue(pay);
                            value.getRef().child("ship").setValue(ship);
                            value.getRef().child("remark").setValue(remark);// Set value by some field
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


    @Override
    public Order getItem(Object key) {

        int id = (int) key;
        Order ent = (Order) adap.getItem(id);

        return ent;
    }

    public Order getItem(String no) {

        Order ent = null;

        for (Order item : getAllItems()) {
            if (item.getNo().equals(no)) {
                ent = item;
                break;
            }
        }

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


    public void filter(String text) {
        this.searchString = text.toLowerCase(Locale.getDefault());
        if(adap.getAllItemsold().size() == 0){
            adap.setOldmodels(adap.getAllItems());
        }

        text = text.toLowerCase(Locale.getDefault());
        List<Order> tempOrder = new ArrayList<>();
        tempOrder.clear();
        if (text.length() == 0)
        {
            adap.setModels(adap.getAllItemsold());
        }
        else{
            for (Order p : adap.getAllItemsold()) {
                if (p.getNo().toLowerCase(Locale.getDefault()).contains(text) || p.getCus_name().toLowerCase(Locale.getDefault()).contains(text))
                    tempOrder.add(p);
            }
            adap.setModels(tempOrder);
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