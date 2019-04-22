package com.example.developernotefree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import java.util.ArrayList;

public class NaviAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    private ArrayList<String> Keys,currunt,getCurrunt;
    private OnItemClick mCallback;
    private int count=0;
    public NaviAdapter(ArrayList<String> k,OnItemClick onItemClick){
        Keys=(ArrayList<String>)k.clone();
        currunt=(ArrayList<String>)k.clone();
        getCurrunt=(ArrayList<String>)k.clone();
        mCallback=onItemClick;
        count=k.size();
    }
    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.index, parent, false);
        }
        Button btn=convertView.findViewById(R.id.Data);
        btn.setText(currunt.get(position));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currunt.get(position).equals("back"))
                    getCurrunt=mCallback.pop();
                else
                    getCurrunt=mCallback.push(currunt.get(position));
                if(getCurrunt.size()==1) {
                    mCallback.onClick(getCurrunt.get(0));
                }else{
                    if((!getCurrunt.equals(Keys))&&!getCurrunt.contains("back"))
                        getCurrunt.add("back");
                    currunt=getCurrunt;
                    count=currunt.size();
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    public void clear(ArrayList<String> k){
        Keys.clear();
        currunt.clear();
        getCurrunt.clear();
        Keys=(ArrayList<String>)k.clone();
        currunt=(ArrayList<String>)k.clone();
        getCurrunt=(ArrayList<String>)k.clone();
        count=k.size();
    }
}
