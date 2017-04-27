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

import com.example.yadisak.androidtest3.ControllerAdap.*;
import com.example.yadisak.androidtest3.DTO.Order;
import com.example.yadisak.androidtest3.PageActivity.CMD.ActOrderCmd;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3.SummaryOrder;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._Extension.Utility;
import com.example.yadisak.androidtest3._Interface.ICRUDResult;


public class ActOrder extends _ActivityCustom {

    ViewOrder adap;

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

            if(ent.getStat().equals("new")) {
                nextact = new Intent(this, ActOrderCmd.class);
                nextact.putExtra(Utility.CMD_STATE, CMDState.EDIT);
                nextact.putExtra(Utility.ENTITY_DTO_NAME, ent);
                toNextActivity(nextact);
            }
            else{
                int total_qty = 0;
                int total_wgt = 0;

                ViewOrderItem adapOrProd;
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
            if(ent.getStat().equals("new")) {
                adap.removeItem(ent, new ICRUDResult() {
                    @Override
                    public void onReturn(DAOState status, String message) {
                        if (status != DAOState.SUCCESS)
                            showMessageAlert(message);
                    }
                });
                return true;
            }
            else {
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
            nextact.putExtra("cusid","1");
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
