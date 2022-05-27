package com.example.healthmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.healthmanagement.R;
import com.example.healthmanagement.bean.LoseWeightBean;
import com.example.healthmanagement.ui.activity.LoseWeightDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class LoseWeightAdapter extends RecyclerView.Adapter<LoseWeightAdapter.ViewHolder> {
    private List<LoseWeightBean> list =new ArrayList<>();
    private Context mActivity;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view= LayoutInflater.from(mActivity).inflate(R.layout.item_rv_lose_weight_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        LoseWeightBean loseWeightBean = list.get(i);
        if (loseWeightBean != null) {
            viewHolder.title.setText(loseWeightBean.getTitle());
            viewHolder.author_name.setText(loseWeightBean.getAuthor_name());
            viewHolder.date.setText(loseWeightBean.getDate());
            Glide.with(mActivity).load(loseWeightBean.getThumbnail_pic_s()).into(viewHolder.thumbnail_pic_s);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, LoseWeightDetailActivity.class);
                    intent.putExtra("url",loseWeightBean.getUrl());
                    mActivity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void addItem(List<LoseWeightBean> listAdd) {
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
        private TextView title;
        private TextView author_name;
        private TextView date;
        private ImageView thumbnail_pic_s;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author_name = itemView.findViewById(R.id.author_name);
            date = itemView.findViewById(R.id.date);
            thumbnail_pic_s = itemView.findViewById(R.id.thumbnail_pic_s);
        }
    }
}
