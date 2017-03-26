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
    private int taxid;

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


    public int getTaxid() {
        return taxid;
    }

    public void setTaxid(int taxid) {
        this.taxid = taxid;
    }
}
