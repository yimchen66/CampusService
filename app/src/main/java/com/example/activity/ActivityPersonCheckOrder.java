package com.example.activity;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.R;
import com.example.common.BaseActivity;
import com.example.common.BaseEntity;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.HistoryTrack;
import com.example.model.domain.Orders;
import com.example.model.domain.TransOrder;
import com.example.model.domain.User;
import com.example.model.domain.UserAcceptOrder;
import com.example.model.domain.UserCreateOrder;
import com.example.model.dto.DetailedOrderDTO;
import com.example.model.request.trans.TransOrderFinishRequest;
import com.example.utils.ChangeTabColor;
import com.example.utils.SharedPreferencesUtil;
import com.example.utils.TimeUtil;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.toast.XToast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ActivityPersonCheckOrder extends BaseActivity {
    private Toolbar toolbar;
    private static final String TAG = "detail ";
    private String orderId;
    private Context context;
    private SimpleImageBanner mSimpleImageBanner;

    private TextView tv_goodprice,tv_goodintro,tv_goodbeizhu,tv_hostname,tv_uploadtime,
            tv_goodtype,tv_good_hot,tv_good_address,tv_good_deadline,tv_good_hostname_accept,
            tv_good_uploadtime_accept;
    private LinearLayout ll_accept_info;
    private Button btn_receive;
    private DetailedOrderDTO detailedOrderDTO;
    private boolean isReceive = false;//默认是没有被接单状态
    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_check_order);
        // 获取传递的参数
        Intent intent = getIntent();
        orderId = intent.getStringExtra("itemId");
        context = this;
        currentUser = SharedPreferencesUtil.getObject(context,"User");

        init();
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
        tv_good_hostname_accept = findViewById(R.id.detail_good_hostname_accept);
        tv_good_uploadtime_accept = findViewById(R.id.detail_good_uploadtime_accept);
        ll_accept_info = findViewById(R.id.ll_accept_info);

        //查找order
        RetrofitFactory.getInstence().API(context)
                .userGetOrderDetail(orderId)
                .compose(this.<BaseEntity<DetailedOrderDTO>>setThread())
                .subscribe(new BaseObserver<DetailedOrderDTO>() {
                    @Override
                    protected void onSuccees(BaseEntity<DetailedOrderDTO> t) throws Exception {
                        if(t.getCode() == 0){
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

        if(detailedOrderDTO.getOrderState() == 0){//没有被接单的情况
            isReceive = false;
            btn_receive.setEnabled(true);
            btn_receive.setText("删除订单");
            ll_accept_info.setVisibility(View.GONE);
        }else{//已经被接单
            ll_accept_info.setVisibility(View.VISIBLE);

            tv_good_hostname_accept.setText(detailedOrderDTO.getAcceptedUser().getUserName());
            tv_good_uploadtime_accept.setText(detailedOrderDTO.getTransOrder().getAcceptTime());


            isReceive = true;
            TransOrder transOrder = detailedOrderDTO.getTransOrder();
            if(transOrder.getAcceptState() == 1 && transOrder.getHostState() == 1){
                btn_receive.setText("全部完成");
                btn_receive.setEnabled(false);
            }else if(transOrder.getAcceptState() == 0 && transOrder.getHostState() == 0){
                btn_receive.setText("确认完成");
            }else if(transOrder.getHostState() == 0){
                if(currentUser.getId().equals(transOrder.getHostId()))//是自己的单子，自己每完成
                    btn_receive.setText("确认完成");
                else{
                    btn_receive.setText("等待对方完成");
                    btn_receive.setEnabled(false);
                }
            }else{
                if(currentUser.getId().equals(transOrder.getHostId())){
                    //是自己的单子，自己每完成
                    btn_receive.setEnabled(false);
                    btn_receive.setText("等待对方完成");
                }else
                    btn_receive.setText("确认完成");
            }
        }

        btn_receive.setOnClickListener(v -> {
            //没有被接单的情况
            if(!isReceive){
                //删除订单操作
                DialogLoader.getInstance().showConfirmDialog(context,
                        "确认删除订单？", "确认", (dialog, which) -> delete(),"取消");
            }else{//确认完成操作
                DialogLoader.getInstance().showConfirmDialog(context,
                        "确认完成订单？", "确认", (dialog, which) -> complete(),"取消");
            }
        });
    }

    /**
     * @author chenyim
     * @Description 删除订单操作
     * @date 2023/6/28 14:00
     */
    public void delete(){
        Orders orders = detailedOrderDTO.getOrders();
        RetrofitFactory.getInstence().API(context)
                .userDeleteOrder(orders.getId())
                .compose(this.<BaseEntity<String>>setThread())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccees(BaseEntity<String> t) throws Exception {
                        orders.setVersion(0);
                        List<UserCreateOrder> all = DataSupport.findAll(UserCreateOrder.class);
                        for (UserCreateOrder userCreateOrder : all) {
                            String json = userCreateOrder.getOrder();
                            Orders orders1 = JSON.parseObject(json, Orders.class);
                            if(orders1.equals(orders))
                                DataSupport.delete(UserCreateOrder.class,userCreateOrder.getId());
                        }
                        errorHandle(t.getData(),"提示");
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        errorHandle(e.toString(),"出错");
                    }
                });
    }
    /**
     * @author chenyim
     * @Description 确认完成订单操作
     * @date 2023/6/28 14:00
     */
    public void complete(){
        Orders orders = detailedOrderDTO.getOrders();
        TransOrderFinishRequest request = new TransOrderFinishRequest();
        request.setOrderID(orders.getId());
        request.setHostId(currentUser.getId());

        RetrofitFactory.getInstence().API(context)
                .userSetOrderCompelete(request)
                .compose(this.<BaseEntity<TransOrder>>setThread())
                .subscribe(new BaseObserver<TransOrder>() {
                    @Override
                    protected void onSuccees(BaseEntity<TransOrder> t) throws Exception {
                        if(t.getCode() == 0)
                            errorHandle("状态修改成功！","提示");
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
        ActivityPersonCheckOrder.this.finish();
    }
}