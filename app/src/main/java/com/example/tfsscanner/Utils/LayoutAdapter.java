package com.example.tfsscanner.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfsscanner.Models.Food;
import com.example.tfsscanner.R;

import java.util.ArrayList;
import java.util.List;

public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.ViewHolder> implements Filterable {
    private List<Food> mFoodList;
    private List<Food> mFoodListFull;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mFoodId,mBreed;

        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            mFoodId = itemView.findViewById(R.id.FoodId_text);
            mBreed = itemView.findViewById(R.id.FoodBreed_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!= null){
                        int pos = getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            listener.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
    public LayoutAdapter(List<Food> foodList) {
        this.mFoodList = foodList;
        mFoodListFull = new ArrayList<>(foodList);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent,false);
        ViewHolder vh = new ViewHolder(v,mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food currentFood = mFoodList.get(position);
        holder.mBreed.setText("Thực Phẩm: "+currentFood.getBreed());
        holder.mFoodId.setText("Mã Sô Thực Phẩm: "+ String.valueOf(currentFood.getFoodId()));
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }
    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Food> filterdList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filterdList.addAll(mFoodListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Food food : mFoodListFull){
                    if(food.getBreed().toLowerCase().contains(filterPattern)){
                        filterdList.add(food);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterdList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFoodList.clear();
            mFoodList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
