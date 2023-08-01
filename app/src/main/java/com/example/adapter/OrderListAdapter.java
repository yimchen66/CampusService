package com.example.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.R;
import com.example.model.domain.Orders;

import java.util.List;

public class OrderListAdapter extends BaseAdapter {
    private List<Orders> list;
    private Context context;

    public OrderListAdapter(List<Orders> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.goodslist_layout, parent, false);
            holder = new ViewHolder();
            holder.goodImg = convertView.findViewById(R.id.good_img);
            holder.goodIntroduct = convertView.findViewById(R.id.good_introduct);
            holder.goodPrice = convertView.findViewById(R.id.good_price);
            holder.goodType = convertView.findViewById(R.id.good_type);
            holder.uploadTime = convertView.findViewById(R.id.uploadtime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Orders item = list.get(position);

//        RequestOptions options = new RequestOptions()
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(item.getOrderPictures().split("\\|")[0])
                .centerCrop()
                .into(holder.goodImg);

        holder.goodPrice.setText(String.valueOf(item.getOrderMoney()));
        holder.goodType.setText(item.getOrderLabel());
        holder.uploadTime.setText(item.getOrderCreateTime());

        String introduce = "啊啊啊啊啊" + item.getOrderTitle()+"\n"+ item.getOrderContent();
        SpannableStringBuilder span = new SpannableStringBuilder(introduce);
        span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.goodIntroduct.setText(span);

        return convertView;
    }

    static class ViewHolder {
        ImageView goodImg;
        TextView goodIntroduct;
        TextView goodPrice;
        TextView goodType;
        TextView uploadTime;
    }

}
