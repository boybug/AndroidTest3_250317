package com.example.yadisak.androidtest3.DTO;

/**
 * Created by yadisak on 3/6/2017.
 */

public class _SelectionProperty {

    public  _SelectionProperty(){
    }

    public  _SelectionProperty(Object _id,String _text){
        setId(_id);
        setText(_text);
    }

    private Object id;
    private String text;

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
}
