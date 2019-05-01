package com.example.developernotefree;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
/*
* main activity. first screen when application on.
* get memo data from realm and show memo using recycler view.
* made by asdfghjkkl11
 */
public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private RecyclerView rcv;
    private RcvAdapter rcvAdapter;
    private Memo memo_Main;
    public List<Memo> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcv = findViewById(R.id.rcvMain);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        rcv.setLayoutManager(linearLayoutManager);

        //get data from realm
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        RealmResults<Memo> realmResults = realm.where(Memo.class).findAllAsync();

        //show list of memo using recycler view
        for(Memo memo : realmResults) {
            list.add(new Memo(memo));
            rcvAdapter = new RcvAdapter(MainActivity.this,list);
            rcv.setAdapter(rcvAdapter);
        }

        //write new memo click floating action button
        FloatingActionButton button = findViewById(R.id.floating);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    //if get result from addactivity, update realm data.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == RESULT_OK) {
            String ID = data.getStringExtra("ID");
            String title = data.getStringExtra("title");
            String text = data.getStringExtra("text");
            Toast.makeText(this,ID + "," + title,Toast.LENGTH_SHORT).show();
            try {
                Memo realmResult = realm.where(Memo.class)
                        .equalTo("ID", ID)
                        .findFirstAsync();
                realm.beginTransaction();
                realmResult.setTitle(title);
                realmResult.setText(text);
                realm.commitTransaction();
                for(Memo m : list){
                    if(m.getID().equals(ID)){
                        m.setTitle(title);
                        m.setText(text);
                        break;
                    }
                }
            }catch (Exception e){
                //realm.beginTransaction();
                memo_Main = realm.createObject(Memo.class);
                memo_Main.setID(ID);
                memo_Main.setTitle(title);
                memo_Main.setText(text);
                realm.commitTransaction();
                list.add(new Memo(ID, title, text));
                rcvAdapter = new RcvAdapter(MainActivity.this, list);
                rcv.setAdapter(rcvAdapter);
            }
            rcvAdapter.notifyDataSetChanged();
        }
    }
}