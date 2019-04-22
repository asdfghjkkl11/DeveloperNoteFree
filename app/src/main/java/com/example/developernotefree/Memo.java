package com.example.developernotefree;

import java.text.SimpleDateFormat;
import java.util.Date;
import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Memo extends RealmObject{

    @Override
    public String toString() {
        return "Memo{" +
                "ID='"+ID+'\'' +
                "title='"+title+'\'' +
                "text='" + text + '\'' +
                '}';
    }

    @Required
    private String ID;
    private String title;
    private String text;
    public Memo() {
        Date dt = new Date();
        SimpleDateFormat full_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.ID=full_sdf.format(dt);
        this.title = "무제";
        this.text = "아무값도 없습니다.";
    }
    public Memo(String title,String text) {
        Date dt = new Date();
        SimpleDateFormat full_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.ID=full_sdf.format(dt);
        this.title =title;
        this.text = text;
    }
    public Memo(String ID,String title,String text) {
        this.ID=ID;
        this.title =title;
        this.text = text;
    }
    public Memo(Memo memo) {
        this.ID=memo.ID;
        this.title = memo.title;
        this.text = memo.text;
    }
    public String getID() {
        return ID;
    }
    public String getTitle() {
        return title;
    }
    public String getText() {
        return text;
    }
    public void setID(String ID) { this.ID = ID; }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setText(String text) {
        this.text = text;
    }
}