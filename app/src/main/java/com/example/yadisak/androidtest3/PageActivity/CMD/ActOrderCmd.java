package com.example.yadisak.androidtest3.PageActivity.CMD;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.yadisak.androidtest3.CollectionAdap._SelectionAdap;
import com.example.yadisak.androidtest3.ControllerAdap.ViewCustomer;
import com.example.yadisak.androidtest3.ControllerAdap.ViewOrder;
import com.example.yadisak.androidtest3.ControllerAdap.ViewOrderItem;
import com.example.yadisak.androidtest3.ControllerAdap.ViewProductOrdPick;
import com.example.yadisak.androidtest3.ControllerAdap.ViewProductOrdPickPoint;
import com.example.yadisak.androidtest3.DTO.Order;
import com.example.yadisak.androidtest3.DTO.OrderItem;
import com.example.yadisak.androidtest3.DTO.Product;
import com.example.yadisak.androidtest3.DTO._SelectionProperty;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._Extension.Utility;
import com.example.yadisak.androidtest3._Interface.ICRUDResult;

import java.util.Date;
import java.util.List;

public class ActOrderCmd extends _ActivityCustom {

    ViewOrder adap;
    ViewOrderItem adapOrProd;
    ViewProductOrdPick adapMsProd;
    ViewProductOrdPickPoint adapMsProdPoint;
    _SelectionAdap adapSeCus;

    DrawerLayout drawer;

    Intent curtact;
    CMDState state;
    Order ent;
    Product ent_pro;

    EditText txt_orno;
    EditText txt_ordate;
    Spinner sp_customer;

    TableRow tr_order_save;
    TableRow tr_item_head;
    TableRow tr_item_detail;

    ListView listViewOrProd;
    ListView listViewMsProd;

    boolean hasChanged;

