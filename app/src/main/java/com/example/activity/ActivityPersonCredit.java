package com.example.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.R;
import com.example.common.BaseActivity;
import com.example.common.BaseEntity;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.User;
import com.example.utils.ChangeTabColor;
import com.example.utils.SharedPreferencesUtil;
import com.xuexiang.xui.widget.dialog.DialogLoader;

import java.util.Random;

import io.netopen.hotbitmapgg.view.NewCreditSesameView;

public class ActivityPersonCredit extends BaseActivity {
    private NewCreditSesameView newCreditSesameView;
    private final int[] mColors = new int[] {
            0xFF8EC5FB,
            0xFF8EC5FB,
            0xFF8EC5FB,
            0xFF8EC5FB
    };
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_credit);
        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#8EC5FB"),true);
        newCreditSesameView = (NewCreditSesameView) findViewById(R.id.sesame_view);
        linearLayout = (LinearLayout) findViewById(R.id.ll_credit);
        linearLayout.setBackgroundColor(mColors[0]);

        User user = new User();
        user.setId(((User) SharedPreferencesUtil.getObject(this, "User")).getId());
        RetrofitFactory.getInstence().API(this)
                .getSingleUser(user)
                .compose(this.<BaseEntity<User>>setThread())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    protected void onSuccees(BaseEntity<User> t) throws Exception {
                        if(t.getCode() == 0){
                            double credit = t.getData().getCredit();
                            int roundedCredit = (int) Math.round(credit);
                            newCreditSesameView.setSesameValues(roundedCredit);
                            startColorChangeAnim();
                        }
                        else
                            errorHandle(t.getMessage(),"提示");
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        errorHandle(e.toString(),"提示");
                    }
                });

    }

    public void startColorChangeAnim() {
        ObjectAnimator animator = ObjectAnimator.ofInt(linearLayout, "backgroundColor", mColors);
        animator.setDuration(3000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
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