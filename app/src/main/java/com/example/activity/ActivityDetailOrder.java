package com.example.activity;

import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.R;
import com.example.common.BaseActivity;
import com.example.common.BaseEntity;
import com.example.common.ErrorCode;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.HistoryTrack;
import com.example.model.domain.Orders;
import com.example.model.domain.User;
import com.example.model.domain.UserAcceptOrder;
import com.example.model.dto.DetailedOrderDTO;
import com.example.utils.ChangeTabColor;
import com.example.utils.SharedPreferencesUtil;
import com.example.utils.TimeUtil;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseBanner;
import com.xuexiang.xui.widget.dialog.DialogLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActivityDetailOrder extends BaseActivity {
    private Toolbar toolbar;
    private static final String TAG = "detail ";
    private String orderId;
    private Context context;
    private SimpleImageBanner mSimpleImageBanner;

    private TextView tv_goodprice,tv_goodintro,tv_goodbeizhu,tv_hostname,tv_uploadtime,
            tv_goodtype,tv_good_hot,tv_good_address,tv_good_deadline;
    private Button btn_receive;
    private DetailedOrderDTO detailedOrderDTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        // 获取传递的参数
        Intent intent = getIntent();
        orderId = intent.getStringExtra("itemId");
        context = this;

        init();

        //获取数据
        RetrofitFactory.getInstence().API(context)
                .getSingleOrder(orderId)
                .compose(this.<BaseEntity<DetailedOrderDTO>>setThread())
                .subscribe(new BaseObserver<DetailedOrderDTO>() {
                    @Override
                    protected void onSuccees(BaseEntity<DetailedOrderDTO> t) throws Exception {
                        int code = t.getCode();
                        if(code == 0){
                            detailedOrderDTO = t.getData();
                            setViews();
                        }
                        else
                            errorHandle(t.getMessage(),"提示");
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        errorHandle(e.toString(),"错误");
                    }
                });

    }
    /**
     * @author chenyim
     * @Description 初始化视图
     * @date 2023/6/26 21:10
     */
    public void init(){
        //设置bar
        toolbar = findViewById(R.id.tb_return);
        toolbar.setTitle("详细情况");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(view -> exitActivity());
        ChangeTabColor.setStatusBarColor(this,Color.parseColor("#4FD494"),true);

        //轮播图
        mSimpleImageBanner = findViewById(R.id.sib_simple_banner);

        //其他
        tv_goodprice = findViewById(R.id.detail_good_price);
        tv_goodintro = findViewById(R.id.detail_good_intro);
        tv_goodbeizhu = findViewById(R.id.detail_good_beizhu);
        tv_hostname = findViewById(R.id.detail_good_hostname);
        tv_uploadtime = findViewById(R.id.detail_good_uploadtime);
        tv_goodtype = findViewById(R.id.detail_good_type);
        btn_receive = findViewById(R.id.detail_receive);
        tv_good_hot = findViewById(R.id.detail_good_hot);
        tv_good_address = findViewById(R.id.detail_good_address);
        tv_good_deadline = findViewById(R.id.detail_good_deadline);

        //用户点击确认按钮
        btn_receive.setOnClickListener(v -> {
            if(detailedOrderDTO != null){
                //获取用户自己的信息
                User userSelf = SharedPreferencesUtil.getObject(context, "User");
                detailedOrderDTO.setAcceptedUser(userSelf);
                RetrofitFactory.getInstence().API(context)
                        .userAcceptOrder(detailedOrderDTO)
                        .compose(this.<BaseEntity<String>>setThread())
                        .subscribe(new BaseObserver<String>() {
                            @Override
                            protected void onSuccees(BaseEntity<String> t) throws Exception {
                                //订单接受成功
                                if(t.getCode() == 0){
                                    errorHandle("接受成功！","提示");
                                    //存到本地数据库
                                    UserAcceptOrder userAcceptOrder = new UserAcceptOrder();
                                    userAcceptOrder.setAcceptTime(TimeUtil.getCurrentTime());
                                    userAcceptOrder.setOrders(JSON.toJSONString(detailedOrderDTO.getOrders()));
                                    userAcceptOrder.setHostUser(JSON.toJSONString(detailedOrderDTO.getHostUser()));
                                    userAcceptOrder.setId(System.currentTimeMillis());
                                    userAcceptOrder.save();
                                }else
                                    errorHandle(t.getMessage(),"提示");

                            }
                            @Override
                            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                                errorHandle(e.toString(),"提示");
                            }
                        });
            }
        });

    }

    /**
     * @author chenyim
     * @Description 拿到order数据后，加载图片、相关信息
     * @date 2023/6/27 0:09
     */
    private void setViews() {
        Orders orders = detailedOrderDTO.getOrders();
        User user = detailedOrderDTO.getHostUser();

        String[] orderPictures = orders.getOrderPictures().split("\\|");
        List<BannerItem> list = initData(orderPictures);
        mSimpleImageBanner.setSource(list)
                .setOnItemClickListener((view, item, position) -> {
                    //点击事件

                })
                .setIsOnePageLoop(true)//设置当页面只有一条时，是否轮播
                .startScroll();//开始滚动
        tv_goodprice.setText(String.valueOf(orders.getOrderMoney()));
        tv_goodtype.setText(orders.getOrderLabel());
        SpannableStringBuilder span = new SpannableStringBuilder("啊啊啊啊啊"+orders.getOrderTitle());
        span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 5,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_goodintro.setText(span);
        tv_goodbeizhu.setText(orders.getOrderContent());
        tv_uploadtime.setText(orders.getOrderCreateTime());
        tv_good_address.setText(orders.getOrderAddress());
        tv_good_deadline.setText(orders.getOrderDeadline());
        tv_hostname.setText(user.getUserName());

        RetrofitFactory.getInstence().API(context)
                .getOrderHot(orderId)
                .compose(this.<BaseEntity<Double>>setThread())
                .subscribe(new BaseObserver<Double>() {
                    @Override
                    protected void onSuccees(BaseEntity<Double> t) throws Exception {
                        if(t.getCode() == 0)
                            tv_good_hot.setText(String.valueOf(t.getData()));
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        errorHandle(e.toString(),"提示");
                    }
                });

        //存入本地浏览记录
        HistoryTrack historyTrack = new HistoryTrack(
            String.valueOf(System.currentTimeMillis()),
                null,
                orders.getId(),
                orders.getOrderTitle(),
                orders.getOrderPictures(),
                orders.getOrderMoney()
        );
        com.example.model.sqlliteentity.HistoryTrack his = new com.example.model.sqlliteentity.HistoryTrack();
        his.setId(System.currentTimeMillis());
        his.setHistoryTrack(JSON.toJSONString(historyTrack));
        his.save();
    }


    /**
     * @author chenyim
     * @Description 设置轮播图图片资源
     * @date 2023/6/27 0:22
     */
    private List<BannerItem> initData(String[] urls){
        List<BannerItem> list = new ArrayList<>(urls.length);
        for (String url : urls) {
            BannerItem item = new BannerItem();
            item.imgUrl = url;
            item.title = " ";
            list.add(item);
        }
        return list;
    }


    /**
     * @author chenyim
     * @Description 网络请求统一错误处理
     * @date 2023/6/25 16:16
     */
    public void errorHandle(String msg, String title){
        DialogLoader.getInstance().showTipDialog(context,R.drawable.warning,title,
                msg,"确认"
                ,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //点击确认按钮后关闭提示框
                        dialogInterface.dismiss();
                        exitActivity();
                    }
                });
    }

    /**
     * @author chenyim
     * @Description 回退页面
     * @date 2023/6/26 21:12
     */
    private void exitActivity() {
        Intent intent=new Intent();
        setResult(0,intent);
        ActivityDetailOrder.this.finish();
    }
}