package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.example.R;
import com.example.adapter.MyTransHistoryAdapter;
import com.example.common.BaseActivity;
import com.example.common.BaseEntity;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.User;
import com.example.model.dto.TransHistoryDTO;
import com.example.utils.ChangeTabColor;
import com.example.utils.SharedPreferencesUtil;
import com.xuexiang.xui.widget.dialog.DialogLoader;

import java.io.Serializable;
import java.util.List;

public class ActivityTransHistory extends BaseActivity {
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String useropenid,nickname;
    private Context context;
    private List<TransHistoryDTO> list;
    private ListView lv_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_history);
        context = this;
        toolbar = findViewById(R.id.tb_moneytrans);
        lv_money = findViewById(R.id.moneytrans_list);
        toolbar.setTitle("交易明细");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(view -> finish());
        ChangeTabColor.setStatusBarColor(this,Color.parseColor("#8EC5FB"),true);

        initdb();
    }

    private void initdb(){
        User user = SharedPreferencesUtil.getObject(context, "User");
        RetrofitFactory.getInstence().API(context)
                .userGetTransHistory(user.getId())
                .compose(this.<BaseEntity<List<TransHistoryDTO>>>setThread())
                .subscribe(new BaseObserver<List<TransHistoryDTO>>() {
                    @Override
                    protected void onSuccees(BaseEntity<List<TransHistoryDTO>> t) throws Exception {
                        if(t.getCode() == 0){
                            MyTransHistoryAdapter adapter = new MyTransHistoryAdapter(context,t.getData());
                            lv_money.setAdapter(adapter);
                        }else
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
     * @Description 网络请求统一错误处理
     * @date 2023/6/25 16:16
     */
    public void errorHandle(String msg, String title){
        DialogLoader.getInstance().showTipDialog(context,R.drawable.warning,title,
                msg,"确认"
                , (dialogInterface, i) -> {
                    //点击确认按钮后关闭提示框
                    dialogInterface.dismiss();
                });
    }
}