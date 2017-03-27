package com.example.yadisak.androidtest3.ControllerAdap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yadisak.androidtest3.DTO._CustomMenu;
import com.example.yadisak.androidtest3.R;

import java.util.ArrayList;
import java.util.List;

public class ViewCustomMenu extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;

    List<_CustomMenu> items;

    public ViewCustomMenu(Context context) {

        this.mContext = context;
        inflater = LayoutInflater.from(mContext);

        // Static Create Menu
        items = new ArrayList();
        items.add(new _CustomMenu(1, "CUSTOMER"));
        items.add(new _CustomMenu(2, "PRODUCT"));
        items.add(new _CustomMenu(3, "ORDERS"));
//        items.add(new _CustomMenu(0, "CREATE DATA TEST"));
        //................................
    }

    // Implement BaseAdapter
    @Override
    public _CustomMenu getItem(int position) {
        _CustomMenu ent = items.get(position);
        return ent;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    //............................

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = inflater.inflate(R.layout.__list_row_menu, parent, false);

        _CustomMenu item = this.getItem(position);

        TextView lab_menu = (TextView) view.findViewById(R.id.lab_menu);
        lab_menu.setText(item.getMenu_name());

        if(item.getMenu_id() != 0){
            lab_menu.setBackgroundResource(R.color.colorFish);
        }
        else{
            lab_menu.setBackgroundResource(R.color.colorBlue);
        }

        return view;
    }

}