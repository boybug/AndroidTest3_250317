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

    private int gross_wgt;

    private String bgcolor;

    private String topprice;

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


    public int getGross_wgt() {
        return gross_wgt;
    }

    public void setGross_wgt(int gross_wgt) {
        this.gross_wgt = gross_wgt;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getTopprice() {
        return topprice;
    }

    public void setTopprice(String topprice) {
        this.topprice = topprice;
    }
}
