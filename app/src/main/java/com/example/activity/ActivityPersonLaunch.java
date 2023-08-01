package com.example.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.R;
import com.example.adapter.MyAcceptListAdapter;
import com.example.adapter.MyFinishListAdapter;
import com.example.adapter.MyLaunchListAdapter;
import com.example.adapter.MyTransListAdapter;
import com.example.common.BaseActivity;
import com.example.common.BaseEntity;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.Orders;
import com.example.model.domain.User;
import com.example.model.domain.UserAcceptOrder;
import com.example.model.domain.UserCreateOrder;
import com.example.model.dto.DetailedOrderDTO;
import com.example.utils.ChangeTabColor;
import com.example.utils.SharedPreferencesUtil;
import com.wyt.searchbox.SearchFragment;
import com.xuexiang.xui.widget.dialog.DialogLoader;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ActivityPersonLaunch extends BaseActivity {
    public static final int PERSON_LAUNCH = 888;
    public static final int PERSON_ACCEPT = 889;
    public static final int PERSON_TRANS = 900;
    public static final int PERSON_FINISH = 901;
    public static final int PERSON_ALL = 902;
    private Context context;
    private int currentRequest;
    private EditText search_text;
    private ListView listView;
    private List<Orders> list = new ArrayList<>();
    private User user;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_launch);
        // 获取传递的参数
        Intent intent = getIntent();
        currentRequest = intent.getIntExtra("source",PERSON_ALL);
        context = this;
        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#8EC5FB"),true);
        initView();
        //获取数据
        switch (currentRequest){
            case PERSON_LAUNCH:
                list.clear();
                List<UserCreateOrder> list_userCreate = DataSupport.findAll(UserCreateOrder.class);
                for (UserCreateOrder userCreateOrder : list_userCreate) {
                    String orderJSON = userCreateOrder.getOrder();
                    Orders orders = JSON.parseObject(orderJSON, Orders.class);
                    list.add(orders);
                    adapter = new MyLaunchListAdapter(list,context,user);
                }
                listView.setAdapter(adapter);
                break;
            case PERSON_ACCEPT:
                list.clear();
                List<UserAcceptOrder> list_accept = DataSupport.findAll(UserAcceptOrder.class);
                adapter = new MyAcceptListAdapter(list_accept,context);
                listView.setAdapter(adapter);
                break;
            case PERSON_TRANS:
                list.clear();
                RetrofitFactory.getInstence().API(context)
                        .userGetOrderTrans(user.getId())
                        .compose(this.<BaseEntity<List<DetailedOrderDTO>>>setThread())
                        .subscribe(new BaseObserver<List<DetailedOrderDTO>>() {
                            @Override
                            protected void onSuccees(BaseEntity<List<DetailedOrderDTO>> t) throws Exception {
                                if(t.getCode() == 0){
                                    List<DetailedOrderDTO> data = t.getData();
                                    adapter = new MyTransListAdapter(data,context);
                                    listView.setAdapter(adapter);
                                }else
                                    errorHandle(t.getMessage(),"提示");
                            }
                            @Override
                            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                                errorHandle(e.toString(),"提示");
                            }
                        });
                break;
            case PERSON_FINISH:
                list.clear();
                RetrofitFactory.getInstence().API(context)
                        .userGetOrderFinish(user.getId())
                        .compose(this.<BaseEntity<List<DetailedOrderDTO>>>setThread())
                        .subscribe(new BaseObserver<List<DetailedOrderDTO>>() {
                            @Override
                            protected void onSuccees(BaseEntity<List<DetailedOrderDTO>> t) throws Exception {
                                if(t.getCode() == 0){
                                    List<DetailedOrderDTO> data = t.getData();
                                    adapter = new MyFinishListAdapter(data,context);
                                    listView.setAdapter(adapter);
                                }else
                                    errorHandle(t.getMessage(),"提示");
                            }
                            @Override
                            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                                errorHandle(e.toString(),"提示");
                            }
                        });
                break;

        }


        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Object itemAtPosition = listView.getItemAtPosition(i);
            Intent intent2 = new Intent(context, ActivityPersonCheckOrder.class);
//            Log.e(TAG, "现在的参数: "+ item.getId());
//            intent2.putExtra("itemId", itemAtPosition);
            if(itemAtPosition instanceof Orders){
                intent2.putExtra("itemId", ((Orders) itemAtPosition).getId());
            }
            else if(itemAtPosition instanceof UserAcceptOrder){
                String json_orders = ((UserAcceptOrder) itemAtPosition).getOrders();
                Orders orders = JSON.parseObject(json_orders, Orders.class);
                intent2.putExtra("itemId", orders.getId());
            }else if(itemAtPosition instanceof  DetailedOrderDTO){
                intent2.putExtra("itemId", ((DetailedOrderDTO) itemAtPosition).getOrders().getId());
            }
            // 启动目标 Activity
            context.startActivity(intent2);
        });

    }

    public void initView(){
        //搜获
        search_text = findViewById(R.id.search_text);
        search_text.setOnClickListener(v -> {
            SearchFragment searchFragment = SearchFragment.newInstance();
            searchFragment.setOnSearchClickListener(keyword -> {
                //这里处理逻辑
                Toast.makeText(context, keyword, Toast.LENGTH_SHORT).show();
            });
            searchFragment.showFragment(getSupportFragmentManager(),SearchFragment.TAG);
        });

        listView = findViewById(R.id.person_order_accept_activity);

        user = SharedPreferencesUtil.getObject(context, "User");
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
                    }
                });
    }
}