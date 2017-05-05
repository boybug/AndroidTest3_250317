package com.example.yadisak.androidtest3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yadisak.androidtest3.ControllerAdap.ViewCustomMenu;
import com.example.yadisak.androidtest3.DTO._CustomMenu;
import com.example.yadisak.androidtest3.PageActivity.ActCustomer;
import com.example.yadisak.androidtest3.PageActivity.ActOrder;
import com.example.yadisak.androidtest3.PageActivity.ActProduct;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends _ActivityCustom {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setContentView(R.layout.__activity_main);
//        TextView lab_main = (TextView) findViewById(R.id.title_description);
//        lab_main.setText("User : "+ Globaldata.Login.getName() + "    Branch : "+ Globaldata.Branch.getCode());

        context = getApplicationContext();

        ViewCustomMenu adap = new ViewCustomMenu(context);

        ListView list = (ListView) findViewById(R.id.list_main_menu);
        list.setAdapter(adap);
        list.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {

            _CustomMenu ent = adap.getItem(position);

            Intent nextact = null;

            if (ent.getMenu_id() == 2) {
                nextact = new Intent(this, ActCustomer.class);
                startActivity(nextact);
            } else if (ent.getMenu_id() == 3) {
                nextact = new Intent(this, ActProduct.class);
                startActivity(nextact);
            } else if (ent.getMenu_id() == 1) {
                nextact = new Intent(this, ActOrder.class);
                startActivity(nextact);
            }else if (ent.getMenu_id() == 4) {
                Toast.makeText(MainActivity.this, "เวอร์ชัน 1.0.6", Toast.LENGTH_SHORT).show();
            }

        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            Logout();
        }
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
                Intent intent = new Intent(MainActivity.this,
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

