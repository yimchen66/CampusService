package com.example.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.common.ErrorCode;
import com.example.R;
import com.example.common.BaseActivity;
import com.example.common.BaseEntity;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.User;
import com.example.model.dto.UserDTO;
import com.example.utils.ChangeTabColor;
import com.example.utils.SharedPreferencesUtil;
import com.xuexiang.xui.widget.button.shadowbutton.ShadowButton;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xutil.common.StringUtils;

public class ActivityLoginUserName extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "login";
    private ShadowButton btnUserLogin,btnUserRegister;
    private MaterialEditText edTextUserName,edTextUserPassword;
    private Context context;
    private boolean loginIsOk =false;
    private String loginContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_name);
        context = this;
        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#FFFFFFFF"),false);
        initViews();


    }

    /**
     * @author chenyim
     * @Description 初始化视图
     * @date 2023/6/25 15:48
     */
    public void initViews(){
        btnUserLogin = findViewById(R.id.btn_username_login);
        btnUserRegister = findViewById(R.id.btn_username_register);
        edTextUserName = findViewById(R.id.edtext_user_name);
        edTextUserPassword = findViewById(R.id.edtext_user_password);

        btnUserLogin.setOnClickListener(this);
        btnUserRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_username_login:
                userLogin();
                break;
            case R.id.btn_username_register:
                userRegister();
                break;
        }
    }

    /**
     * @author chenyim
     * @Description 登录并跳转
     * @date 2023/6/25 15:51
     */
    private void userLogin() {
        String userNameText = edTextUserName.getText().toString();
        String passwordText = edTextUserPassword.getText().toString();
        if( StringUtils.isEmpty(userNameText) || StringUtils.isEmpty(passwordText) ||
                userNameText.length() > 10 || passwordText.length() > 16) {
            errorHandle("信息填写不规范,请重新填写", "提示");
            return;
        }
        User user = new User();
        user.setUserName(userNameText);
        user.setPassword(passwordText);
        userLoginByUserName(user);
    }

    /**
     * @author chenyim
     * @Description 注册
     * @date 2023/6/25 15:51
     */
    private void userRegister() {
        userRegisterByUserName();
    }

    /**
     * @author chenyim
     * @Description 登录
     * @date 2023/6/25 19:31
     */
    private void userLoginByUserName(User user) {
        RetrofitFactory.getInstence().API(context)
                .getUserLogin(user)
                .compose(this.<BaseEntity<UserDTO>>setThread())
                .subscribe(new BaseObserver<UserDTO>() {
                    @Override
                    protected void onSuccees(BaseEntity<UserDTO> t) throws Exception {
                        int code = t.getCode();
                        if( code == ErrorCode.NO_USERNAME_ERROR.getCode())
                            errorHandle(ErrorCode.NO_USERNAME_ERROR.getMessage(),"提示");
                        else if( code == ErrorCode.PASSWORD_ERROR.getCode() )
                            errorHandle(ErrorCode.PASSWORD_ERROR.getMessage(),"提示");
                        else if( code == ErrorCode.SUCCESS.getCode()){//登录成功，存缓存
                            UserDTO data = t.getData();
                            SharedPreferencesUtil.putString(context,"token",data.getToken());
                            SharedPreferencesUtil.putObject(context,"User",data.getUser());
                            jumpToCertainActivity("com.example.MainActivity");
                        }
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        errorHandle(e.toString(),"出错");
                    }
                });
    }

    /**
     * @author chenyim
     * @Description 注册
     * @date 2023/6/25 15:52
     */
    private void userRegisterByUserName(){
        loginIsOk = false;
        loginContent = "";
        String userNameText = edTextUserName.getText().toString();
        String passwordText = edTextUserPassword.getText().toString();
        if( StringUtils.isEmpty(userNameText) || StringUtils.isEmpty(passwordText) ||
            userNameText.length() > 10 || passwordText.length() > 16){
            loginContent = "信息填写不规范,请重新填写";
            successHandle(null,null);
        }
        else{
            //发起网络请求，查看用户名是否重复
            RetrofitFactory.getInstence().API(context)
                    .checkUserNameAvailable(userNameText)
                    .compose(this.<BaseEntity<String>>setThread())
                    .subscribe(new BaseObserver<String>() {
                        @Override
                        protected void onSuccees(BaseEntity<String> t) throws Exception {
                            if(t.getCode() == 0){
                                loginIsOk = true;
                                loginContent = "可用";
                            }else{
                                loginIsOk = false;
                                loginContent = t.getMessage();
                            }
                            successHandle(userNameText,passwordText);
                        }
                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            errorHandle(e.toString(),"出错");
                            Log.e(TAG, e.toString());
                        }
                    });
        }
    }

    /**
     * @author chenyim
     * @Description 正确的处理
     * @date 2023/6/25 17:47
     */
    private void successHandle(String userNameText, String passwordText) {
        if(!loginIsOk){
            DialogLoader.getInstance().showTipDialog(context,R.drawable.warning,"提示",
                    loginContent,"确认"
                    , (dialogInterface, i) -> {
                        //点击确认按钮后关闭提示框
                        dialogInterface.dismiss();
                    });
        }else{
            User user = new User();
            user.setUserName(userNameText);
            user.setNickName(userNameText);
            user.setPassword(passwordText);
            user.setSex("保密");
            user.setLocation("上海");
//            user.setFigureUrl(passwordText);
            //注册
            RetrofitFactory.getInstence().API(context)
                    .userNameRegister(user)
                    .compose(this.<BaseEntity<User>>setThread())
                    .subscribe(new BaseObserver<User>() {
                        @Override
                        protected void onSuccees(BaseEntity<User> t) throws Exception {
                            Log.e(TAG, t.getMessage() );
                            Log.e(TAG, String.valueOf(t.getCode()));
                            //注册成功，则自动发起登录请求并将token存到缓存
                            userLoginByUserName(user);
                        }
                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            errorHandle(e.toString(), "出错");
                        }
                    });
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
            Intent intent = new Intent(ActivityLoginUserName.this, aClass);
            startActivity(intent);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString() );
        }
    }
}