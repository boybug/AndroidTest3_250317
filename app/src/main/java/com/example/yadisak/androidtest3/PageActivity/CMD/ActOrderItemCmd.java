package com.example.yadisak.androidtest3.PageActivity.CMD;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yadisak.androidtest3.ControllerAdap.*;
import com.example.yadisak.androidtest3.DTO.Order;
import com.example.yadisak.androidtest3.DTO.OrderItem;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._Extension.Utility;

public class ActOrderItemCmd extends _ActivityCustom {

    EditText txt_name, txtStock, txt_price, txt_discount, txt_discount_amt, txt_total_amt;

    ViewOrderItem adap;

    OrderItem ent;
    Order entOrder;


    void calculate() {

        float sum = ent.getQty() * ent.getPrice();
        float dis_amt = (sum * ent.getDiscount()) / 100;
        float total = sum - dis_amt;

        txtStock.setText(String.valueOf(ent.getQty()));
        txt_discount_amt.setText(String.valueOf(dis_amt));
        txt_discount_amt.setText(String.valueOf(dis_amt));
        txt_total_amt.setText(String.valueOf(total));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cmd_order_item);

        txt_name = (EditText) findViewById(R.id.txt_name);
        txtStock = (EditText) findViewById(R.id.txt_stock);
        txt_price = (EditText) findViewById(R.id.txt_price);
        txt_discount = (EditText) findViewById(R.id.txt_discount);
        txt_discount_amt = (EditText) findViewById(R.id.txt_discount_amt);
        txt_total_amt = (EditText) findViewById(R.id.txt_total_amt);

        Intent curtact = getIntent();
        CMDState state = (CMDState) curtact.getSerializableExtra(Utility.CMD_STATE);

        switch (state) {
            case NEW:

                break;
            case EDIT:

                ent = (OrderItem) curtact.getSerializableExtra(Utility.ENTITY_DTO_NAME);
                entOrder = (Order) curtact.getSerializableExtra("entOrder");

                adap = new ViewOrderItem(this, entOrder.getFirebaseId());

                setTitle("Order No. : " + entOrder.getNo());

                txt_name.setText(ent.getPro_name());
                txtStock.setText(String.valueOf(ent.getQty()));
                txt_price.setText(String.valueOf(ent.getPrice()));
                txt_discount.setText(String.valueOf(ent.getDiscount()));

                calculate();

                break;
        }

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

        int xxx = Integer.parseInt(txtStock.getText().toString());
        txtStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ent.setDelta(xxx);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button bt_cmd_save = (Button) findViewById(R.id.bt_cmd_save);
        bt_cmd_save.setOnClickListener(view -> {
            switch (state) {
                case NEW:
                    break;
                case EDIT:
                    ent.setQty(Integer.valueOf(txtStock.getText().toString()));
                    ent.setAmt(Float.valueOf(txt_total_amt.getText().toString()));
                    adap.updateItem(ent, (DAOState status, String message) -> {
                        if (status == DAOState.SUCCESS) {

                            Intent intent = new Intent();
                            intent.putExtra(Utility.CMD_STATE, CMDState.EDIT);
                            intent.putExtra(Utility.ENTITY_DTO_NAME, entOrder);
                            setResult(RESULT_OK, intent);
                            finish();

                        } else
                            showMessageAlert(message);
                    });

                    break;
            }
        });




    }
}

