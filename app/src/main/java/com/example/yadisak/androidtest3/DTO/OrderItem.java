package com.example.yadisak.androidtest3.DTO;


import com.google.firebase.database.Exclude;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "tbt_order_detail")
@SuppressWarnings("serial")
public class OrderItem extends _FirebaseAttribute implements Serializable {

    public OrderItem() {
//            priceList = new ArrayList<>();
    }

    @DatabaseField(generatedId = true)
    private int no;
    @DatabaseField
    private float price;
    @DatabaseField
    private int qty;
    @DatabaseField
    private float amt;
    @DatabaseField
    private float discount;
    @DatabaseField
    private String pro_code;
    @DatabaseField
    private String pro_name;
    @DatabaseField
    private String pro_key_id;
    @DatabaseField
    private int point;
    @DatabaseField
    private String foc_flag;
    @DatabaseField
    private String pro_code_foc_flag;

    @Exclude
    private int delta;

    @Exclude
    private String cus_code;
//    @Exclude
//    private ArrayList<ProductPrice> priceList;


    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getAmt() {
        return amt;
    }

    public void setAmt(float amt) {
        this.amt = amt;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getPro_code() {
        return pro_code;
    }

    public void setPro_code(String pro_code) {
        this.pro_code = pro_code;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }


    public String getPro_key_id() {
        return pro_key_id;
    }

    public void setPro_key_id(String pro_key_id) {
        this.pro_key_id = pro_key_id;
    }

    @Exclude
    public int getDelta() {
        return delta;
    }
    @Exclude
    public void setDelta(int delta) {
        this.delta = delta;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getFoc_flag() {
        return foc_flag;
    }

    public void setFoc_flag(String foc_flag) {
        this.foc_flag = foc_flag;
    }

    public String getPro_code_foc_flag() {
        return pro_code_foc_flag;
    }

    public void setPro_code_foc_flag(String pro_code_foc_flag) {
        this.pro_code_foc_flag = pro_code_foc_flag;
    }
    @Exclude
    public String getCus_code() {
        return cus_code;
    }
    @Exclude
    public void setCus_code(String cus_code) {
        this.cus_code = cus_code;
    }

//    @Exclude
//    public ArrayList<ProductPrice> getPriceList() {
//        return priceList;
//    }
//    @Exclude
//    public void addPriceList(ProductPrice price) {
//        priceList.add(price);
//    }

//    @Exclude
//    public List<ProductPrice> getPriceList() {
//        return priceList;
//    }

//    @Exclude
//    public void setPriceList(List<ProductPrice> priceList) {
//        this.priceList = priceList;
//    }
//
//    @Exclude
//    public float getPrice(int orderQty) {
//        float price = 0;
//        for (int i = 0; i < priceList.size(); i++) {
//            ProductPrice p = priceList.get(i);
//            if (orderQty >= p.getFrom() && orderQty <= p.getTo()) {
//                price = p.getPrice();
//                break;
//            }
//        }
//        return price;
//    }
}
