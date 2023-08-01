package com.example.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.MainActivity;
import com.example.R;
import com.example.common.BaseActivity;
import com.example.common.BaseEntity;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.User;
import com.example.model.dto.UserDTO;
import com.example.model.sqlliteentity.HistoryTrack;
import com.example.utils.ChangeTabColor;
import com.example.utils.SharedPreferencesUtil;

import org.litepal.crud.DataSupport;


public class ActivityWelcome extends BaseActivity {
    private static final String TAG = "chenchen";
    private boolean isLogined = false;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;
        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#FFFFFFFF"),false);
//        SharedPreferencesUtil.putString(context,"token","");
//
//        DataSupport.deleteAll(com.example.model.sqlliteentity.User.class);
//        DataSupport.deleteAll(com.example.model.domain.UserCreateOrder.class);
//        DataSupport.deleteAll(com.example.model.domain.UserAcceptOrder.class);



        //通过缓存判断用户状态
        String token = SharedPreferencesUtil.getString(this, "token");
        //token如果不存在，则一定处于未登录状态
        //如果存在，则需要发起请求判断是否过期,传入参数username和password
        //过期返回码40500
        if(token != null && !token.equals("")) {
            //先发送检验token请求
            User user = SharedPreferencesUtil.getObject(this, "User");

            UserDTO userDTO = new UserDTO(user,token);
            RetrofitFactory.getInstence().API(this)
                    .checkToken(userDTO)
                    .compose(this.<BaseEntity<Boolean>>setThread())
                    .subscribe(new BaseObserver<Boolean>() {
                        @Override
                        protected void onSuccees(BaseEntity<Boolean> t) throws Exception {
                            Log.e(TAG, "-----------: "+t);
                            //如果没有过期，则登录状态符合
                            if(t.getData())
                                isLogined = true;
                            //如果过期则跳转登录界面
                        }
                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            Log.e(TAG, "-----------: "+e);
                        }
                    });
        }
        handler.sendEmptyMessageDelayed(0,3000);

    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            Intent intent;
            if(isLogined){
                intent=new Intent(ActivityWelcome.this,MainActivity.class);
            }else
                intent=new Intent(ActivityWelcome.this,ActivityLogin.class);
            startActivity(intent);
            finish();
            super.handleMessage(msg);
        }
    };

}