package com.donars.srp.bloodbank;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.donars.srp.bloodbank.fetcher.NotificationFetcher;
import com.donars.srp.bloodbank.model.BloodModel;
import com.donars.srp.bloodbank.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin1 on 10/15/2016.
 */

public class NotificationListAdapter extends BaseAdapter {

    ArrayList<NotificationModel> myList;
    Context mCtx;
    LayoutInflater inflater;

    NotificationListAdapter(Context ctx,ArrayList<NotificationModel> myList) {
        this.myList = myList;
        this.mCtx = ctx;
        inflater=LayoutInflater.from(ctx);
    }
    private class ViewHolder{
        TextView tv,tv1,tv2,tv3,tv4,tv5;
    }

    @Override
    public int getCount() {
        return myList==null?0:myList.size();
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
        if(v==null) {
            v = inflater.inflate(R.layout.notificationitem,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) v.findViewById(R.id.item);
            viewHolder.tv1 = (TextView) v.findViewById(R.id.item1);
            viewHolder.tv2 = (TextView) v.findViewById(R.id.item2);
            viewHolder.tv3 = (TextView) v.findViewById(R.id.item3);
            viewHolder.tv4 = (TextView) v.findViewById(R.id.item4);
            viewHolder.tv5 = (TextView) v.findViewById(R.id.item5);
            v.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder) v.getTag();
        }


        viewHolder.tv.setText(myList.get(i).getDonor_name());
        viewHolder.tv1.setText(myList.get(i).getPatient_name());
        viewHolder.tv2.setText(myList.get(i).getPatient_blood_group());
        viewHolder.tv3.setText(myList.get(i).getDonor_phone());
        viewHolder.tv4.setText(myList.get(i).getTime_of_submit());
        viewHolder.tv5.setText(myList.get(i).getTime_of_request());
        if (i == FragmentNotification.mSelectedItem) {
            // set your color
            v.setBackgroundColor(v.getResources().getColor(R.color.md_green_100));
        }
        else
        {
            v.setBackgroundColor(v.getResources().getColor(R.color.accent));
        }
        return v;
    }

    /*
       TextView tv = (TextView) v.findViewById(R.id.item);
        TextView tv1 = (TextView) v.findViewById(R.id.item1);
        TextView tv2 = (TextView) v.findViewById(R.id.item2);
        TextView tv3 = (TextView) v.findViewById(R.id.item3);
        TextView tv4 = (TextView) v.findViewById(R.id.item4);


     */

}
