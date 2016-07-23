package com.example.junwoo.androidstudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] setting = {
                "1", "2", "3", "4"
        };

        ListView list = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, setting);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(getApplicationContext(),setting[arg2],Toast.LENGTH_SHORT).show();
            }
        });
    }
}


