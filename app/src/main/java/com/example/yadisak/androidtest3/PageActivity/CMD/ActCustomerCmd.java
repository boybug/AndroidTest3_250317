package com.example.yadisak.androidtest3.PageActivity.CMD;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yadisak.androidtest3.ControllerAdap.ViewCustomer;
import com.example.yadisak.androidtest3.DTO.Customer;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.Utility;

public class ActCustomerCmd extends _ActivityCustom {

    ViewCustomer adap;
    Customer ent;

    TextView txt_code, txt_name, txt_addr;
    TextView txt_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cmd_customer);

        adap = new ViewCustomer(this);

        txt_code = (TextView) findViewById(R.id.txt_code);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_addr = (TextView) findViewById(R.id.txt_addr);
        txt_tel = (TextView) findViewById(R.id.txt_tel);


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
                txt_addr.setText(ent.getAddr());
                txt_tel.setText(ent.getTel());
                txt_tel.setInputType(InputType.TYPE_CLASS_PHONE);

                txt_tel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(ent.getTel().equals("")) {
                            return;
                        }
                        else {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + txt_tel.getText()));
                            startActivity(callIntent);
                        }
                    }
                });




                break;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem actionViewItem = menu.findItem(R.id.miActionCustom);
        View v = MenuItemCompat.getActionView(actionViewItem);

        Button bt_item = (Button) v.findViewById(R.id.bt_action_custom);
        bt_item.setOnClickListener(view -> {

            Intent nextact = new Intent(this, ActOrderCmd.class);
            nextact.putExtra(Utility.CMD_STATE, CMDState.NEW);
            nextact.putExtra("cusid",ent.getFirebaseId());
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
