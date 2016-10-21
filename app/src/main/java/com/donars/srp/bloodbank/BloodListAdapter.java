package com.donars.srp.bloodbank;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.donars.srp.bloodbank.model.BloodModel;

import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
/**
 * Created by Lakshman sai on 13-10-2016.
 */

public class BloodListAdapter extends BaseAdapter {

    List<BloodModel> myList;
    Context mCtx;
    LayoutInflater inflater;

    BloodListAdapter(Context ctx, List<BloodModel> myList) {
        this.myList = myList;
        this.mCtx = ctx;
        inflater = LayoutInflater.from(ctx);
    }

    private class ViewHolder {
        TextView tv, tv1, tv2, tv3, tv4;
        AppCompatImageView im1;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int i, View v, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (v == null) {
            v = inflater.inflate(R.layout.listitem, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) v.findViewById(R.id.item);
            viewHolder.tv1 = (TextView) v.findViewById(R.id.item1);
            viewHolder.im1 = (AppCompatImageView) v.findViewById(R.id.image_blood);
            //         viewHolder.tv2 = (TextView) v.findViewById(R.id.item2);
            viewHolder.tv3 = (TextView) v.findViewById(R.id.item3);
            //    viewHolder.tv4 = (TextView) v.findViewById(R.id.item4);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.tv.setText(myList.get(i).getHop_name());
        viewHolder.tv1.setText(myList.get(i).getAddress());
        //  viewHolder.tv2.setText(myList.get(i).getBlood_group());
        viewHolder.tv3.setText(Integer.toString(myList.get(i).getQuantity()) + "(units)");
        viewHolder.im1.setBackgroundResource(getImage(myList.get(i).getBlood_group()));

        //   viewHolder.tv4.setText(myList.get(i).getName());
        return v;
    }

    private static int getImage(String type) {
        switch (type) {
            case "A+ve":
                return R.drawable.ap;
            case "A-ve":
                return R.drawable.ane;
            case "AB+ve":
                return R.drawable.abp;
            case "AB-ve":
                return R.drawable.abne;
            case "O+ve":
                return R.drawable.op;
            case "O-ve":
                return R.drawable.one;
            case "B+ve":
                return R.drawable.bpe;
            case "B-ve":
                return R.drawable.bne;
        }
        return R.drawable.ap;
    }
}
