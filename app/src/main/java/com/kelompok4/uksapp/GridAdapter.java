package com.kelompok4.uksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private String[] daysOfWeek;

    public GridAdapter(Context context, String[] daysOfWeek) {
        this.context = context;
        this.daysOfWeek = daysOfWeek;
    }

    @Override
    public int getCount() {
        return daysOfWeek.length;
    }

    @Override
    public Object getItem(int position) {
        return daysOfWeek[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_day, parent, false);
        }

        TextView dayTextView = convertView.findViewById(R.id.textViewDay);
        dayTextView.setText(daysOfWeek[position]);

        return convertView;
    }
}
