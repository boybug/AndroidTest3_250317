package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.example.yadisak.androidtest3.Globaldata;
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

public class ViewOrderItem implements ICRUDAdap<OrderItem> {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB;

    FirebaseCustomAdapter<OrderItem> adap;

    public FirebaseCustomAdapter<OrderItem> getAdapter() {
        return adap;
    }

    public ViewOrderItem(Activity activity, String orderKeyId) {

        refTB = refDB.child("order_" + Globaldata.Branch.getCode()).child(orderKeyId).child("item");

        adap = new FirebaseCustomAdapter<OrderItem>(activity
                , OrderItem.class, R.layout._listrow_item_order_item, refTB.orderByChild("no")) {
            @Override
            protected void populateView(View v, OrderItem model) {
//                TextView lab_pro_code = (TextView) v.findViewById(R.id.lab_pro_code);
//                lab_pro_code.setText(model.getPro_code());

                TextView lab_pro_name = (TextView) v.findViewById(R.id.lab_pro_name);
                lab_pro_name.setText(model.getPro_name() + " : " + model.getFoc_flag());

                TextView lab_pro_cal = (TextView) v.findViewById(R.id.lab_pro_cal);
                lab_pro_cal.setText(model.getQty() + "x" + model.getPrice());

//                TextView lab_pro_dis_cal = (TextView) v.findViewById(R.id.lab_pro_dis_cal);
//                lab_pro_dis_cal.setText(String.valueOf(model.getDiscount()));
            }

            @Override
            protected List<OrderItem> modifyArrayAdapter(List<OrderItem> models) {
                return models;
            }

        };
    }

    public float calTotal(OrderItem _item) {
        float sum = _item.getQty() * _item.getPrice();
        float total = sum - ((sum * _item.getDiscount()) / 100);

        return total;
    }

    private void updateProductStock(OrderItem _item, int qty, ICustomResult result) {

        refDB.child("product_" + Globaldata.Branch.getCode())
                .orderByChild("code").equalTo(_item.getPro_code())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot value = dataSnapshot.getChildren().iterator().next();

                            Product prod = (Product) value.getValue(Product.class);
                            int curr_qty = prod.getStock() - qty;

                            if (curr_qty >= 0) {
                                value.getRef().child("stock").setValue(curr_qty); // Set value by some field

                                float price = 0;
                                ProductPrice entPrice;
                                for (DataSnapshot snapPrices : value.child("price").getChildren()) {
                                    entPrice = (ProductPrice) snapPrices.getValue(ProductPrice.class);
                                    if (_item.getQty() >= entPrice.getFrom() && _item.getQty() <= entPrice.getTo()) {
                                        price = entPrice.getPrice();
                                        break;
                                    }
                                }

                                result.onReturn(DAOState.SUCCESS, CRUDMessage.MSG_UPDATED, price);
                            } else
                                result.onReturn(DAOState.CONDITION, "!Product : " + _item.getPro_code() + " qty not enough.", null);
                        } else {
                            result.onReturn(DAOState.CONDITION, "!Product : " + _item.getPro_code() + " not in system.", null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result.onReturn(DAOState.ERROR, databaseError.getMessage(), null);
                    }
                });
    }

    @Override
    public void addItem(OrderItem _item, ICRUDResult result) {

        _item.setQty(1);

        updateProductStock(_item, 1, (status, message, price) -> {
            if (status == DAOState.SUCCESS) {

                int last_no = 0;
                if (getCount() > 0)
                    last_no = getItem(getCount() - 1).getNo();

                _item.setNo(last_no + 1);
                _item.setPro_code_foc_flag(_item.getPro_code() + "_" + _item.getFoc_flag());

                if (_item.getFoc_flag() == "N") {
                    _item.setPrice((float) price);
                } else {
                    _item.setPrice(0);
                }

                _item.setAmt(calTotal(_item));
                refTB.push().setValue(_item);

                result.onReturn(status, CRUDMessage.MSG_ADDED);
            } else {
                result.onReturn(status, message);
            }

        });
    }

    @Override
    public void updateItem(OrderItem _item, ICRUDResult result) {

        updateProductStock(_item, (_item.getQty() - _item.getDelta())
                , (status, message, price) -> {
                    if (status == DAOState.SUCCESS) {

                        if (_item.getFoc_flag() == "N") {
                            _item.setPrice((float) price);
                        } else {
                            _item.setPrice(0);
                        }

                        _item.setAmt(calTotal(_item));
                        refTB.child(_item.getFirebaseId()).setValue(_item);

                        result.onReturn(status, CRUDMessage.MSG_UPDATED);
                    } else {
                        result.onReturn(status, message);
                    }
                });

    }

    @Override
    public void removeItem(OrderItem _item, ICRUDResult result) {

        updateProductStock(_item, (_item.getQty() * -1), (status, message, price) -> {
            if (status == DAOState.SUCCESS) {

                refTB.child(_item.getFirebaseId()).removeValue();
                result.onReturn(status, CRUDMessage.MSG_DELETED);

            } else {
                result.onReturn(status, message);
            }

        });

    }

    @Override
    public OrderItem getItem(Object key) {

        int id = (int) key;
        OrderItem ent = (OrderItem) adap.getItem(id);

        return ent;
    }

    public OrderItem getItem(String pro_code, String foc_flag) {

        OrderItem ent = null;

        for (OrderItem item : getAllItems()) {
            if (item.getPro_code_foc_flag().equals(pro_code + "_" + foc_flag)) {
                ent = item;
                break;
            }
        }

        return ent;
    }

    @Override
    public List<OrderItem> getAllItems() {
        return adap.getAllItems();
    }

    @Override
    public int getCount() {
        return adap.getCount();
    }

}