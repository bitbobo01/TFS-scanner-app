package com.example.tfsscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfsscanner.Models.Food;
import com.example.tfsscanner.Utils.Controller;
import com.example.tfsscanner.Utils.ExpandableAdapter;
import com.example.tfsscanner.Utils.HistoryFile;
import com.example.tfsscanner.Utils.LayoutAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class History extends AppCompatActivity {
    LinkedList<Food> foodLinkedList;
    private Controller controller = new Controller();
    RecyclerView recycler;
    ExpandableListView expandableListView;
    ExpandableAdapter expandableAdapter;
    LayoutAdapter mAdapter;
    Dialog dialog;
    HistoryFile historyFile = new HistoryFile(this);
    RecyclerView.LayoutManager rLayoutMag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recycler = findViewById(R.id.recycler_view);
        rLayoutMag = new LinearLayoutManager(this);
        loadHistory();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_popup);
    }

    public void loadHistory(){
        try {

            foodLinkedList = new LinkedList<>();
            foodLinkedList = historyFile.loadHistory();
            if(foodLinkedList == null){
                foodLinkedList = new LinkedList<>();
            }
            recycler.setHasFixedSize(true);
            mAdapter = new LayoutAdapter(foodLinkedList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recycler.setLayoutManager(linearLayoutManager);
            recycler.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new LayoutAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Food food = foodLinkedList.get(position);
                    Intent intent = new Intent(History.this,Details.class);
                    intent.putExtra("food",(Serializable) food);
                    startActivity(intent);
                }
            });
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
    private void loadPopupView(Food food){
        List<String> dataHeaders = controller.getHeaders();

        expandableListView =(ExpandableListView) dialog.findViewById(R.id.expanded_list_history);
        expandableAdapter = new ExpandableAdapter(History.this,dataHeaders,controller.getDataChild(food));
        expandableListView.setAdapter(expandableAdapter);
        TextView foodIdTxt,breedTxt,categoryTxt;

        foodIdTxt = dialog.findViewById(R.id.fooid_text);
        breedTxt = dialog.findViewById(R.id.fbreed_text);
        categoryTxt = dialog.findViewById(R.id.cate_text);

        foodIdTxt.setText("Mã số: "+ String.valueOf(food.getFoodId()));
        breedTxt.setText("Giống: "+food.getBreed());
        categoryTxt.setText("Loại: "+food.getCategory());
        showPopUp();
    }

    private void showPopUp(){
        TextView closeTxt;
        closeTxt =dialog.findViewById(R.id.close_txt);
        closeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
