package com.example.tfsscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfsscanner.Models.Farm;
import com.example.tfsscanner.Models.Food;
import com.example.tfsscanner.RetrofitService.RetrofitService;
import com.example.tfsscanner.RetrofitService.UnsafeOkHttpClient;
import com.example.tfsscanner.Utils.DateDeserializer;
import com.example.tfsscanner.Utils.LayoutAdapter;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_CAMERA_CODE = 1;

    private String historyData = "history.txt";
    View farmLine,providerLine,distributorLine;
    EditText ipconfig_text;
    TextView barcodeResult,
            foodId_text,
            foodBreed_text,
            category_text,
            farmName_text,
            farmAddress_text,
            farmFeeding_text,
            farmVaccine_text,
            certiDate_text,
            certiNum_text,
            foodSentDate_text,
            provider_text,
            distributor_text;
    private ActionBar nav;
    private String url = "https://192.168.1.43:4201/";
    LinkedList<Food> foodLinkedList = new LinkedList<Food>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get id of content
        barcodeResult =  findViewById(R.id.barcode_result);
        foodId_text =  findViewById(R.id.FoodId_text);
        foodBreed_text = findViewById(R.id.FoodBreed_text);
        category_text = findViewById(R.id.Category_text);
        farmName_text = findViewById(R.id.FarmName_text);
        farmAddress_text = findViewById(R.id.FarmAddress_text);
        farmFeeding_text = findViewById(R.id.FarmFeeding_text);
        farmVaccine_text = findViewById(R.id.FarmVaccine_text);
        certiDate_text = findViewById(R.id.CertiDate_text);
        certiNum_text = findViewById(R.id.CertiNum_text);
        foodSentDate_text = findViewById(R.id.FoodSentDate_text);
        provider_text = findViewById(R.id.Provider_text);
        distributor_text = findViewById(R.id.Distributor_text);
        ipconfig_text = findViewById(R.id.ip_config);
        //Get Line content
        farmLine = findViewById(R.id.FarmLine);
        providerLine = findViewById(R.id.ProviderLine);
        distributorLine = findViewById(R.id.DistributorLine);
        //check Permission camera and internet
        checkPermission();
        loadHistory();
        //Get Action Bar
        nav = getSupportActionBar();
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.scan_barcode:

                    scanBarcode(null);
                    return true;
                case R.id.action_history:
                    Intent intent = new Intent(MainActivity.this, History.class);
                    intent.putExtra("history-list",foodLinkedList);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
    public void scanBarcode(View view) {
        Intent intent = new Intent(this, ScanBarcode.class);
        startActivityForResult(intent,0);
    }
    private void jsonParse(Barcode barcode) {
        try{

            int foodId =  Integer.valueOf(barcode.rawValue.substring(5));
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
                        saveHistory(response.body());

                        //Set visible for line
                        farmLine.setVisibility(View.VISIBLE);
                        distributorLine.setVisibility(View.VISIBLE);
                        providerLine.setVisibility(View.VISIBLE);

                        //Set Food Details
                        foodId_text.setText("Mã thực phẩm: "+String.valueOf(response.body().getFoodId()));
                        foodBreed_text.setText("Giống loại: "+checkNull(response.body().getBreed()));
                        category_text.setText("Phân Loại: "+checkNull(response.body().getCategory()));
                        //Farm Details
                        if (response.body().getFarm()!=null){
                            farmName_text.setText("Tên Trại Nuôi/Trồng: "+checkNull(response.body().getFarm().getName()));
                            farmAddress_text.setText("Địa chỉ Nông Trại: "+checkNull(response.body().getFarm().getAddress()));
                            farmFeeding_text.setText("Thức ăn Trại Nuôi/Trồng sử dụng: "+getListString(response.body().getFarm().getFeedings()));
                            farmVaccine_text.setText("Vắc xin Trại Nuôi/Trồng sử dụng: "+getListString(response.body().getFarm().getVaccinations()));
                            certiDate_text.setText("Ngày Chứng Nhận: "+checkNull(response.body().getFarm().getCertificationDate().toString()));
                            certiNum_text.setText("Mã Chứng Nhận: "+checkNull(response.body().getFarm().getCertificationNumber()));
                            foodSentDate_text.setText("Ngày vận chuyển thực phẩm: "+checkNull(response.body().getFarm().getFoodSentDate().toString()));
                        }else
                        {
                            farmName_text.setText("Không có dữ liệu Nông Trại !!!");
                        }

                        //Provider Details
                        if (response.body().getProvider()!=null){

                            provider_text.setText("Nhà Cung Cấp: "+checkNull(response.body().getProvider().getName()));

                        }else
                        {
                            provider_text.setText("Không có dữ liệu Nhà Cung Cấp !!!");
                        }

                        //Distributor Details
                        if (response.body().getDistributor()!=null){
                            distributor_text.setText("Nhà Phân Phối"+checkNull(response.body().getDistributor().getName()));

                        }else
                        {
                            distributor_text.setText("Không có dữ liệu Nhà Phân Phối !!!");
                        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if(requestCode==0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data!=null){
                    //Get the barcode result
                    Barcode barcode = data.getParcelableExtra("barcode");

                    jsonParse(barcode);
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
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            //if permission is Granted
        } else {
            //if not then ask for permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_CAMERA_CODE);
        }
    }
    //Save food history
    public void saveHistory(Food food){
        Gson gson = new GsonBuilder().create();
        if (foodLinkedList.size()>=10){
            foodLinkedList.removeFirst();
            foodLinkedList.addLast(food);
        }else{
            foodLinkedList.add(food);}
        String json = gson.toJson(foodLinkedList);
        try {

            // Mở một luồng ghi file.
            FileOutputStream out = this.openFileOutput(historyData, MODE_PRIVATE);
            // Ghi dữ liệu.
            out.write(json.getBytes());
            out.close();
        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        /*SharedPreferences sharedPreferences =  getSharedPreferences("history_item",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("food_list",json);
            editor.apply();*/
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

        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    private String checkNull(String string){
        if(string != null){
            return string;
        }else {
            return "Không có dữ liệu";
        }
    }
}
