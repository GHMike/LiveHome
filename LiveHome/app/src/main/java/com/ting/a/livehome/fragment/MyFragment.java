package com.ting.a.livehome.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ting.a.livehome.R;
import com.ting.a.livehome.activity.CheckOrderActivity;
import com.ting.a.livehome.adapter.OrderAdapter;
import com.ting.a.livehome.bean.UserInfo;
import com.ting.a.livehome.bean.UserOrderInfo;
import com.ting.a.livehome.dao.DataDao;
import com.ting.a.livehome.unit.DataContact;
import com.ting.a.livehome.unit.Toast;
import com.ting.a.livehome.unit.Tools;
import com.ting.a.livehome.unit.ZProgressHUD;

import org.json.JSONObject;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 我的页面（层）
 */
public class MyFragment extends Fragment {
    private TextView userName_tv;
    private TextView adds_con_tv;
    private TextView phone_con_tv;
    private ListView orderListView;
    private View adds_tv;
    private View phone_tv;
    private View adds_View_lin;
    private View adds_View;
    private UserInfo userInfo = null;
    private View loading_view;
    private RoundedImageView handView;
    List<UserOrderInfo> orderList;

    public ZProgressHUD pDialog;//加載窗口

    public MyFragment() {
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = DataDao.getInstance(getActivity()).findgUser();
    }

