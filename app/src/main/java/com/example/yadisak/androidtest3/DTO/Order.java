package com.example.yadisak.androidtest3.DTO;

import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "tbt_order")
@SuppressWarnings("serial")
public class Order extends _FirebaseAttribute implements Serializable {

    public Order() {
        // ORMLite needs a no-arg constructor
    }


    private String no;
    private Date date;
    private String cus_code;
    private String cus_name;
    private String ship;
    private String User;
    private String Stat;
    private float total;
    private String pay;
    private String remark;

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


    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getStat() {
        return Stat;
    }

    public void setStat(String stat) {
        Stat = stat;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
