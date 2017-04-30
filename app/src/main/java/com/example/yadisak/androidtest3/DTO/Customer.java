package com.example.yadisak.androidtest3.DTO;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

@DatabaseTable(tableName = "tbm_customer")
@SuppressWarnings("serial")
public class Customer extends _FirebaseAttribute implements Serializable {

    public Customer() {

    }

    @DatabaseField(id = true)
    private String code;
    @DatabaseField
    private String name;
    @DatabaseField
    private int point;

    private String addr;
    private String tel;
    private String ship;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }
}
