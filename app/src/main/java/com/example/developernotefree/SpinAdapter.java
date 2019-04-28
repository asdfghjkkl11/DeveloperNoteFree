package com.example.developernotefree;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
/*
 * spinadapter. adapter of spinner.
 * show language lists.
 * made by asdfghjkkl11
 */
public class SpinAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> items;

    public SpinAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(items.get(position));
        tv.setTextColor(ContextCompat.getColor(context,R.color.buttontext));
        tv.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonbg));
        return convertView;
    }

   @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    android.R.layout.simple_spinner_item, parent, false);
        }

        TextView tv = (TextView) convertView
                .findViewById(android.R.id.text1);
        tv.setText(items.get(position));
        tv.setTextColor(ContextCompat.getColor(context,R.color.buttontext));
        tv.setBackgroundColor(ContextCompat.getColor(context,R.color.buttonbg));
        return convertView;
    }
}