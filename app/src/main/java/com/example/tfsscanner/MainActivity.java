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
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfsscanner.Models.Distributor;
import com.example.tfsscanner.Models.Farm;
import com.example.tfsscanner.Models.Food;
import com.example.tfsscanner.RetrofitService.RetrofitService;
import com.example.tfsscanner.RetrofitService.UnsafeOkHttpClient;
import com.example.tfsscanner.Utils.DateDeserializer;
import com.example.tfsscanner.Utils.ExpandableAdapter;
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
import java.util.HashMap;
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
    ExpandableListView expandableListView;
    ExpandableAdapter expandableAdapter;
    private String historyData = "history.txt";
    TextView barcodeResult,
            foodId_text,
            foodBreed_text,
            category_text;

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

        //Get list view
        expandableListView =(ExpandableListView)findViewById(R.id.expanded_list);

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
                        foodId_text.setText("Mã thực phẩm: "+String.valueOf(response.body().getFoodId()));
                        foodBreed_text.setText("Giống loại: "+checkNull(response.body().getBreed()));
                        category_text.setText("Phân Loại: "+checkNull(response.body().getCategory()));
                        saveHistory(response.body());
                        List<String> dataHeaders = new ArrayList<>();
                        //Farm Header
                        dataHeaders.add("Thông tin Trang Trại");
                        //Farm Details
                        List<String> farmInfo = new ArrayList<>();
                        if (response.body().getFarm()!=null){
                            farmInfo.add("Tên Trại Nuôi/Trồng: "+checkNull(response.body().getFarm().getName()));
                            farmInfo.add("Địa chỉ Nông Trại: "+checkNull(response.body().getFarm().getAddress()));
                            farmInfo.add("Thức ăn Trại Nuôi/Trồng sử dụng: "+getListString(response.body().getFarm().getFeedings()));
                            farmInfo.add("Vắc xin Trại Nuôi/Trồng sử dụng: "+getListString(response.body().getFarm().getVaccinations()));
                            farmInfo.add("Ngày Chứng Nhận: "+checkNull(response.body().getFarm().getCertificationDate().toString()));
                            farmInfo.add("Mã Chứng Nhận: "+checkNull(response.body().getFarm().getCertificationNumber()));
                            farmInfo.add("Ngày vận chuyển thực phẩm: "+checkNull(response.body().getFarm().getFoodSentDate().toString()));
                        }else{
                            farmInfo.add("Không có dữ liệu trang trại");
                        }
                        //Provider Header
                        dataHeaders.add("Thông tin Nhà cung cấp");
                        //Provider Details
                        List<String> providerInfo = new ArrayList<>();
                        if (response.body().getProvider()!=null){
                            providerInfo.add("Nhà Cung Cấp: "+checkNull(response.body().getProvider().getName()));
                            providerInfo.add("Địa chỉ Nhà Cung Cấp: "+checkNull(response.body().getProvider().getAddress()));
                            providerInfo.add("Nhà Cung Cấp: "+checkNull(response.body().getProvider().getName()));
                            providerInfo.add("Ngày nhận: "+checkNull(response.body().getProvider().getReceivedDate().toString()));
                            providerInfo.add("Ngày Điều trị: "+checkNull(response.body().getProvider().getTreatment().getTreatmentDate().toString()));
                            providerInfo.add("Tình trạng Điều trị: "+getListString(response.body().getProvider().getTreatment().getTreatmentProcess()));
                            providerInfo.add("Ngày Đóng gói: "+checkNull(response.body().getProvider().getPackaging().getPackagingDate().toString()));
                            providerInfo.add("Ngày Hết hạn : "+checkNull(response.body().getProvider().getPackaging().getEXPDate().toString()));
                            providerInfo.add("Ngày cung cấp: "+checkNull(response.body().getProvider().getProvideDate().toString()));

                        }
                        else{
                            providerInfo.add("Không có dữ liệu nhà cung cấp");
                        }
                        //Distributor Header
                        dataHeaders.add("Thông tin Nhà Phân Phối");
                        //Distributor Details
                        List<String> distributorInfo = new ArrayList<>();
                        if (response.body().getDistributor()!=null){
                            distributorInfo.add("Nhà Phân Phối: "+checkNull(response.body().getDistributor().getName()));
                            distributorInfo.add("Ngày nhận: "+checkNull(response.body().getDistributor().getReceivedDate().toString()));

                        }else {
                            distributorInfo.add("Không có dữ liệu nhà phân phối");
                        }
                        HashMap<String,List<String>> listDataChild = new HashMap<String,List<String>>();
                        listDataChild.put(dataHeaders.get(0),farmInfo);
                        listDataChild.put(dataHeaders.get(1),providerInfo);
                        listDataChild.put(dataHeaders.get(2), distributorInfo);
                        expandableAdapter = new ExpandableAdapter(MainActivity.this,dataHeaders,listDataChild);
                        expandableListView.setAdapter(expandableAdapter);


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
