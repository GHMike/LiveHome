package com.ting.a.livehome.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ting.a.livehome.R;
import com.ting.a.livehome.bean.UserInfo;
import com.ting.a.livehome.dao.DataDao;
import com.ting.a.livehome.dao.DataDao;
import com.ting.a.livehome.unit.DataContact;
import com.ting.a.livehome.unit.LiveHomeApp;
import com.ting.a.livehome.unit.Toast;
import com.ting.a.livehome.unit.ZProgressHUD;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 登录页面
 */
public class LoginActivity extends Activity implements OnClickListener {

    private Context context = LoginActivity.this;//
    private EditText user_text;//用戶名文本框
    private EditText pwass_text;//密码文本框
    public ZProgressHUD pDialog;//加载窗口
    private Button submit_but;//登录按钮
    private TextView re_but;//注册按钮
    OkHttpClient client = new OkHttpClient();//网络请求


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initW();
    }

    /**
     * 初始化页面
     */
    public void initW() {
        //寻找控件
        user_text = this.findViewById(R.id.user_text);
        pwass_text = this.findViewById(R.id.pwass_text);
        submit_but = this.findViewById(R.id.submit_but);
        re_but = this.findViewById(R.id.re_but);
        //设置点击事件
        re_but.setOnClickListener(this);
        submit_but.setOnClickListener(this);


        //获取保存的user标签里的属性username的值
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String username = pref.getString("username", "");
        if (username != null) {
            user_text.setText(username);
            pwass_text.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_but:
                //验证账号密码是否填写，验证通过才可登录
                if (verificationAccount())
                    logindb();
                break;
            case R.id.re_but:
                //调往注册页面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }

    }

    /**
     * 验证是否有填写账号和密码
     *
     * @return
     */
    private boolean verificationAccount() {
        if (user_text.getText().toString().isEmpty()) {
            Toast.show(context, "请填写账号", Toast.LENGTH_LONG);
            return false;
        } else if (pwass_text.getText().toString().isEmpty()) {
            Toast.show(context, "请填写登录密码", Toast.LENGTH_LONG);
            return false;
        } else
            return true;

    }


    /**
     * 本地登錄
     */
    private void logindb() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Integer, Integer> t = new AsyncTask<Void, Integer, Integer>() {
            @Override
            //第一执行
            protected void onPreExecute() {
                //初始化加载窗
                pDialog = new ZProgressHUD(context);
                pDialog.setMessage("登录中");
                pDialog.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
                if (!isFinishing())
                    pDialog.show();
            }

            @SuppressLint("WrongThread")
            @Override
            //第二执行
            protected Integer doInBackground(Void... voids) {
                try {
                    String data = getUrlData(DataContact.LOGIN_API);
                    JSONObject jsonObject = new JSONObject(data);
                    Gson gson = new Gson();
                    //利用Gson将获得的数据转换为userInfo对象
                    UserInfo userInfo = gson.fromJson(jsonObject.getString("data"), new TypeToken<UserInfo>() {
                    }.getType());
                    DataDao.getInstance(context).saveUser(userInfo, true);
                    int res = jsonObject.getInt("code");
                    return res;
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            }

            @Override
            //第三执行
            protected void onPostExecute(Integer s) {
                if (s == 0) {
                    pDialog.dismissWithSuccess("登录成功");
                    //保存 user标签下的属性 username的值
                    SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                    editor.putString("username", user_text.getText().toString());
                    editor.commit();//提交并写入

                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (s == -1) {
                    pDialog.dismissWithFailure("用户名或密码错误");
                } else {
                    pDialog.dismissWithFailure("登录失败");
                }
            }


        };
        t.execute();
    }


    public String getUrlData(String url) throws IOException {

        //构建FormBody，传入要提交的参数
        FormBody formBody = new FormBody.Builder()
                .add("username", user_text.getText().toString())
                .add("password", pwass_text.getText().toString())
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

}
