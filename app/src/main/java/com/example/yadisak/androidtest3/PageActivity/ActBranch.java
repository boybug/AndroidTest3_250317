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
import android.widget.TableRow;

import com.example.yadisak.androidtest3.ControllerAdap.*;
import com.example.yadisak.androidtest3.DTO.Branch;
import com.example.yadisak.androidtest3.DTO.Customer;
import com.example.yadisak.androidtest3.Globaldata;
import com.example.yadisak.androidtest3.MainActivity;
import com.example.yadisak.androidtest3.PageActivity.CMD.ActCustomerCmd;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.example.yadisak.androidtest3._Extension.*;
import com.example.yadisak.androidtest3._Interface.ICRUDResult;

public class ActBranch extends _ActivityCustom {

    ViewBranch adap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setContentView(R.layout.view_data);
        setTitle("Branch");

        adap = new ViewBranch(this);

        TableRow tr = (TableRow) findViewById(R.id.tr_search);
        tr.setVisibility(View.GONE);

        ListView list = (ListView) findViewById(R.id.list_view_data);
        list.setAdapter(adap.getAdapter());
        list.setEmptyView(findViewById(R.id.emptyElement));
        list.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {

            Globaldata.Branch = adap.getItem(position);
            Intent nextact = new Intent(this, MainActivity.class);
            startActivity(nextact);

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
