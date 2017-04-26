package com.example.yadisak.androidtest3.PageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yadisak.androidtest3.ControllerAdap.ViewCustomer;
import com.example.yadisak.androidtest3.DTO.Customer;
import com.example.yadisak.androidtest3.PageActivity.CMD.ActCustomerCmd;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._Extension.Utility;
import com.example.yadisak.androidtest3._Interface.ICRUDResult;

import java.util.Locale;

public class ActCustomer extends _ActivityCustom {

    ViewCustomer adap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_data);
        setTitle("ลูกค้า");

        adap = new ViewCustomer(this);

        ListView list = (ListView) findViewById(R.id.list_view_data);
        list.setAdapter(adap.getAdapter());
        list.setEmptyView(findViewById(R.id.emptyElement));
        list.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {

            Customer ent = adap.getItem(position);
            Intent nextact = new Intent(this, ActCustomerCmd.class);
            nextact.putExtra(Utility.CMD_STATE, CMDState.EDIT);
            nextact.putExtra(Utility.ENTITY_DTO_NAME, ent);
            toNextActivity(nextact);

        });
        list.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {

            Customer ent = adap.getItem(position);
            adap.removeItem(ent, new ICRUDResult() {
                @Override
                public void onReturn(DAOState status, String message) {
                    if (status != DAOState.SUCCESS)
                        showMessageAlert(message);
                }
            });

            return true;
        });

        EditText txt_search = (EditText) findViewById(R.id.txt_search);
        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                String text = txt_search.getText().toString().toLowerCase(Locale.getDefault());
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

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//        MenuItem actionViewItem = menu.findItem(R.id.miActionCustom);
//        View v = MenuItemCompat.getActionView(actionViewItem);
//
//        Button bt_item = (Button) v.findViewById(R.id.bt_action_custom);
//        bt_item.setOnClickListener(view -> {
//
//            Intent nextact = new Intent(this, ActCustomerCmd.class);
//            nextact.putExtra(Utility.CMD_STATE, CMDState.NEW);
//            toNextActivity(nextact);
//        });
//
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.custom, menu);
        return true;
    }
}
