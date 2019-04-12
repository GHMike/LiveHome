package com.ting.a.livehome.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ting.a.livehome.R;
import com.ting.a.livehome.bean.CommodityInfo;
import com.ting.a.livehome.bean.UserInfo;
import com.ting.a.livehome.bean.UserOrderInfo;
import com.ting.a.livehome.dao.DataDao;
import com.ting.a.livehome.unit.DataContact;
import com.ting.a.livehome.unit.LiveHomeApp;
import com.ting.a.livehome.unit.Toast;
import com.ting.a.livehome.unit.ZProgressHUD;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 添加订单页面
 */
public class AddOrderActivity extends Activity implements View.OnClickListener {


    private Context context = AddOrderActivity.this;
    //时间格式转换
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat orderCodeFor = new SimpleDateFormat("yyyyMMddHHmmss");
    private TextView tv_title;//标题
    private View left_but;//左上角的返回按钮
    private Button cancelBut;//取消按钮
    private Button confirmBut;//确定按钮,总价格也会显示在这个按钮上

    private EditText num;//订购的数量文本狂
    private EditText backNum;//置换的空桶文本框
    private TextView commodity_type;//选择商品种类文本框
    private TextView commodity_size;
    private TextView commodity_price;
    private TextView commodity_supplier;
    private Button subtract_but, plus_but;//加减按钮
    private View loading_view;


    private float totalPrice;//总价格
    public ZProgressHUD pDialog;//加載窗口
    private UserInfo userInfo;//用户信息
    private ArrayList<CommodityInfo> commosityInfoArrayList = null;//商品信息
    private String[] selectDataList;//商品选择数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);//加載頁面

        //寻找最上方的Title
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("添加订单");//修改为添加订单的title
        left_but = findViewById(R.id.left_but);//寻找左上方的返回按钮
        left_but.setVisibility(View.VISIBLE);//并将它显示出来

        //绑定控件
        cancelBut = findViewById(R.id.cancelBut);
        confirmBut = findViewById(R.id.confirmBut);
        commodity_type = findViewById(R.id.commodity_type);
        backNum = findViewById(R.id.backNum);
        num = findViewById(R.id.num);
        commodity_size = findViewById(R.id.commodity_size);
        commodity_price = findViewById(R.id.commodity_price);
        commodity_supplier = findViewById(R.id.commodity_supplier);
        subtract_but = findViewById(R.id.subtract_but);
        plus_but = findViewById(R.id.plus_but);
        loading_view = findViewById(R.id.loading_view);

        //添加点击事件
        confirmBut.setOnClickListener(this);
        cancelBut.setOnClickListener(this);
        left_but.setOnClickListener(this);
        commodity_type.setOnClickListener(this);
        subtract_but.setOnClickListener(this);
        plus_but.setOnClickListener(this);
        getDataLoding();


        //给订购数量的文本框设置文本改变监听器
        num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //里面的文本开始发生改变时，如果不为空我们就将填写的数量*单价=总价
                if (!s.toString().isEmpty()) {
                    String type = commodity_type.getTag().toString();//获取类型以便计算价格 1：直接使用单价*数量 2：单价*12*数量
                    float price = 0;
                    try {
                        //获取单价，因为显示的是字符串，所以需要强行转换
                        price = Float.parseFloat(commodity_price.getText().toString());
                    } catch (Exception e) {
                    }
                    switch (type) {
                        case "1":
                            totalPrice = Float.parseFloat(s.toString()) * price;
                            break;
                        case "2":
                            totalPrice = Float.parseFloat(s.toString()) * price * 12;
                            break;
                    }
                    //把算好的总价显示在提交按钮上
                    confirmBut.setText("(￥" + (float) (Math.round(totalPrice * 100)) / 100 + ") 提交");
                } else {//如果是空的我們就*0
                    totalPrice = 0 * 0;
                    //把算好的总价显示在提交按钮上
                    confirmBut.setText("(￥" + totalPrice + ") 提交");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //我们默认设置订购数量为1
        commodity_type.setTag(1);
        num.setText("1");
    }


