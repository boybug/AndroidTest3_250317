package com.example.yadisak.androidtest3.CollectionAdap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yadisak.androidtest3.DTO._SelectionProperty;
import com.example.yadisak.androidtest3.R;

import java.util.List;

public class _SelectionAdap extends ArrayAdapter<_SelectionProperty> {

    LayoutInflater flater;

    public _SelectionAdap(Context context, int resouceId, int textViewResourceId, List<_SelectionProperty> list) {
        super(context, resouceId, textViewResourceId, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position);
    }

    private View rowview(View convertView, int position) {

        _SelectionProperty rowItem = getItem(position);

        viewHolder holder;
        View rowview = convertView;
        if (rowview == null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout._spinner_item_custom, null, false);
            holder.item_title = (TextView) rowview.findViewById(R.id.lab_spin_item_custom);
            rowview.setTag(holder);
        } else {
            holder = (viewHolder) rowview.getTag();
        }

        holder.item_title.setText(rowItem.getText());

        return rowview;
    }

    private class viewHolder {
        TextView item_title;
    }

}