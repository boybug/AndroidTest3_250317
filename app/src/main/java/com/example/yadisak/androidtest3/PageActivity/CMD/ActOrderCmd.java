package com.example.yadisak.androidtest3.PageActivity.CMD;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yadisak.androidtest3.CollectionAdap._SelectionAdap;
import com.example.yadisak.androidtest3.ControllerAdap.ViewCustomer;
import com.example.yadisak.androidtest3.ControllerAdap.ViewOrder;
import com.example.yadisak.androidtest3.ControllerAdap.ViewOrderItem;
import com.example.yadisak.androidtest3.ControllerAdap.ViewProductOrdPick;
import com.example.yadisak.androidtest3.DTO.Customer;
import com.example.yadisak.androidtest3.DTO.Order;
import com.example.yadisak.androidtest3.DTO.OrderItem;
import com.example.yadisak.androidtest3.DTO.Product;
import com.example.yadisak.androidtest3.DTO._SelectionProperty;
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
import java.util.List;
import java.util.Locale;

public class ActOrderCmd extends _ActivityCustom {

    ViewOrder adap;
    ViewOrderItem adapOrProd;
    ViewProductOrdPick adapMsProd;

    _SelectionAdap adapSeCus;

    DrawerLayout drawer;

    Intent curtact;
    CMDState state;

    Order ent;
    Product ent_pro;

    TextView txt_orno;
    TextView txt_qty;
    TextView txt_wgt;
    TextView txt_total;
    Spinner sp_customer;

    float total_price = 0;
    int total_qty = 0;
    int total_wgt = 0;

    TableRow tr_customer_new;
    TableRow tr_order_save;
    TableRow tr_item_head;
    TableRow tr_item_detail;

    ListView listViewOrProd;
    ListView listViewMsProd;

    boolean hasChanged;

