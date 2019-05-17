package com.ting.a.livehome.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ting.a.livehome.R;
import com.ting.a.livehome.bean.UserInfo;
import com.ting.a.livehome.dao.DataDao;
import com.ting.a.livehome.unit.DataContact;
import com.ting.a.livehome.unit.Toast;
import com.ting.a.livehome.unit.Tools;
import com.ting.a.livehome.unit.ZProgressHUD;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 注册页面
 */
public class RegisterActivity extends Activity implements OnClickListener {

    private Context context = RegisterActivity.this;
    private TextView tv_title;//最上方的标题
    private View left_but;//左上角的返回按钮
    private EditText user_text;//用戶名文本框
    private EditText phone_text;//用戶手机号码
    private EditText pwass_text;//用戶密碼
    private EditText pwass_text2;//第二个用户密码
    public ZProgressHUD pDialog;//加載窗
    private Button submit_but;//注册按钮


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initW();
    }

    /**
     * 初始化页面
     */
    public void initW() {
        //寻找页面上用到的控件
        user_text = this.findViewById(R.id.user_text);
        pwass_text = this.findViewById(R.id.pwass_text);
        pwass_text2 = this.findViewById(R.id.pwass_text2);
        phone_text = this.findViewById(R.id.phone_text);
        submit_but = this.findViewById(R.id.submit_but);
        //
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("用户注册");
        left_but = findViewById(R.id.left_but);
        left_but.setVisibility(View.VISIBLE);
        //设置点击事件
        submit_but.setOnClickListener(this);
        left_but.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_but:
                //需要对填写的信息进行验证，验证通过才能提交注册
                if (verificationAccount())
                    register();
                break;
            case R.id.left_but:
                finish();
                break;
        }

    }

    /**
     * 验证是否有填写手机号码
     *
     * @return
     */
    private boolean verificationAccount() {
        if (user_text.getText().toString().isEmpty()) {
            Toast.show(context, "请填写用户名", Toast.LENGTH_LONG);
            return false;
        } else if (phone_text.getText().toString().isEmpty()) {
            Toast.show(context, "请填写手机号码", Toast.LENGTH_LONG);
            return false;
        } else if (Tools.isMobile(phone_text.getText().toString())) {
            Toast.show(context, "请填写正确的手机号码", Toast.LENGTH_LONG);
            return false;
        } else if (pwass_text.getText().toString().isEmpty()) {
            Toast.show(context, "请填写登录密码", Toast.LENGTH_LONG);
            return false;
        } else if (pwass_text.getText().toString().trim().length() < 6) {
            Toast.show(context, "密码必须大于6位", Toast.LENGTH_LONG);
            return false;
        } else if (!pwass_text.getText().toString().equals(pwass_text2.getText().toString())) {
            Toast.show(context, "请确认两次密码相同", Toast.LENGTH_LONG);
            return false;
        } else
            return true;

    }


    /**
     * 本地注册
     */
    private void register() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Integer, Integer> t = new AsyncTask<Void, Integer, Integer>() {
            @Override
            protected void onPreExecute() {
                pDialog = new ZProgressHUD(context);
                pDialog.setMessage("注册中");
                pDialog.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
                pDialog.show();
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                try {
                    String data = getUrlRegisterData(DataContact.REGISTER_API);
                    JSONObject jsonObject = new JSONObject(data);
                    Log.d("RegisterData", jsonObject.toString());
                    int res = jsonObject.getInt("code");
                    return res;
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            }

            @Override
            protected void onPostExecute(Integer s) {
                if (s == 0) {
                    Toast.show(RegisterActivity.this, "注册成功,请重新登录", Toast.LENGTH_LONG);
                    finish();
                } else {
                    pDialog.dismissWithFailure("注册失败,必要信息不完整!");
                }
            }


        };
        t.execute();
    }


    //通过url以post的方式提交用户注册信息
    public String getUrlRegisterData(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        //构建FormBody，传入要提交的参数
        FormBody formBody = new FormBody.Builder()
                .add("userName", user_text.getText().toString())
                .add("userPaw", pwass_text.getText().toString())
                .add("userPhone", phone_text.getText().toString())
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
