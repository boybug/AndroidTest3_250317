package com.example.yadisak.androidtest3.PageActivity.CMD;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.yadisak.androidtest3.ControllerAdap.*;
import com.example.yadisak.androidtest3.DTO.Customer;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._Extension.Utility;
import com.example.yadisak.androidtest3._Interface.ICRUDResult;

public class ActCustomerCmd extends _ActivityCustom {

    ViewCustomer adap;
    Customer ent;

    EditText txt_code, txt_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cmd_customer);

        adap = new ViewCustomer(this);

        txt_code = (EditText) findViewById(R.id.txt_code);
        txt_name = (EditText) findViewById(R.id.txt_name);

        Intent curtact = getIntent();
        CMDState state = (CMDState) curtact.getSerializableExtra(Utility.CMD_STATE);

        Button bt_cmd_save = (Button) findViewById(R.id.bt_cmd_save);
        bt_cmd_save.setOnClickListener(view -> {

            final ICRUDResult iCRUDRes = new ICRUDResult() {
                @Override
                public void onReturn(DAOState status, String message) {
                    if (status == DAOState.SUCCESS) {
                        toPrevActivityRefresh();
                    } else
                       showMessageAlert(message);
                }
            };

            switch (state) {
                case NEW:

                    if (txt_code.getText().toString().isEmpty()) {
                        showMessageAlert("!Please input Customer Code");
                        return;
                    }

                    ent = new Customer();
                    ent.setCode(txt_code.getText().toString());
                    ent.setName(txt_name.getText().toString());

                    adap.addItem(ent, iCRUDRes);


                    break;
                case EDIT:

                    ent.setName(txt_name.getText().toString());

                    adap.updateItem(null, iCRUDRes);

                    break;
            }
        });

        switch (state) {
            case NEW:

                setTitle("New Customer");

                txt_code.setText("");
                txt_name.setText("");

                txt_code.setEnabled(true);

                break;
            case EDIT:

                setTitle("Edit Customer");

                this.ent = (Customer) curtact.getSerializableExtra(Utility.ENTITY_DTO_NAME);

                txt_code.setText(ent.getCode());
                txt_name.setText(ent.getName());

                txt_code.setEnabled(false);

                break;
        }
    }

}
