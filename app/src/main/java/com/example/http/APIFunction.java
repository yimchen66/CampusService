package com.example.http;


import com.example.common.BaseEntity;
import com.example.model.domain.Carousel;
import com.example.model.domain.NavigateAct;
import com.example.model.domain.Orders;
import com.example.model.domain.RecommandOrder;
import com.example.model.domain.TransOrder;
import com.example.model.domain.User;
import com.example.http.config.URLConfig;
import com.example.model.dto.DetailedOrderDTO;
import com.example.model.dto.TransHistoryDTO;
import com.example.model.dto.UserDTO;
import com.example.model.request.order.CreateOrderRequest;
import com.example.model.request.order.PageOrderRequest;
import com.example.model.request.trans.TransOrderFinishRequest;
import com.example.model.request.user.UpdateRequest;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author chenyim
 * @date 2023/6/25 9:04
 * @description API接口!
 */

public interface APIFunction {

    /**
     * @author chenyim
     * @Description 账号密码登录
     * @date 2023/6/25 9:04
     */
    @POST(URLConfig.user_login_name_password)
    Observable<BaseEntity<UserDTO>> getUserLogin(@Body User user);

    /**
     * @author chenyim
     * @Description 验证token
     * @date 2023/6/25 9:04
     */
    @POST(URLConfig.user_check_token)
    Observable<BaseEntity<Boolean>> checkToken(@Body UserDTO userDTO);

    /**
     * @author chenyim
     * @Description qq登录
     * @date 2023/6/25 14:22
     */
    @POST(URLConfig.user_login_qq)
    Observable<BaseEntity<UserDTO>> qqLogin(@Body User user);

    /**
     * @author chenyim
     * @Description 检查注册的用户名是否可用
     * @date 2023/6/25 16:09
     */
    @GET(URLConfig.user_check_username)
    Observable<BaseEntity<String>> checkUserNameAvailable(@Path("name") String name);

    /**
     * @author chenyim
     * @Description 账号密码注册
     * @date 2023/6/25 19:26
     */
    @POST(URLConfig.user_register_username)
    Observable<BaseEntity<User>> userNameRegister(@Body User user);

    /**
     * @author chenyim
     * @Description 获取首页资源
     * @date 2023/6/26 10:36
     */
    @GET(URLConfig.main_navigate_act)
    Observable<BaseEntity<List<NavigateAct>>> getNavigateAct();

    /**
     * @author chenyim
     * @Description 获取首页资源
     * @date 2023/6/26 10:36
     */
    @GET(URLConfig.main_recommend_order)
    Observable<BaseEntity<List<RecommandOrder>>> getRecommendOrder();

    /**
     * @author chenyim
     * @Description 获取某个详细订单
     * @date 2023/6/26 10:36
     */
    @GET(URLConfig.main_user_get_order)
    Observable<BaseEntity<DetailedOrderDTO>> getSingleOrder(@Path("id") String id);

    /**
     * @author chenyim
     * @Description 获取某个人的基本信息
     * @date 2023/6/26 10:36
     */
    @POST(URLConfig.user_get_user)
    Observable<BaseEntity<User>> getSingleUser(@Body User user);

    /**
     * @author chenyim
     * @Description 获取某个人的基本信息
     * @date 2023/6/26 10:36
     */
    @GET(URLConfig.user_get_order_hot)
    Observable<BaseEntity<Double>> getOrderHot(@Path("orderId") String orderId);

    /**
     * @author chenyim
     * @Description 用户接受订单
     * @date 2023/6/26 10:36
     */
    @POST(URLConfig.user_accept_order)
    Observable<BaseEntity<String>> userAcceptOrder(@Body DetailedOrderDTO detailedOrderDTO);

    /**
     * @author chenyim
     * @Description 用户获取所有订单
     * @date 2023/6/26 10:36
     */
    @POST(URLConfig.main_user_get_order_all)
    Observable<BaseEntity<List<Orders>>> userGetAllOrders(@Body PageOrderRequest pageOrderRequest);

    /**
     * @author chenyim
     * @Description 用户获取排行榜的订单
     * @date 2023/6/26 10:36
     */
    @POST(URLConfig.main_user_get_order_all_hot)
    Observable<BaseEntity<List<Orders>>> userGetAllOrdersByHot(@Body PageOrderRequest pageOrderRequest);

    /**
     * @author chenyim
     * @Description 用户更改信息
     * @date 2023/6/26 10:36
     */
    @POST(URLConfig.user_update_info)
    Observable<BaseEntity<User>> userUpdateInfo(@Body UpdateRequest updateRequest);

    /**
     * @author chenyim
     * @Description 用户获取正在交易的订单
     * @date 2023/6/26 10:36
     */
    @GET(URLConfig.user_get_order_trans)
    Observable<BaseEntity<List<DetailedOrderDTO>>> userGetOrderTrans(@Path("userID") String userID);

    /**
     * @author chenyim
     * @Description 用户获取已经完成的订单
     * @date 2023/6/26 10:36
     */
    @GET(URLConfig.user_get_order_finish)
    Observable<BaseEntity<List<DetailedOrderDTO>>> userGetOrderFinish(@Path("userID") String userID);

    /**
     * @author chenyim
     * @Description 用户获取某个订单详情
     * @date 2023/6/26 10:36
     */
    @GET(URLConfig.user_get_order_detail)
    Observable<BaseEntity<DetailedOrderDTO>> userGetOrderDetail(@Path("id") String id);

    /**
     * @author chenyim
     * @Description 用户删除某个订单
     * @date 2023/6/26 10:36
     */
    @DELETE(URLConfig.user_get_order_delete)
    Observable<BaseEntity<String>> userDeleteOrder(@Path("id") String id);

    /**
     * @author chenyim
     * @Description 用胡确认完成订单
     * @date 2023/6/26 10:36
     */
    @POST(URLConfig.user_set_order_compelete)
    Observable<BaseEntity<TransOrder>> userSetOrderCompelete(@Body TransOrderFinishRequest transOrderFinishRequest);

    /**
     * @author chenyim
     * @Description 用户查询交易记录
     * @date 2023/6/26 10:36
     */
    @GET(URLConfig.user_get_order_finish_record)
    Observable<BaseEntity<List<TransHistoryDTO>>> userGetTransHistory(@Path("userId") String userId);

    /**
     * @author chenyim
     * @Description 用户发起一个订单
     * @date 2023/6/26 10:36
     */
    @POST(URLConfig.user_create_order)
    Observable<BaseEntity<Orders>> userCreateOrder(@Body CreateOrderRequest createOrderRequest);

    /**
     * @author chenyim
     * @Description 获取首页轮播图
     * @date 2023/6/26 10:36
     */
    @GET(URLConfig.main_carousel)
    Observable<BaseEntity<List<Carousel>>> getMainCarousel();














    /**
     * @author chenyim
     * @Description 上传单张图片
     * @date 2023/6/25 14:22
     */
    @Multipart
    @POST(URLConfig.image_upload_order)
    Observable<BaseEntity<String>> uploadImage(@Part MultipartBody.Part image);
}
