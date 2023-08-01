package com.example.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.R;
import com.example.model.dto.TransHistoryDTO;

import java.util.List;

public class MyTransHistoryAdapter extends BaseAdapter {
    private Context context;
    List<TransHistoryDTO> list;


    public MyTransHistoryAdapter(Context context, List<TransHistoryDTO> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null ) {
            convertView = LayoutInflater.from(context).inflate(R.layout.moneytrans_layout, parent, false);
        }

        TransHistoryDTO moneyTransaction = list.get(position);
        TextView tv_money = convertView.findViewById(R.id.money_money);
        TextView tv_time = convertView.findViewById(R.id.money_time);
        ImageView iv_touxiang = convertView.findViewById(R.id.money_touxiang);
        TextView tv_name = convertView.findViewById(R.id.money_tv_nickname);
        TextView tv_money_source = convertView.findViewById(R.id.money_source);

        Glide.with(context).load(moneyTransaction.getPicUrl())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.drawable.defaulttouxaing)//图片加载出来前，显示的图片
                .fallback( R.drawable.defaulttouxaing) //url为空的时候,显示的图片
                .error(R.drawable.defaulttouxaing)//图片加载失败后，显示的图片
                .into(iv_touxiang);
        tv_name.setText(moneyTransaction.getNickName());
        tv_time.setText(moneyTransaction.getTime());

        double order_money = moneyTransaction.getOrder_money();
        if(order_money > 0){
            tv_money.setText("+"+order_money);
            tv_money.setTextColor(Color.parseColor("#FAC542"));
            tv_money_source.setText("来自");
        }else{
            tv_money.setText("-"+order_money);
            tv_money.setTextColor(Color.parseColor("#000000"));
            tv_money_source.setText("转账给");
        }
        return convertView;
    }
}
