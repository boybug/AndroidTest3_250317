package com.example.yadisak.androidtest3.PageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yadisak.androidtest3.ControllerAdap.ViewOrder;
import com.example.yadisak.androidtest3.ControllerAdap.ViewOrderItem;
import com.example.yadisak.androidtest3.DTO.Order;
import com.example.yadisak.androidtest3.DTO.OrderItem;
import com.example.yadisak.androidtest3.DTO._FirebaseAttribute;
import com.example.yadisak.androidtest3.Globaldata;
import com.example.yadisak.androidtest3.PageActivity.CMD.ActOrderCmd;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3.SummaryOrder;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._Extension.Utility;
import com.example.yadisak.androidtest3._Interface.ICRUDResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ActOrder extends _ActivityCustom {

    ViewOrder adap;
    ViewOrderItem adapOrProd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_data);
        setTitle("ขาย");

        adap = new ViewOrder(this);

        ListView list = (ListView) findViewById(R.id.list_view_data);
        list.setAdapter(adap.getAdapter());
        list.setEmptyView(findViewById(R.id.emptyElement));
        list.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {

            Intent nextact = null;

            Order ent = adap.getItem(position);

            if (ent.getStat().equals("new")) {
                nextact = new Intent(this, ActOrderCmd.class);
                nextact.putExtra(Utility.CMD_STATE, CMDState.EDIT);
                nextact.putExtra(Utility.ENTITY_DTO_NAME, ent);
                toNextActivity(nextact);
            } else {
                int total_qty = 0;
                int total_wgt = 0;

                adapOrProd = new ViewOrderItem(this, ent.getFirebaseId());
                total_qty = adapOrProd.getTotalqty();
                total_wgt = adapOrProd.getTotalwgt();

                nextact = new Intent(this, SummaryOrder.class);
                nextact.putExtra(Utility.ENTITY_DTO_NAME, ent);
                nextact.putExtra("name", ent.getCus_name().toString());
                nextact.putExtra("no", ent.getNo().toString());
                nextact.putExtra("qty", String.valueOf(total_qty));
                nextact.putExtra("wgt", String.valueOf(total_wgt));
                nextact.putExtra("total", String.valueOf(ent.getTotal()));
                toNextActivity(nextact);
            }

        });
        list.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {


            Order ent = adap.getItem(position);


            if (ent.getStat().equals("new")) {

                DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
                DatabaseReference refTB;
                refTB = refDB.child("order_" + Globaldata.Branch.getId()).child(ent.getFirebaseId()).child("item");
                refTB.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        OrderItem oitem = dataSnapshot.getValue(OrderItem.class);
                        String keyId = dataSnapshot.getKey();
                        _FirebaseAttribute fbAttr = (_FirebaseAttribute) oitem;
                        fbAttr.setFirebaseId(keyId);

                        DatabaseReference prodtb = FirebaseDatabase.getInstance().getReference();
                        prodtb = prodtb.child("product_" + Globaldata.Branch.getId()).child(oitem.getPro_key_id()).child("stock");

                        prodtb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int stk = dataSnapshot.getValue(int.class);
                                stk += oitem.getQty();

                                DatabaseReference prodtb2 = FirebaseDatabase.getInstance().getReference();
                                prodtb2 = prodtb2.child("product_" + Globaldata.Branch.getId()).child(oitem.getPro_key_id()).child("stock");
                                prodtb2.setValue(stk);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


//                for (OrderItem p : ) {
//                  adapOrProd.removeItem(p, new ICRUDResult() {
//                      @Override
//                      public void onReturn(DAOState status, String message) {
//                          if (status != DAOState.SUCCESS)
//                              showMessageAlert(message);
//                      }
//                  });
//                }

                adap.removeItem(ent, new ICRUDResult() {
                    @Override
                    public void onReturn(DAOState status, String message) {
                        if (status != DAOState.SUCCESS)
                            showMessageAlert(message);
                    }
                });
                return true;
            } else {
                return true;
            }
        });

        EditText txt_search = (EditText) findViewById(R.id.txt_search);
        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                String text = txt_search.getText().toString();
                adap.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });

        Button cancle = (Button) findViewById(R.id.calc_clear_txt_Prise);
        cancle.setOnClickListener(view -> {
            txt_search.setText("");
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem actionViewItem = menu.findItem(R.id.miActionCustom);
        View v = MenuItemCompat.getActionView(actionViewItem);

        Button bt_item = (Button) v.findViewById(R.id.bt_action_custom);
        bt_item.setOnClickListener(view -> {

            Intent nextact = new Intent(this, ActOrderCmd.class);
            nextact.putExtra(Utility.CMD_STATE, CMDState.NEW);
            nextact.putExtra("cusid", "0");
            toNextActivity(nextact);
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom, menu);
        return true;
    }

}
