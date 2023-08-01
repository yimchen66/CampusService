package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.R;
import com.example.activity.ActivityDetailOrder;
import com.example.model.domain.HistoryTrack;
import com.example.model.domain.RecommandOrder;
import com.example.utils.ScreenUtil;

import java.util.List;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ViewHolder> {

    private static final String TAG = "picpic";
    private List<HistoryTrack> itemList;
    private Context context;
    private int space;

    private final double STANDARD_SCALE = 1.1; //当图片宽高比例大于STANDARD_SCALE时，采用3:4比例，小于时，则采用1:1比例
    private final float SCALE = 4 * 1.0f / 3;       //图片缩放比例

    public HistoryItemAdapter(List<HistoryTrack> itemList, Context context, int space) {
        this.itemList = itemList;
        this.context = context;
        this.space = space;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryTrack item = itemList.get(position);

        Glide.with(context)
                .asBitmap()
                .load(item.getOrderPicture().split("\\|")[0])
                .into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                execute(holder,item,bitmap.getWidth(),bitmap.getHeight());
            }
        });
    }
    private void execute(ViewHolder holder,HistoryTrack item,float width,float height) {

        //计算图片宽高
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.image.getLayoutParams();
        //2列的瀑布流，屏幕宽度减去两列间的间距space所的值再除以2，计算出单列的imageview的宽度，space的值在RecyclerView初始化时传入
        float itemWidth = (ScreenUtil.getScreenWidth(context) - 2*space) / 3;
        layoutParams.width = (int) itemWidth;
        float scale = height / width;
        boolean isTooLong = false;
        if (scale > 2) {
            isTooLong = true;
            layoutParams.height = (int) (itemWidth * 1.5);
        } else if (scale > STANDARD_SCALE) {
            //采用3:4显示
            layoutParams.height = (int) (itemWidth * SCALE);
        } else {
            //采用1:1显示
            layoutParams.height = (int) itemWidth;
        }
        holder.image.setLayoutParams(layoutParams);
        if (isTooLong)
            Glide.with(context).load(item.getOrderPicture().split("\\|")[0]).placeholder(R.mipmap.ic_launcher)
                    .override(layoutParams.width, layoutParams.height).centerCrop()
                    .transform(new RoundedCorners(20))
                    .into(holder.image);
        else
            Glide.with(context).load(item.getOrderPicture().split("\\|")[0]).placeholder(R.mipmap.ic_launcher)
                    .centerCrop().transform(new RoundedCorners(20))
                    .into(holder.image);

        holder.order_title.setText(item.getOrderTitle());
        holder.order_price.setText(String.valueOf(item.getOrderMoney()));


        // 点击事件监听器
        holder.itemView.setOnClickListener(view -> {
            // 根据点击的位置获取对应的 item 数据：RecommandOrder clickedItem = itemList.get(clickedPosition);
            Intent intent = new Intent(context, ActivityDetailOrder.class);
            Log.e(TAG, "现在的参数: "+ item.getOrderId());
            intent.putExtra("itemId", item.getOrderId());
            // 启动目标 Activity
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        TextView order_title,order_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_image);
            order_title = itemView.findViewById(R.id.main_order_title);
            order_price = itemView.findViewById(R.id.main_order_price);
        }
    }
}
