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
import com.example.healthmanagement.bean.EatRecordVo;
import com.example.healthmanagement.enums.TimeBucketEnum;

import java.util.ArrayList;
import java.util.List;

public class EatRecordAdapter extends RecyclerView.Adapter<EatRecordAdapter.ViewHolder> {
    private List<EatRecordVo> list =new ArrayList<>();
    private Context mActivity;
    private Integer[] images = new Integer[]{R.drawable.ic_breakfast,R.drawable.ic_lunch,R.drawable.ic_dinner,R.drawable.ic_snacks};



    private ItemListener mItemListener;
    public void setItemListener(ItemListener itemListener){
        this.mItemListener = itemListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view= LayoutInflater.from(mActivity).inflate(R.layout.item_rv_eat_record_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        EatRecordVo eatRecordVo = list.get(i);
        if (eatRecordVo != null) {
            viewHolder.tvName.setText(TimeBucketEnum.getName(eatRecordVo.getTimeId()));
            viewHolder.tvTips.setText(String.format("建议摄入%s大卡",TimeBucketEnum.getTips(eatRecordVo.getTimeId())));
            viewHolder.tvKcal.setText(String.format("已摄入%.2f大卡",eatRecordVo.getKcal()));
            viewHolder.image.setImageResource(images[eatRecordVo.getTimeId()]);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemListener!=null) {
                        mItemListener.ItemClick(eatRecordVo);
                    }
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mItemListener!=null) {
                        mItemListener.ItemLongClick(eatRecordVo);
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
    public void addItem(List<EatRecordVo> listAdd) {
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
        private TextView tvName;
        private TextView tvTips;
        private TextView tvKcal;
        private ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTips = itemView.findViewById(R.id.tv_tips);
            tvKcal = itemView.findViewById(R.id.tv_kcal);
            image = itemView.findViewById(R.id.image);
        }
    }
    public interface ItemListener{
        void ItemClick(EatRecordVo eatRecordVo);
        void ItemLongClick(EatRecordVo eatRecordVo);
    }
}
