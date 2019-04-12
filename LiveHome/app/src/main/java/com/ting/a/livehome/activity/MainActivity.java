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
import android.view.MenuItem;
import android.widget.TextView;

import com.ting.a.livehome.R;
import com.ting.a.livehome.fragment.FindFragment;
import com.ting.a.livehome.fragment.MainFragment;
import com.ting.a.livehome.fragment.MyFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//加载页面
        navigation = findViewById(R.id.navigation);//寻找到底部显示三个切换按钮的控件
        //设置里面的按钮选择的监听事件
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        tv_title = findViewById(R.id.tv_title);

        mainFragment = new MainFragment();
        findFragment = new FindFragment();
        myFragment = new MyFragment();
        fragments = new Fragment[]{mainFragment, findFragment,
                myFragment};
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(mainFragment, fragmentsTag[0])
                .add(findFragment, fragmentsTag[1])
                .add(myFragment, fragmentsTag[2])
                .hide(findFragment).hide(myFragment).show(mainFragment).commit();
    }

    //底部三个按钮的切换的监听事件
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                //点击到第一个按钮
                case R.id.navigation_home:
                    index = 0;
                    if (currentTabIndex != index) {
                        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
                        trx.hide(fragments[currentTabIndex]);

                        if (!fragments[index].isAdded()) {
                            trx.add(fragments[index], fragmentsTag[index]);
                        }
                        trx.show(fragments[index]).commit();
                    }
                    currentTabIndex = index;
                    return true;
                //点击到第二个按钮
                case R.id.navigation_dashboard:
                    index = 1;
                    if (currentTabIndex != index) {
                        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
                        trx.hide(fragments[currentTabIndex]);

                        if (!fragments[index].isAdded()) {
                            trx.add(fragments[index], fragmentsTag[index]);
                        }
                        trx.show(fragments[index]).commit();
                    }
                    currentTabIndex = index;
                    return true;
                //点击到第三个按钮
                case R.id.navigation_notifications:
                    index = 2;
                    if (currentTabIndex != index) {
                        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
                        trx.hide(fragments[currentTabIndex]);

                        if (!fragments[index].isAdded()) {
                            trx.add(fragments[index], fragmentsTag[index]);
                        }
                        trx.show(fragments[index]).commit();
                    }
                    currentTabIndex = index;
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
}
