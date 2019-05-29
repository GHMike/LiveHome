package com.ting.a.livehome.unit;

public class DataContact {

    private static String SERVER_URL = "http://livehome.hookaka.com:8015/";//云服务器地址
//    private static String SERVER_URL = "http://192.168.11.152:8080/";//本地服务器地址


    public static String LOGIN_API = SERVER_URL + "Home/LoginServlet";//登录
    public static String REGISTER_API = SERVER_URL + "Home/UserRegisterServlet";//注册用户
    public static String UPDATE_USER_API = SERVER_URL + "Home/UserUpdateServlet";//修改用户
    public static String GET_NEWS_LIST_API = SERVER_URL + "Home/NewsServlet";//获取新闻列表
    public static String SAVE_ORDER_API = SERVER_URL + "Home/SubmitOrderServlet";//订单提交
    public static String UPDATE_ORDER_API = SERVER_URL + "Home/UpdateOrderServlet";//修改订单
    public static String GET_COMMODITY_API = SERVER_URL + "Home/GetCommodityServlet";//获取商品
    public static String GET_MERCHAN_API = SERVER_URL + "Home/GetMerchanServlet";//获取商家
    public static String GET_ORDER_API = SERVER_URL + "Home/GetOrderServlet";//获取订单信息


}
