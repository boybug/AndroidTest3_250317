package com.example.yadisak.androidtest3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class _ActivityCustom extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState, String _titleText) {
        this.onCreate(savedInstanceState);
        setTitle(_titleText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
            getSupportActionBar().setCustomView(R.layout._actionbar_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            showMessageAlert("Error : " + ex.getMessage());
        }
    }

    public void setTitle(String _titleText) {
        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText(_titleText);
    }

    public void showMessageNoti(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void showMessageAlert(String message) {
        showMessageAlert("Alert", message);
    }

    public void showMessageAlert(String titleText, String messageText) {

        AlertDialog.Builder builder = new AlertDialog.Builder(_ActivityCustom.this);
        builder.setTitle(titleText);
        builder.setMessage(messageText);
        builder.setNeutralButton("OK", (DialogInterface idialog, int i) -> {
            idialog.dismiss();
        }).show();
    }

    // ActivityResult Begin-----
    public void toNextActivity(Intent intent) {
        startActivityForResult(intent, 1);
    }

    public void toPrevActivityRefresh() {
        Intent intent = new Intent();
        //intent.putExtra("edittextvalue","value_here")
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
            startActivity(getIntent());
        }
    }
    // ActivityResult End-----

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the up button
            case android.R.id.home:
                finish();
                //onBackPressed();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}