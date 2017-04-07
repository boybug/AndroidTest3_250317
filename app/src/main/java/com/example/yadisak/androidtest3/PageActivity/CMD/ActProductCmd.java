package com.example.yadisak.androidtest3.PageActivity.CMD;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yadisak.androidtest3.ControllerAdap.ViewProductPrice;
import com.example.yadisak.androidtest3.DTO.Product;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.Utility;

public class ActProductCmd extends _ActivityCustom {


    Product ent;

    TextView txt_code, txt_stock, txt_price,txt_name,txt_gross_wgt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cmd_product);



        txt_code = (TextView) findViewById(R.id.txt_code);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_stock = (TextView) findViewById(R.id.txt_stock);
        txt_price = (TextView) findViewById(R.id.txt_price);
        txt_gross_wgt = (TextView) findViewById(R.id.txt_gross_wgt);

                Intent curtact = getIntent();


                setTitle("รายละเอียดสินค้า");

                ent = (Product) curtact.getSerializableExtra(Utility.ENTITY_DTO_NAME);

                txt_code.setText(ent.getCode());
                txt_name.setText(ent.getName());
                txt_stock.setText(String.valueOf(ent.getStock()));
                txt_gross_wgt.setText(String.valueOf(ent.getGross_wgt()));
//                txt_code.setBackgroundColor(Color.parseColor(ent.getBgcolor()));
//                txt_name.setBackgroundColor(Color.parseColor(ent.getBgcolor()));
//                txt_stock.setBackgroundColor(Color.parseColor(ent.getBgcolor()));
//                txt_gross_wgt.setBackgroundColor(Color.parseColor(ent.getBgcolor()));

                ViewProductPrice adapPri = new ViewProductPrice(this,ent.getFirebaseId());

                ListView list = (ListView) findViewById(R.id.list_item_price);
                list.setAdapter(adapPri.getAdapter());
                list.setEmptyView(findViewById(R.id.emptyElement));


    }
}
