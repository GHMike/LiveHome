package com.ting.a.livehome.unit;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.ting.a.livehome.bean.UserInfo;


public class LiveHomeApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.i

//        nstall(this);
    }

}
