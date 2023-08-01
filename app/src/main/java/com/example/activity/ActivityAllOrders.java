package com.example.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.R;
import com.example.adapter.OrderListAdapter;
import com.example.common.BaseActivity;
import com.example.common.BaseEntity;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.Orders;
import com.example.model.request.order.PageOrderRequest;
import com.example.utils.ChangeTabColor;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.wyt.searchbox.SearchFragment;
import com.xuexiang.xui.widget.dialog.DialogLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ActivityAllOrders extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "flash  ";
    public static final int TYPE_DEFAULT = 1;
    public static final int TYPE_MONEY = 2;
    public static final int TYPE_HOT = 3;
    public static final int PAGE_NUMBER = 8;
    private boolean moneyUpToDown = false;
    private boolean hotUpToDown = false;
    private boolean timeUpToDown = true;
    private int currentStrategy = TYPE_DEFAULT;

    private RefreshLayout refreshLayout;
    private Context context;
    private EditText search_text;
    private ListView listView;
    private LinearLayout llPrice,llHot,llTime;
    private int currentPage = 1;
    private List<Orders> list;
    private OrderListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);
        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#4FD494"),true);
        context = this;
        initView();
    }

    /**
     * @author chenyim
     * @Description 加载视图
     * @date 2023/6/27 11:07
     */
    private void initView() {
        //加载滑动视图
        refreshLayout = findViewById(R.id.refreshLayout);
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setRefreshHeader(new ClassicsHeader(context));
        //refreshLayout.setRefreshFooter(new ClassicsFooter(context));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //当前页设置为1，重新获取数据
                currentPage = 1;
                list.clear();
                PageOrderRequest pageOrderRequest = new PageOrderRequest(currentPage,PAGE_NUMBER);
                setListViewDate(currentStrategy,pageOrderRequest);
                refreshlayout.finishRefresh(1000/*,false*/);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                //当前页加1，并获取数据
                currentPage++;
                PageOrderRequest pageOrderRequest = new PageOrderRequest(currentPage,PAGE_NUMBER);
                if(currentStrategy == TYPE_MONEY){
                    pageOrderRequest.setColumn("order_money");
                    pageOrderRequest.setSortedType(moneyUpToDown?"desc":"asc");
                }else{
                    pageOrderRequest.setColumn("order_create_time");
                    pageOrderRequest.setSortedType(timeUpToDown?"desc":"asc");
                }
                setListViewDate(currentStrategy,pageOrderRequest);
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
            }
        });

        //加载搜索框
        search_text =findViewById(R.id.search_text);
        search_text.setOnClickListener(this);

        //加载list数据,默认
        listView = findViewById(R.id.main_order_all_list);
        PageOrderRequest pageOrderRequest = new PageOrderRequest(1,8);
        RetrofitFactory.getInstence().API(context)
                .userGetAllOrders(pageOrderRequest)
                .compose(this.<BaseEntity<List<Orders>>>setThread())
                .subscribe(new BaseObserver<List<Orders>>() {
                    @Override
                    protected void onSuccees(BaseEntity<List<Orders>> t) throws Exception {
                        if(t.getCode() == 0){
                            list = t.getData();
                            adapter = new OrderListAdapter(list,context);
                            listView.setAdapter(adapter);
                        }
                        else
                            errorHandle(t.getMessage(),"提示");
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        errorHandle(e.toString(),"出错");
                    }
                });

        //加载筛选
        llPrice = findViewById(R.id.ll_chooseprice);
        llHot = findViewById(R.id.ll_choose_hot);
        llTime = findViewById(R.id.ll_choose_time);
        llPrice.setOnClickListener(this);
        llHot.setOnClickListener(this);
        llTime.setOnClickListener(this);


        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Orders item =(Orders) listView.getItemAtPosition(i);
            Intent intent = new Intent(context, ActivityDetailOrder.class);
            Log.e(TAG, "现在的参数: "+ item.getId());
            intent.putExtra("itemId", item.getId());
            // 启动目标 Activity
            context.startActivity(intent);
        });
    }

    /**
     * @author chenyim
     * @Description 获取默认数据
     * @date 2023/6/27 12:43
     */
    private void setListViewDate(int dataType, PageOrderRequest pageOrderRequest) {
        if(dataType == TYPE_DEFAULT || dataType == TYPE_MONEY)
            RetrofitFactory.getInstence().API(context)
                .userGetAllOrders(pageOrderRequest)
                .compose(this.<BaseEntity<List<Orders>>>setThread())
                .subscribe(new BaseObserver<List<Orders>>() {
                    @Override
                    protected void onSuccees(BaseEntity<List<Orders>> t) throws Exception {
                        if(t.getCode() == 0){
                            list.addAll(t.getData());
                            setListView(dataType);
                        }
                        else
                            errorHandle(t.getMessage(),"提示");
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        errorHandle(e.toString(),"出错");
                    }
                });
        else
            RetrofitFactory.getInstence().API(context)
                    .userGetAllOrdersByHot(pageOrderRequest)
                    .compose(this.<BaseEntity<List<Orders>>>setThread())
                    .subscribe(new BaseObserver<List<Orders>>() {
                        @Override
                        protected void onSuccees(BaseEntity<List<Orders>> t) throws Exception {
                            if(t.getCode() == 0){
                                list.addAll(t.getData());
                                setListView(dataType);
                            }
                            else
                                errorHandle(t.getMessage(),"提示");
                        }
                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            errorHandle(e.toString(),"出错");
                        }
                    });
    }

    /**
     * @author chenyim
     * @Description 为listview渲染数据
     * @date 2023/6/27 13:09
     */
    private void setListView(int dataType) {
        //更新
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_text:
                SearchFragment searchFragment = SearchFragment.newInstance();
                searchFragment.setOnSearchClickListener(keyword -> {
                    //这里处理逻辑
                    Toast.makeText(context, keyword, Toast.LENGTH_SHORT).show();
                });
                searchFragment.showFragment(getSupportFragmentManager(),SearchFragment.TAG);
                break;
            case R.id.ll_choose_hot:
                list.clear();
                currentStrategy = TYPE_HOT;
                setListViewDate(TYPE_HOT, new PageOrderRequest(1,8));
                break;
            case R.id.ll_choose_time:
                list.clear();
                currentStrategy = TYPE_DEFAULT;
                timeUpToDown = !timeUpToDown;
                setListViewDate(TYPE_DEFAULT, new PageOrderRequest(1,8,
                        "order_create_time",timeUpToDown?"desc":"asc"));
                break;
            case R.id.ll_chooseprice:
                list.clear();
                currentStrategy = TYPE_MONEY;
                moneyUpToDown = !moneyUpToDown;
                setListViewDate(TYPE_MONEY, new PageOrderRequest(1,8,
                        "order_money", moneyUpToDown?"desc":"asc"));
                break;
        }
    }


    /**
     * @author chenyim
     * @Description 对list进行排序
     * @date 2023/6/27 14:17
     */
    private void getSortedList(List<Orders> list, int dataType) {
        if (dataType == TYPE_MONEY) {
            if (moneyUpToDown)
                Collections.sort(list, (o1, o2) -> {
                    if (o1.getOrderMoney() - o2.getOrderMoney() > 0)
                        return 1;
                    else
                        return -1;
                });
            else
                Collections.sort(list, (o1, o2) -> {
                    if (o1.getOrderMoney() - o2.getOrderMoney() > 0)
                        return -1;
                    else
                        return 1;
                });
        } else {//按日期
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (timeUpToDown) {
                Collections.sort(list, (o1, o2) -> {
                    try {
                        Date date1 = dateFormat.parse(o1.getOrderCreateTime());
                        Date date2 = dateFormat.parse(o2.getOrderCreateTime());
                        return date2.compareTo(date1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                });
            } else {
                Collections.sort(list, (o1, o2) -> {
                    try {
                        Date date1 = dateFormat.parse(o1.getOrderCreateTime());
                        Date date2 = dateFormat.parse(o2.getOrderCreateTime());
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                });
            }

        }
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
        ActivityAllOrders.this.finish();
    }
}
