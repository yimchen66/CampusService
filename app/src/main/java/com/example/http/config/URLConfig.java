package com.example.http.config;

/**
 * @author chenyim
 * @Description 网络接口地址
 * @date 2023/6/24 16:11
 */
public interface URLConfig {
    String image_upload_order = "/order/upload";
    String user_login_name_password = "/user/login";
    String user_check_token = "/user/checktoken";
    String user_login_qq = "/user/qqlogin";
    String user_check_username = "/user/check/{name}";
    String user_register_username = "/user/register";
    String main_navigate_act = "/main/act";
    String main_recommend_order = "/main/recommand";
    String main_user_get_order = "/order/get/one/user/{id}";
    String user_get_user = "/user/get/one";
    String user_get_order_hot = "/order/get/hot/{orderId}";
    String user_accept_order = "/order/accept";
    String main_user_get_order_all = "/order/pagenoorder";
    String main_user_get_order_all_hot = "/order/get/ranking";
    String user_update_info = "/user/update";
    String user_get_order_trans = "/order/get/user/trans/{userID}";
    String user_get_order_finish = "/order/get/user/finish/{userID}";
    String user_get_order_detail = "/order/get/one/{id}";
    String user_get_order_delete = "/order/delete/unreceive/{id}";
    String user_set_order_compelete = "/trans/finish";
    String user_get_order_finish_record = "/trans/history/{userId}";
    String user_create_order = "/order/create";
    String main_carousel = "/main/carousel";
}
