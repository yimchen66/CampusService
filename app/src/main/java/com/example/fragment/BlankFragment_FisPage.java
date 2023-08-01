package com.example.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.example.MainActivity;
import com.example.R;
import com.example.activity.ActivityLoginUserName;
import com.example.adapter.BannerAdapter;
import com.example.adapter.ItemAdapter;
import com.example.adapter.TagListViewAdapter;
import com.example.common.BaseEntity;
import com.example.common.BaseFragment;
import com.example.http.RetrofitFactory;
import com.example.http.base.BaseObserver;
import com.example.model.domain.Carousel;
import com.example.model.domain.NavigateAct;
import com.example.model.domain.RecommandOrder;
import com.example.view.HorizontalListView;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;
import com.xuexiang.xui.widget.banner.recycler.BannerLayout;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.flowlayout.FlowTagLayout;
import com.xuexiang.xui.widget.toast.XToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class BlankFragment_FisPage extends BaseFragment implements View.OnClickListener,
        BannerLayout.OnBannerItemClickListener, LocationListener {
    private static final String TAG = "chen";
    private Context context;
    private View root;
    private EditText search_text;
    private RefreshLayout refreshLayout;
    private BannerLayout bannerLayout;
    private HorizontalListView horizontalListView;
    private Fragment fragment;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private TextView tv_location;
    // 定义双精度类型的经纬度
    private Double longitude,latitude;
    // 定义位置管理器
    private LocationManager locationManager;
    List<Carousel> data;


    public BlankFragment_FisPage(Context context) {
        this.context = context;
    }

    public BlankFragment_FisPage(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(null == root){
            root = inflater.inflate(R.layout.fragment_blank__fis_page, container, false);
        }
        fragment = this;

        init();

        return root;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_text:
                SearchFragment searchFragment = SearchFragment.newInstance();
                searchFragment.setOnSearchClickListener(keyword -> {
                    //这里处理逻辑
                    Toast.makeText(context, keyword, Toast.LENGTH_SHORT).show();
                });
                searchFragment.showFragment(getFragmentManager(),SearchFragment.TAG);
                break;
        }
    }

    /**
     * @author chenyim
     * @Description 初始化视图
     * @date 2023/6/26 9:04
     */
    public void init(){
        // 判断当前是否拥有使用GPS的权限
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // 申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        refreshLayout = root.findViewById(R.id.refreshLayout);
        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setRefreshHeader(new ClassicsHeader(context));
        //refreshLayout.setRefreshFooter(new ClassicsFooter(context));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getRecommendOrder();
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(500/*,false*/);//传入false表示加载失败
            }
        });
        //搜获
        search_text = root.findViewById(R.id.search_text);
        search_text.setOnClickListener(this);
        //轮播图
        bannerLayout = root.findViewById(R.id.bl_horizontal);
