package com.example.yadisak.androidtest3.DTO;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Branch extends _FirebaseAttribute implements Serializable {

    public Branch() {

    }

    @DatabaseField(id = true)
    private String code;
    @DatabaseField
    private String name;
    @DatabaseField
    private String taxid;
    private int id;
    private int number;

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


    public String getTaxid() {
        return taxid;
    }

    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
