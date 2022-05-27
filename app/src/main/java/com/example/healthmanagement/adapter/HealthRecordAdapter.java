package com.example.healthmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthmanagement.R;
import com.example.healthmanagement.bean.FoodEnergy;
import com.example.healthmanagement.bean.HealthRecord;

import java.util.ArrayList;
import java.util.List;

public class HealthRecordAdapter extends RecyclerView.Adapter<HealthRecordAdapter.ViewHolder> {
    private List<HealthRecord> list = new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;

    public void setItemListener(ItemListener itemListener) {
        this.mItemListener = itemListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_health_record_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HealthRecord healthRecord = list.get(i);
        if (healthRecord != null) {
            viewHolder.tv_heartRate.setText(String.format("心率：%s",healthRecord.getHeartRate()));
            viewHolder.tv_kcal.setText(String.format("卡路里：%s",healthRecord.getKcal()));
            viewHolder.tv_weight.setText(String.format("体重：%s",healthRecord.getWeight()));
            viewHolder.tv_date.setText(String.format("记录日期：%s",healthRecord.getDate()));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener != null) {
                        mItemListener.ItemClick(healthRecord);
                    }
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mItemListener != null) {
                        mItemListener.ItemLongClick(healthRecord);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(List<HealthRecord> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd != null) {
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_heartRate;
        private TextView tv_kcal;
        private TextView tv_weight;
        private TextView tv_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_heartRate = itemView.findViewById(R.id.tv_heartRate);
            tv_kcal = itemView.findViewById(R.id.tv_kcal);
            tv_weight = itemView.findViewById(R.id.tv_weight);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }

    public interface ItemListener {
        void ItemClick(HealthRecord healthRecord);
        void ItemLongClick(HealthRecord healthRecord);
    }
}
