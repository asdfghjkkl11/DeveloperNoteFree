package com.example.developernotefree;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Syntax;
/*
 * add activity. you can write new memo from hear.
 * prepare json list in JSONList.
 * user can select language using spinner.
 * if select language, navigator get data newly from json file in asset folder
 * button list can show or hide click main floating action button.
 * made by asdfghjkkl11
 */

public class AddActivity extends AppCompatActivity implements OnItemClick{
    private TextView textView;
    private EditText editText1,editText2;
    private String ID,title,text;
    private Spinner spinner;
    private ArrayList<String> Menu,SMenu;
    private ListView listView;
    private LinearLayout linearLayout;
    private Navigator navigator;
    private SyntaxCode syntax;
    private String[] JSONList={"type","element","CPP"};
    private int fontDP=20;
    private int leftDP=20;
    private int W,H;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Menu = new ArrayList<>();
        SMenu = new ArrayList<>();
        Point p=new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        W=p.x;
        H=p.y;
        //make default json file list
        for(int i=0;i<JSONList.length;i++){
            if(i<2)
                Menu.add(JSONList[i]);
            else
                SMenu.add(JSONList[i]);
        }
        textView=findViewById(R.id.line_text);
        editText1 = findViewById(R.id.title_text);
        editText2 = findViewById(R.id.body_text);
        syntax=new SyntaxCode(this,editText2);
        //make equal between textView and editText
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontDP);
        editText2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontDP);
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            //check code lines every change.
            @Override
            public void afterTextChanged(Editable s) {
                drawLine(s.toString());
                syntax.paint(s);
            }
        });
        Button btn = findViewById(R.id.addFinish);
        Intent intent=getIntent();
        linearLayout = findViewById(R.id.linear);
        navigator=new Navigator(this,Menu);

        //if enter from already exist data, load data from intent
        if(!intent.equals(null)){
            ID=intent.getStringExtra("ID");
            title=intent.getStringExtra("title");
            text=intent.getStringExtra("text");
            editText1.setText(title);
            editText2.setText(text);
        }
        listView = findViewById(R.id.listView);
        final NaviAdapter naviAdapter= new NaviAdapter(Menu,this);
        listView.setAdapter(naviAdapter);
        spinner=findViewById(R.id.spinner);
        final SpinAdapter spinAdapter=new SpinAdapter(this, R.layout.support_simple_spinner_dropdown_item, SMenu);
        spinner.setAdapter(spinAdapter);
        //select program language
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Menu.clear();
                Menu.add(JSONList[0]);
                Menu.add(JSONList[1]);
                Menu.add(spinner.getItemAtPosition(position).toString());
                naviAdapter.clear(Menu);
                navigator.clear(Menu);
                naviAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //control visible
        linearLayout.setVisibility(View.INVISIBLE);
        FloatingActionButton navi= findViewById(R.id.navigator);
        navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayout.getVisibility()==View.VISIBLE)
                    linearLayout.setVisibility(View.INVISIBLE);
                else
                    linearLayout.setVisibility(View.VISIBLE);
            }
        });

        //save data button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ID==null) {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    ID = df.format(c);
                }
                title = editText1.getText().toString();
                text = editText2.getText().toString();
                Intent add = new Intent();
                add.putExtra("ID",ID);
                add.putExtra("title",title);
                add.putExtra("text",text);
                setResult(RESULT_OK,add);
                finish();
            }
        });
        setTheme();
    }

    //callback functions called in navigator adapter
    @Override
    public void onClick(String value) {
        insertText(editText2,value);
    }

    @Override
    public ArrayList<String> push(String value) {
        return navigator.push(value);
    }

    @Override
    public ArrayList<String> pop() {
        return navigator.pop();
    }

    //edit code using button
    public void insertText(EditText view, String text)
    {
        int s = Math.max(view.getSelectionStart(), 0);
        int e = Math.max(view.getSelectionEnd(), 0);
        view.getText().replace(Math.min(s, e), Math.max(s, e), text, 0, text.length());
        if(text.equals("( )")||text.equals("[]")||text.equals(";\n")) {
            view.setSelection(s+1);
        }
        if(text.equals("{\n\n}")) {
            view.setSelection(s+2);
        }
    }
    //check how many lines in code.
    public void drawLine(String str){
        String nstr = "1\n";
        int count = 2;
        String[] lines = str.split("\n");
        leftDP=(int)(Math.log10(lines.length+1)+1)*fontDP*2;
        for (int i = 0; i < lines.length; i++) {
            int len=lines[i].length()*fontDP*2;
            while(len+leftDP>W){
                nstr+='\n';
                len-=W-leftDP;
            }
            nstr += String.valueOf(count) + '\n';
            count++;
        }

        textView.setWidth(leftDP);
        textView.setText(nstr);
    }
    //redraw when screen orientation is changed
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("onConfigurationChanged" , "onConfigurationChanged");
        int t=W;
        W=H;
        H=t;
        try {
            drawLine(editText2.getText().toString());
        }catch (Exception e){
        }
    }
    //init theme
    public void setTheme(){
        textView.setBackgroundColor(ContextCompat.getColor(this,R.color.background));
        textView.setTextColor(ContextCompat.getColor(this,R.color.lines));
        editText1.setBackgroundColor(ContextCompat.getColor(this,R.color.background));
        editText1.setTextColor(ContextCompat.getColor(this,R.color.keyword));
        editText1.setHintTextColor(ContextCompat.getColor(this,R.color.code));
        editText2.setBackgroundColor(ContextCompat.getColor(this,R.color.background));
        editText2.setTextColor(ContextCompat.getColor(this,R.color.code));
        editText2.setHintTextColor(ContextCompat.getColor(this,R.color.code));
    }
}