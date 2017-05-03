package com.example.yadisak.androidtest3;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yadisak.androidtest3.DTO.Login;
import com.example.yadisak.androidtest3.PageActivity.ActBranch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ActLogin extends _ActivityCustom {

    private EditText txt_username, txt_password;
    private FirebaseAuth mAuth;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private String username, password;
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._sys_cmd_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mAuth = FirebaseAuth.getInstance();

        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);

        saveLoginCheckBox = (CheckBox) findViewById(R.id.chk_box);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin == true) {
            txt_username.setText(loginPreferences.getString("username", ""));
            txt_password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        Button bt_cmd_save = (Button) findViewById(R.id.bt_cmd_login);
        bt_cmd_save.setOnClickListener(v -> {
            showProgressDialog();

            username = txt_username.getText().toString();
            password = txt_password.getText().toString();

            if (Validate()) {
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, task -> {
                    getFirebaseUser(username, task.isSuccessful());
                });
            }
        });

        Button bt_cmd_new = (Button) findViewById(R.id.bt_cmd_newaccount);
        bt_cmd_new.setOnClickListener(view -> {
            showProgressDialog();

            username = txt_username.getText().toString();
            password = txt_password.getText().toString();
            if (Validate()) {
                mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                    getFirebaseUser(username, task.isSuccessful());
                });
            }
        });
    }

    private boolean Validate() {
        Boolean isValid = true;
        if (username == null) {
            txt_username.setError("ต้องกรอก");
            isValid = false;
        }
        if (password == null) {
            txt_password.setError("ต้องกรอก");
            isValid = false;
        } else if (password.length() < 6) {
            txt_password.setError("6 หลักขึ้นไป");
            isValid = false;
        }
        return isValid;
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void getFirebaseUser(String email, boolean isSuccessful) {

        if (!isSuccessful) {
            hideProgressDialog();
            Toast.makeText(ActLogin.this, "อีเมลหรือรหัสผ่าน ไม่ถูกต้อง...กรุณาตรวจสอบ", Toast.LENGTH_SHORT).show();
        } else {
            Query query = FirebaseDatabase.getInstance().getReference().child("login").orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot value = dataSnapshot.getChildren().iterator().next();
                    Login login = value.getValue(Login.class);
                    Globaldata.Login = login;

                    hideProgressDialog();
                    Intent nextact = new Intent(getApplicationContext(), ActBranch.class);
                    startActivity(nextact);
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            if (saveLoginCheckBox.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", username);
                loginPrefsEditor.putString("password", password);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ปิดโปรแกรม");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setCancelable(true);
        dialog.setMessage("คุณต้องการออกจากโปรแกรมหรือไม่ ?");
        dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });

        dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
