package com.example.yadisak.androidtest3.PageActivity.CMD;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.yadisak.androidtest3.ControllerAdap.ViewOrderItem;
import com.example.yadisak.androidtest3.DTO.Order;
import com.example.yadisak.androidtest3.DTO.OrderItem;
import com.example.yadisak.androidtest3.DTO.Product;
import com.example.yadisak.androidtest3.Globaldata;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._Extension.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActOrderItemCmd extends _ActivityCustom {

    EditText txt_code,txt_name, txtStock, txt_price, txt_total_amt,txt_balance;

    ViewOrderItem adap;

    OrderItem ent;
    Order entOrder;


    void calculate() {

        float sum = ent.getQty() * ent.getPrice();
        float total = sum;

        txtStock.setText(String.valueOf(ent.getQty()));
        txt_total_amt.setText(String.valueOf(total));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cmd_order_item);

        txt_code = (EditText) findViewById(R.id.txt_code);
        txt_name = (EditText) findViewById(R.id.txt_name);
        txtStock = (EditText) findViewById(R.id.txt_stock);
        txt_price = (EditText) findViewById(R.id.txt_price);
        txt_total_amt = (EditText) findViewById(R.id.txt_total_amt);
        txt_balance = (EditText) findViewById(R.id.txt_balance);

        Intent curtact = getIntent();
        CMDState state = (CMDState) curtact.getSerializableExtra(Utility.CMD_STATE);


                ent = (OrderItem) curtact.getSerializableExtra(Utility.ENTITY_DTO_NAME);
                entOrder = (Order) curtact.getSerializableExtra("entOrder");
                ent.setDelta(ent.getQty());

                adap = new ViewOrderItem(this, entOrder.getFirebaseId());

        DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
        refDB.child("product_"+ Globaldata.Branch.getId())
                .orderByChild("code").equalTo(ent.getPro_code())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot value = dataSnapshot.getChildren().iterator().next();
                            Product prod = (Product) value.getValue(Product.class);

                            txt_balance.setText(String.valueOf(prod.getStock()+ent.getQty()));
                            txtStock.setWidth(600);
                            txtStock.requestFocus();
                            txtStock.selectAll();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        setTitle("เลขที่ : " + entOrder.getNo());
        txt_code.setText(ent.getPro_code());
        txt_name.setText(ent.getPro_name());
        txtStock.setText(String.valueOf(ent.getQty()));
        txt_price.setText(String.valueOf(ent.getPrice()));
        calculate();
        txtStock.setWidth(600);
        txtStock.requestFocus();
        txtStock.selectAll();


        Button bt_qty_add = (Button) findViewById(R.id.bt_qty_add);
        bt_qty_add.setOnClickListener(view -> {
            int qty = ent.getQty();
            qty++;
            ent.setQty(qty);

            calculate();
        });

        Button bt_qty_rem = (Button) findViewById(R.id.bt_qty_rem);
        bt_qty_rem.setOnClickListener(view -> {
            int qty = ent.getQty();
            if (qty > 0) {
                qty--;
                ent.setQty(qty);

                calculate();
            }
        });


        Button bt_cmd_save = (Button) findViewById(R.id.bt_cmd_save);
        bt_cmd_save.setOnClickListener(view -> {

            Intent intent = new Intent(this, ActOrderCmd.class);
            switch (state) {
                case NEW:

                    intent.putExtra(Utility.CMD_STATE, CMDState.EDIT);
                    intent.putExtra(Utility.ENTITY_DTO_NAME, entOrder);
                    setResult(RESULT_OK, intent);
                    toNextActivity(intent);
                    finish();

                case EDIT:
                    ent.setQty(Integer.valueOf(txtStock.getText().toString()));
                    ent.setAmt(Float.valueOf(txt_total_amt.getText().toString()));
//                    ent.setCus_code(entOrder.getCus_code());
                    adap.updateItem(ent, (DAOState status, String message) -> {
                        if (status == DAOState.SUCCESS) {

                            intent.putExtra(Utility.CMD_STATE, CMDState.EDIT);
                            intent.putExtra(Utility.ENTITY_DTO_NAME, entOrder);
                            setResult(RESULT_OK, intent);
                            toNextActivity(intent);
                            finish();

                        } else
                            showMessageAlert(message);
                    });

                    break;
            }
        });




    }
}

