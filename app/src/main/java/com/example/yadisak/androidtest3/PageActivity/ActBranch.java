package com.example.yadisak.androidtest3.PageActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.yadisak.androidtest3.ActLogin;
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
import com.google.firebase.auth.FirebaseAuth;

public class ActBranch extends _ActivityCustom {

    ViewBranch adap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setContentView(R.layout.view_data);
        setTitle("สาขา");

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
    public void Logout()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ออกจากระบบ");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setCancelable(true);
        dialog.setMessage("คุณต้องการออกจากระบบ?");
        dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ActBranch.this,
                        ActLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
            }
        });

        dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void onBackPressed() {
        Logout();
    }

}
