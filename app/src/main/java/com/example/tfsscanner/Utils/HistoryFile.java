package com.example.tfsscanner.Utils;

import android.content.Context;
import android.widget.Toast;

import com.example.tfsscanner.MainActivity;
import com.example.tfsscanner.Models.Food;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.LinkedList;

import static android.content.Context.MODE_PRIVATE;

public class HistoryFile {
    Context context;
    private String historyData = "history.txt";
    LinkedList<Food> foodLinkedList = new LinkedList<Food>();

    public HistoryFile(Context context) {
        this.context = context;
    }

    public LinkedList<Food> loadHistory(){
        try {

            // Mở một luồng đọc file.
            FileInputStream in = context.openFileInput(historyData);

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

        } catch (Exception e) {
            Toast.makeText(context,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return foodLinkedList;
    }

    public void saveHistory(Food food){
        boolean isCreated = false;
        for(int i=0;i<foodLinkedList.size();i++){
            if(food.getFoodId() == foodLinkedList.get(i).getFoodId()){
                isCreated = true;
            }
        }
        Gson gson = new GsonBuilder().create();
        if(isCreated == false){
            if (foodLinkedList.size()>=10){
                foodLinkedList.removeFirst();
                foodLinkedList.addLast(food);
            }else{
                foodLinkedList.add(food);}
        }

        String json = gson.toJson(foodLinkedList);
        try {
            // Mở một luồng ghi file.
            FileOutputStream out = context.openFileOutput(historyData, MODE_PRIVATE);
            // Ghi dữ liệu.
            out.write(json.getBytes());
            out.close();
        } catch (Exception e) {
            Toast.makeText(context,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}
