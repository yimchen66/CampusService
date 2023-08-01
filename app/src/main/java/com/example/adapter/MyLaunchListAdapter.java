package com.example.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.R;
import com.example.model.domain.Orders;
import com.example.model.domain.User;
import com.example.model.domain.UserCreateOrder;
import com.example.utils.SharedPreferencesUtil;


import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyLaunchListAdapter extends BaseAdapter {
    private static final String TAG = "hua";
    private Context context;
    private List<Orders> list = new ArrayList<>();
    private User user;

    public MyLaunchListAdapter(List<Orders> list, Context context, User user) {
        this.context = context;
        this.list = list;
        this.user = user;
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

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mylaunch_layout, parent, false);
            holder = new ViewHolder();
            holder.iv_touxiang = convertView.findViewById(R.id.mylaunch_iv_touxiang);
            holder.iv_goodimg = convertView.findViewById(R.id.mylaunch_iv_goodimg);
            holder.tv_goodintro = convertView.findViewById(R.id.mylaunch_tv_goodintro);
            holder.tv_nickname = convertView.findViewById(R.id.mylaunch_tv_nickname);
            holder.tv_goodprice = convertView.findViewById(R.id.mylaunch_tv_goodprice);
            holder.tv_time = convertView.findViewById(R.id.mylaunch_tv_time);

            convertView.setTag(holder);
        } else {
            holder =(ViewHolder) convertView.getTag();
        }

        Orders order = list.get(i);
        Glide.with(context)
                .load(user.getFigureUrl())
                .placeholder(R.drawable.defaulttouxaing)//图片加载出来前，显示的图片
                .fallback( R.drawable.defaulttouxaing) //url为空的时候,显示的图片
                .error(R.drawable.defaulttouxaing)//图片加载失败后，显示的图片
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.iv_touxiang);

        String nickname = user.getNickName();
        holder.tv_nickname.setText(nickname==null||nickname.equals("")?"飞翔的企鹅":nickname);

        Glide.with(context)
                .load(order.getOrderPictures().split("\\|")[0])
                .into(holder.iv_goodimg);

        holder.tv_goodintro.setText(order.getOrderTitle()+"\n"+order.getOrderContent());
        holder.tv_goodprice.setText(String.valueOf(order.getOrderMoney()));
        holder.tv_time.setText(order.getOrderCreateTime());


        return convertView;
    }


    static class ViewHolder {
        ImageView iv_touxiang,iv_goodimg;
        TextView tv_goodintro;
        TextView tv_nickname;
        TextView tv_goodprice;
        TextView tv_time;
    }
}
