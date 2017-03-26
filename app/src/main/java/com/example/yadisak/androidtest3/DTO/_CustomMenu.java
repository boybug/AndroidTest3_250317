package com.example.yadisak.androidtest3.DTO;

/**
 * Created by yadisak on 3/7/2017.
 */

public class _CustomMenu {

    public _CustomMenu(int menu_id,String menu_name){
        setMenu_name(menu_name);
        setMenu_id(menu_id);
    }


    private int menu_id;
    private String menu_name;

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public int getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(int menu_id) {
        this.menu_id = menu_id;
    }
}
