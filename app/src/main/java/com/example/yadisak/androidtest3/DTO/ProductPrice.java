package com.example.yadisak.androidtest3.DTO;

/**
 * Created by coffee break on 3/18/2017.
 */

public class ProductPrice extends _FirebaseAttribute {
    private int from;
    private float price;
    private int to;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