//                "http://photocdn.sohu.com/tvmobilemvms/20150907/144160323071011277.jpg",
//                "http://photocdn.sohu.com/tvmobilemvms/20150907/144158380433341332.jpg",
//                "http://photocdn.sohu.com/tvmobilemvms/20150907/144160286644953923.jpg",
//                "http://photocdn.sohu.com/tvmobilemvms/20150902/144115156939164801.jpg",
//                "http://photocdn.sohu.com/tvmobilemvms/20150907/144159406950245847.jpg",
//        };
        RetrofitFactory.getInstence().API(context)
                .getMainCarousel()
                .compose(this.<BaseEntity<List<Carousel>>>setThread())
                .subscribe(new BaseObserver<List<Carousel>>() {
                    @Override
                    protected void onSuccees(BaseEntity<List<Carousel>> t) throws Exception {
                        if(t.getCode() == 0){
                            data = t.getData();
                            initBanner();
                        }
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

                    }
                });

        //tag
        horizontalListView = root.findViewById(R.id.hlv);
        sendGetTagList();

        //获取更多
        LinearLayout llMoreOrder = root.findViewById(R.id.main_more_order);
        llMoreOrder.setOnClickListener(v -> {
            //跳转页面
            jumpToCertainActivity("com.example.activity.ActivityAllOrders");
        });

        //推荐订单
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        StaggeredGridLayoutManager layoutManager =
                        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        //获取订单
        getRecommendOrder();

        tv_location = root.findViewById(R.id.location);
        LinearLayout ll_getlocation = root.findViewById(R.id.getlocation);
        ll_getlocation.setOnClickListener(v -> {
            getLocation();
        });
    }

    /**
     * @author chenyim
     * @Description 轮播图
     * @date 2023/6/29 4:14
     */
    private void initBanner() {
        String urls[] = new String[data.size()];
        int i=0;
        for (Carousel datum : data) {
            urls[i++] =datum.getPicUrl();
        }
        BannerAdapter bannerAdapter = new BannerAdapter(context,urls);
        bannerAdapter.setOnBannerItemClickListener(this);
        bannerLayout.setAdapter(bannerAdapter);
    }

    /**
     * @author chenyim
     * @Description 远程请求订单
     * @date 2023/6/26 20:03
     */
    private void getRecommendOrder() {
        RetrofitFactory.getInstence().API(context)
                .getRecommendOrder()
                .compose(this.<BaseEntity<List<RecommandOrder>>>setThread())
                .subscribe(new BaseObserver<List<RecommandOrder>>() {
                    @Override
                    protected void onSuccees(BaseEntity<List<RecommandOrder>> t) throws Exception {
                        if(t.getCode() == 0){
                            setRecommend(t.getData());
                        }
                    }
                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        errorHandle("网络有问题","提示");
                    }
                });
    }

    /**
     * @author chenyim
     * @Description 加载数据
     * @date 2023/6/30 3:27
     */
    public void setRecommend(List<RecommandOrder> itemList){
        for (RecommandOrder recommandOrder : itemList) {
            RetrofitFactory.getInstence().API(context)
                    .getOrderHot(recommandOrder.getId())
                    .compose(this.<BaseEntity<Double>>setThread())
                    .subscribe(new BaseObserver<Double>() {
                        @Override
                        protected void onSuccees(BaseEntity<Double> t) throws Exception {
                            Double data = t.getData();
                            recommandOrder.setOrderHot((int) data.intValue());
                        }
                        @Override
                        protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        }
                    });
        }
        itemAdapter = new ItemAdapter(itemList,context,20);
        recyclerView.setAdapter(itemAdapter);
    }


    //轮播图点击事件接口
    @Override
    public void onItemClick(int position) {
        String url = data.get(position).getActivityName(); // 要打开的网页的URL
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);

        XToast.normal(context,"点击了第 "+(position+1)+" 个"  ).show();
    }

    /**
     * @author chenyim
     * @Description 获取tag的资源
     * @date 2023/6/26 10:35
     */
    private void sendGetTagList(){
        RetrofitFactory.getInstence().API(context)
                .getNavigateAct()
                .compose(this.<BaseEntity<List<NavigateAct>>>setThread())
                .subscribe(new BaseObserver<List<NavigateAct>>() {
                    @Override
                    protected void onSuccees(BaseEntity<List<NavigateAct>> t) throws Exception {
                        if(t.getCode() == 0)
                            getTagList(t.getData());
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        errorHandle("网络有问题","提示");
                    }
                });
    }
    /**
     * @author chenyim
     * @Description 处理act请求
     * @date 2023/6/26 10:39
     */
    public void getTagList(List<NavigateAct> list) {
        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
        TagListViewAdapter tagListViewAdapter = new TagListViewAdapter(list,context,screenWidth);
        //调用控制水平滚动的方法

        horizontalListView.setAdapter(tagListViewAdapter);

        horizontalListView.setOnItemClickListener((parent,
                                                   view,
                                                   position,
                                                   id) -> XToast.normal(context,"点击了第 "+(position+1)+" 个"  ).show());

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
            Intent intent = new Intent(context, aClass);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString() );
        }
    }


    @SuppressLint("MissingPermission")
    private void getLocation() {
        // 获取当前位置管理器
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
    }

    // 当位置改变时执行，除了移动设置距离为 0时
    @Override
    public void onLocationChanged(@NonNull Location location) {
        // 获取当前纬度
        latitude = location.getLatitude();
        // 获取当前经度
        longitude = location.getLongitude();
        // 定义位置解析
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            // 获取经纬度对于的位置
            // getFromLocation(纬度, 经度, 最多获取的位置数量)
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            // 得到第一个经纬度位置解析信息
            Address address = addresses.get(0);
            String info = address.getAddressLine(0);// + // 获取国家名称
            // 赋值
            tv_location.setText(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 需要一直获取位置信息可以去掉这个
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