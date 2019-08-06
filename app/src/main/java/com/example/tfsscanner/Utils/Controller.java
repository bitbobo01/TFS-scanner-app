package com.example.tfsscanner.Utils;

import com.example.tfsscanner.Models.Food;
import com.example.tfsscanner.Models.Vaccination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Controller {
    public String getListString(List<String> list){
        String listString ="";
        if(list!=null){
            for(int i=0;i < list.size();i++){
                listString += "\n"+list.get(i);
            }
            return listString;
        }else {
            return "Không có";
        }
    }
    public String checkNull(String string){
        if(string != null){
            return string;
        }else {
            return "Không có dữ liệu";
        }
    }
    public List<String> getHeaders(){
        List<String> dataHeaders = new ArrayList<>();
        dataHeaders.add("Thông tin Trang Trại");
        dataHeaders.add("Thông tin Nhà cung cấp");
        dataHeaders.add("Thông tin Nhà Phân Phối");
        return dataHeaders;
    }
    public HashMap<String,List<String>> getDataChild(Food food){

        //Farm Header
        List<String> dataHeaders = getHeaders();
        //Farm Details
        List<String> farmInfo = new ArrayList<>();
        if (food.getFarm()!=null){
            farmInfo.add("Tên Trại Nuôi/Trồng: "+checkNull(food.getFarm().getName()));
            farmInfo.add("Địa chỉ Nông Trại: "+checkNull(food.getFarm().getAddress()));
            farmInfo.add("Thức ăn Trại Nuôi/Trồng sử dụng: "+getListString(food.getFarm().getFeedings()));
            farmInfo.add("Vắc xin Trại Nuôi/Trồng sử dụng: "+getVacList(food.getFarm().getVaccinations()));
            farmInfo.add("Ngày Chứng Nhận: "+checkNull(food.getFarm().getCertificationDate().toString()));
            farmInfo.add("Mã Chứng Nhận: "+checkNull(food.getFarm().getCertificationNumber()));
            farmInfo.add("Ngày vận chuyển thực phẩm: "+checkNull(food.getFarm().getFoodSentDate().toString()));
        }else{
            farmInfo.add("Không có dữ liệu trang trại");
        }


        //Provider Details
        List<String> providerInfo = new ArrayList<>();
        if (food.getProvider()!=null){
            providerInfo.add("Nhà Cung Cấp: "+checkNull(food.getProvider().getName()));
            providerInfo.add("Địa chỉ Nhà Cung Cấp: "+checkNull(food.getProvider().getAddress()));
            providerInfo.add("Nhà Cung Cấp: "+checkNull(food.getProvider().getName()));
            providerInfo.add("Ngày nhận: "+checkNull(food.getProvider().getReceivedDate().toString()));
            //providerInfo.add("Ngày Điều trị: "+checkNull(food.getProvider().getTreatment().getTreatmentDate().toString()));
            //providerInfo.add("Tình trạng Điều trị: "+getListString(food.getProvider().getTreatment().getTreatmentProcess()));
            //providerInfo.add("Ngày Đóng gói: "+checkNull(food.getProvider().getPackaging().getPackagingDate().toString()));
            //providerInfo.add("Ngày Hết hạn : "+checkNull(food.getProvider().getPackaging().getEXPDate().toString()));
            providerInfo.add("Ngày cung cấp: "+checkNull(food.getProvider().getProvideDate().toString()));

        }
        else{
            providerInfo.add("Không có dữ liệu nhà cung cấp");
        }
        //Distributor Header

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
        return listDataChild;
    }
    public String getVacList(List<Vaccination> VacList){
        if(VacList!=null){
            StringBuilder sb = new StringBuilder();
            for(Vaccination v:VacList){
                sb.append("Ngày Tiêm chủng: "+v.getVaccinationDate().toString()+"\n"+"Loại Tiêm Chủng: "+v.getVaccinationType());
            }
            return sb.toString();
        }else
        {
            return "Không có tiêm vắc xin";
        }

    }
}
