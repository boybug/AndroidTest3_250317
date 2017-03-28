package com.example.yadisak.androidtest3.DTO;

import android.support.annotation.DrawableRes;

/**
 * Created by yadisak on 3/7/2017.
 */

public class _CustomMenu {

    public _CustomMenu(int menu_id,String menu_name,@DrawableRes int menu_image,@DrawableRes int menu_color){
        setMenu_name(menu_name);
        setMenu_id(menu_id);
        setMenu_image(menu_image);
        setMenu_color(menu_color);
    }


    private int menu_id;
    private String menu_name;
    private @DrawableRes int menu_image;
    private @DrawableRes int menu_color;

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

    public int getMenu_image() {
        return menu_image;
    }

    public void setMenu_image(int menu_image) {
        this.menu_image = menu_image;
    }

    public int getMenu_color() {
        return menu_color;
    }

    public void setMenu_color(int menu_color) {
        this.menu_color = menu_color;
    }
}
