package com.example.yadisak.androidtest3.PageActivity.CMD;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yadisak.androidtest3.ControllerAdap.ViewCustomer;
import com.example.yadisak.androidtest3.ControllerAdap.ViewOrder;
import com.example.yadisak.androidtest3.DTO.Customer;
import com.example.yadisak.androidtest3.DTO.Order;
import com.example.yadisak.androidtest3.Globaldata;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._Extension.Utility;
import com.example.yadisak.androidtest3._Interface.ICRUDResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActCustomerCmd extends _ActivityCustom {

    ViewCustomer adapcus;
    ViewOrder adaporder;
    Customer entcus;
    Order entorder;
    TextView txt_code, txt_name, txt_addr;
    TextView txt_tel,txt_ship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cmd_customer);

        adapcus = new ViewCustomer(this);

        txt_code = (TextView) findViewById(R.id.txt_code);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_addr = (TextView) findViewById(R.id.txt_addr);
        txt_tel = (TextView) findViewById(R.id.txt_tel);
        txt_ship = (TextView) findViewById(R.id.txt_ship);

        Intent curtact = getIntent();
//        CMDState state = (CMDState) curtact.getSerializableExtra(Utility.CMD_STATE);

                setTitle("รายละเอียดลูกค้า");

                this.entcus = (Customer) curtact.getSerializableExtra(Utility.ENTITY_DTO_NAME);

                txt_code.setText(entcus.getCode());
                txt_name.setText(entcus.getName());
                txt_addr.setText(entcus.getAddr());
                txt_ship.setText(entcus.getShip());
                txt_tel.setText(entcus.getTel());
                txt_tel.setInputType(InputType.TYPE_CLASS_PHONE);

                txt_tel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(entcus.getTel().equals("")) {
                            return;
                        }
                        else {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + txt_tel.getText()));
                            startActivity(callIntent);
                        }
                    }
                });


        int current = Globaldata.Branch.getNumber();

        Button bt_cmd_save = (Button) findViewById(R.id.bt_cmd_save);
        bt_cmd_save.setText("เปิดบิล");
        bt_cmd_save.setOnClickListener(view -> {
            adaporder = new ViewOrder(this);
            String date = new SimpleDateFormat("yyMM").format(new Date());
            String last_no = "000";
            last_no = last_no + String.valueOf(current + 1);
            last_no = last_no.substring(last_no.length()-4, last_no.length());

            Date currDateTime = new Date(System.currentTimeMillis());

            String finalLast_no = last_no;
            final ICRUDResult iCRUDRes = new ICRUDResult() {
                @Override
                public void onReturn(DAOState status, String message) {
                    if (status == DAOState.SUCCESS) {
                            DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
                            refDB.child("branch")
                                    .orderByChild("id").equalTo(Globaldata.Branch.getId())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                DataSnapshot value = dataSnapshot.getChildren().iterator().next();

                                                Globaldata.Branch.setNumber(current + 1);
                                                value.getRef().child("number").setValue(current + 1);


                                                Order ent = adaporder.getItem(Globaldata.Branch.getCode()+date+ finalLast_no.toString());
                                                Intent nextact = new Intent(ActCustomerCmd.this, ActOrderCmd.class);
                                                nextact.putExtra(Utility.CMD_STATE, CMDState.EDIT);
                                                nextact.putExtra(Utility.ENTITY_DTO_NAME, ent);
                                                toNextActivity(nextact);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                    } else
                        showMessageAlert(message);
                }
            };

                    this.entorder = new Order();
                    this.entorder.setNo(Globaldata.Branch.getCode()+date+last_no.toString());
                    this.entorder.setDate(currDateTime);
                    this.entorder.setCus_code(entcus.getCode().toString());
                    this.entorder.setCus_name(entcus.getName().toString());
                    this.entorder.setUser(Globaldata.Login.getName());
                    this.entorder.setStat("new");
                    this.entorder.setShip(entcus.getShip());
                    adaporder.addItem(entorder,iCRUDRes);

        });














    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//        MenuItem actionViewItem = menu.findItem(R.id.miActionCustom);
//        View v = MenuItemCompat.getActionView(actionViewItem);
//
//        Button bt_item = (Button) v.findViewById(R.id.bt_action_custom);
//        bt_item.setOnClickListener(view -> {
//
//            Intent nextact = new Intent(this, ActOrderCmd.class);
//            nextact.putExtra(Utility.CMD_STATE, CMDState.NEW);
//            nextact.putExtra("cusid",ent.getCode());
//            toNextActivity(nextact);
//        });
//
//        return super.onPrepareOptionsMenu(menu);
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.custom, menu);
//        return true;
//    }

}
