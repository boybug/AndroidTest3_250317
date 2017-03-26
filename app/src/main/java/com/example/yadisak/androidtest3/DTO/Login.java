package com.example.yadisak.androidtest3.DTO;

import java.io.Serializable;

public class Login implements Serializable {

    public Login() {

    }


    private String name;

    private String email;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
