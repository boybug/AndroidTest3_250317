package com.example.yadisak.androidtest3.DTO;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

@DatabaseTable(tableName = "tbm_user")
@SuppressWarnings("serial")
public class User extends _FirebaseAttribute implements Serializable {

    public User() {

    }

    @DatabaseField(id = true)
    private String username;
    @DatabaseField
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
