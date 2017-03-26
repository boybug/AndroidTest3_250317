package com.example.yadisak.androidtest3._Interface;

import java.util.List;

public interface ICRUDAdap<T> {

    void addItem(T _item,ICRUDResult result);
    void updateItem(T _item,ICRUDResult result);
    void removeItem(T _item,ICRUDResult result);
    List<T> getAllItems();
    T getItem(Object key);
    int getCount();
}
