package com.example.adapter;


import android.content.Context;
import android.os.Handler;
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
import com.example.model.domain.Orders;
import com.example.model.domain.TransOrder;
import com.example.model.domain.User;
import com.example.model.dto.DetailedOrderDTO;

import java.util.ArrayList;
import java.util.List;

public class MyTransListAdapter extends BaseAdapter {
    private static final String TAG = "hua";
    private Context context;
    private List<DetailedOrderDTO> list = new ArrayList<>();

    public MyTransListAdapter(List<DetailedOrderDTO> list, Context context) {
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

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mytrans_layout, parent, false);
            holder = new ViewHolder();
            holder.iv_touxiang = convertView.findViewById(R.id.mylaunch_iv_touxiang);
            holder.iv_goodimg = convertView.findViewById(R.id.mylaunch_iv_goodimg);
            holder.tv_goodintro = convertView.findViewById(R.id.mylaunch_tv_goodintro);
            holder.tv_nickname = convertView.findViewById(R.id.mylaunch_tv_nickname);
            holder.tv_goodprice = convertView.findViewById(R.id.mylaunch_tv_goodprice);
            holder.tv_time = convertView.findViewById(R.id.mylaunch_tv_time);
            holder.tv_state = convertView.findViewById(R.id.mylaunch_tv_state);
            holder.tv_acceptTime = convertView.findViewById(R.id.person_accept_time);

            convertView.setTag(holder);
        } else {
            holder =(ViewHolder) convertView.getTag();
        }
        DetailedOrderDTO detailedOrderDTO = list.get(i);

        User hostUser = detailedOrderDTO.getHostUser();
        Orders orders = detailedOrderDTO.getOrders();
        TransOrder transOrder = detailedOrderDTO.getTransOrder();

        Glide.with(context)
                .load(hostUser.getFigureUrl())
                .placeholder(R.drawable.defaulttouxaing)//图片加载出来前，显示的图片
                .fallback( R.drawable.defaulttouxaing) //url为空的时候,显示的图片
                .error(R.drawable.defaulttouxaing)//图片加载失败后，显示的图片
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.iv_touxiang);
        String nickname = hostUser.getNickName();
        holder.tv_nickname.setText(nickname==null||nickname.equals("")?"飞翔的企鹅":nickname);

        Glide.with(context)
                .load(orders.getOrderPictures().split("\\|")[0])
                .into(holder.iv_goodimg);

        holder.tv_goodintro.setText(orders.getOrderTitle()+"\n"+orders.getOrderContent());
        holder.tv_goodprice.setText(String.valueOf(orders.getOrderMoney()));
        holder.tv_time.setText(orders.getOrderCreateTime());

        holder.tv_state.setText(
                transOrder.getAcceptState() == 1?"买家确认":
                        transOrder.getHostState() == 1?"卖家确认":"均未确认"
        );
        if(transOrder.getAcceptState() == 1){
            holder.tv_state.setBackgroundResource(R.drawable.shape_order_trans1);
        }else if(transOrder.getHostState() == 1)
            holder.tv_state.setBackgroundResource(R.drawable.shape_order_trans2);
        else
            holder.tv_state.setBackgroundResource(R.drawable.shape_order_trans);


        holder.tv_acceptTime.setText(transOrder.getAcceptTime());
        return convertView;
    }

    static class ViewHolder {
        ImageView iv_touxiang,iv_goodimg;
        TextView tv_goodintro;
        TextView tv_nickname;
        TextView tv_goodprice;
        TextView tv_time;
        TextView tv_state;
        TextView tv_acceptTime;
    }

}

