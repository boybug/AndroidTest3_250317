package com.example.yadisak.androidtest3.PageActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.yadisak.androidtest3.ActLogin;
import com.example.yadisak.androidtest3.ControllerAdap.ViewBranch;
import com.example.yadisak.androidtest3.Globaldata;
import com.example.yadisak.androidtest3.MainActivity;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._ActivityCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActBranch extends _ActivityCustom {

    ViewBranch adap;

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB = refDB.child("branch");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setContentView(R.layout.view_data);
        setTitle("สาขา");
        TableRow tr = (TableRow) findViewById(R.id.tr_search);
        tr.setVisibility(View.GONE);

        TextView empty = (TextView) findViewById(R.id.emptyElement);
        ProgressBar a = (ProgressBar) findViewById(R.id.progressbar);
        TableRow tabempty = (TableRow) findViewById(R.id.tab_empty);
        ListView list = (ListView) findViewById(R.id.list_view_data);

        adap = new ViewBranch(this);

        refTB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() == true) {
                    list.setAdapter(adap.getAdapter());
                    list.setEmptyView(tabempty);
                }
                else
                {
                    empty.setText("ไม่พบข้อมูล");
                    a.getIndeterminateDrawable().setColorFilter(0x00000000, android.graphics.PorterDuff.Mode.MULTIPLY);
                    list.setAdapter(adap.getAdapter());
                    list.setEmptyView(tabempty);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        list.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            AlertDialog.Builder dialog_go = new AlertDialog.Builder(this);
            dialog_go.setTitle("ยืนยันการเลือก");
            dialog_go.setIcon(R.mipmap.ic_launcher);
            dialog_go.setCancelable(true);
            dialog_go.setMessage("คุณต้องการเข้าสู่สาขา "+adap.getItem(position).getName()+ " ?");
            dialog_go.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Globaldata.Branch = adap.getItem(position);
                    Intent nextact = new Intent(ActBranch.this, MainActivity.class);
                    startActivity(nextact);

                    finish();
                }
            });

            dialog_go.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            dialog_go.show();
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