    private void initW(View v) {
        userName_tv = v.findViewById(R.id.userName_tv);
        adds_con_tv = v.findViewById(R.id.adds_con_tv);
        phone_con_tv = v.findViewById(R.id.phone_con_tv);
        adds_tv = v.findViewById(R.id.adds_tv);
        phone_tv = v.findViewById(R.id.phone_tv);
        adds_View = v.findViewById(R.id.adds_View);
        adds_View_lin = v.findViewById(R.id.adds_View_lin);
        orderListView = v.findViewById(R.id.orderListView);
        loading_view = v.findViewById(R.id.loading_view);
        handView = v.findViewById(R.id.hand);

        userName_tv.setText(userInfo.getUserName());
        if (userInfo.getUserAdds() == null || userInfo.getUserAdds().trim().isEmpty()) {
            adds_View_lin.setVisibility(View.GONE);
            adds_View.setVisibility(View.GONE);
        } else {
            adds_View_lin.setVisibility(View.VISIBLE);
            adds_View.setVisibility(View.VISIBLE);
        }
        adds_con_tv.setText(userInfo.getUserAdds());
        phone_con_tv.setText(userInfo.getPhone());

        if (userInfo.getUserHand() != null && !userInfo.getUserHand().trim().isEmpty())
            Glide.with(this).load(userInfo.getUserHand()).into(handView);

        //这里添加地址点击事件监听
        adds_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击之后我们创建一个EditText放在AlertDialog.Builder，弹窗里面，然后显示弹窗
                final EditText inputServer = new EditText(getActivity());
                inputServer.setText(userInfo.getUserAdds());//设置之前已经有的地址，便于修改
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//初始化弹窗
                builder.setTitle("请输入收货地址").setView(inputServer)//设置提示和把EditText添加到弹窗里面
                        .setNegativeButton("取消", null);//设置取消按钮，没有设置点击事件
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {//设置保存按钮，监听事件如下
                        userInfo.setUserAdds(inputServer.getText().toString());
                        updateUserData(1);
                    }
                });
                builder.show();
            }
        });
        phone_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点击之后我们创建一个EditText放在AlertDialog.Builder，弹窗里面，然后显示弹窗
                final EditText inputServer = new EditText(getActivity());
                inputServer.setText(userInfo.getPhone());//设置之前已经有的电话，便于修改
                //设置最大的输入长度为11
                inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                //设置填写的要是电话号码的格式
                inputServer.setInputType(InputType.TYPE_CLASS_PHONE);
                //初始化彈窗
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("请输入电话号码").setView(inputServer)//设置提示和把EditText添加到弹窗里面
                        .setNegativeButton("取消", null);//设置取消按钮，没有设置点击事件

                //dialog点击其他地方不关闭
                builder.setCancelable(false);
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {//设置保存按钮，监听事件如下
                        if (Tools.isMobile(inputServer.getText().toString())) {
                            userInfo.setPhone(inputServer.getText().toString());
                            updateUserData(2);
                        } else {
                            Toast.show(getActivity(), "保存失败,请输入正确的手机号码", Toast.LENGTH_LONG);
                        }

                    }
                });
                builder.show();

            }
        });
        loadingData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadingData();
    }

    //修改用户信息
    @SuppressLint("StaticFieldLeak")
    private void updateUserData(final int updateType) {
        //异步网络请求加载网络数据
        new AsyncTask<Void, Integer, Integer>() {

            @Override
            protected void onPreExecute() {
                //初始化加载窗口
                pDialog = new ZProgressHUD(getActivity());
                pDialog.setMessage("保存中...");
                pDialog.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
                pDialog.show();
            }

            @Override
            protected Integer doInBackground(Void... voids) {

                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody body = null;
                    if (updateType == 1) {//修改地址
                        body = new FormBody.Builder()
                                .add("userId", userInfo.getUserId())
                                .add("userPaw", userInfo.getPassword())
                                .add("userAdds", userInfo.getUserAdds())
                                .build();
                    } else {//修改电话
                        body = new FormBody.Builder()
                                .add("userId", userInfo.getUserId())
                                .add("userPaw", userInfo.getPassword())
                                .add("userPhone", userInfo.getPhone())
                                .build();
                    }
                    Request request = new Request.Builder().url(DataContact.UPDATE_USER_API).post(body).build();
                    Response response = client.newCall(request).execute();
                    String data = "";
                    if (response.isSuccessful()) {
                        data = response.body().string();
                    }

                    if (data != null) {
                        JSONObject jsonObject = new JSONObject(data);
                        int res = jsonObject.getInt("code");
                        return res;
                    } else {
                        return -1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            }

            @Override
            protected void onPostExecute(Integer res) {
                if (res == 0) {
                    showData();
                    pDialog.dismissWithSuccess("保存成功");

                    if (updateType == 1) {
                        //将刚刚我们放进弹窗的文本框inputServer ，从里面获取填写的信息保存到用户表里面
                        DataDao.getInstance(getActivity()).updataUserAdds(userInfo.getID(), userInfo.getUserAdds());
                        //重新把当前页面的地址刷新到页面上去
                        adds_con_tv.setText(userInfo.getUserAdds());
                        //如果我们发现地址是空的，我们将隐藏地址栏这一块，反之就显示
                        if (userInfo.getUserAdds() == null || userInfo.getUserAdds().isEmpty()) {
                            adds_View_lin.setVisibility(View.GONE);
                            adds_View.setVisibility(View.GONE);
                        } else {
                            adds_View_lin.setVisibility(View.VISIBLE);
                            adds_View.setVisibility(View.VISIBLE);
                        }
                    } else {
                        //将刚刚我们放进弹窗的文本框inputServer ，从里面获取填写的信息保存到用户表里面
                        DataDao.getInstance(getActivity()).updataUserPhone(userInfo.getID(), userInfo.getPhone());
                        //重新把当前页面的地址刷新到页面上去
                        phone_con_tv.setText(userInfo.getPhone());
                    }

                } else {
                    pDialog.dismissWithFailure("保存失败");
                }
            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    private void loadingData() {
        //异步网络请求加载网络数据
        new AsyncTask<Void, Integer, Integer>() {

            @Override
            protected void onPreExecute() {
                //初始化加载progressBar
                loading_view.setVisibility(View.VISIBLE);
            }

            @Override
            protected Integer doInBackground(Void... voids) {

                try {
                    String data = getUrlNewsData(DataContact.GET_ORDER_API);
                    if (data != null) {
                        JSONObject jsonObject = new JSONObject(data);
                        int res = jsonObject.getInt("code");
                        Gson gos = new Gson();
                        orderList = gos.fromJson(jsonObject.getString("data"), new TypeToken<List<UserOrderInfo>>() {
                        }.getType());
                        return res;
                    } else {
                        return -1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            }

            @Override
            protected void onPostExecute(Integer res) {
                if (res == 0)
                    showData();
                else
                    Toast.show(getActivity(), "加载数据失败，请检查网络", Toast.LENGTH_LONG);
                loading_view.setVisibility(View.GONE);
            }
        }.execute();
    }

    //通过地址获取订单信息
    private String getUrlNewsData(String url) {
        try {
            OkHttpClient client = new OkHttpClient();
            FormBody body = new FormBody.Builder()
                    .add("userId", userInfo.getUserId())
                    .build();

            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取订单提示
    private void showData() {
        //查询所有订单显示在列表上
        if (orderList == null)
            return;
        //设置适配器，将数据和上下文传入
        final OrderAdapter orderAdapter = new OrderAdapter(getActivity(), orderList);
        orderListView.setAdapter(orderAdapter);//给Listview設置適配器
        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击进入订单查看页面，并且获取点击的条目的订单编号传进订单查看页面
                Intent intent = new Intent(getActivity(), CheckOrderActivity.class);
                intent.putExtra("orderCode", orderAdapter.getItem(position).getOrderNo());
                startActivityForResult(intent, 10001);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View con = inflater.inflate(R.layout.fragment_my, container, false);
        initW(con);
        return con;
    }

}
