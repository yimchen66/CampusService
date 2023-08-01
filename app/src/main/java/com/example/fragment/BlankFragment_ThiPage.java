package com.example.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import com.bumptech.glide.Glide;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.dto.DetailedOrderDTO;
import com.example.model.request.user.UpdateRequest;
import com.example.widget.DialogManager;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.yalantis.ucrop.UCrop;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.R;
import com.example.common.BaseFragment;
import com.example.model.domain.User;
import com.example.utils.SharedPreferencesUtil;
import com.example.utils.UploadUtil;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseBanner;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import androidx.annotation.Nullable;
import com.example.common.BaseEntity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BlankFragment_ThiPage extends BaseFragment {
    public static final int REQUEST_CODE_CAMERA = 103; //相机
    public static final int REQUEST_CODE_ALBUM = 102; //相册
    public static final int PERSON_LAUNCH = 888;
    public static final int PERSON_ACCEPT = 889;
    public static final int PERSON_TRANS = 900;
    public static final int PERSON_FINISH = 901;
    public static final int PERSON_ALL = 902;
    private static final String AUTHORITIES = "com.example.campusservice.fileprovider";
    private static final String TAG = "person";
    private Uri photoUri;//记录图片地址标识
    private File cropFile ;//记录图片地址
    private String fileName;//记录图片地址用于命名
    private SimpleImageBanner mSimpleImageBanner;
    private RadiusImageView iv_personFigural;
    private TextView tv_nickName,tv_userName;
    private LinearLayout ll_lookMore,ll_orderLaunch,ll_orderAccept,ll_orderTrans,ll_orderFinish,
                        ll_personWallet,ll_personCredit,ll_personTransNumber,
                        ll_personInfo,ll_personHistory,ll_exit;
    private User user;

    //banner 中的标题
    public static String[] titles = new String[]{
            "伪装者:胡歌演绎'痞子特工'",
            "无心法师:生死离别!月牙遭虐杀",
            "花千骨:尊上沦为花千骨",
            "综艺饭:胖轩偷看夏天洗澡掀波澜",
            "碟中谍4:阿汤哥高塔命悬一线,超越不可能",
    };
    //banner 的图片资源
    public static String[] urls = new String[]{
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160323071011277.jpg",//伪装者:胡歌演绎"痞子特工"
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144158380433341332.jpg",//无心法师:生死离别!月牙遭虐杀
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160286644953923.jpg",//花千骨:尊上沦为花千骨
            "http://photocdn.sohu.com/tvmobilemvms/20150902/144115156939164801.jpg",//综艺饭:胖轩偷看夏天洗澡掀波澜
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144159406950245847.jpg",//碟中谍4:阿汤哥高塔命悬一线,超越不可能
    };
    private List<BannerItem> mData;
    private Context context;
    View root;

    public BlankFragment_ThiPage(Context context) {
        this.context = context;
        // Required empty public constructor
    }

    public BlankFragment_ThiPage(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(null == root){
            root = inflater.inflate(R.layout.fragment_blank__thi_page, container, false);
        }
        //加载轮播图
        initData();
        mSimpleImageBanner = root.findViewById(R.id.sib_simple_banner);
        sib_simple_usage();
        user = SharedPreferencesUtil.getObject(context, "User");

        initView();


        return root;
    }

    /**
     * @author chenyim
     * @Description 初始化视图
     * @date 2023/6/27 21:12
     */
    private void initView(){
        iv_personFigural = root.findViewById(R.id.person_figural);
        tv_nickName = root.findViewById(R.id.person_nick_name);
        tv_userName = root.findViewById(R.id.person_user_name);
        ll_lookMore = root.findViewById(R.id.personal_look_more);
        ll_orderLaunch = root.findViewById(R.id.person_order_launch);
        ll_orderAccept = root.findViewById(R.id.person_order_accept);
        ll_orderTrans = root.findViewById(R.id.person_order_trans);
        ll_orderFinish = root.findViewById(R.id.person_order_finish);
        ll_personWallet = root.findViewById(R.id.person_order_wallet);
        ll_personCredit = root.findViewById(R.id.person_order_credit);
        ll_personTransNumber = root.findViewById(R.id.person_order_tans_number);
        ll_personInfo = root.findViewById(R.id.person_order_person_info);
        ll_personHistory = root.findViewById(R.id.person_history);
        ll_exit = root.findViewById(R.id.exit);

        Glide.with(context).load(user.getFigureUrl())
                .placeholder(R.drawable.defaulttouxaing)//图片加载出来前，显示的图片
                .fallback( R.drawable.defaulttouxaing) //url为空的时候,显示的图片
                .error(R.drawable.defaulttouxaing)//图片加载失败后，显示的图片
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(iv_personFigural);
        tv_nickName.setText((user.getNickName() == null||user.getNickName().equals(""))?
                user.getUserName():user.getNickName()
                );
        tv_userName.setText(user.getUserName());

        iv_personFigural.setOnClickListener(v -> showBottomDialog());

        ll_personWallet.setOnClickListener(v ->
                jumpToCertainActivity("com.example.activity.ActivityPersonWallet"));
        ll_personCredit.setOnClickListener(v ->
                jumpToCertainActivity("com.example.activity.ActivityPersonCredit"));

        ll_personInfo.setOnClickListener(v ->
                jumpToCertainActivity("com.example.activity.ActivityPersonInfo"));
        ll_personHistory.setOnClickListener(v ->
                jumpToCertainActivity("com.example.activity.ActivityPersonHistory"));

        ll_orderLaunch.setOnClickListener(v ->
                jumpToCertainActivity("com.example.activity.ActivityPersonLaunch",PERSON_LAUNCH));
        ll_orderAccept.setOnClickListener(v ->
                jumpToCertainActivity("com.example.activity.ActivityPersonLaunch",PERSON_ACCEPT));
        ll_orderTrans.setOnClickListener(v ->
                jumpToCertainActivity("com.example.activity.ActivityPersonLaunch",PERSON_TRANS));
        ll_orderFinish.setOnClickListener(v ->
                jumpToCertainActivity("com.example.activity.ActivityPersonLaunch",PERSON_FINISH));
        ll_lookMore.setOnClickListener(v ->
                jumpToCertainActivity("com.example.activity.ActivityTransHistory"));

        ll_personTransNumber.setOnClickListener(v ->
                RetrofitFactory.getInstence().API(context)
                .userGetOrderFinish(user.getId())
                .compose(this.<BaseEntity<List<DetailedOrderDTO>>>setThread())
                .subscribe(new BaseObserver<List<DetailedOrderDTO>>() {
                    @Override
                    protected void onSuccees(BaseEntity<List<DetailedOrderDTO>> t) throws Exception {
                        if(t.getCode() == 0)
                            dialogHandle("共完成交易 "+t.getData().size(),"提示");
                        else
                            dialogHandle(t.getMessage(),"提示");
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        dialogHandle(e.toString(),"错误");
                    }
                })
                );

        ll_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogLoader.getInstance().showConfirmDialog(context,
                        "确认退出？", "确认", (dialog, which) -> userExit(),"取消");
            }
        });
    }
    /**
     * @author chenyim
     * @Description 修改头像
     * @date 2023/6/27 20:52
     */
    private void replacePhote(BaseEntity<String> res) {
        dialogHandle("头像修改成功！","提示");
        UpdateRequest updateRequest = new UpdateRequest();
        user.setFigureUrl(res.getData());
        updateRequest.setPassword(user.getPassword());
        updateRequest.setUser(user);
        SharedPreferencesUtil.putObject(context,"User",user);
        //上传
        RetrofitFactory.getInstence().API(context)
                .userUpdateInfo(updateRequest)
                .compose(this.<BaseEntity<User>>setThread())
                .subscribe(new BaseObserver<User>() {
                    @Override
                    protected void onSuccees(BaseEntity<User> t) throws Exception {
                        if(t.getCode() != 0)
                            dialogHandle("头像同步失败，请检查网络","提示");
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        dialogHandle(e.toString(),"错误");
                    }
                });
    }
    /**
     * @author chenyim
     * @Description TDOO
     * @date 2023/6/29 3:36
     */
    public void userExit(){
        SharedPreferencesUtil.putString(context,"token","");
        jumpToCertainActivity("com.example.activity.ActivityWelcome");
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
        SharedPreferencesUtil.putBoolean(context,"isFromFragmentA",false);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    // 打开相册
    private void openAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        SharedPreferencesUtil.putBoolean(context,"isFromFragmentA",false);
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
                    Glide.with(context).load(UCrop.getOutput(data))
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(iv_personFigural);
                    Log.e(TAG, "----: "+cropFile);
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
                            replacePhote(res);
                            Log.e(TAG, "onNext "+res);
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
        SharedPreferencesUtil.putBoolean(context,"isFromFragmentA",false);
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
     * @Description 统一对话框处理
     * @date 2023/6/25 16:16
     */
    public void dialogHandle(String msg, String title){
        DialogLoader.getInstance().showTipDialog(context,R.drawable.warning,title,
                msg,"确认"
                , (dialogInterface, i) -> {
                    //点击确认按钮后关闭提示框
                    dialogInterface.dismiss();
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
            Intent intent = new Intent(context, aClass);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString() );
        }
    }
    private void jumpToCertainActivity(String className,int source){
        try {
            Class<?> aClass = Class.forName(className);
            Intent intent = new Intent(context, aClass);
            intent.putExtra("source",source);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString() );
        }
    }

    private void sib_simple_usage() {
        mSimpleImageBanner.setSource(mData)
                .setOnItemClickListener(new BaseBanner.OnItemClickListener<BannerItem>() {
                    @Override
                    public void onItemClick(View view, BannerItem item, int position) {
                    }
                })
                .setIsOnePageLoop(false)//设置当页面只有一条时，是否轮播
                .startScroll();//开始滚动
    }
    private void initData(){
        mData = new ArrayList<>(urls.length);
        for (int i = 0; i < urls.length; i++) {
            BannerItem item = new BannerItem();
            item.imgUrl = urls[i];
            item.title = titles[i];
            mData.add(item);
        }
    }
}