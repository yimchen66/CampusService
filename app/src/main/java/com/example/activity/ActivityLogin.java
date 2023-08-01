package com.example.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MainActivity;
import com.example.R;
import com.example.common.BaseActivity;
import com.example.common.BaseEntity;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.User;
import com.example.model.dto.UserDTO;
import com.example.utils.ChangeTabColor;
import com.example.utils.SharedPreferencesUtil;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.xuexiang.xui.widget.button.ButtonView;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityLogin extends BaseActivity implements View.OnClickListener{
    public static final String APPID = "102029896";
    public static final String AUTHORITIES = "com.tencent.login.campusservice.fileprovider";
    private static final String TAG = "qq";
    private Tencent mTencent;
    private BaseUiListener mloginlistener;

    private TextView userPhoneNumber;
    private ImageView loginQQ,loginUserName;
    private ButtonView loginPhone;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#FFFFFFFF"),false);
        context = this;
        mTencent = Tencent.createInstance(APPID,context, AUTHORITIES);
        Tencent.setIsPermissionGranted(true);
        //渲染视图
        initViews();

        loginQQ.setOnClickListener(this);

    }

    /**
     * @author chenyim
     * @Description 加载视图
     * @date 2023/6/25 14:42
     */
    public void initViews(){
        userPhoneNumber = findViewById(R.id.user_phone_number);
        loginQQ = findViewById(R.id.login_qq);
        loginUserName = findViewById(R.id.login_userName);
        loginPhone = findViewById(R.id.login_phone);

        loginQQ.setOnClickListener(this);
        loginUserName.setOnClickListener(this);
        loginPhone.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //qq登录
            case R.id.login_qq:
                loginByQQ();
                break;
            //账号密码登录
            case R.id.login_userName:
                loginByUserName();
                break;
            case R.id.login_phone:
                loginByPhone();
                break;
        }
    }

    /**
     * @author chenyim
     * @Description 通过账号密码登录
     * @date 2023/6/25 14:36
     */
    private void loginByUserName() {
        jumpToCertainActivity("com.example.activity.ActivityLoginUserName");
    }


    /**
     * @author chenyim
     * @Description 通过qq登录
     * @date 2023/6/25 14:36
     */
    private void loginByQQ(){
        mTencent = Tencent.createInstance(APPID,this, AUTHORITIES);
        mloginlistener = new BaseUiListener();
        mTencent.login((Activity) context, "all", mloginlistener);
    }

    /**
     * @author chenyim
     * @Description 通过手机号一键登录
     * @date 2023/6/25 14:36
     */
    private void loginByPhone(){

    }



    /**
     * @author chenyim
     * @Description 回调
     * @date 2023/6/25 14:42
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResultData(requestCode,resultCode,data,mloginlistener);
        if(Constants.REQUEST_API == requestCode){
            if(Constants.REQUEST_LOGIN ==  requestCode){
                Tencent.handleResultData(data,mloginlistener);
            }
        }
    }

    /**
     * @author chenyim
     * @Description qq登录回调处理
     * @date 2023/6/25 14:42
     */
    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            JSONObject jsonObject = (JSONObject) o;
            try {
                String openid = jsonObject.getString("openid");
//                Log.e(TAG, "onComplete:  openid  "+openid);
                String access_token = jsonObject.getString("access_token");
                String expires_in = jsonObject.getString("expires_in");
                mTencent.setOpenId(openid);
                mTencent.setAccessToken(access_token,expires_in);
                QQToken qqToken = mTencent.getQQToken();
                UserInfo userInfo = new UserInfo(context,qqToken);
                userInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject2 = (JSONObject) o;
                        String nickname = jsonObject2.optString("nickname");
                        String figureurl_qq_2 =  jsonObject2.optString("figureurl_qq_2");
                        String location = jsonObject2.optString("province") + jsonObject2.optString("city");
                        String sex = jsonObject2.optString("gender");
                        saveUser(openid,nickname,figureurl_qq_2,sex,location);
//                        Glide.with(context).load(figureurl_qq_2).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv_login_pic);
                    }
                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "onError: 内错误   "+uiError );
                    }
                    @Override
                    public void onCancel() {                    }
                    @Override
                    public void onWarning(int i) {                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onError(UiError uiError) {
            Log.e(TAG, "onError: 外错误   "+uiError );
        }
        @Override
        public void onCancel() {
        }
        @Override
        public void onWarning(int i) {
        }
    };

    //保存qq登录
    public void saveUser(String openId, String nick_Name, String figureUrl,
                            String sex, String location){
        User user = new User();
        user.setId(openId);
        user.setFigureUrl(figureUrl);
        user.setNickName(nick_Name);
        user.setUserName(nick_Name);
        user.setPassword(openId);
        user.setLocation(location);
        user.setSex(sex);
        //发送网络请求,请求登录或注册，然后保存最新token
        RetrofitFactory.getInstence().API(context)
                .qqLogin(user)
                .compose(this.<BaseEntity<UserDTO>>setThread())
                .subscribe(new BaseObserver<UserDTO>() {
                    @Override
                    protected void onSuccees(BaseEntity<UserDTO> t) throws Exception {
                        //没问题
                        if(t.getCode() == 0 && t.getData() != null){
                            User user1 = t.getData().getUser();
                            String token = t.getData().getToken();
                            SharedPreferencesUtil.putString(context,"token",token);
                            SharedPreferencesUtil.putObject(context,"User",user1);

                            //跳转主页面
                            jumpToCertainActivity("com.example.MainActivity");
                        }
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                    }
                });
    }

    /**
     * @author chenyim
     * @Description 通过反射跳转页面
     * @date 2023/6/25 14:40
     */
    private void jumpToCertainActivity(String className){
        try {
            Class<?> aClass = Class.forName(className);
            Intent intent = new Intent(ActivityLogin.this, aClass);
            startActivity(intent);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString() );
        }
    }
}