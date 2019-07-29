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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfsscanner.Models.Food;
import com.example.tfsscanner.Utils.ExpandableAdapter;
import com.example.tfsscanner.Utils.LayoutAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class History extends AppCompatActivity {
    LinkedList<Food> foodLinkedList;
    RecyclerView recycler;
    ExpandableListView expandableListView;
    ExpandableAdapter expandableAdapter;
    LayoutAdapter mAdapter;
    Dialog dialog;
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

            Intent intent =getIntent();
            foodLinkedList = new LinkedList<>((List<Food>) intent.getSerializableExtra("history-list"));
            if(foodLinkedList == null){
                foodLinkedList = new LinkedList<>();
            }
            recycler.setHasFixedSize(true);
            mAdapter = new LayoutAdapter(foodLinkedList);
            recycler.setLayoutManager(rLayoutMag);
            recycler.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new LayoutAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Food food = foodLinkedList.get(position);
                    loadPopupView(food);
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
        List<String> dataHeaders = new ArrayList<>();
        //Farm Header
        dataHeaders.add("Thông tin Trang Trại");
        //Farm Details
        List<String> farmInfo = new ArrayList<>();
        if (food.getFarm()!=null){
            farmInfo.add("Tên Trại Nuôi/Trồng: "+checkNull(food.getFarm().getName()));
            farmInfo.add("Địa chỉ Nông Trại: "+checkNull(food.getFarm().getAddress()));
            farmInfo.add("Thức ăn Trại Nuôi/Trồng sử dụng: "+getListString(food.getFarm().getFeedings()));
            farmInfo.add("Vắc xin Trại Nuôi/Trồng sử dụng: "+getListString(food.getFarm().getVaccinations()));
            farmInfo.add("Ngày Chứng Nhận: "+checkNull(food.getFarm().getCertificationDate().toString()));
            farmInfo.add("Mã Chứng Nhận: "+checkNull(food.getFarm().getCertificationNumber()));
            farmInfo.add("Ngày vận chuyển thực phẩm: "+checkNull(food.getFarm().getFoodSentDate().toString()));
        }else{
            farmInfo.add("Không có dữ liệu trang trại");
        }
        //Provider Header
        dataHeaders.add("Thông tin Nhà cung cấp");
        //Provider Details
        List<String> providerInfo = new ArrayList<>();
        if (food.getProvider()!=null){
            providerInfo.add("Nhà Cung Cấp: "+checkNull(food.getProvider().getName()));
            providerInfo.add("Địa chỉ Nhà Cung Cấp: "+checkNull(food.getProvider().getAddress()));
            providerInfo.add("Nhà Cung Cấp: "+checkNull(food.getProvider().getName()));
            providerInfo.add("Ngày nhận: "+checkNull(food.getProvider().getReceivedDate().toString()));
            providerInfo.add("Ngày Điều trị: "+checkNull(food.getProvider().getTreatment().getTreatmentDate().toString()));
            providerInfo.add("Tình trạng Điều trị: "+getListString(food.getProvider().getTreatment().getTreatmentProcess()));
            providerInfo.add("Ngày Đóng gói: "+checkNull(food.getProvider().getPackaging().getPackagingDate().toString()));
            providerInfo.add("Ngày Hết hạn : "+checkNull(food.getProvider().getPackaging().getEXPDate().toString()));
            providerInfo.add("Ngày cung cấp: "+checkNull(food.getProvider().getProvideDate().toString()));

        }
        else{
            providerInfo.add("Không có dữ liệu nhà cung cấp");
        }
        //Distributor Header
        dataHeaders.add("Thông tin Nhà Phân Phối");
        //Distributor Details
        List<String> distributorInfo = new ArrayList<>();
        if (food.getDistributor()!=null){
            distributorInfo.add("Nhà Phân Phối: "+checkNull(food.getDistributor().getName()));
            distributorInfo.add("Ngày nhận: "+checkNull(food.getDistributor().getReceivedDate().toString()));

        }else {
            distributorInfo.add("Không có dữ liệu nhà phân phối");
        }
        HashMap<String,List<String>> listDataChild = new HashMap<String,List<String>>();
        listDataChild.put(dataHeaders.get(0),farmInfo);
        listDataChild.put(dataHeaders.get(1),providerInfo);
        listDataChild.put(dataHeaders.get(2), distributorInfo);
        expandableListView =(ExpandableListView) dialog.findViewById(R.id.expanded_list_history);
        expandableAdapter = new ExpandableAdapter(History.this,dataHeaders,listDataChild);
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
    private String checkNull(String string){
        if(string != null){
            return string;
        }else {
            return "Không có dữ liệu";
        }
    }
    private String getListString(List list){
        String listString ="";
        if(list!=null){
            for(int i=0;i == list.size();i++){
                listString = listString + list.get(i).toString()+"/n";
            }
            return listString;
        }else {
            return "Không có";
        }
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
