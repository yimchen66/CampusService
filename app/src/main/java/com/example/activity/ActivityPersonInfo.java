package com.example.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.R;
import com.example.common.BaseActivity;
import com.example.common.BaseEntity;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.User;
import com.example.model.request.user.UpdateRequest;
import com.example.utils.ChangeTabColor;
import com.example.utils.SharedPreferencesUtil;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;
import com.xuexiang.xutil.common.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ActivityPersonInfo extends BaseActivity implements LocationListener {
    private Context context;
    private Toolbar toolbar;
    private ImageView iv_touxiang;
    private TextView tv_nickName,tv_location,tv_sex;
    private LinearLayout ll_location;
    // 定义双精度类型的经纬度
    private Double longitude,latitude;
    // 定义位置管理器
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#8EC5FB"),true);
        context = this;
        //设置bar
        toolbar = findViewById(R.id.tb_info);
        toolbar.setTitle("详细情况");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(view -> exitActivity());

        initView();
        // 判断当前是否拥有使用GPS的权限
        if (ActivityCompat.checkSelfPermission(ActivityPersonInfo.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // 申请权限
            ActivityCompat.requestPermissions(ActivityPersonInfo.this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    /**
     * @author chenyim
     * @Description 初始化视图
     * @date 2023/6/28 19:00
     */
    public void initView(){
        tv_nickName = findViewById(R.id.person_info_nickName);
        tv_location = findViewById(R.id.location);
        tv_sex = findViewById(R.id.person_info_sex);
        iv_touxiang = findViewById(R.id.person_info_touxaing);
        ll_location = findViewById(R.id.ll_location);

        User user1 = SharedPreferencesUtil.getObject(context, "User");
        RetrofitFactory.getInstence().API(context)
                .getSingleUser(user1)
                .compose(this.<BaseEntity<User>>setThread())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    protected void onSuccees(BaseEntity<User> t) throws Exception {
                        if(t.getCode() == 0){
                            User data = t.getData();
                            tv_location.setText(data.getLocation());
                            tv_sex.setText(data.getSex());
                            tv_nickName.setText(data.getNickName());
                            Glide.with(context)
                                    .load(data.getFigureUrl())
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                    .placeholder(R.drawable.defaulttouxaing)//图片加载出来前，显示的图片
                                    .fallback( R.drawable.defaulttouxaing) //url为空的时候,显示的图片
                                    .error(R.drawable.defaulttouxaing)//图片加载失败后，显示的图片
                                    .into(iv_touxiang);
                        }else
                            errorHandle(t.getMessage(),"提示");
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        errorHandle(e.toString(),"错误");
                    }
                });
        ll_location.setOnClickListener(v -> {
            getLocation();
        });
        tv_nickName.setOnClickListener(v -> {
            //对用户需要输入的内容做一些限制
            InputInfo inputInfo = new InputInfo(InputType.TYPE_CLASS_TEXT);
            inputInfo.setHint("hint:请输入昵称");

            DialogLoader.getInstance().showInputDialog(ActivityPersonInfo.this
                    , R.drawable.warning,
                    "请输入您的昵称", "请在输入框中输入您的昵称", inputInfo,
                    (dialog, input) -> {
                    }, "确认", (dialogInterface, i) -> {
                        String input = ((MaterialDialog)dialogInterface).
                                getInputEditText().getText().toString();
                        if(!StringUtils.isEmpty(input)){
                            User user = SharedPreferencesUtil.getObject(context, "User");
                            user.setNickName(input);
                            updateUser(user);
                        }
                        dialogInterface.dismiss();
                    }, "取消", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                    });
        });

        tv_sex.setOnClickListener(v -> {
            final String items[] = {"保密","男","女"};
            DialogLoader.getInstance().showSingleChoiceDialog(ActivityPersonInfo.this
                    ,"选择您的性别"
                    ,items
                    ,0
                    , (dialog, i) -> {
                        //点击确认按钮后，执行该方法
                        User user = SharedPreferencesUtil.getObject(context, "User");
                        user.setSex(items[i]);
                        updateUser(user);
                    },"确认"
                    ,"关闭");
        });
    }

    /**
     * @author chenyim
     * @Description 更新信息
     * @date 2023/6/28 19:05
     */
    public void updateUser(User user){
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setUser(user);
        updateRequest.setPassword(user.getPassword());

        RetrofitFactory.getInstence().API(context)
                .userUpdateInfo(updateRequest)
                .compose(this.<BaseEntity<User>>setThread())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    protected void onSuccees(BaseEntity<User> t) throws Exception {
                        if(t.getCode() == 0){
                            initView();
                            errorHandle("更新成功","提示");
                        }
                        else
                            errorHandle(t.getMessage(),"提示");
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        errorHandle(e.toString(),"错误");
                    }
                });
        SharedPreferencesUtil.putObject(context,"User",user);
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
     * @Description 回退页面
     * @date 2023/6/26 21:12
     */
    private void exitActivity() {
        Intent intent=new Intent();
        setResult(0,intent);
        ActivityPersonInfo.this.finish();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        // 获取当前位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, ActivityPersonInfo.this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        // 获取当前纬度
        latitude = location.getLatitude();
        // 获取当前经度
        longitude = location.getLongitude();
        // 定义位置解析
        Geocoder geocoder = new Geocoder(ActivityPersonInfo.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = addresses.get(0);
            String info = address.getAddressLine(0);// + // 获取国家名称
            // 赋值
            tv_location.setText(info);
            User user = SharedPreferencesUtil.getObject(context, "User");
            user.setLocation(info);
            updateUser(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
        locationManager.removeUpdates(this);
    }

    // 当前定位提供者状态
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("onStatusChanged", provider);
    }

    // 任意定位提高者启动执行
    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.e("onProviderEnabled", provider);
    }

    // 任意定位提高者关闭执行
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.e("onProviderDisabled", provider);
    }
}