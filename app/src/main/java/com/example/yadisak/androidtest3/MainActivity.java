package com.example.yadisak.androidtest3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yadisak.androidtest3.ControllerAdap.ViewCustomMenu;
import com.example.yadisak.androidtest3.DTO._CustomMenu;
import com.example.yadisak.androidtest3.PageActivity.ActCustomer;
import com.example.yadisak.androidtest3.PageActivity.ActOrder;
import com.example.yadisak.androidtest3.PageActivity.ActProduct;

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

            if (ent.getMenu_id() == 1) {
                nextact = new Intent(this, ActCustomer.class);
                startActivity(nextact);
            } else if (ent.getMenu_id() == 2) {
                nextact = new Intent(this, ActProduct.class);
                startActivity(nextact);
            } else if (ent.getMenu_id() == 3) {
                nextact = new Intent(this, ActOrder.class);
                startActivity(nextact);
            }else if (ent.getMenu_id() == 4) {
                Toast.makeText(MainActivity.this, "เวอร์ชัน...ถัดไป", Toast.LENGTH_SHORT).show();
            }

        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
//    @Override
//    public boolean onCreateOptionsMenu(_CustomMenu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

}

//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.GridView;
//import android.widget.Toast;
//
//import com.example.yadisak.androidtest3.PageActivity.ActCustomer;
//import com.example.yadisak.androidtest3.PageActivity.ActOrder;
//import com.example.yadisak.androidtest3.PageActivity.ActProduct;
//
//
//public class MainActivity extends AppCompatActivity {
//    GridView grid;
//    String[] menuItems = {
//            "ขาย",
//            "ลูกค้า",
//            "สินค้า",
//            "เครื่องมือ"
//    };
//    int[] menuIcons = {
//            R.drawable.order,
//            R.drawable.customer,
//            R.drawable.product,
//            R.drawable.utility
//
//
//    };
//
//    private Context context;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_main);
//
//        context = getApplicationContext();
//
//        MainAdapter adapter = new MainAdapter(MainActivity.this, menuItems, menuIcons);
//        grid = (GridView) findViewById(R.id.grid);
//        grid.setAdapter(adapter);
//        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Intent nextact = null;
//
//                if(menuItems[+position] == "ขาย"){
//                    nextact = new Intent(MainActivity.this, ActOrder.class);
//                startActivity(nextact);
//                }else if(menuItems[+position] == "ลูกค้า"){
//                    nextact = new Intent(MainActivity.this, ActCustomer.class);
//                    startActivity(nextact);
//                }else if(menuItems[+position] == "สินค้า"){
//                    nextact = new Intent(MainActivity.this, ActProduct.class);
//                    startActivity(nextact);
//                }else if(menuItems[+position] == "เครื่องมือ"){
//                    Toast.makeText(MainActivity.this, "เวอร์ชัน...ถัดไป", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });
//
//    }
//
//}