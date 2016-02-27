package Client;

import java.net.InetAddress;

import ServerInterface.OriginInterface;
import ServerInterface.PlayerInfo;
import ServerInterface.TableInfo;

/**
 * Created by Administrator on 2016/2/27.
 */
public class AndroidInterface extends OriginInterface{
    public AndroidInterface(InetAddress inetAddress, int port){
        super(inetAddress,port);
    }
    @Override
    public void onRespondRegister(boolean ifRegistered, String reason) {

    }

    @Override
    public void onConnectionFail(String reason) {

    }

    @Override
    public void onLostConnection(String reason) {

    }

    @Override
    public void onRespondLogin(boolean ifLogined, int score, String reason) {

    }

    @Override
    public void onRespondGetTables(TableInfo[] tableInfos) {

    }

    @Override
    public void onRespondEnterTable(int tableId, boolean ifEntered, String reason) {

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
