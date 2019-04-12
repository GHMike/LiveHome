package com.ting.a.livehome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.ting.a.livehome.R;
import com.ting.a.livehome.fragment.FindFragment;
import com.ting.a.livehome.fragment.MainFragment;
import com.ting.a.livehome.fragment.MyFragment;
import com.ting.a.livehome.unit.Toast;

import java.util.List;


/**
 * 主界面外框
 */
public class MainActivity extends FragmentActivity {

    private int index;
    // 当前fragment的index
    private int currentTabIndex;
    private TextView tv_title;//最上面的title
    private BottomNavigationView navigation;//底部的三个切换按钮
    private MainFragment mainFragment;
    private FindFragment findFragment;
    private MyFragment myFragment;
    private Fragment[] fragments;
    private String[] fragmentsTag = new String[]{"main", "find", "my"};
    FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//加载页面
        navigation = findViewById(R.id.navigation);//寻找到底部显示三个切换按钮的控件
        //设置里面的按钮选择的监听事件
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        tv_title = findViewById(R.id.tv_title);

        mainFragment = MainFragment.newInstance();
        findFragment = FindFragment.newInstance("", "");
        myFragment = MyFragment.newInstance();

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.contentPanel, MainFragment.newInstance());
        transaction.commit();
    }

    //底部三个按钮的切换的监听事件
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // 点击时启动trancaction事件
            transaction = manager.beginTransaction();
            switch (item.getItemId()) {
                //点击到第一个按钮
                case R.id.navigation_home:
                    tv_title.setText(R.string.app_name);
                    transaction.replace(R.id.contentPanel, mainFragment);
                    transaction.commit();
                    return true;
                //点击到第二个按钮
                case R.id.navigation_dashboard:
                    tv_title.setText(R.string.title_dashboard);
                    transaction.replace(R.id.contentPanel, findFragment);
                    transaction.commit();
                    return true;
                //点击到第三个按钮
                case R.id.navigation_notifications:
                    tv_title.setText(R.string.title_notifications);
                    transaction.replace(R.id.contentPanel, myFragment);
                    transaction.commit();
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0
                    || index >= fm.getFragments().size()) {
                Log.w("onActivityResult", "Activity result fragment index out of range: 0x"
                        + Integer.toHexString(requestCode));
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {
                Log.w("onActivityResult", "Activity result no fragment exists for index: 0x"
                        + Integer.toHexString(requestCode));
            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }
    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }


    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.show(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
