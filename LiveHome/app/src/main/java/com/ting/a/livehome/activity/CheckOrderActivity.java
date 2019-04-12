package com.ting.a.livehome.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ting.a.livehome.R;
import com.ting.a.livehome.bean.UserInfo;
import com.ting.a.livehome.bean.UserOrderInfo;
import com.ting.a.livehome.dao.DataDao;
import com.ting.a.livehome.unit.DataContact;
import com.ting.a.livehome.unit.ZProgressHUD;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 查看订单页面
 */
public class CheckOrderActivity extends Activity implements View.OnClickListener {


    private Context context = CheckOrderActivity.this;
    //时间格式转换
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TextView tv_title;//标题
    private View left_but;//左上角的返回按钮
    private Button cancelBut;//取消訂單按钮
    private Button confirmBut;//确定收貨按钮

    private TextView num;//订购的数量文本狂
    private TextView order_size;//订单规格
    private TextView order_name;//订单品种
    private TextView order_price;//订单单价
    private TextView order_sup;//订单供应商
    private TextView orderState;//订单状态
    private TextView orderCodeView;//订单编号
    private TextView orderCreatDataView;//订单创建时间
    private TextView orderDealTimeView;//订单交易时间
    private TextView orderFHTime;//订单发货时间
    private TextView totalPriceView;//订单总金额
    private TextView orders_Cconsigne;//订单收貨人
    private TextView orders_CconsigneAdds;//订单收货地址

