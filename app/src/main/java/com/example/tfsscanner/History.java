package com.example.tfsscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.tfsscanner.Models.Food;
import com.example.tfsscanner.Utils.LayoutAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;

public class History extends AppCompatActivity {
    private String historyData = "history.txt";
    LinkedList<Food> foodLinkedList;
    RecyclerView recycler;
    LayoutAdapter mAdapter;
    RecyclerView.LayoutManager rLayoutMag;
    Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recycler = findViewById(R.id.recycler_view);
        rLayoutMag = new LinearLayoutManager(this);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadHistory();
    }

    public void loadHistory(){
        try {

            // Mở một luồng đọc file.
            FileInputStream in = this.openFileInput(historyData);

            BufferedReader br= new BufferedReader(new InputStreamReader(in));

            StringBuilder sb= new StringBuilder();
            String s= null;
            while((s= br.readLine())!= null)  {
                sb.append(s);
            }
            //SharedPreferences sharedPreferences = getSharedPreferences("history_item",MODE_PRIVATE);
            Gson gson = new GsonBuilder().setLenient().create();
            String json = sb.toString();
            Type type = new TypeToken<LinkedList<Food>>(){}.getType();
            foodLinkedList =gson.fromJson(json,type);
            if(foodLinkedList == null){
                foodLinkedList = new LinkedList<>();
            }
            recycler.setHasFixedSize(true);
            mAdapter = new LayoutAdapter(foodLinkedList);
            recycler.setLayoutManager(rLayoutMag);
            recycler.setAdapter(mAdapter);

        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
