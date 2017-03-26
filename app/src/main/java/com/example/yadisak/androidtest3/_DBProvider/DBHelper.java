package com.example.yadisak.androidtest3._DBProvider;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.yadisak.androidtest3.DTO.*;


public class DBHelper extends OrmLiteSqliteOpenHelper {

    //Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +
    private static final String DATABASE_NAME = "possys.sqlite";
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    //pressure
    private Dao<Product, Integer> DaoProduct = null;
    private Dao<Customer, Integer> DaoCustomer = null;
    private Dao<Order, Integer> DaoOrder = null;
    private Dao<OrderItem, Integer> DaoOrderItem = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, Product.class);
            TableUtils.createTable(connectionSource, Customer.class);
            TableUtils.createTable(connectionSource, Order.class);
            TableUtils.createTable(connectionSource, OrderItem.class);

        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {

            TableUtils.dropTable(connectionSource, Product.class, true);
            TableUtils.dropTable(connectionSource, Customer.class, true);
            TableUtils.dropTable(connectionSource, Order.class, true);
            TableUtils.dropTable(connectionSource, OrderItem.class, true);

            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "exception during onUpgrade", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Product, Integer> getDaoProduct() {
        if (null == DaoProduct) {
            try {
                DaoProduct = getDao(Product.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return DaoProduct;
    }

    public Dao<Customer, Integer> getDaoCustomer() {
        if (null == DaoCustomer) {
            try {
                DaoCustomer = getDao(Customer.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return DaoCustomer;
    }

    public Dao<Order, Integer> getDaoOrder() {
        if (null == DaoOrder) {
            try {
                DaoOrder = getDao(Order.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return DaoOrder;
    }

    public Dao<OrderItem, Integer> getDaoOrderItems() {
        if (null == DaoOrderItem) {
            try {
                DaoOrderItem = getDao(OrderItem.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return DaoOrderItem;
    }

    @Override
    public void close() {
        DaoProduct = null;
        DaoCustomer = null;
        DaoOrder = null;
        DaoOrderItem = null;

        super.close();
    }
}
