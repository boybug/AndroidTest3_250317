package com.example.yadisak.androidtest3._DBProvider;

import android.content.Context;

public class DBManager {

    static private DBManager instance;

    static public void init(Context ctx) {
        if (null == instance) {
            instance = new DBManager(ctx);
        }
    }

    static public DBManager getInstance() {
        return instance;
    }

    private DBHelper helper;

    private DBManager(Context ctx) {
        helper = new DBHelper(ctx);
    }

    public DBHelper getHelper() {
        return helper;
    }
}