package com.example.developernotefree;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import com.asd.codeview.CodeView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
 * add activity. you can write new memo from hear.
 * prepare json list in JSONList.
 * user can select language using spinner.
 * if select language, navigator get data newly from json file in asset folder
 * button list can show or hide click main floating action button.
 * made by asdfghjkkl11
 */

public class AddActivity extends AppCompatActivity implements OnItemClick{
    private EditText editText;
    private String ID,title,text;
    private Spinner spinner;
    private ArrayList<String> Menu,SMenu;
    private ListView listView;
    private LinearLayout linearLayout;
    private Navigator navigator;
    private CodeView codeView;
    private String[] JSONList={"type","element","CPP"};
    private float x,y,H;
    private int action;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Point p=new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        H=p.y/2;
        Menu = new ArrayList<>();
        SMenu = new ArrayList<>();
        //make default json file list
        for(int i=0;i<JSONList.length;i++){
            if(i<2)
                Menu.add(JSONList[i]);
            else
                SMenu.add(JSONList[i]);
        }
        editText = findViewById(R.id.title_text);
        codeView=findViewById(R.id.codeview);
        final Button btn = findViewById(R.id.addFinish);
        Intent intent=getIntent();
        linearLayout = findViewById(R.id.linear);
        navigator=new Navigator(this,Menu);

        //if enter from already exist data, load data from intent
        if(!intent.equals(null)){
            ID=intent.getStringExtra("ID");
            title=intent.getStringExtra("title");
            text=intent.getStringExtra("text");
            editText.setText(title);
            codeView.setText(text);
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
                linearLayout.setY(y);
                linearLayout.setX(x);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //control visible
        linearLayout.setVisibility(View.INVISIBLE);
        FloatingActionButton navi= findViewById(R.id.navigator);
        navi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        x = view.getX() - event.getRawX();
                        y = view.getY() - event.getRawY();
                        action = MotionEvent.ACTION_DOWN;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.setY(event.getRawY() + y);
                        view.setX(event.getRawX() + x);
                        if(event.getRawY() + y>H)
                            linearLayout.setY(event.getRawY() + y-linearLayout.getHeight());
                        else
                            linearLayout.setY(event.getRawY() + y);
                        linearLayout.setX(event.getRawX() + x-20);
                        action = MotionEvent.ACTION_MOVE;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (action == MotionEvent.ACTION_DOWN){
                            if(linearLayout.getVisibility()==View.VISIBLE)
                                linearLayout.setVisibility(View.INVISIBLE);
                            else
                                linearLayout.setVisibility(View.VISIBLE);
                        }
                        if(event.getRawY() + y>H)
                            linearLayout.setY(event.getRawY() + y-linearLayout.getHeight());
                        else
                            linearLayout.setY(event.getRawY() + y);
                        linearLayout.setX(event.getRawX() + x-20);
                        break;
                    default:
                        return false;
                }
                return true;
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
                title = editText.getText().toString();
                text = codeView.getText();
                Intent add = new Intent();
                add.putExtra("ID",ID);
                add.putExtra("title",title);
                add.putExtra("text",text);
                setResult(RESULT_OK,add);
                finish();
            }
        });
        codeView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(bottom<oldBottom)
                    btn.setVisibility(View.GONE);
                else if(bottom>oldBottom)
                    btn.setVisibility(View.VISIBLE);
            }
        });
        setTheme();
    }
    //callback functions called in navigator adapter
    @Override
    public void onClick(String value) {
        codeView.insertText(value);
    }

    @Override
    public ArrayList<String> push(String value) {
        return navigator.push(value);
    }

    @Override
    public ArrayList<String> pop() {
        return navigator.pop();
    }

    //init theme
    public void setTheme(){
        editText.setBackgroundColor(ContextCompat.getColor(this,R.color.background));
        editText.setTextColor(ContextCompat.getColor(this,R.color.keyword));
        editText.setHintTextColor(ContextCompat.getColor(this,R.color.code));
        codeView.setTheme();
    }
}