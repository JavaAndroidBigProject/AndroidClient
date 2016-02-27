package shu.gobang.androidclient;

import android.app.Application;

import Client.AndroidInterface;

/**
 * Created by Administrator on 2016/2/27.
 */
public class MyApplication extends Application{
    public boolean isInTableActivity;
    public TableActivity.TableHandle tableHandle;
    public AndroidInterface androidInterface;
    public LoginActivity.LoginHandle loginHandle;
    public RegisterActivity.RegisterHandle registerHandle;
    public String currentActivity;
    public MyApplication(){
        super();
    }
}
