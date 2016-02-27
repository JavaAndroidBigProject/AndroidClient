package Client;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.net.InetAddress;
import ServerInterface.OriginInterface;
import ServerInterface.PlayerInfo;
import ServerInterface.TableInfo;
import shu.gobang.androidclient.MyApplication;

/**
 * Created by Administrator on 2016/2/27.
 */
public class AndroidInterface extends OriginInterface{
    public MyApplication myApplication;
    public AndroidInterface(InetAddress inetAddress, int port, MyApplication myApplication){
        super(inetAddress,port);
        this.myApplication = myApplication;

    }
    @Override
    public void onRespondRegister(boolean ifRegistered, String reason) {
        Message msg= myApplication.registerHandle.obtainMessage();
        if(ifRegistered)
            msg.what = 1;
        else{
            msg.what = 0;
            Bundle bundle = new Bundle();
            bundle.putString("reason",reason);
            msg.setData(bundle);
        }
        myApplication.registerHandle.sendMessage(msg);
    }

    @Override
    public void onConnectionFail(String reason) {
//        Log.e("xie","连接失败"+reason);
    }

    @Override
    public void onLostConnection(String reason) {
        System.exit(0);
    }

    @Override
    public void onRespondLogin(boolean ifLogined, int score, String reason) {
        Message msg= myApplication.loginHandle.obtainMessage();
        if(ifLogined)
            msg.what = 1;
        else{
            msg.what = 0;
            Bundle bundle = new Bundle();
            bundle.putString("reason",reason);
            msg.setData(bundle);
        }
        myApplication.loginHandle.sendMessage(msg);
    }

    @Override
    public void onRespondGetTables(TableInfo[] tableInfos) {
        if(myApplication.isInTableActivity){
            Bundle bundle = new Bundle();
            bundle.putSerializable("tableinfos", tableInfos);
            Message msg = myApplication.tableHandle.obtainMessage();
            msg.what = 0;
            msg.setData(bundle);
            myApplication.tableHandle.sendMessage(msg);
        }
    }

    @Override
    public void onRespondEnterTable(int tableId, boolean ifEntered, String reason) {
        Bundle bundle = new Bundle();
        Message msg = myApplication.tableHandle.obtainMessage();
        msg.what = 1;
        bundle.putInt("tableId",tableId);
        bundle.putBoolean("ifEntered", ifEntered);
        bundle.putString("reason",reason);
        msg.setData(bundle);
        myApplication.tableHandle.sendMessage(msg);
    }

    @Override
    public void onTableChange(PlayerInfo myInfo, PlayerInfo opponentInfo, boolean ifMyHandUp, boolean ifOpponentHandUp, boolean isPlaying, int[][] board, boolean isBlack, boolean isMyTurn) {

    }

    @Override
    public void onGameOver(boolean isDraw, boolean ifWin, boolean ifGiveUp) {

    }

    @Override
    public void onRespondRetract(boolean ifAgree) {

    }

    @Override
    public void onOpponentRetract() {

    }

    @Override
    public void onReceiveMessage(String message) {

    }

    @Override
    public void onRespondQuitTable() {

    }
}
