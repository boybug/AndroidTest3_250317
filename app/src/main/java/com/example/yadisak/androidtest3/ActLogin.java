package com.example.yadisak.androidtest3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yadisak.androidtest3.DTO.Login;
import com.example.yadisak.androidtest3.PageActivity.ActBranch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActLogin extends _ActivityCustom {


    EditText txt_username, txt_password;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;


    @VisibleForTesting
    public ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setContentView(R.layout._sys_cmd_login);



        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);


        Button bt_cmd_save = (Button) findViewById(R.id.bt_cmd_login);
        Button bt_cmd_new = (Button) findViewById(R.id.bt_cmd_newaccount);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    // User is signed in


                } else {

                    // User is signed out


                }

                // [START_EXCLUDE]

                updateUI(user);

                // [END_EXCLUDE]

            }
        };


        bt_cmd_save.setOnClickListener(view -> {
            signIn(txt_username.getText().toString(), txt_password.getText().toString());
        });

        bt_cmd_new.setOnClickListener(view -> {
            createAccount(txt_username.getText().toString(), txt_password.getText().toString());
        });

    }

    private void updateUI(FirebaseUser user) {

        hideProgressDialog();

        if (user != null) {

        } else {

        }

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    public void onStart() {

        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void createAccount(String email, String password) {


        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {

                            Toast.makeText(ActLogin.this, "อีเมลหรือรหัสผ่าน ไม่ถูกต้อง...กรุณาตรวจสอบ",Toast.LENGTH_SHORT).show();

                        }
                        else{
                            // คิวรี่ user มาจาก database แล้วก็ ไปหน้า branch

                            processlogin();

                        }
                        hideProgressDialog();
                    }

                });

        // [END create_user_with_email]

    }

    private void processlogin() {

        DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
        DatabaseReference refTB = refDB.child("login");


        refTB.orderByChild("email").equalTo(txt_username.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() == true) {

                            DataSnapshot value = dataSnapshot.getChildren().iterator().next();
                            Globaldata.Login = value.getValue(Login.class);
                            Intent nextact = new Intent(getApplicationContext(), ActBranch.class);
                            startActivity(nextact);

                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private boolean validateForm() {

        boolean valid = true;
        String email = txt_username.getText().toString();

        if (TextUtils.isEmpty(email)) {

            txt_username.setError("Required.");

            valid = false;

        } else {

            txt_username.setError(null);

        }

        String password = txt_password.getText().toString();

        if (TextUtils.isEmpty(password)) {

            txt_password.setError("Required.");

            valid = false;

        }
        else if (password.length() < 6 ){
            txt_password.setError("รหัสผ่านต้องไม่น้อยหกว่า 6 หลัก");

            valid = false;
        }
        else {

            txt_password.setError(null);

        }
        return valid;
    }

    private void signIn(String email, String password) {

        if (!validateForm()) {

            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {

                            Toast.makeText(ActLogin.this, "อีเมลหรือรหัสผ่าน ไม่ถูกต้อง...กรุณาตรวจสอบ",Toast.LENGTH_SHORT).show();

                        }
                        else{
                            processlogin();
                        }

                        hideProgressDialog();

                    }

                });

        // [END sign_in_with_email]

    }

    private void signOut() {

        mAuth.signOut();

        updateUI(null);

    }

//    private void sendEmailVerification() {
//
//        // Disable button
//
//        findViewById(R.id.verify_email_button).setEnabled(false);
//
//
//
//        // Send verification email
//
//        // [START send_email_verification]
//
//        final FirebaseUser user = mAuth.getCurrentUser();
//
//        user.sendEmailVerification()
//
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//
//                    @Override
//
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        // [START_EXCLUDE]
//
//                        // Re-enable button
//
//                        findViewById(R.id.verify_email_button).setEnabled(true);
//
//
//
//                        if (task.isSuccessful()) {
//
//                            Toast.makeText(EmailPasswordActivity.this,
//
//                                    "Verification email sent to " + user.getEmail(),
//
//                                    Toast.LENGTH_SHORT).show();
//
//                        } else {
//
//                            Log.e(TAG, "sendEmailVerification", task.getException());
//
//                            Toast.makeText(EmailPasswordActivity.this,
//
//                                    "Failed to send verification email.",
//
//                                    Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        // [END_EXCLUDE]
//
//                    }
//
//                });
//
//        // [END send_email_verification]
//
//    }
}
