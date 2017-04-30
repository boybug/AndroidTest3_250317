package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.yadisak.androidtest3.DTO.OrderItem;
import com.example.yadisak.androidtest3.DTO.Product;
import com.example.yadisak.androidtest3.DTO.ProductPrice;
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

public class ViewOrderItem implements ICRUDAdap<OrderItem> {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB;

    FirebaseCustomAdapter<OrderItem> adap;

    public FirebaseCustomAdapter<OrderItem> getAdapter() {
        return adap;
    }

    public ViewOrderItem(Activity activity, String orderKeyId) {

        refTB = refDB.child("order_" + Globaldata.Branch.getId()).child(orderKeyId).child("item");

        adap = new FirebaseCustomAdapter<OrderItem>(activity
                        , OrderItem.class, R.layout._listrow_item_order_item, refTB.orderByChild("no")) {
                    @Override
                    protected void populateView(View v, OrderItem model) {

                        TextView lab_pro_no = (TextView) v.findViewById(R.id.lab_pro_no);
                        lab_pro_no.setText(model.getNo()+".");

                        TextView lab_pro_name = (TextView) v.findViewById(R.id.lab_pro_name);
                        lab_pro_name.setText(model.getPro_name());

                        TextView lab_pro_cal = (TextView) v.findViewById(R.id.lab_pro_cal);
                        lab_pro_cal.setText(Math.round(model.getPrice()) + "x" + model.getQty());

            }

            @Override
            protected List<OrderItem> modifyArrayAdapter(List<OrderItem> models) {
                return models;
            }

        };
    }

    public float calTotal(OrderItem _item) {
        float total = _item.getQty() * _item.getPrice();
//        float total = sum - ((sum * _item.getDiscount()) / 100);

        return total;
    }
    public int calTotalqty(OrderItem _item) {
        int total = _item.getQty();
        return total;
    }

    public float calTotalwgt(OrderItem _item) {
        float total = _item.getQty()* _item.getGross_wgt();
        return total;
    }
    private void updateProductStock(OrderItem _item, int qty, ICustomResult result) {

        refDB.child("product_" + Globaldata.Branch.getId())
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
                                    if (_item.getQty() >= entPrice.getFrom() && (_item.getQty() <= entPrice.getTo() || entPrice.getTo() == 0)) {
                                        price = entPrice.getPrice();
                                        break;
                                    }
                                }

                                result.onReturn(DAOState.SUCCESS, CRUDMessage.MSG_UPDATED, price);
                            } else
                                result.onReturn(DAOState.CONDITION, "จำนวนไม่พอ", null);
                        } else {
                            result.onReturn(DAOState.CONDITION, "ไม่มีสินค้าในระบบ", null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        result.onReturn(DAOState.ERROR, databaseError.getMessage(), null);
                    }
                });
    }

//    private void updatePoint(OrderItem _item, int point) {
//        refDB.child("customer_" + Globaldata.Branch.getCode())
//                .orderByChild("code").equalTo(_item.getCus_code()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                DataSnapshot value = dataSnapshot.getChildren().iterator().next();
//                Customer cus = (Customer) value.getValue(Customer.class);
//                int curr_point = cus.getPoint() + point;
//                value.getRef().child("point").setValue(curr_point); // Set value by some field
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }

    @Override
    public void addItem(OrderItem _item, ICRUDResult result) {

        _item.setQty(1);

        updateProductStock(_item, 1, (status, message, price) -> {
            if (status == DAOState.SUCCESS) {

                int last_no = 0;
                if (getCount() > 0)
                    last_no = getItem(getCount() - 1).getNo();

                _item.setNo(last_no + 1);
//                _item.setPro_code_foc_flag(_item.getPro_code() + "_" + _item.getFoc_flag());
//
//                if (_item.getFoc_flag().equals("N")) {
                    _item.setPrice((float) price);
//                } else {
//                    _item.setPrice(0);
//                }
                _item.setAmt(calTotal(_item));
//                updatePoint(_item, _item.getPoint());

                String keyId = refTB.push().getKey();
                refTB.child(keyId).setValue(_item);
                _item.setFirebaseId(keyId);

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

//                        if (_item.getFoc_flag().equals("N")) {
                            _item.setPrice((float) price);
//                        } else {
//                            _item.setPrice(0);
//                        }

                        _item.setAmt(calTotal(_item));

//                        updatePoint(_item, (_item.getPoint() * (_item.getQty() - _item.getDelta())));
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

//                updatePoint(_item, (_item.getPoint() * _item.getQty() * -1));
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

    public OrderItem getItem(String pro_code) {

        OrderItem ent = null;

        for (OrderItem item : getAllItems()) {
            if (item.getPro_code().equals(pro_code)) {
                ent = item;
                break;
            }
        }

        return ent;
    }

    public float getTotalPrice() {

        float total = 0;

        for (OrderItem item : getAllItems()) {
            total += calTotal(item);
        }

        return total;
    }

    public int getTotalqty() {

        int total = 0;

        for (OrderItem item : getAllItems()) {
            total += calTotalqty(item);
        }

        return total;
    }

    public int getTotalwgt() {

        int total = 0;

        for (OrderItem item : getAllItems()) {
            total += calTotalwgt(item);
        }

        return total;
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