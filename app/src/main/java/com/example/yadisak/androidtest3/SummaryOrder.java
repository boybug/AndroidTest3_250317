package com.example.yadisak.androidtest3;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yadisak.androidtest3.ControllerAdap.ViewOrder;
import com.example.yadisak.androidtest3.ControllerAdap.ViewOrderItem;
import com.example.yadisak.androidtest3.DTO.Order;
import com.example.yadisak.androidtest3.PageActivity.ActOrder;
import com.example.yadisak.androidtest3._Extension.Utility;

public class SummaryOrder extends _ActivityCustom {


    ViewOrderItem adapOrProd;
    ViewOrder adap;


    Order ent;

    TextView txt_orno;
    TextView txt_qty;
    TextView txt_wgt;
    TextView txt_total;
    TextView sp_customer;
    TextView txt_remark;
    RadioButton rd_cash;
    RadioButton rd_tranfer;
    RadioButton rd_credit;
    Switch switchship;
    RadioGroup rg;
    TableRow tr_customer_new;
    TableRow tr_order_save;
    TableRow tr_item_head;
    TableRow tr_item_detail;

    ListView listViewOrProd;
    private String paytyp;
    private String ship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_order);

        ImageView img_logo_bar = (ImageView) findViewById(R.id.img_logo_bar);
        img_logo_bar.setVisibility(View.GONE);

        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("สรุปบิล");


        txt_orno = (TextView) findViewById(R.id.txt_order_no);
        txt_total = (TextView) findViewById(R.id.txt_total);
        txt_qty = (TextView) findViewById(R.id.txt_qty);
        txt_wgt = (TextView) findViewById(R.id.txt_weight);
        sp_customer = (TextView) findViewById(R.id.sp_customer);
        txt_remark = (TextView) findViewById(R.id.txt_remark);

        listViewOrProd = (ListView) findViewById(R.id.list_order_item);

        tr_customer_new = (TableRow) findViewById(R.id.tr_customer_new);
        tr_order_save = (TableRow) findViewById(R.id.tr_order_save);
        tr_item_head = (TableRow) findViewById(R.id.tr_item_head);
        tr_item_detail = (TableRow) findViewById(R.id.tr_item_detail);

        switchship = (Switch) findViewById(R.id.switch1);
        rg = (RadioGroup) findViewById(R.id.radio_group);
        rd_cash = (RadioButton) findViewById(R.id.radio_cash);
        rd_tranfer = (RadioButton) findViewById(R.id.radio_tranfer);
        rd_credit = (RadioButton) findViewById(R.id.radio_credit);

        Intent curtact = getIntent();

        tr_item_head.setVisibility(View.VISIBLE);
        tr_item_detail.setVisibility(View.VISIBLE);

        if (ent == null)
            ent = (Order) curtact.getSerializableExtra(Utility.ENTITY_DTO_NAME);

        // Order Item

        adapOrProd = new ViewOrderItem(this, ent.getFirebaseId());
        listViewOrProd.setAdapter(adapOrProd.getAdapter());

        sp_customer.setText(curtact.getExtras().getString("name"));
        txt_orno.setText(curtact.getExtras().getString("no"));
        txt_total.setText(curtact.getExtras().getString("total"));
        txt_qty.setText(curtact.getExtras().getString("qty"));
        txt_wgt.setText(curtact.getExtras().getString("wgt"));

        if(ent.getStat().toString().equals("confirm")) {

            if (ent.getPay().toString().equals("เงินสด")) {
                rd_cash.setChecked(true);
            } else if (ent.getPay().toString().equals("โอนเงิน")) {
                rd_tranfer.setChecked(true);
            } else if (ent.getPay().toString().equals("บัตรเครดิต")) {
                rd_credit.setChecked(true);
            }
            txt_remark.setText(ent.getRemark().toString());
        }


        rg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_cash:
                    paytyp = "เงินสด";
                    break;
                case R.id.radio_tranfer:
                    paytyp = "โอนเงิน";
                    break;
                case R.id.radio_credit:
                    paytyp = "บัตรเครดิต";
                    break;
            }
        });

        if (ent.getShip().toString().equals("ส่ง")) {
            switchship.setChecked(true);
            ship = ent.getShip().toString();
        }

        switchship.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ship = switchship.getTextOn().toString();
            } else {
                ship = switchship.getTextOff().toString();
            }
        });

    }

    public void Confirm()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ยืนยันเอกสาร");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setCancelable(true);
        dialog.setMessage("เอกสารยันแล้วจะไม่สามารถแก้ไขได้   คุณต้องการยืนยันใช่หรือไม่");
        dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                adap = new ViewOrder(SummaryOrder.this);
                adap.updatestatus(ent,"confirm",paytyp,ship,txt_remark.getText().toString());

                onBackPressed();
            }
        });

        dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem actionViewItem = menu.findItem(R.id.ActionClose);
        View v = MenuItemCompat.getActionView(actionViewItem);

        Button bt_item = (Button) v.findViewById(R.id.bt_action_close);
        bt_item.setOnClickListener(view -> {
            if(ent.getStat().equals("confirm"))
                Toast.makeText(SummaryOrder.this, "เอกสารได้ถูกยืนยันไปแล้ว.", Toast.LENGTH_SHORT).show();
            else
                Confirm();
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.close, menu);
        return true;
    }

    public void onBackPressed() {
        Intent nextact = new Intent(this, ActOrder.class);
        startActivity(nextact);
        finish();
    }
}