    //控件的点击事件监听器
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //当点击的是提交按钮时
            case R.id.confirmBut:
                //我们根据全局登录的user的ID来查询他的具体的信息
                userInfo = DataDao.getInstance(AddOrderActivity.this).findgUser();
                //查询出来之后判断他是否有填写电话号码，如果没有就提示填写
                if (userInfo.getPhone() == null || userInfo.getPhone().trim().isEmpty()) {
                    Toast.show(AddOrderActivity.this, "请在填写默认的收货电话。", Toast.LENGTH_LONG);
                    setUserPhone();
                    break;
                }
                //查询出来之后判断他是否有填写收货地址，如果没有就提示填写
                if (userInfo.getUserAdds() == null || userInfo.getUserAdds().trim().isEmpty()) {
                    Toast.show(AddOrderActivity.this, "请填写默认的收货地址", Toast.LENGTH_LONG);
                    setUserAdds();
                    break;
                }
                //如果都填写了我们就进行订单的保存
                saveLoding();
                break;
            //取消和返回的按鈕事件都是一样的关闭这个页面
            case R.id.cancelBut:
            case R.id.left_but:
                finish();
                break;
            case R.id.commodity_type://需要弹窗选择种类
                selectCommodityTypeDialog(selectDataList);
                break;
            case R.id.subtract_but://减数量
                setNum(-1);
                break;
            case R.id.plus_but://加数量
                setNum(1);
                break;
        }
    }

    //订单数量加减
    private void setNum(int type) {
        String nText = num.getText().toString();
        if (nText != null && !nText.trim().isEmpty()) {
            int n = Integer.parseInt(nText);
            if (type == 1) {
                if (n < 99) {
                    n++;
                    num.setText(n + "");
                }
            } else {
                if (n > 1) {
                    n--;
                    num.setText(n + "");
                }
            }
        }
    }

    //创建一个选择商品的弹窗
    private void selectCommodityTypeDialog(final String[] data) {

        AlertDialog.Builder commodityDialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        commodityDialog.setTitle("请选择商品");
        commodityDialog.setItems(data, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //根据选择的商品类型去更新其他页面显示信息
                updateUI(data[i]);
            }
        });
        commodityDialog.show();
    }

    //根据选择的商品更新页面方法
    private void updateUI(String commodityType) {
        CommodityInfo cInfo = new CommodityInfo();
        for (int i = 0; i < commosityInfoArrayList.size(); i++) {
            cInfo = commosityInfoArrayList.get(i);
            if (cInfo.getCommodityName().equals(commodityType))
                break;
        }
        //更新选择的商品文本框
        commodity_type.setText(commodityType);
        commodity_type.setTag(cInfo.getCommodityType());
        //更新规格
        commodity_size.setText(cInfo.getCommoditySize());
        //更新价格
        commodity_price.setText(cInfo.getCommodityPrice());
        //更新供应商
        commodity_supplier.setText(cInfo.getCommoditySupplier());
        //更新文本框 默认订购为1
        num.setText("1");
    }


    //创建保存订单
    private List<UserOrderInfo> getOrder() {
        //声明一个list集合来存放订单
        List<UserOrderInfo> orderList = new ArrayList<>();

        UserOrderInfo order = new UserOrderInfo();

        order.setUserId(DataDao.getInstance(context).findgUser().getUserId());
        //判断页面上填写的值，并转换为Integer
        if (!num.getText().toString().trim().isEmpty())
            order.setOrderNum(num.getText().toString());
        //获取当前的时间
        order.setOrderCreatDate(formatter.format(new Date()));
        order.setOrderDealDate(formatter.format(new Date()));
        //订单更新时间
        order.setOrderModifyDate(formatter.format(new Date()));
        //这里获取商品规格
        order.setOrderSize(commodity_size.getText().toString());
        //订单供应商
        order.setOrderSupplier(commodity_supplier.getText().toString());
        //订单的单价
        order.setOrderPrice(commodity_price.getText().toString());
        //订单的总金额
        order.setOrderTotalPrice(totalPrice + "");
        //订单的编号，自己写的157794的字符加上时间转换的字符
        order.setOrderNo("157794" + orderCodeFor.format(new Date()));
        //订单的状态，默认为等待确认 ，1是等待确认，2是订单确认，3是发货中，4是订单完成，-1是已取消
        order.setOrderState("1");
        //订单类型也就是商品名字
        order.setOrderType(commodity_type.getText().toString());
        //收货人,还未收货默认为空
        order.setOrderConsignee("");
        //收货地址
        order.setOrderConsigneeAdd(DataDao.getInstance(context).findgUser().getUserAdds());
        //收货时间，还未收货默认为空
        order.setOrderConsigneeDate("");
        //发货时间，后台更新发货时间
        order.setOrderSenDate("");

        //添加到list集合中
        orderList.add(order);
        return orderList;
    }


    //保存订单异步保存
    private void saveLoding() {
        {
            @SuppressLint("StaticFieldLeak")
            AsyncTask<Void, Integer, Integer> t = new AsyncTask<Void, Integer, Integer>() {
                @Override
                //第一个执行
                protected void onPreExecute() {
                    //初始化加载窗口
                    pDialog = new ZProgressHUD(AddOrderActivity.this);
                    pDialog.setMessage("提交订单中...");
                    pDialog.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
                    pDialog.show();
                }

                @Override
                //第二个执行
                protected Integer doInBackground(Void... voids) {
                    try {
                        String data = submitOrder(DataContact.SAVE_ORDER_API, getOrder());
                        JSONObject jsonObject = new JSONObject(data);
                        int res = jsonObject.getInt("code");
                        if (res == 0)
                            return 1;
                        else
                            return 2;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return -1;
                    }
                }

                @Override
                //第三个执行
                protected void onPostExecute(Integer s) {
                    //根据返回的值进行判断
                    try {
                        //返回的是1就是成功
                        if (s == 1) {
                            Toast.show(AddOrderActivity.this, "订单提交成功", Toast.LENGTH_LONG);
                            finish();
                        } else {
                            pDialog.dismissWithFailure("订单提交失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            t.execute();
        }
    }


    //获取商品
    private void getDataLoding() {
        {
            @SuppressLint("StaticFieldLeak")
            AsyncTask<Void, Integer, Integer> t = new AsyncTask<Void, Integer, Integer>() {
                @Override
                //第一个执行
                protected void onPreExecute() {
                    //初始化加载窗口
                    loading_view.setVisibility(View.VISIBLE);
                }

                @Override
                //第二个执行
                protected Integer doInBackground(Void... voids) {
                    try {
                        Integer result = null;
                        String data = getCommodityData(DataContact.GET_COMMODITY_API);
                        JSONObject json = new JSONObject(data);
                        result = json.getInt("code");
                        if (result == 0) {
                            Gson gson = new Gson();
                            commosityInfoArrayList = gson.fromJson(json.getString("data"), new TypeToken<List<CommodityInfo>>() {
                            }.getType());

                            if (commosityInfoArrayList.size() > 0)
                                selectDataList = new String[commosityInfoArrayList.size()];
                            for (int i = 0; i < commosityInfoArrayList.size(); i++) {
                                selectDataList[i] = commosityInfoArrayList.get(i).getCommodityName();
                            }
                            return 1;
                        } else
                            return 2;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return -1;
                    }
                }

                @Override
                //第三个执行
                protected void onPostExecute(Integer s) {
                    //根据返回的值进行判断
                    try {
                        //返回的是1就是成功
                        if (s == 1) {
                            if (selectDataList.length > 1) {
                                commodity_type.setText(selectDataList[0]);
                                updateUI(selectDataList[0]);
                            }
                        }
                        loading_view.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            };
            t.execute();
        }
    }

    //提交商品订单
    private String submitOrder(String url, List<UserOrderInfo> orderSubmitList) throws Exception {
        OkHttpClient httpClient = new OkHttpClient();
        Gson gson = new Gson();
        //使用Gson将对象转换为json字符串
        String json = gson.toJson(orderSubmitList);
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful())
            return response.body().string();
        else
            throw new IOException("Unexpected code" + response);
    }

    //获取商品数据
    private String getCommodityData(String url) throws Exception {

        OkHttpClient httpClient = new OkHttpClient();
        FormBody body = new FormBody.Builder().build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful())
            return response.body().string();
        else
            throw new IOException("Unexpected code" + response);
    }


    //如果没有收货地址会要求填写收货地址
    private void setUserAdds() {
        //点击之后我们创建一个EditText放在AlertDialog.Builder，弹窗里面，然后显示弹窗
        final EditText inputServer = new EditText(context);
        inputServer.setText(userInfo.getUserAdds());//设置之前已经有的地址，便于修改
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);//初始化弹窗
        builder.setTitle("请输入收货地址").setView(inputServer)//设置提示和把EditText添加到弹窗里面
                .setNegativeButton("取消", null);//设置取消按钮，没有设置点击事件
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {//设置保存按钮，监听事件如下
                //将刚刚我们放进弹窗的文本框inputServer ，从里面获取填写的信息保存到用户表里面
                DataDao.getInstance(context).updataUserAdds(userInfo.getID(), inputServer.getText().toString());
                //保存成功后重新查询。
                userInfo = DataDao.getInstance(context).findgUserByID(userInfo.getID());
                Toast.show(context, "添加成功，可以继续下订单了", Toast.LENGTH_LONG);
            }
        });
        builder.show();
    }

    //如果没有收货电话会要求填写收货电话
    private void setUserPhone() {
        //点击之后我们创建一个EditText放在AlertDialog.Builder，弹窗里面，然后显示弹窗
        final EditText inputServer = new EditText(context);
        inputServer.setText(userInfo.getPhone());//设置之前已经有的电话，便于修改
        //设置最大的输入长度为11
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        //设置填写的要是电话号码的格式
        inputServer.setInputType(InputType.TYPE_CLASS_PHONE);
        //初始化彈窗
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("请输入电话号码").setView(inputServer)//设置提示和把EditText添加到弹窗里面
                .setNegativeButton("取消", null);//设置取消按钮，没有设置点击事件
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {//设置保存按钮，监听事件如下
                //将刚刚我们放进弹窗的文本框inputServer ，从里面获取填写的信息保存到用户表里面
                DataDao.getInstance(context).updataUserPhone(userInfo.getID(), inputServer.getText().toString());
                //保存成功后重新查询。
                userInfo = DataDao.getInstance(context).findgUserByID(userInfo.getID());
                Toast.show(context, "添加成功，可以继续下订单了", Toast.LENGTH_LONG);
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commosityInfoArrayList = null;
    }
}
