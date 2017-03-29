package com.example.yadisak.androidtest3.PageActivity.CMD;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yadisak.androidtest3.ControllerAdap.ViewProduct;
import com.example.yadisak.androidtest3.ControllerAdap.ViewProductPrice;
import com.example.yadisak.androidtest3.DTO.Product;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.Utility;

public class ActProductCmd extends _ActivityCustom {

    ViewProduct adap;
    Product ent;

    TextView txt_code, txt_stock, txt_price,txt_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cmd_product);

        adap = new ViewProduct(this);

        txt_code = (TextView) findViewById(R.id.txt_code);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_stock = (TextView) findViewById(R.id.txt_stock);
        txt_price = (TextView) findViewById(R.id.txt_price);

        Intent curtact = getIntent();
        CMDState state = (CMDState) curtact.getSerializableExtra(Utility.CMD_STATE);

        switch (state) {
            case NEW:

                setTitle("New Product");

                txt_code.setText("");
                txt_name.setText("");
                txt_stock.setText("");
                txt_price.setText("");

                txt_code.setEnabled(true);

                break;
            case EDIT:

                setTitle("รายละเอียดสินค้า");

                ent = (Product) curtact.getSerializableExtra(Utility.ENTITY_DTO_NAME);

                txt_code.setText(ent.getCode());
                txt_name.setText(ent.getName());
                txt_stock.setText(String.valueOf(ent.getStock()));
                txt_code.setEnabled(false);
                txt_name.setEnabled(false);
                txt_stock.setEnabled(false);

                ViewProductPrice adapPri = new ViewProductPrice(this,ent.getFirebaseId());

                ListView list = (ListView) findViewById(R.id.list_item_price);
                list.setAdapter(adapPri.getAdapter());
                list.setEmptyView(findViewById(R.id.emptyElement));

                break;
        }
    }
}
