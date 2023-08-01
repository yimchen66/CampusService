package com.example.adapter;


import android.content.Context;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyFinishListAdapter extends BaseAdapter {
    private static final String TAG = "hua";
    private Context context;
    private List<DetailedOrderDTO> list = new ArrayList<>();

    public MyFinishListAdapter(List<DetailedOrderDTO> list, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.myfinish_layout, parent, false);
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

        String acceptFinishTime = transOrder.getAcceptFinishTime();
        String hostFinishTime = transOrder.getHostFinishTime();

        holder.tv_acceptTime.setText(
                compareDate(acceptFinishTime,hostFinishTime) == 1?
                        hostFinishTime:acceptFinishTime
        );
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

    public int compareDate(String date1,String date2){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = dateFormat.parse(date1);
            Date d2 = dateFormat.parse(date2);
            if(d1.equals(d2)){
                return 0;
            }else if(d1.before(d2)){
                return 1;
            }else if(d1.after(d2)){
                return -1;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("方法——compareDate（{}，{}）异常"+date1+","+date2);
        }
        return 1;
    }
}

