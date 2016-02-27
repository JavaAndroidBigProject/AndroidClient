package shu.gobang.androidclient;

import android.app.Application;

import Client.AndroidInterface;

/**
 * Created by Administrator on 2016/2/27.
 */
public class MyApplication extends Application{
    public AndroidInterface androidInterface;
    public String currentActivity;
    public MyApplication(){
        super();
    }
}
