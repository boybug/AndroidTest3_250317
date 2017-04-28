package com.example.yadisak.androidtest3.ControllerAdap;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.example.yadisak.androidtest3.DTO.Product;
import com.example.yadisak.androidtest3.Globaldata;
import com.example.yadisak.androidtest3.R;
import com.example.yadisak.androidtest3._FBProvider.FirebaseCustomAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ViewProductOrdPick {

    DatabaseReference refDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refTB ;

    FirebaseCustomAdapter<Product> adap;
    String searchString = "";

    public FirebaseCustomAdapter<Product> getAdapter() {
        return adap;
    }

    public ViewProductOrdPick(Activity activity) {

        refTB = refDB.child("product_" + Globaldata.Branch.getId());

        adap = new FirebaseCustomAdapter<Product>(activity
                , Product.class, R.layout._listrow_item_order_pick, refTB.orderByChild("rownum")) {
            @Override
            protected void populateView(View v, Product model) {

                    v.setBackgroundColor(Color.parseColor(model.getBgcolor()));

                    TextView lab_pro_name = (TextView) v.findViewById(R.id.lab_pro_name);
                    lab_pro_name.setText(model.getName());

                    TextView lab_pro_qty = (TextView) v.findViewById(R.id.lab_pro_qty);
                    lab_pro_qty.setText("[" + String.valueOf(model.getStock()) + "]");

                    TextView lab_pro_price = (TextView) v.findViewById(R.id.lab_pro_price);
                    lab_pro_price.setText(String.valueOf(model.getTopprice()) + " ฿");

                    if(model.getStep().equals("Y")) {
                        lab_pro_price.setTextColor(Color.RED);
                    }else{
                        lab_pro_price.setTextColor(Color.BLACK);
                    }

                    if(String.valueOf(model.getStock()).equals("0")){
                        lab_pro_name.setPaintFlags(lab_pro_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    else { lab_pro_name.setPaintFlags(lab_pro_name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);}

                if(searchString != null)
                {
                    setspantext(searchString,model.getName(),lab_pro_name);
                }

            }

            @Override
            protected List<Product> modifyArrayAdapter(List<Product> models) {
                return models;
            }
        };
    }

    public Product getItem(int id) {

        Product ent = (Product) adap.getItem(id);
        return ent;
    }

    public void filter(String text) {
        this.searchString = text.toLowerCase(Locale.getDefault());
        if(adap.getAllItemsold().size() == 0){
            adap.setOldmodels(adap.getAllItems());
        }

        text = text.toLowerCase(Locale.getDefault());
        List<Product> tempProduct = new ArrayList<>();
        tempProduct.clear();
        if (text.length() == 0)
        {
            adap.setModels(adap.getAllItemsold());
        }
        else{
            for (Product p : adap.getAllItemsold()) {
                if (p.getName().toLowerCase(Locale.getDefault()).contains(text))
                    tempProduct.add(p);
            }
            adap.setModels(tempProduct);
        }
        adap.notifyChanged();
    }

    public void setspantext(String search, String name, TextView textView )
    {
        int firstIndex = name.toLowerCase(Locale.getDefault()).indexOf(search,0);
        Spannable span = new SpannableString(name);
        for(int i = 0; i < name.length() && firstIndex != -1; i = firstIndex +1){
            firstIndex = name.toLowerCase(Locale.getDefault()).indexOf(search, i);
            if(firstIndex == -1)
                break;
            else{
                span.setSpan(new BackgroundColorSpan(0xFFFFFF00),firstIndex,firstIndex + search.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(span,TextView.BufferType.SPANNABLE);
            }

        }

    }
}
