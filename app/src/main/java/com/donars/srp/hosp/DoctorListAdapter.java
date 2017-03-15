package com.donars.srp.hosp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.donars.srp.hosp.model.DoctorModel;
import com.donars.srp.hosp.model.RequestModel;

import java.util.ArrayList;

import static com.donars.srp.hosp.FragmentRequest.mSelectedItem;

/**
 * Created by Admin1 on 10/16/2016.
 */

public class DoctorListAdapter extends BaseAdapter{


    ArrayList<DoctorModel> myList;
    Context mCtx;
    LayoutInflater inflater;

    DoctorListAdapter(Context ctx, ArrayList<DoctorModel> myList) {
        this.myList = myList;
        this.mCtx = ctx;
        inflater=LayoutInflater.from(ctx);
    }
    private class ViewHolder{
        TextView tv1,tv2;
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
            v = inflater.inflate(R.layout.listdoctor,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv1 = (TextView) v.findViewById(R.id.item);
            viewHolder.tv2 = (TextView) v.findViewById(R.id.item1);
            v.setTag(viewHolder);
        }
        else {
          viewHolder=(ViewHolder) v.getTag();
        }
        viewHolder.tv1.setText(myList.get(i).getName());
        viewHolder.tv2.setText(myList.get(i).getSpecial());
        if (i == mSelectedItem) {
            // set your color
            v.setBackgroundColor(v.getResources().getColor(R.color.md_red_200));
        }
        else
        {
            v.setBackgroundColor(v.getResources().getColor(R.color.accent));
        }

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
