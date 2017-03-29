package com.example.yadisak.androidtest3.DTO;

/**
 * Created by yadisak on 3/6/2017.
 */

public class _SelectionProperty {

    public  _SelectionProperty(){
    }

    public  _SelectionProperty(Object _id,String _text,Object _udf1){
        setId(_id);
        setText(_text);
        setUdf1(_udf1);
    }

    private Object id;
    private String text;
    private Object udf1;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getUdf1() {
        return udf1;
    }

    public void setUdf1(Object udf1) {
        this.udf1 = udf1;
    }
}
