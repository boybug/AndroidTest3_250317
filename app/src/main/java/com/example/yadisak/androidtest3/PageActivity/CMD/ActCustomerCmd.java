package com.example.yadisak.androidtest3.PageActivity.CMD;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.yadisak.androidtest3.ControllerAdap.ViewCustomer;
import com.example.yadisak.androidtest3.DTO.Customer;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.Utility;

public class ActCustomerCmd extends _ActivityCustom {

    ViewCustomer adap;
    Customer ent;

    EditText txt_code, txt_name, txt_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cmd_customer);

        adap = new ViewCustomer(this);

        txt_code = (EditText) findViewById(R.id.txt_code);
        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_point = (EditText) findViewById(R.id.txt_point);

        Intent curtact = getIntent();
        CMDState state = (CMDState) curtact.getSerializableExtra(Utility.CMD_STATE);


        switch (state) {
            case NEW:

                setTitle("New Customer");

                txt_code.setText("");
                txt_name.setText("");

                txt_code.setEnabled(true);

                break;
            case EDIT:

                setTitle("รายละเอียดลูกค้า");

                this.ent = (Customer) curtact.getSerializableExtra(Utility.ENTITY_DTO_NAME);

                txt_code.setText(ent.getCode());
                txt_name.setText(ent.getName());
                txt_point.setText(String.valueOf(ent.getPoint()));

                txt_code.setEnabled(false);
                txt_name.setEnabled(false);
                txt_point.setEnabled(false);
                break;
        }
    }

}
