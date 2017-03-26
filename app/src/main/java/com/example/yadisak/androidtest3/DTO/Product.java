package com.example.yadisak.androidtest3.DTO;

import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "tbm_product")
@SuppressWarnings("serial")
public class Product extends _FirebaseAttribute implements Serializable {

    public Product(){

    }

    private String code;

    private String name;

    private int stock;

    private int point;

    private int focpoint;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getFocpoint() {
        return focpoint;
    }

    public void setFocpoint(int focpoint) {
        this.focpoint = focpoint;
    }
}
