package com.ting.a.livehome.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ting.a.livehome.R;
import com.ting.a.livehome.activity.MessageActivity;
import com.ting.a.livehome.adapter.MainAdapter;
import com.ting.a.livehome.bean.MessageInfo;
import com.ting.a.livehome.unit.DataContact;

import org.json.JSONObject;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 主页面消息列表页面（层）
 */
public class MainFragment extends Fragment {
    private ListView mainList;
    private TextView res_But;//加载失败，点击刷新按钮
    private List<MessageInfo> messageList;
    private View loading_view;

    public MainFragment() {
    }

    public static Fragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //加载控件
    private void initW(View v) {
        mainList = v.findViewById(R.id.mainList);
        res_But = v.findViewById(R.id.res_But);
        loading_view = v.findViewById(R.id.loading_view);
        res_But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lodingTask.execute();
            }
        });


        lodingTask.execute();
    }

    //异步网络请求加载网络数据
    @SuppressLint("StaticFieldLeak")
    AsyncTask<Void, Integer, Integer> lodingTask = new AsyncTask<Void, Integer, Integer>() {

        @Override
        protected void onPreExecute() {
            //初始化加载窗
            loading_view.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... voids) {

            try {
                String data = getUrlNewsData(DataContact.GET_NEWS_LIST_API);
                if (data != null) {
                    JSONObject jsonObject = new JSONObject(data);
                    int res = jsonObject.getInt("code");
                    Gson gos = new Gson();
                    messageList = gos.fromJson(jsonObject.getString("data"), new TypeToken<List<MessageInfo>>() {
                    }.getType());
                    return res;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer res) {
            if (res != null && res == 0) {
                showData();
                res_But.setVisibility(View.GONE);
            } else {
                res_But.setVisibility(View.VISIBLE);
            }
            loading_view.setVisibility(View.GONE);
        }
    };


    //通过地址获取新闻
    private String getUrlNewsData(String url) {
        try {
            OkHttpClient client = new OkHttpClient();
            FormBody body = new FormBody.Builder().build();

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

    //显示数据
    private void showData() {
        try {
            final MainAdapter min = new MainAdapter(getActivity(), messageList);
            mainList.setAdapter(min);
            mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), MessageActivity.class);
                    MessageInfo info = min.getItem(position);
                    intent.putExtra("newsUrl", info.getNewsUrl());
                    intent.putExtra("title", info.getNewsName());
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View con = inflater.inflate(R.layout.fragment_main, container, false);
        initW(con);
        return con;

    }
}
