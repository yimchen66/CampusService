package com.example;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.adapter.MyViewPagerAdapter;
import com.example.common.BaseActivity;
import com.example.common.BaseEntity;
import com.example.fragment.BlankFragment_FisPage;
import com.example.fragment.BlankFragment_SecPage;
import com.example.fragment.BlankFragment_ThiPage;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.User;
import com.example.model.dto.UserDTO;
import com.example.utils.ChangeTabColor;
import com.example.utils.SharedPreferencesUtil;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "chenchen";
//    public static final int REQUEST_CODE_CAMERA = 103; //相机
//    public static final int REQUEST_CODE_ALBUM = 102; //相册
//    private static final String AUTHORITIES = "com.example.campusservice.fileprovider";
//    private Uri photoUri;//记录图片地址标识
//    private File cropFile ;//记录图片地址
//    private String fileName;//记录图片地址用于命名
    private static final int REQUEST_CODE_ALBUM_FRAGMENT_A = 1001;
    private static final int REQUEST_CODE_CAMERA_FRAGMENT_A = 1002;
    private static final int REQUEST_CODE_ALBUM_FRAGMENT_B = 103;
    private static final int REQUEST_CODE_CAMERA_FRAGMENT_B = 2002;
    private static final int REQUEST_CODE_UCROP = UCrop.REQUEST_CROP;

    ViewPager2 viewpager;
    private LinearLayout llmain,llmess,llperson;
    private ImageView ivmain,ivmess,ivperson,ivcurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPaper();
        initTavLayout();


    }

    private void initTavLayout() {
        llmain = findViewById(R.id.tab_main);
        llmess = findViewById(R.id.tab_message);
        llperson = findViewById(R.id.tab_personal);

        llmain.setOnClickListener(this);
        llmess.setOnClickListener(this);
        llperson.setOnClickListener(this);

        ivmain = findViewById(R.id.tab_main_pic);
        ivmess = findViewById(R.id.tab_message_pic);
        ivperson = findViewById(R.id.tab_personal_pic);

        //默认选择订单
        ivmain.setSelected(true);
        ivcurrent = ivmain;
    }

    private void initPaper() {
        viewpager = findViewById(R.id.id_viewpaper);
        List<Fragment> list = new ArrayList<>();
        list.add(new BlankFragment_FisPage(this));
        list.add(new BlankFragment_SecPage(this));
        list.add(new BlankFragment_ThiPage(this));
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(),getLifecycle(),list);
        viewpager.setAdapter(adapter);
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            //滑动过度动画
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            //页面选中 添加滑动效果
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changePager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    //设置页面左右滑动效果
    private void changePager(int position) {
        ivcurrent.setSelected(false);
        switch (position){
            case 0:
                ivmain.setSelected(true);
                ivcurrent = ivmain;
                ChangeTabColor.setStatusBarColor(this, Color.parseColor("#4FD494"),true);
                break;
            case 1:
                ivmess.setSelected(true);
                ivcurrent = ivmess;
                ChangeTabColor.setStatusBarColor(this,Color.parseColor("#f0f0f0"),false);
                break;
            case 2:
                ivperson.setSelected(true);
                ivcurrent = ivperson;
                ChangeTabColor.setStatusBarColor(this,Color.parseColor("#8EC5FB"),true);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tab_main:
                viewpager.setCurrentItem(0);
                break;
            case R.id.tab_message:
                viewpager.setCurrentItem(1);
                break;
            case R.id.tab_personal:
                viewpager.setCurrentItem(2);
                break;
        }
    }


    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);

    //fragment触摸事件分发，将触摸事件分发给每个能够响应的fragment
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            if(listener != null) {
                listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }
    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener) ;
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
//            List<Fragment> fragments = getSupportFragmentManager().getFragments();
//            for (Fragment mFragment : fragments) {
//                mFragment.onActivityResult(requestCode, resultCode, data);
//            }
//        }
//    }
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
        if (requestCode == REQUEST_CODE_ALBUM_FRAGMENT_A || requestCode == REQUEST_CODE_CAMERA_FRAGMENT_A) {
            SharedPreferencesUtil.putBoolean(this,"isFromFragmentA",true);
            handleFragmentResult(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_CODE_ALBUM_FRAGMENT_B || requestCode == REQUEST_CODE_CAMERA_FRAGMENT_B) {
            SharedPreferencesUtil.putBoolean(this,"isFromFragmentA",false);
            handleFragmentResult(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_CODE_UCROP) {
            handleUCropResult(requestCode, resultCode, data);
        }
    }
}

    private void handleFragmentResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof BlankFragment_SecPage && requestCode == REQUEST_CODE_ALBUM_FRAGMENT_A) {
                fragment.onActivityResult(requestCode, resultCode, data);
            } else if (fragment instanceof BlankFragment_SecPage && requestCode == REQUEST_CODE_CAMERA_FRAGMENT_A) {
                fragment.onActivityResult(requestCode, resultCode, data);
            } else if (fragment instanceof BlankFragment_ThiPage && requestCode == REQUEST_CODE_ALBUM_FRAGMENT_B) {
                fragment.onActivityResult(requestCode, resultCode, data);
            } else if (fragment instanceof BlankFragment_ThiPage && requestCode == REQUEST_CODE_CAMERA_FRAGMENT_B) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void handleUCropResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof BlankFragment_SecPage &&
                    SharedPreferencesUtil.getBoolean(this,"isFromFragmentA",true)) {
                fragment.onActivityResult(requestCode, resultCode, data);
            } else if (fragment instanceof BlankFragment_ThiPage &&
                    !SharedPreferencesUtil.getBoolean(this,"isFromFragmentA",true)) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    //返回键，不退出应用
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void fun(){
        User user = new User();
        user.setUserName("johndoe");
        user.setPassword("password1");
        RetrofitFactory.getInstence().API(this)
                .getUserLogin(user)
                .compose(this.<BaseEntity<UserDTO>>setThread())
                .subscribe(new BaseObserver<UserDTO>() {
                    @Override
                    protected void onSuccees(BaseEntity<UserDTO> t) throws Exception {
                        Log.e(TAG, "成功   "+t.getData().toString());
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        Log.e(TAG, e.toString()+"   "+isNetWorkError);
                    }
                });
    }



}