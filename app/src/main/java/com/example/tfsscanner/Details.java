package com.example.tfsscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfsscanner.Models.Food;
import com.example.tfsscanner.RetrofitService.RetrofitService;
import com.example.tfsscanner.RetrofitService.UnsafeOkHttpClient;
import com.example.tfsscanner.Utils.Controller;
import com.example.tfsscanner.Utils.DateDeserializer;
import com.example.tfsscanner.Utils.ExpandableAdapter;
import com.example.tfsscanner.Utils.HistoryFile;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Console;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.LinkedList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Details extends AppCompatActivity {
    private Controller controller = new Controller();
    ExpandableListView expandableListView;
    ExpandableAdapter expandableAdapter;
    HistoryFile historyFile = new HistoryFile(this);
    private String historyData = "history.txt";
    TextView barcodeResult,
            foodId_text,
            foodBreed_text,
            category_text;
    private MainActivity mainActivity= new MainActivity();
    private ActionBar nav;
    private String url = "https://192.168.43.184:4201/";
    LinkedList<Food> foodLinkedList = new LinkedList<Food>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        barcodeResult =  findViewById(R.id.barcode_result);
        foodId_text =  findViewById(R.id.FoodId_text);
        foodBreed_text = findViewById(R.id.FoodBreed_text);
        category_text = findViewById(R.id.Category_text);
        //Get list view
        expandableListView =(ExpandableListView)findViewById(R.id.expanded_list);
        getInfo();


    }
    public void getInfo(){
        try{
            Intent intent = getIntent();
            if (intent.getParcelableExtra("barcode") != null){
                Barcode barcode = intent.getParcelableExtra("barcode");
                jsonParse(barcode);
            }else if(intent.getSerializableExtra("food") != null){
                Food food = (Food) intent.getSerializableExtra("food");
                loadDataView(food);
            }else {
                Toast toast = Toast.makeText(getApplicationContext(),"Không tìm thấy dữ liệu",Toast.LENGTH_LONG);
                toast.show();
            }


        }catch (Exception e){
            Toast toast = Toast.makeText(getApplicationContext(),"Đã có lỗi xảy ra",Toast.LENGTH_LONG);
            toast.show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data!=null){
                    //Get the barcode result

                    //jsonParse(barcode);
                }
                else {
                    //didnt recieve the barcode
                    Toast toast = Toast.makeText(getApplicationContext(),"Không tìm thấy barcode",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }else {
            //cannot recieve the request code
            super.onActivityResult(requestCode,resultCode,data);
        }

    }
    private void jsonParse(Barcode barcode) {
        try{
            int foodId = 0;
            try {
                String id[] = barcode.rawValue.split("-");
                foodId =  Integer.valueOf(id[1]);
            }catch (Exception e){
                Toast toast = Toast.makeText(getApplicationContext(),"Mã sản phẩm sai",Toast.LENGTH_LONG);
                toast.show();
            }


            //barcodeResult.setText(String.valueOf(foodId));
            //barcodeResult.setText("barcode value: "+foodId);
            //Logging
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
            // URL build unsafe
            OkHttpClient unSafeBuilder = UnsafeOkHttpClient.getUnsafeOkHttpClient();
            // URL build only certificated
            OkHttpClient builder = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .retryOnConnectionFailure(true)
                    .build();
            //Gson builder
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateDeserializer())
                    .setLenient()
                    .create();
            //Retrofit build
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(unSafeBuilder)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            RetrofitService service = retrofit.create(RetrofitService.class);
            //Get API with foodId
            service.FoodDetails(foodId).enqueue(new Callback<Food>() {
                @Override
                public void onResponse(Call<Food> call, Response<Food> response) {
                    if (response.body()!=null){
                        historyFile.loadHistory();
                        historyFile.saveHistory(response.body());
                        loadDataView(response.body());
                    }else
                    {
                        Toast toast = Toast.makeText(getApplicationContext(),"Không có dữ liệu",Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                @Override
                public void onFailure(Call<Food> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(),"Kết nối thất bại",Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        }catch (NumberFormatException e){
            Toast toast = Toast.makeText(getApplicationContext(),"Mã sản phẩm sai",Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public void loadDataView(Food food){
        foodId_text.setText("Mã thực phẩm: "+String.valueOf(food.getFoodId()));
        foodBreed_text.setText("Giống loại: "+controller.checkNull(food.getBreed()));
        category_text.setText("Phân Loại: "+controller.checkNull(food.getCategory()));

        expandableAdapter = new ExpandableAdapter(Details.this,
                controller.getHeaders(),
                controller.getDataChild(food));
        expandableListView.setAdapter(expandableAdapter);
    }




    public void getFoodInfo(Barcode code){
        String foodId[] ;

        foodId = code.displayValue.split("-");
        int Foodid = Integer.valueOf(foodId[1]);
        Gson gson = new GsonBuilder().create();
        OkHttpClient unsafeOkHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(unsafeOkHttpClient)
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        service.FoodDetails(Foodid).enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                loadDataView(response.body());
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
                toast.show();
            }
        });



    }




























}
