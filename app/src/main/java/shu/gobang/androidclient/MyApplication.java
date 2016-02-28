package shu.gobang.androidclient;

import android.app.Application;

import Client.AndroidInterface;

/**
 * Created by Administrator on 2016/2/27.
 */
public class MyApplication extends Application{
    public static MyApplication application;
//    public GameActivity.GameHandle gameHandle;
//    public TableActivity.TableHandle tableHandle;
//    //    public AndroidInterface androidInterface;
//    public LoginActivity.LoginHandle loginHandle;
//    public RegisterActivity.RegisterHandle registerHandle;
//    public boolean isInTableActivity;
//    public boolean isInGameActivity;
//    public String currentActivity;
    public MyApplication(){
        super();
        application = this;
    }
}
