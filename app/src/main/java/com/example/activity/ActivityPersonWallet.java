package com.example.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.widget.TextView;

import com.example.R;
import com.example.common.BaseActivity;
import com.example.common.BaseEntity;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.User;
import com.example.utils.ChangeTabColor;
import com.example.utils.SharedPreferencesUtil;
import com.xuexiang.xui.widget.dialog.DialogLoader;


public class ActivityPersonWallet extends BaseActivity {
    private TextView tv_mywallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_wallet);

        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#8EC5FB"),true);
        tv_mywallet = findViewById(R.id.mywallet);
        initdb();

    }
    private void initdb(){
        User user = new User();
        user.setId(((User) SharedPreferencesUtil.getObject(this, "User")).getId());
        RetrofitFactory.getInstence().API(this)
                .getSingleUser(user)
                .compose(this.<BaseEntity<User>>setThread())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    protected void onSuccees(BaseEntity<User> t) throws Exception {
                        if(t.getCode() == 0)
                            tv_mywallet.setText(String.valueOf(t.getData().getMoney()));
                        else
                            errorHandle(t.getMessage(),"提示");
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        errorHandle(e.toString(),"提示");
                    }
                });
    }
    /**
     * @author chenyim
     * @Description 网络请求统一错误处理
     * @date 2023/6/25 16:16
     */
    public void errorHandle(String msg, String title){
        DialogLoader.getInstance().showTipDialog(this,R.drawable.warning,title,
                msg,"确认"
                , (dialogInterface, i) -> {
                    //点击确认按钮后关闭提示框
                    dialogInterface.dismiss();
                });
    }
}