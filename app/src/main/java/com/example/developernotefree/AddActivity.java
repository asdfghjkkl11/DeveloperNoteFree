package com.example.developernotefree;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
public class AddActivity extends AppCompatActivity implements OnItemClick{
    private EditText editText1,editText2;
    private String ID,title,text;
    private Spinner spinner;
    private ArrayList<String> Menu,SMenu;
    private ListView listView;
    private LinearLayout linearLayout;
    private Navigator navigator;
    private String[] JSONList={"type","element","CPP"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Menu = new ArrayList<>();
        SMenu = new ArrayList<>();
        for(int i=0;i<JSONList.length;i++){
            if(i<2)
                Menu.add(JSONList[i]);
            else
                SMenu.add(JSONList[i]);
        }
        editText1 = findViewById(R.id.title_text);
        editText2 = findViewById(R.id.body_text);
        Button btn = findViewById(R.id.addFinish);
        Intent intent=getIntent();
        linearLayout = findViewById(R.id.linear);
        navigator=new Navigator(this,Menu);
        if(intent!=null){
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
        final ArrayAdapter<String> arrAdapt=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, SMenu);
        spinner.setAdapter(arrAdapt);
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
    }

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

    public void insertText(EditText view, String text)
    {
        // Math.max 는 에초에 커서가 잡혀있지않을때를 대비해서 넣음.
        int s = Math.max(view.getSelectionStart(), 0);
        int e = Math.max(view.getSelectionEnd(), 0);
        // 역으로 선택된 경우 s가 e보다 클 수 있다 때문에 이렇게 Math.min Math.max를 쓴다.
        view.getText().replace(Math.min(s, e), Math.max(s, e), text, 0, text.length());
        if(text.equals("( )")||text.equals("[]")||text.equals(";\n")) {
            view.setSelection(s+1);
        }
        if(text.equals("{\n\n}")) {
            view.setSelection(s+2);
        }
    }
}