    boolean initNavListProd;
    boolean isNavListProdPoint;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    void initActivity() {
        try {

            switch (state) {
                case NEW:

                    setTitle("ย้ายชื่อลูกค้าขึ้นมา");

                    Date currDateTime = new Date(System.currentTimeMillis());

                    txt_orno.setText("");
                    txt_ordate.setText(Utility.DATE_FORMAT.format(currDateTime));
                    sp_customer.setSelection(0);

                    txt_orno.setEnabled(true);
                    sp_customer.setEnabled(true);

                    tr_order_save.setVisibility(View.VISIBLE);
                    tr_item_head.setVisibility(View.GONE);
                    tr_item_detail.setVisibility(View.GONE);

                    break;
                case EDIT:

                    setTitle("ย้ายชื่อลูกค้าขึ้นมา");

                    if (ent == null)
                        ent = (Order) curtact.getSerializableExtra(Utility.ENTITY_DTO_NAME);

                    // Order Item
                    adapOrProd = new ViewOrderItem(this, ent.getFirebaseId());
                    listViewOrProd.setAdapter(adapOrProd.getAdapter());
                    listViewOrProd.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {

                        OrderItem orit = adapOrProd.getItem(position);
                        Intent nextact = new Intent(this, ActOrderItemCmd.class);
                        nextact.putExtra(Utility.CMD_STATE, CMDState.EDIT);
                        nextact.putExtra(Utility.ENTITY_DTO_NAME, orit);
                        nextact.putExtra("entOrder", ent);
                        toNextActivity(nextact);

                    });
                    listViewOrProd.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {

                        OrderItem ordi = adapOrProd.getItem(position);
                        ordi.setCus_code(ent.getCus_code());
                        adapOrProd.removeItem(ordi, new ICRUDResult() {
                            @Override
                            public void onReturn(DAOState status, String message) {
                                if (status != DAOState.SUCCESS)
                                    showMessageAlert(message);
                            }
                        });

                        return true;
                    });
                    //....................................


                    txt_orno.setText(ent.getNo());
                    txt_ordate.setText(Utility.DATE_FORMAT.format(ent.getDate()));

                    txt_orno.setEnabled(false);
                    sp_customer.setEnabled(false);

                    tr_order_save.setVisibility(View.GONE);
                    tr_item_head.setVisibility(View.VISIBLE);
                    tr_item_detail.setVisibility(View.VISIBLE);

                    break;
            }
        } catch (Exception ex) {
            showMessageAlert(ex.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cmd_order);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        hasChanged = false;

        initNavListProd = false;
        isNavListProdPoint = false;

        adap = new ViewOrder(this);

        curtact = getIntent();
        this.state = (CMDState) curtact.getSerializableExtra(Utility.CMD_STATE);

        this.txt_orno = (EditText) findViewById(R.id.txt_order_no);
        this.txt_ordate = (EditText) findViewById(R.id.txt_order_date);
        this.sp_customer = (Spinner) findViewById(R.id.sp_customer);
        this.listViewOrProd = (ListView) findViewById(R.id.list_order_item);

        this.tr_order_save = (TableRow) findViewById(R.id.tr_order_save);
        this.tr_item_head = (TableRow) findViewById(R.id.tr_item_head);
        this.tr_item_detail = (TableRow) findViewById(R.id.tr_item_detail);

        // Customer Spinner
        ViewCustomer adapCus = new ViewCustomer(this);
        adapCus.getSelectionList((DAOState status, String message, Object obj) -> {

            List<_SelectionProperty> items = (List<_SelectionProperty>) obj;
            // items.add(0,new _SelectionProperty(null, "--Not Select--"));

            adapSeCus = new _SelectionAdap(getApplicationContext(), R.layout._spinner_item_custom, R.id.title, items);
            sp_customer.setAdapter(adapSeCus);

            if (state == CMDState.EDIT && ent != null) {
                for (int inx = 0; inx < adapSeCus.getCount(); inx++) {

                    String id = (String) adapSeCus.getItem(inx).getId();
                    if (id.equals(ent.getCus_code())) {
                        sp_customer.setSelection(inx);
                        break;
                    }
                }
            }
        });

        adapCus = null;
        //.............................

        this.initActivity();


        Button bt_cmd_save = (Button) findViewById(R.id.bt_cmd_save);
        bt_cmd_save.setText("สร้าง");
        bt_cmd_save.setOnClickListener(view -> {

            if (txt_orno.getText().toString().isEmpty()) {
                showMessageAlert("!กรุณาระบุเลขที่");
                return;
            }

            Date currDateTime = new Date(System.currentTimeMillis());
            _SelectionProperty seit = (_SelectionProperty) sp_customer.getSelectedItem();

            final ICRUDResult iCRUDRes = new ICRUDResult() {
                @Override
                public void onReturn(DAOState status, String message) {
                    if (status == DAOState.SUCCESS) {
                        if (state == CMDState.NEW) {

                            state = CMDState.EDIT;
                            hasChanged = true;

                            initActivity();
                        }
                    } else
                        showMessageAlert(message);
                }
            };

            switch (state) {
                case NEW:

                    this.ent = new Order();
                    this.ent.setNo(txt_orno.getText().toString());
                    this.ent.setDate(currDateTime);
                    this.ent.setCus_code(seit.getId().toString());
                    adap.addItem(ent, iCRUDRes);


                    break;
                case EDIT:

                    break;
            }
        });

        // Product For Select Items
        Button bt_add_item = (Button) findViewById(R.id.bt_add_item);
        bt_add_item.setOnClickListener(view -> {
            if ((isNavListProdPoint == true) || (initNavListProd == false)) {
                showProgressDialog();
                listViewMsProd.setAdapter(adapMsProd.getAdapter());
                isNavListProdPoint = false;

                if (!initNavListProd) initNavListProd = true;
            }

            toggleNavListProduct();
            hideProgressDialog();
        });

        Button bt_add_item_point = (Button) findViewById(R.id.bt_add_item_point);
        bt_add_item_point.setOnClickListener(view -> {
            if (isNavListProdPoint == false) {
                showProgressDialog();
                listViewMsProd.setAdapter(adapMsProdPoint.getAdapter());
                isNavListProdPoint = true;
            }

            toggleNavListProduct();
            hideProgressDialog();
        });


        adapMsProd = new ViewProductOrdPick(this);
        adapMsProdPoint = new ViewProductOrdPickPoint(this);

        listViewMsProd = (ListView) findViewById(R.id.list_order_prod_pick);
        listViewMsProd.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {

            String foc_flag = "N";
            if (isNavListProdPoint == false)
                ent_pro = adapMsProd.getItem(position);
            else {
                ent_pro = adapMsProdPoint.getItem(position);
                foc_flag = "Y";
            }

            OrderItem orit = adapOrProd.getItem(ent_pro.getCode(),foc_flag);

            if (orit == null) {
                orit = new OrderItem();
                orit.setPro_key_id(ent_pro.getFirebaseId());
                orit.setPro_code(ent_pro.getCode());
                orit.setPro_name(ent_pro.getName());
                orit.setCus_code(ent.getCus_code());

                if (!isNavListProdPoint) {
                    orit.setFoc_flag(foc_flag);
                    orit.setPoint(ent_pro.getPoint());
                } else {
                    orit.setFoc_flag(foc_flag);
                    orit.setPoint(ent_pro.getFocpoint()* -1);
                }

                adapOrProd.addItem(orit, (DAOState istatus, String imessage) -> {
                    if (istatus == DAOState.SUCCESS) {
                        showMessageNoti("Item : " + ent_pro.getName() + " added in order.");
                    } else
                        showMessageAlert(imessage);
                });
            } else {
                orit.setDelta(orit.getQty());
                orit.setQty(orit.getQty() + 1);
                orit.setCus_code(ent.getCus_code());
                adapOrProd.updateItem(orit, (DAOState ostatus, String omessage) -> {
                    if (ostatus == DAOState.SUCCESS) {
                        showMessageNoti("Item : " + ent_pro.getName() + " + quantity");
                    } else
                        showMessageAlert(omessage);
                });
            }
        });

        EditText editsearch = (EditText) findViewById(R.id.txt_search);
        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                String text = editsearch.getText().toString();
                //adapMsProd.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
        //.........................................
    }

    void toggleNavListProduct() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the up button
            case android.R.id.home:

                if (hasChanged == true) toPrevActivityRefresh();
                else finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (hasChanged == true) toPrevActivityRefresh();
            else super.onBackPressed();
        }
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem actionViewItem = menu.findItem(R.id.ActionSummary);
        View v = MenuItemCompat.getActionView(actionViewItem);
        switch (state) {
            case NEW:

                actionViewItem.setVisible(false);
                this.invalidateOptionsMenu();
                break;
            case EDIT:

                break;
        }



        Button bt_item = (Button) v.findViewById(R.id.bt_action_summary);
        bt_item.setOnClickListener(view -> {

            Toast.makeText(ActOrderCmd.this, "ทำหน้า Summary", Toast.LENGTH_SHORT).show();
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.summary, menu);
        return true;
    }
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    public void onStart() {

        super.onStart();

    }
}