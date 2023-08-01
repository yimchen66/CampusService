package com.example.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.R;
import com.example.activity.ActivityPersonInfo;
import com.example.common.BaseEntity;
import com.example.common.BaseFragment;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.Orders;
import com.example.model.domain.User;
import com.example.model.domain.UserCreateOrder;
import com.example.model.request.order.CreateOrderRequest;
import com.example.utils.SharedPreferencesUtil;
import com.example.utils.TimeUtil;
import com.example.utils.UploadUtil;
import com.example.widget.DialogManager;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.tip.ToastUtils;
import com.yalantis.ucrop.UCrop;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BlankFragment_SecPage extends BaseFragment implements LocationListener,View.OnClickListener
        {
//    public static final int REQUEST_CODE_CAMERA = 110; //相机
//    public static final int REQUEST_CODE_ALBUM = 111; //相册
    private static final int REQUEST_CODE_ALBUM = 1001;
    private static final int REQUEST_CODE_CAMERA = 1002;
    private static final int REQUEST_CODE_UCROP = UCrop.REQUEST_CROP;
    private static final String AUTHORITIES = "com.example.campusservice.fileprovider";
    private static final String TAG = "neworder";
    private Uri photoUri;//记录图片地址标识
    private File cropFile ;//记录图片地址
    private String fileName;//记录图片地址用于命名
    View root;
    Context context;
    private RadiusImageView image1, image2,image3,image4,image5,image6;
    private LinearLayout llLocation;
    private MaterialEditText et_orderTitle;
    private EditText et_orderContent;
    private TextView tv_orderLocation,tv_orderMoney,tv_orderDeadline,tv_orderType;
    private ButtonView btn_submit;
    // 定义双精度类型的经纬度
    private Double longitude,latitude;
    // 定义位置管理器
    private LocationManager locationManager;
    private List<String> list_CurImages = new ArrayList<>();
    private int curImagesCount = 0;

    public BlankFragment_SecPage() {
    }

    public BlankFragment_SecPage(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "调用pause   ");
    }

    @Override
    public void onResume() {
        Log.e(TAG, "调用resume: ");
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.e(TAG, "调用start: ");
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(null == root){
            root = inflater.inflate(R.layout.fragment_blank__sec_page, container, false);
        }
        // 判断当前是否拥有使用GPS的权限
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // 申请权限
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        initView();





        return root;
    }

    public void initView(){
        image1 = root.findViewById(R.id.image1);
        image2 = root.findViewById(R.id.image2);
        image3 = root.findViewById(R.id.image3);
        image4 = root.findViewById(R.id.image4);
        image5 = root.findViewById(R.id.image5);
        image6 = root.findViewById(R.id.image6);
        image2.setVisibility(View.INVISIBLE);
        image3.setVisibility(View.INVISIBLE);
        image4.setVisibility(View.GONE);
        image5.setVisibility(View.GONE);
        image6.setVisibility(View.GONE);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);
        image5.setOnClickListener(this);
        image6.setOnClickListener(this);

        llLocation = root.findViewById(R.id.ll_location);

        et_orderTitle = root.findViewById(R.id.new_order_order_title);
        et_orderContent = root.findViewById(R.id.new_order_content);

        tv_orderLocation = root.findViewById(R.id.location);
        tv_orderMoney = root.findViewById(R.id.new_order_money);
        tv_orderDeadline = root.findViewById(R.id.new_order_deadline);
        tv_orderType = root.findViewById(R.id.new_order_type);

        btn_submit = root.findViewById(R.id.order_launch);

        //获取位置
        llLocation.setOnClickListener(v -> {
            getLocation();
        });
        //获取时间
        tv_orderDeadline.setOnClickListener(v -> chooseTime());
        //设置金额
        tv_orderMoney.setOnClickListener(v -> setMoney());
        //设置类型
        tv_orderType.setOnClickListener(v -> setType());
        //设置提交
        btn_submit.setOnClickListener(v -> submit());
    }


    /**
     * @author chenyim
     * @Description 确认发布订单
     * @date 2023/6/29 2:44
     */
    public void submit(){
        //检验数据
        String content = et_orderContent.getText().toString();
        String title = et_orderTitle.getText().toString();
        String type = tv_orderType.getText().toString();
        String money = tv_orderMoney.getText().toString();
        String location = tv_orderLocation.getText().toString();
        String deadline = tv_orderDeadline.getText().toString();
        if(StringUtils.isEmpty(content) || StringUtils.isEmpty(title) ||
                    type.equals("点击输入") || money.equals("点击设置") ||
                    deadline.equals("点击设置") || list_CurImages == null || list_CurImages.size() == 0)
            errorHandle("输入不完整！至少选择一张图片","提示");
        else{
            CreateOrderRequest orderRequest = new CreateOrderRequest();

            orderRequest.setTitle(title);
            orderRequest.setContent(content);
            orderRequest.setPrice(Double.parseDouble(money));
            orderRequest.setAddress(location);
            orderRequest.setLabel(type);
            orderRequest.setDeadLine(deadline);
            User user = SharedPreferencesUtil.getObject(context, "User");
            orderRequest.setUser(user);
            orderRequest.setHostId(user.getId());

            StringBuilder stringBuilder = new StringBuilder("");
            for(int i=0;i<list_CurImages.size();i++){
                stringBuilder.append(list_CurImages.get(i));
                if(i != list_CurImages.size() - 1)
                    stringBuilder.append("|");
            }
            orderRequest.setPictureUrls(stringBuilder.toString());
            RetrofitFactory.getInstence().API(context)
                    .userCreateOrder(orderRequest)
                    .compose(this.<BaseEntity<Orders>>setThread())
                    .subscribe(new BaseObserver<Orders>() {
                        @Override
                        protected void onSuccees(BaseEntity<Orders> t) throws Exception {
                            if(t.getCode() == 0){
                                errorHandle("发布成功！","提示");
                                UserCreateOrder userCreateOrder = new UserCreateOrder();
                                userCreateOrder.setId(System.currentTimeMillis());
                                userCreateOrder.setOrder(
                                        JSON.toJSONString(t.getData())
                                );
                                userCreateOrder.save();
                                deleteViews();
                            }else
                                errorHandle(t.getMessage(),"提示");
                        }

                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                            errorHandle(e.toString(),"错误");
                        }
                    });
        }
    }

    /**
     * @author chenyim
     * @Description 旋转时间
     * @date 2023/6/28 22:24
     */
    private void chooseTime() {
        TimePickerView mTimePicker;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.string2Date(TimeUtil.getCurrentTime(), DateUtils.yyyyMMddHHmmss.get()));
        mTimePicker = new TimePickerBuilder(context, (date, v) -> {
            String s = DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get());
            tv_orderDeadline.setText(s);
        })      .setType(TimePickerType.ALL)
                .setTitleText("时间选择")
                .isDialog(true)
                .setOutSideCancelable(false)
                .setDate(calendar)
                .build();
        mTimePicker.show();
    }

    /**
     * @author chenyim
     * @Description 设置佣金
     * @date 2023/6/28 22:33
     */
    private void setMoney() {
        InputInfo inputInfo = new InputInfo(InputType.TYPE_CLASS_TEXT);
        inputInfo.setHint("请输入金额");
        DialogLoader.getInstance().showInputDialog(context
                , R.drawable.warning,
                "请输入金额", "大于0，小数点后最多两位，小于1000", inputInfo,
                (dialog, input) -> {
                }, "确认", (dialogInterface, i) -> {
                    String input = ((MaterialDialog)dialogInterface).
                                        getInputEditText().getText().toString();
                    String regex = "^(?!0\\.0*$)(?!0$)(?!^1000$)^([1-9]\\d{0,2}(\\.\\d{1,2})?|0\\.\\d{1,2})$";
                    boolean isNumber = input.matches(regex);
                    if(isNumber){
                        tv_orderMoney.setText(input);
                        dialogInterface.dismiss();
                    }else{
                        dialogInterface.dismiss();
                        errorHandle("输入不合法","提示");
                    }
                }, "取消", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                });
    }

    /**
     * @author chenyim
     * @Description 设置类型
     * @date 2023/6/29 0:24
     */
    private void setType() {
        final String items[] = {"生活服务","代拿快递","学业帮助","其他服务"};
        DialogLoader.getInstance().showSingleChoiceDialog(context
                ,"选择订单类别"
                ,items
                ,0
                , (dialog, i) -> {
                    //点击确认按钮后，执行该方法
                    tv_orderType.setText(items[i]);
                },"确认"
                ,"关闭");
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        // 获取当前位置管理器
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {

        // 获取当前纬度
        latitude = location.getLatitude();
        // 获取当前经度
        longitude = location.getLongitude();
        // 定义位置解析
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = addresses.get(0);
            String info = address.getAddressLine(0);// + // 获取国家名称
            // 赋值
            tv_orderLocation.setText(info);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image1:
                if(curImagesCount == 0){
                    Log.e(TAG, "选择第一张图片: " );
                    showBottomDialog();
                }
                break;
            case R.id.image2:
                if(curImagesCount == 1){
                    Log.e(TAG, "选择第二张图片: " );
                    showBottomDialog();
                }
                break;
            case R.id.image3:
                if(curImagesCount == 2){
                    Log.e(TAG, "选择第三张图片: " );
                    showBottomDialog();
                }
                break;
            case R.id.image4:
                if(curImagesCount == 3){
                    Log.e(TAG, "选择第四张图片: " );
                    showBottomDialog();
                }
                break;
            case R.id.image5:
                if(curImagesCount == 4){
                    Log.e(TAG, "选择第五张图片: " );
                    showBottomDialog();
                }
                break;
            case R.id.image6:
                if(curImagesCount == 5){
                    Log.e(TAG, "选择第六张图片: " );
                    showBottomDialog();
                }
                break;
        }
    }



    // 打开相机
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Build.VERSION.SDK_INT：获取当前系统sdk版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
            String fileName = String.format("pic_%s_%s.jpg", System.currentTimeMillis(),"tttttttt");
            File cropFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
            photoUri = FileProvider.getUriForFile(context, AUTHORITIES, cropFile);//7.0
        } else {
            photoUri = getDestinationUri();
        }
        // android11以后强制分区存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        SharedPreferencesUtil.putBoolean(context,"isFromFragmentA",true);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    // 打开相册
    private void openAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        SharedPreferencesUtil.putBoolean(context,"isFromFragmentA",true);
        startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }

    //回调
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ALBUM:
                    doCrop(data.getData());
                    break;
                case REQUEST_CODE_CAMERA:
                    //截图
                    doCrop(photoUri);
                    break;
                case UCrop.REQUEST_CROP:

                    switch (curImagesCount){
                        case 0:
                            Glide.with(context).load(UCrop.getOutput(data))
                                    .centerCrop()
                                    .into(image1);
                            image1.setEnabled(false);
                            image2.setVisibility(View.VISIBLE);
                            curImagesCount = 1;
                            break;
                        case 1:
                            Glide.with(context).load(UCrop.getOutput(data))
                                    .centerCrop()
                                    .into(image2);
                            image2.setEnabled(false);
                            image3.setVisibility(View.VISIBLE);
                            curImagesCount = 2;
                            break;
                        case 2:
                            Glide.with(context).load(UCrop.getOutput(data))
                                    .centerCrop()
                                    .into(image3);
                            image3.setEnabled(false);
                            image4.setVisibility(View.VISIBLE);
                            image5.setVisibility(View.INVISIBLE);
                            image6.setVisibility(View.INVISIBLE);
                            curImagesCount = 3;
                            break;
                        case 3:
                            Glide.with(context).load(UCrop.getOutput(data))
                                    .centerCrop()
                                    .into(image4);
                            image4.setEnabled(false);
                            image5.setVisibility(View.VISIBLE);
                            curImagesCount = 4;
                            break;
                        case 4:
                            Glide.with(context).load(UCrop.getOutput(data))
                                    .centerCrop()
                                    .into(image5);
                            image5.setEnabled(false);
                            image6.setVisibility(View.VISIBLE);
                            curImagesCount = 5;
                            break;
                        case 5:
                            Glide.with(context).load(UCrop.getOutput(data))
                                    .centerCrop()
                                    .into(image6);
                            image6.setEnabled(false);
                            curImagesCount = 6;
                            break;
                    }

                    Log.e(TAG, "--图片路径--: "+cropFile);
                    //上传图片
                    UploadUtil.uploadImage(cropFile, new Observer() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.e(TAG, "onSubscribe "+d);
                        }
                        @Override
                        public void onNext(Object o) {
                            //上传成功
                            BaseEntity<String> res = (BaseEntity<String>) o;
                            list_CurImages.add(res.getData());
                        }
                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError "+e);
                        }
                        @Override
                        public void onComplete() {
                            Log.e(TAG, "onComplete ");
                        }
                    });
                    break;
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    // 裁剪方法
    private void doCrop(Uri data) {
        SharedPreferencesUtil.putBoolean(context,"isFromFragmentA",true);
        UCrop.of(data, getDestinationUri())//当前资源，保存目标位置
                .start(getActivity());
    }

    private void showBottomDialog() {
        // 使用Dialog、设置style
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        // 设置布局
        View view = View.inflate(context, R.layout.dialog_choosepic_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        // 设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        // 设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        // 设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // 判断是否有相机权限
                ifHaveCameraPermission();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_take_pic).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // 判断是否有文件存储权限
                ifHaveAlbumPermission(getActivity());
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    // 判断是否有相机权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void ifHaveCameraPermission() {
        //是否有摄像权限
        if (!AndPermission.hasPermissions(this, Permission.Group.CAMERA)) {
            // 动态申请权限
            AndPermission.with(this).runtime().permission(Permission.Group.CAMERA)
                    .onGranted(permissions -> {
                        openCamera();
                    })
                    .onDenied(denieds -> {
                        if (denieds != null && denieds.size() > 0) {
                            for (int i = 0; i < denieds.size(); i++) {
                                if (!shouldShowRequestPermissionRationale(denieds.get(i))) {
                                    DialogManager.permissionDialog(context, "没有拍摄和录制权限！");
                                    break;
                                }
                            }
                        }
                    }).start();
        } else {
            // 有权限 打开相机
            openCamera();
        }
    }

    // 判断是否有文件存储权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void ifHaveAlbumPermission(Activity activity) {
        //  Permission.Group.STORAGE：文件存储权限
        if (!AndPermission.hasPermissions(activity, Permission.Group.STORAGE)) {
            // Request permission
            AndPermission.with(activity).runtime().permission(Permission.Group.STORAGE).onGranted(permissions -> {
                openAlbum();
            }).onDenied(denieds -> {
                if (denieds != null && denieds.size() > 0) {
                    for (int i = 0; i < denieds.size(); i++) {
                        if (!activity.shouldShowRequestPermissionRationale(denieds.get(i))) {
                            DialogManager.permissionDialog(activity, "没有访问存储权限！");
                            break;
                        }
                    }
                }
            }).start();
        } else {
            openAlbum();
        }
    }

    //获取存储SD卡路径
    private Uri getDestinationUri() {
        fileName = String.format("pic_%s_%s.jpg", System.currentTimeMillis(),"userId");
        cropFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);

        return Uri.fromFile(cropFile);
    }


    /**
     * @author chenyim
     * @Description 清空view
     * @date 2023/6/29 3:15
     */
    public void deleteViews(){
        list_CurImages.clear();
        curImagesCount = 0;
        et_orderTitle.clear();
        et_orderContent.setText("");
        tv_orderLocation.setText("上海");
        tv_orderMoney.setText("点击设置");
        tv_orderDeadline.setText("点击设置");
        tv_orderType.setText("点击输入");
        image1.setEnabled(true);
        image2.setEnabled(true);
        image3.setEnabled(true);
        image4.setEnabled(true);
        image5.setEnabled(true);
        image6.setEnabled(true);
        image2.setVisibility(View.INVISIBLE);
        image3.setVisibility(View.INVISIBLE);
        image4.setVisibility(View.GONE);
        image5.setVisibility(View.GONE);
        image6.setVisibility(View.GONE);
        image1.setImageResource(R.drawable.add_pic);
        image2.setImageResource(R.drawable.add_pic);
        image3.setImageResource(R.drawable.add_pic);
        image4.setImageResource(R.drawable.add_pic);
        image5.setImageResource(R.drawable.add_pic);
        image6.setImageResource(R.drawable.add_pic);
    }
}