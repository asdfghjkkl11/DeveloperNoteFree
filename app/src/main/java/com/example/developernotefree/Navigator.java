package com.example.developernotefree;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;
/*
 * navigator. control json files
 * get file list from addactivity and read json files from asset folder.
 * data saved by keys and values.
 * save button click log using stack.
 * made by asdfghjkkl11
 */
public class Navigator {
    private AssetManager assetManager;
    private InputStream is;
    private Stack<ArrayList<String>> key;
    private Stack<ArrayList<String>> value;
    private ArrayList<String> keylist;
    private ArrayList<String> valuelist;
    //str must be json file name in asset folder. type.json --x type --o
    public Navigator(Context context, ArrayList<String> str) {
        key=new Stack<>();
        value=new Stack<>();
        valuelist=new ArrayList<>();
        keylist = (ArrayList<String>)str.clone();
        Log.d(TAG, "Navigator: "+str);
        assetManager = context.getAssets();
        try {
            for (int i=0;i<str.size();i++) {
                is = assetManager.open(str.get(i)+".json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String json =new String(buffer, "UTF-8");
                JSONObject J=new JSONObject(json);
                valuelist.add(J.getString(str.get(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        key.push(keylist);
        value.push(valuelist);
    }
    public ArrayList<String> push(String str){
        Log.d(TAG, "push: " + str);
        String S = value.peek().get(key.peek().indexOf(str));
        try {
            JSONObject data = new JSONObject(S);
            Iterator i = data.keys();
            ArrayList<String> klist = new ArrayList<>();
            ArrayList<String> vlist = new ArrayList<>();
            while (i.hasNext()) {
                String s = i.next().toString();
                klist.add(s);
                vlist.add(data.getString(s));
            }
            //has no key == leaf value
            if(klist.size()==0)
                throw new JSONException("kilst size()==0");
            key.push(klist);
            value.push(vlist);
            Log.d(TAG, "push: " + klist.toString());
            Log.d(TAG, "push: " + vlist.toString());
            return klist;
        }catch (JSONException e) {
            e.printStackTrace();
            ArrayList<String> leaf = new ArrayList<>();
            leaf.add(S);
            Log.d(TAG, "push: " + e);
            return leaf;
        }
    }
    public ArrayList<String> pop(){
        key.pop();
        value.pop();
        return key.peek();
    }
    //reset keys and values when language is changed.
    public void clear(ArrayList<String> str){
        key.clear();
        value.clear();
        keylist.clear();
        valuelist.clear();
        key=new Stack<>();
        value=new Stack<>();
        valuelist=new ArrayList<>();
        keylist = (ArrayList<String>)str.clone();
        Log.d(TAG, "Navigator: "+str);
        try {
            for (int i=0;i<str.size();i++) {
                is = assetManager.open(str.get(i)+".json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String json =new String(buffer, "UTF-8");
                JSONObject J=new JSONObject(json);
                valuelist.add(J.getString(str.get(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();

        }
        key.push(keylist);
        value.push(valuelist);
    }
}
