package Client;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import ServerInterface.OriginInterface;
import ServerInterface.PlayerInfo;
import ServerInterface.TableInfo;
import shu.gobang.androidclient.GameActivity;
import shu.gobang.androidclient.LoginActivity;
import shu.gobang.androidclient.MyApplication;
import shu.gobang.androidclient.RegisterActivity;
import shu.gobang.androidclient.TableActivity;

/**
 * Created by Administrator on 2016/2/27.
 */
public class AndroidInterface extends OriginInterface{
    static boolean isConnneted;
    public boolean reaction, changed;

    public GameActivity.GameHandle gameHandle;
    public TableActivity.TableHandle tableHandle;
    //    public AndroidInterface androidInterface;
    public LoginActivity.LoginHandle loginHandle;
    public RegisterActivity.RegisterHandle registerHandle;
    public boolean isInTableActivity;
    public boolean isInGameActivity;

    private static AndroidInterface androidInterface;
    public MyApplication myApplication;
    private AndroidInterface(InetAddress inetAddress, int port, MyApplication myApplication){
        super(inetAddress,port);
        this.myApplication = myApplication;
    }

    public static AndroidInterface getInstance(){
        if(androidInterface == null){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        androidInterface = new AndroidInterface(InetAddress.getByName("yidea.xyz"), 4000, MyApplication.application);
                    } catch (IOException e) {

                    }
                }
            });
            thread.start();
            try {
                thread.join();
            }catch (InterruptedException e){

            }
        }
        return androidInterface;
    }
    @Override
    public void onRespondRegister(boolean ifRegistered, String reason) {
        Log.e("xie","onRespondRegister");
        Message msg= registerHandle.obtainMessage();
        if(ifRegistered)
            msg.what = 1;
        else{
            msg.what = 0;
            Bundle bundle = new Bundle();
            bundle.putString("reason",reason);
            msg.setData(bundle);
        }
        registerHandle.sendMessage(msg);
    }

    @Override
    public void onConnectionFail(String reason) {
        Log.e("xie","onConnectionFail");
//        Log.e("xie","连接失败"+reason);
    }

    @Override
    public void onLostConnection(String reason) {
//        isConnneted = false;
        Log.e("xie","onLostConnection  "+reason);
//        System.exit(0);
    }

    @Override
    public void onRespondLogin(boolean ifLogined, int score, String reason) {
        if(loginHandle != null) {
            Message msg = loginHandle.obtainMessage();
            if (ifLogined)
                msg.what = 1;
            else {
                msg.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("reason", reason);
                msg.setData(bundle);
            }
            loginHandle.sendMessage(msg);
        }
    }

    @Override
    public void onRespondGetTables(TableInfo[] tableInfos) {
//        Log.e("xie","oonRespondGetTables");
        if(isInTableActivity){
            if(tableHandle != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("tableinfos", tableInfos);
                Message msg = tableHandle.obtainMessage();
                msg.what = 0;
                msg.setData(bundle);
                tableHandle.sendMessage(msg);
            }
        }
    }

    @Override
    public void onRespondEnterTable(int tableId, boolean ifEntered, String reason) {
//        Log.e("xie","onRespondEnterTable");
        if(tableHandle != null) {
            Bundle bundle = new Bundle();
            Message msg = tableHandle.obtainMessage();
            msg.what = 1;
            bundle.putInt("tableId", tableId);
            bundle.putBoolean("ifEntered", ifEntered);
            bundle.putString("reason", reason);
            msg.setData(bundle);
            tableHandle.sendMessage(msg);
        }
    }

    @Override
    public void onTableChange(PlayerInfo myInfo, PlayerInfo opponentInfo, boolean ifMyHandUp, boolean ifOpponentHandUp, boolean isPlaying, int[][] board, boolean isBlack, boolean isMyTurn) {
//        Log.e("xie","onTableChange");
        if(isInGameActivity) {
            if (gameHandle != null) {
                Message message = gameHandle.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("myname", myInfo.name);
                bundle.putInt("myscroe", myInfo.score);
                bundle.putString("opponentname", opponentInfo.name);
                bundle.putInt("opponentscroe", opponentInfo.score);
                bundle.putSerializable("board", board);
                bundle.putBoolean("ifMyHandUp", ifMyHandUp);
                bundle.putBoolean("ifOpponentHandUp", ifOpponentHandUp);
                bundle.putBoolean("isPlaying", isPlaying);
                bundle.putBoolean("isBlack", isBlack);
                bundle.putBoolean("isMyTurn", isMyTurn);
                message.setData(bundle);
                message.what = 0;
                gameHandle.sendMessage(message);
            }
        }
    }

    @Override
    public void onGameOver(boolean isDraw, boolean ifWin, boolean ifGiveUp) {
        if(isInGameActivity){
            if(gameHandle != null) {
                String message= "";
                if(isDraw)
                    message = "和棋!";
                else{
                    if(ifWin && ifGiveUp)
                        message = "对手认输了!";
                    else if(ifWin && !ifGiveUp)
                        message = "恭喜你,你赢了!";
                    else if(!ifWin && ifGiveUp)
                        message = "你认输了!";
                    else if(!ifWin && !ifGiveUp)
                        message = "你输了!";
                }
                Message msg = gameHandle.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("message",message);
                msg.what = 5;
                msg.setData(bundle);
                gameHandle.sendMessage(msg);
            }
        }
    }

    @Override
    public void onRespondRetract(boolean ifAgree) {
        if(isInGameActivity){
            if(gameHandle != null) {
                Message msg = gameHandle.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putBoolean("ifAgree",ifAgree);
                msg.what = 4;
                msg.setData(bundle);
                gameHandle.sendMessage(msg);
            }
        }
    }

    @Override
    public void onOpponentRetract() {
        if(isInGameActivity){
            if(gameHandle != null) {
                Message msg = gameHandle.obtainMessage();
                msg.what = 3;
                gameHandle.sendMessage(msg);
            }
        }
    }

    @Override
    public void onReceiveMessage(String message) {
//        Log.e("xie","onReceiveMessage");
        if(isInGameActivity){
            if(gameHandle != null) {
                Message msg = gameHandle.obtainMessage();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("msg",message);
                msg.setData(bundle);
                gameHandle.sendMessage(msg);
            }
        }
    }

    @Override
    public void onRespondQuitTable() {
//        Log.e("xie","onRespondQuitTable");
    }
}
