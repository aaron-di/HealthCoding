package com.example.healthmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthmanagement.R;
import com.example.healthmanagement.bean.FoodEnergy;

import java.util.ArrayList;
import java.util.List;

public class FoodEnergyAdapter extends RecyclerView.Adapter<FoodEnergyAdapter.ViewHolder> {
    private List<FoodEnergy> list =new ArrayList<>();
    private Context mActivity;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view= LayoutInflater.from(mActivity).inflate(R.layout.item_rv_food_energy_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FoodEnergy foodEnergy = list.get(i);
        if (foodEnergy != null) {
            viewHolder.tv_food.setText(foodEnergy.getFood());
            viewHolder.tv_quantity.setText(foodEnergy.getQuantity());
            viewHolder.tv_heat.setText(foodEnergy.getHeat());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void addItem(List<FoodEnergy> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd!=null){
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_food;
        private TextView tv_quantity;
        private TextView tv_heat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_food = itemView.findViewById(R.id.tv_food);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_heat = itemView.findViewById(R.id.tv_heat);
        }
    }
}
