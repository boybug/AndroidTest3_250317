package com.example.yadisak.androidtest3.DTO;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "tbt_order")
@SuppressWarnings("serial")
public class Order extends _FirebaseAttribute implements Serializable {

    public Order() {
        // ORMLite needs a no-arg constructor
    }

    @DatabaseField(id = true)
    private String no;
    @DatabaseField
    private Date date;
    @DatabaseField
    private String cus_code;
    private String cus_name;

//    private List<OrderItem> items;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCus_code() {
        return cus_code;
    }

    public void setCus_code(String cus_code) {
        this.cus_code = cus_code;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }


//    public List<OrderItem> getItems() {
//        return items;
//    }
//
//    public void setItems(List<OrderItem> items) {
//        this.items = items;
//    }
}