    public ZProgressHUD pDialog;//加载窗
    private View but_view;//显示取消订单和确认订单按钮的View
    private String orderCode = "";//订单编号
    private UserInfo userInfo;
    private View loading_view;
    private List<UserOrderInfo> orderInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order);//加載頁面
        //寻找最上方的Title
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("订单详情");//修改为订单详情的title
        left_but = findViewById(R.id.left_but);//寻找左上方的返回按钮
        left_but.setVisibility(View.VISIBLE);//并将它显示出来
        //寻找取消订单按钮
        cancelBut = findViewById(R.id.cancelBut);
        //寻找确定订单按钮
        confirmBut = findViewById(R.id.confirmBut);

        //寻找界面上的需要用到的控件
        num = findViewById(R.id.num);
        order_size = findViewById(R.id.order_size);
        order_name = findViewById(R.id.order_name);
        order_price = findViewById(R.id.order_price);
        order_sup = findViewById(R.id.order_sup);
        orderState = findViewById(R.id.orderState);
        orderCodeView = findViewById(R.id.orderCodeView);
        orderCreatDataView = findViewById(R.id.orderCreatDataView);
        orderDealTimeView = findViewById(R.id.orderDealTimeView);
        orderFHTime = findViewById(R.id.orderFHTime);
        totalPriceView = findViewById(R.id.totalPriceView);
        orders_Cconsigne = findViewById(R.id.orders_Cconsigne);
        orders_CconsigneAdds = findViewById(R.id.orders_CconsigneAdds);
        but_view = findViewById(R.id.but_view);
        loading_view = findViewById(R.id.loading_view);

        //添加点击事件
        confirmBut.setOnClickListener(this);
        cancelBut.setOnClickListener(this);
        left_but.setOnClickListener(this);
        userInfo = DataDao.getInstance(context).findgUser();


        //获取上个页面传来的订单编号
        orderCode = this.getIntent().getStringExtra("orderCode");
        //获得订单的信息并且显示
        lodingTask.execute();
    }


    //异步网络请求加载网络数据
    @SuppressLint("StaticFieldLeak")
    AsyncTask<Void, Integer, Integer> lodingTask = new AsyncTask<Void, Integer, Integer>() {

        @Override
        protected void onPreExecute() {
            //初始化加载progressBar
            loading_view.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... voids) {

            try {

                OkHttpClient client = new OkHttpClient();
                FormBody body = new FormBody.Builder()
                        .add("orderNo", orderCode)
                        .build();

                Request request = new Request.Builder().url(DataContact.GET_ORDER_API).post(body).build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    if (data != null) {
                        JSONObject jsonObject = new JSONObject(data);
                        int res = jsonObject.getInt("code");
                        Gson gos = new Gson();
                        orderInfoList = gos.fromJson(jsonObject.getString("data"), new TypeToken<List<UserOrderInfo>>() {
                        }.getType());
                        return res;
                    }
                } else
                    return -1;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer res) {
            if (res == 0)
                getOrderAndShow();
            loading_view.setVisibility(View.GONE);
        }
    };


    //根据传进来的订单编号查询订单信息并且显示
    private void getOrderAndShow() {
        UserOrderInfo orderInfo = new UserOrderInfo();
        if (orderInfoList.size() > 0)
            orderInfo = orderInfoList.get(0);
        //将查到的订单信息显示在页面的控件上，已下就是绑定数据
        num.setText(orderInfo.getOrderNum() + "");
        //根据订单状态显示文字描述

        switch (orderInfo.getOrderState()) {
            case "-1":
                but_view.setVisibility(View.GONE);
                orderState.setText("已取消");
                break;
            case "1":
                but_view.setVisibility(View.VISIBLE);
                orderState.setText("待确认");
                break;
            case "2":
                but_view.setVisibility(View.VISIBLE);
                orderState.setText("已确认");
                break;
            case "3":
                but_view.setVisibility(View.VISIBLE);
                orderState.setText("已发货");
                break;
            case "4":
                but_view.setVisibility(View.GONE);
                orderState.setText("已完成");
                break;

        }
        order_size.setText(orderInfo.getOrderSize());
        order_name.setText(orderInfo.getOrderType());
        order_price.setText("￥ " + orderInfo.getOrderPrice());
        order_sup.setText(orderInfo.getOrderSupplier());
        orderCodeView.setText(orderInfo.getOrderNo());
        orderCreatDataView.setText(orderInfo.getOrderCreatDate());
        orderDealTimeView.setText(orderInfo.getOrderDealDate());
        orderFHTime.setText(orderInfo.getOrderSenDate());
        orders_Cconsigne.setText(orderInfo.getOrderConsignee());
        orders_CconsigneAdds.setText(orderInfo.getOrderConsigneeAdd());
        totalPriceView.setText("￥ " + orderInfo.getOrderTotalPrice() + "");
    }


    //点击事件监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //当点击确认订单按钮时
            case R.id.confirmBut:
                orderConfirm(4);
                break;
            //当点击取消订单按钮时
            case R.id.cancelBut:
                orderConfirm(-1);
                break;
            //点击返回按钮
            case R.id.left_but:
                finish();
                break;
        }
    }

    /**
     * 订单确认
     */
    private void orderConfirm(final int submitType) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Integer, Integer> t = new AsyncTask<Void, Integer, Integer>() {
            @Override
            //第一执行
            protected void onPreExecute() {
                //初始化加载窗口
                pDialog = new ZProgressHUD(CheckOrderActivity.this);
                if (submitType == 4)
                    pDialog.setMessage("订单确认中...");
                else
                    pDialog.setMessage("订单取消中...");
                pDialog.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
                pDialog.show();
            }


            @Override
            //第二执行
            protected Integer doInBackground(Void... voids) {
                try {

                    List<UserOrderInfo> upstaeOrderInfoList = orderInfoList;//用来修改的临时集合
                    if (upstaeOrderInfoList.size() > 0) {
                        upstaeOrderInfoList.get(0).setOrderState(submitType + "");
                        if (submitType == 4) {//收货要更新收货时间和收货人还有订单变更时间
                            upstaeOrderInfoList.get(0).setOrderConsigneeDate(formatter.format(new Date()));
                            upstaeOrderInfoList.get(0).setOrderModifyDate(formatter.format(new Date()));
                            upstaeOrderInfoList.get(0).setOrderConsignee(userInfo.getUserName());
                        } else {//取消只需要更新订单变更时间
                            upstaeOrderInfoList.get(0).setOrderModifyDate(formatter.format(new Date()));
                        }
                    }

                    OkHttpClient client = new OkHttpClient();
                    Gson gson = new Gson();
                    //使用Gson将对象转换为json字符串
                    String json = gson.toJson(upstaeOrderInfoList);
                    //MediaType  设置Content-Type 标头中包含的媒体类型值
                    RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                    Request request = new Request.Builder().url(DataContact.UPDATE_ORDER_API).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        if (data != null) {
                            JSONObject jsonObject = new JSONObject(data);
                            int res = jsonObject.getInt("code");
                            Gson gos = new Gson();
                            orderInfoList = gos.fromJson(jsonObject.getString("data"), new TypeToken<List<UserOrderInfo>>() {
                            }.getType());
                            if (res == 0)
                                return 1;
                            else return -1;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
                return -1;
            }

            @Override
            //第三执行
            protected void onPostExecute(Integer s) {
                if (s == 1) {
                    if (submitType == 4)
                        pDialog.dismissWithSuccess("收货成功");
                    else
                        pDialog.dismissWithSuccess("取消成功");
                    //重新获取订单信息，刷新下界面
                    getOrderAndShow();
                } else {
                    if (submitType == 4)
                        pDialog.dismissWithSuccess("收货失败");
                    else
                        pDialog.dismissWithSuccess("取消失败");
                }
            }
        };
        t.execute();
    }

}
