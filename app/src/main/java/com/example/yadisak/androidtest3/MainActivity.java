package com.example.yadisak.androidtest3;

//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.support.v7.app.AlertDialog;
//import android.view.Menu;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.yadisak.androidtest3.ControllerAdap.ViewCustomMenu;
//import com.example.yadisak.androidtest3.DTO.Customer;
//import com.example.yadisak.androidtest3.DTO.Product;
//import com.example.yadisak.androidtest3.DTO._CustomMenu;
//import com.example.yadisak.androidtest3.PageActivity.ActCustomer;
//import com.example.yadisak.androidtest3.PageActivity.ActOrder;
//import com.example.yadisak.androidtest3.PageActivity.ActProduct;
//import com.example.yadisak.androidtest3._DBProvider.DBHelper;
//import com.example.yadisak.androidtest3._DBProvider.DBManager;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.GenericTypeIndicator;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MainActivity extends _ActivityCustom {
//
//    private Context context;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//
//        setContentView(R.layout.__activity_main);
//        TextView lab_main = (TextView) findViewById(R.id.lab_main);
//        lab_main.setText("User : "+ Globaldata.Login.getName() + "    Branch : "+ Globaldata.Branch.getCode());
//
//        context = getApplicationContext();
//
//        ViewCustomMenu adap = new ViewCustomMenu(context);
//
//        ListView list = (ListView) findViewById(R.id.list_main_menu);
//        list.setAdapter(adap);
//        list.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
//
//            _CustomMenu ent = adap.getItem(position);
//
//            Intent nextact = null;
//
//            if (ent.getMenu_id() == 1) {
//                nextact = new Intent(this, ActCustomer.class);
//                startActivity(nextact);
//            } else if (ent.getMenu_id() == 2) {
//                nextact = new Intent(this, ActProduct.class);
//                startActivity(nextact);
//            } else if (ent.getMenu_id() == 3) {
//                nextact = new Intent(this, ActOrder.class);
//                startActivity(nextact);
//            }
//        });
//    }
//
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return true;
//    }
////    @Override
////    public boolean onCreateOptionsMenu(_CustomMenu menu) {
////        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.menu_main, menu);
////        return true;
////    }
//
//}


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.yadisak.androidtest3.PageActivity.ActCustomer;
import com.example.yadisak.androidtest3.PageActivity.ActOrder;
import com.example.yadisak.androidtest3.PageActivity.ActProduct;


public class MainActivity extends AppCompatActivity {
    GridView grid;
    String[] menuItems = {
            "Order",
            "Customer",
            "Product",
            "Utility"
    };
    int[] menuIcons = {
            R.drawable.order,
            R.drawable.customer,
            R.drawable.product,
            R.drawable.utility


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        setTitle("Boonsiri Point of Sales");
        MainAdapter adapter = new MainAdapter(MainActivity.this, menuItems, menuIcons);
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent nextact = null;

                if(menuItems[+position] == "Order"){
                    nextact = new Intent(MainActivity.this, ActOrder.class);
                startActivity(nextact);
                }else if(menuItems[+position] == "Customer"){
                    nextact = new Intent(MainActivity.this, ActCustomer.class);
                    startActivity(nextact);
                }else if(menuItems[+position] == "Product"){
                    nextact = new Intent(MainActivity.this, ActProduct.class);
                    startActivity(nextact);
                }else if(menuItems[+position] == "Utility"){
                    Toast.makeText(MainActivity.this, "Next Version...", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

}