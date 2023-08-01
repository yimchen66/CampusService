package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.R;
import com.example.model.domain.NavigateAct;

import java.util.ArrayList;
import java.util.List;

public class TagListViewAdapter extends BaseAdapter {
    List<NavigateAct> list;
    Context context;
    private  int screenWidth;

    public TagListViewAdapter(List<NavigateAct> list, Context context, int screenWidth) {
        this.list = list;
        this.context = context;
        this.screenWidth = screenWidth;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null ) {
            view = LayoutInflater.from(context).inflate(R.layout.tag_item, viewGroup, false);
        }
        ImageView icon = view.findViewById(R.id.tag_item_pic);
        TextView text = view.findViewById(R.id.tag_item_text);
        LinearLayout rl = view.findViewById(R.id.rl);

        //计算每条的宽度
//        ViewGroup.LayoutParams layoutParams = rl.getLayoutParams();
//        layoutParams.width = screenWidth / 7 * 2;
//        rl.setLayoutParams(layoutParams);

        text.setText(list.get(i).getText());
        Glide.with(context).load(list.get(i).getPicUrl()).into(icon);
        return view;
    }
}
