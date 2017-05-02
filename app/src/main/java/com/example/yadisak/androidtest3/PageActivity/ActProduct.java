package com.example.yadisak.androidtest3.PageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.yadisak.androidtest3.ControllerAdap.ViewProduct;
import com.example.yadisak.androidtest3.DTO.Product;
import com.example.yadisak.androidtest3.MainActivity;
import com.example.yadisak.androidtest3.PageActivity.CMD.ActProductCmd;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.CMDState;
import com.example.yadisak.androidtest3._Extension.DAOState;
import com.example.yadisak.androidtest3._Extension.Utility;
import com.example.yadisak.androidtest3._Interface.ICRUDResult;

import java.util.Locale;

public class ActProduct extends _ActivityCustom {

    ViewProduct adap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_data);
        setTitle("สินค้า");

        adap = new ViewProduct(this);

        TextView empty = (TextView) findViewById(R.id.emptyElement);
        ProgressBar a = (ProgressBar) findViewById(R.id.progressbar);
        TableRow tabempty = (TableRow) findViewById(R.id.tab_empty);
        ListView list = (ListView) findViewById(R.id.list_view_data);
        list.setAdapter(adap.getAdapter());
        list.setEmptyView(tabempty);

        list.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {

            Product ent = adap.getItem(position);
            Intent nextact = new Intent(this, ActProductCmd.class);
            nextact.putExtra(Utility.CMD_STATE, CMDState.EDIT);
            nextact.putExtra(Utility.ENTITY_DTO_NAME, ent);
            toNextActivity(nextact);

        });
        list.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {

            Product ent = adap.getItem(position);
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
                empty.setText("ไม่พบข้อมูล");
                a.getIndeterminateDrawable().setColorFilter(0x00000000, android.graphics.PorterDuff.Mode.MULTIPLY);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