    int cusid = 0;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    void initActivity() {
        try {

            switch (state) {
                case NEW:

                    cusid  = Integer.parseInt(curtact.getExtras().getString("cusid"));

                    ViewCustomer adapCus = new ViewCustomer(this);
                    adapCus.getSelectionList((DAOState status, String message, Object obj) -> {

                        List<_SelectionProperty> items = (List<_SelectionProperty>) obj;
                        adapSeCus = new _SelectionAdap(getApplicationContext(), R.layout._spinner_item_custom, R.id.title, items);
                        sp_customer.setAdapter(adapSeCus);

//                        if (items.size() > 0) {
//                            sp_customer.setSelection(0);
//                        }

                        if (cusid != 0 ) {
                            sp_customer.setSelection(cusid-1);
                        }
                    });
                    sp_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            _SelectionProperty seit = (_SelectionProperty) sp_customer.getSelectedItem();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });

                    adapCus = null;
                    //.............................

                    txt_orno.setText("");
                    txt_total.setText("0.0");
                    txt_qty.setText("0");
                    //txt_ordate.setText(Utility.DATE_FORMAT.format(currDateTime));

                    tr_customer_new.setVisibility(View.VISIBLE);
                    tr_order_save.setVisibility(View.VISIBLE);
                    tr_item_head.setVisibility(View.GONE);
                    tr_item_detail.setVisibility(View.GONE);

                    break;
                case EDIT:

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

                        adapOrProd.removeItem(ordi, new ICRUDResult() {
                            @Override
                            public void onReturn(DAOState status, String message) {
                                if (status == DAOState.SUCCESS) {

                                    funcCalTotal();
                                } else {
                                    showMessageAlert(message);
                                }
                            }
                        });

                        return true;
                    });

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            total_price = adapOrProd.getTotalPrice();
                            total_qty = adapOrProd.getTotalqty();
                            total_wgt = adapOrProd.getTotalwgt();
                            txt_total.setText(String.valueOf(total_price));
                            txt_qty.setText(String.valueOf(total_qty));
                            txt_wgt.setText(String.valueOf(total_wgt));
                            adap.updateItemtotal(ent,total_price);
                        }
                    }, 1000);

                    //....................................

                    adap.getCustomer(ent, (status, message, obj) -> {
                        if (status == DAOState.SUCCESS) {
                            Customer cus = (Customer) obj;

                        } else {
                            showMessageNoti(message);
                        }
                    });

                    sp_customer.setAdapter(null);
                    txt_orno.setText(ent.getNo());


                    tr_customer_new.setVisibility(View.GONE);
                    tr_order_save.setVisibility(View.GONE);
                    tr_item_head.setVisibility(View.VISIBLE);
                    tr_item_detail.setVisibility(View.VISIBLE);



                    setTitle(ent.getCus_name());

                    break;
            }
        } catch (Exception ex) {
            showMessageAlert(ex.getMessage());
        }
    }

    void funcCalTotal() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                total_price = adapOrProd.getTotalPrice();
                total_qty = adapOrProd.getTotalqty();
                total_wgt = adapOrProd.getTotalwgt();
                txt_total.setText(String.valueOf(total_price));
                txt_qty.setText(String.valueOf(total_qty));
                txt_wgt.setText(String.valueOf(total_wgt));
                adap.updateItemtotal(ent,total_price);
            }
        }, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cmd_order);

        ImageView img_logo_bar = (ImageView) findViewById(R.id.img_logo_bar);
        img_logo_bar.setVisibility(View.GONE);

        TextView title = (TextView) findViewById(R.id.title_text);
        title.setTextColor(Color.parseColor("#bcdee7"));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        hasChanged = false;



        adap = new ViewOrder(this);

        curtact = getIntent();
        this.state = (CMDState) curtact.getSerializableExtra(Utility.CMD_STATE);

        this.txt_orno = (TextView) findViewById(R.id.txt_order_no);

        this.txt_total = (TextView) findViewById(R.id.txt_total);
        this.txt_qty = (TextView) findViewById(R.id.txt_qty);
        this.txt_wgt = (TextView) findViewById(R.id.txt_weight);

        this.sp_customer = (Spinner) findViewById(R.id.sp_customer);
        this.listViewOrProd = (ListView) findViewById(R.id.list_order_item);

        this.tr_customer_new = (TableRow) findViewById(R.id.tr_customer_new);
        this.tr_order_save = (TableRow) findViewById(R.id.tr_order_save);
        this.tr_item_head = (TableRow) findViewById(R.id.tr_item_head);
        this.tr_item_detail = (TableRow) findViewById(R.id.tr_item_detail);

        this.initActivity();


        int current = Globaldata.Branch.getNumber();

        Button bt_cmd_save = (Button) findViewById(R.id.bt_cmd_save);
        bt_cmd_save.setText("สร้าง");
        bt_cmd_save.setOnClickListener(view -> {

            String date = new SimpleDateFormat("yyMM").format(new Date());
            String last_no = "000";
                last_no = last_no + String.valueOf(current + 1);
                last_no = last_no.substring(last_no.length()-4, last_no.length());

            txt_orno.setText(Globaldata.Branch.getCode()+date+last_no);



            Date currDateTime = new Date(System.currentTimeMillis());
            _SelectionProperty seit = (_SelectionProperty) sp_customer.getSelectedItem();

            final ICRUDResult iCRUDRes = new ICRUDResult() {
                @Override
                public void onReturn(DAOState status, String message) {
                    if (status == DAOState.SUCCESS) {
                        if (state == CMDState.NEW) {

                            state = CMDState.EDIT;
                            hasChanged = true;

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
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });

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
                    this.ent.setCus_name(seit.getText().toString());
                    this.ent.setUser(Globaldata.Login.getName());
                    this.ent.setStat("new");
                    adap.addItem(ent,iCRUDRes);

                    break;
                case EDIT:

                    break;
            }
        });

        // Product For Select Items
        Button bt_add_item = (Button) findViewById(R.id.bt_add_item);
        bt_add_item.setOnClickListener(view -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });




        adapMsProd = new ViewProductOrdPick(this);

        listViewMsProd = (ListView) findViewById(R.id.list_order_prod_pick);
        listViewMsProd.setAdapter(adapMsProd.getAdapter());

        listViewMsProd.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {


                ent_pro = adapMsProd.getItem(position);

            OrderItem orit = adapOrProd.getItem(ent_pro.getCode());

            if (orit == null) {
                orit = new OrderItem();
                orit.setPro_key_id(ent_pro.getFirebaseId());
                orit.setPro_code(ent_pro.getCode());
                orit.setPro_name(ent_pro.getName());
                orit.setGross_wgt(ent_pro.getGross_wgt());


                adapOrProd.addItem(orit, (DAOState istatus, String imessage) -> {
                    if (istatus == DAOState.SUCCESS) {

                        funcCalTotal();

//                        showMessageNoti("Item : " + ent_pro.getName() + " added in order.");
                    } else
                        showMessageAlert(imessage);
                });
            } else {
                orit.setDelta(orit.getQty());
                orit.setQty(orit.getQty() + 1);
                adapOrProd.updateItem(orit, (DAOState ostatus, String omessage) -> {
                    if (ostatus == DAOState.SUCCESS) {

                        funcCalTotal();

//                        showMessageNoti("Item : " + ent_pro.getName() + " + quantity");
                    } else
                        showMessageAlert(omessage);
                });
            }
        });

        EditText editsearch = (EditText) findViewById(R.id.txt_search);
        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapMsProd.filter(text);
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
            editsearch.setText("");
        });
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

    public int getCount() {
        return adap.getCount();
    }
}