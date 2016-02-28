package shu.gobang.androidclient;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Client.AndroidInterface;

/**
 * Created by Administrator on 2016/2/28.
 */
public class GameActivity extends AppCompatActivity{
    GameView gameView;
    Button send, giveup, retract;
    TextView my, op;
    EditText editText;
    GameHandle gameHandle;
    ImageView myiv, myiv2, myiv3, opiv, opiv2, opiv3;
//    boolean handup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        setTitle("游戏");

        gameView = (GameView)findViewById(R.id.gameview);
        send = (Button)findViewById(R.id.send);
        giveup = (Button)findViewById(R.id.giveup);
        retract = (Button)findViewById(R.id.retract);
        my = (TextView)findViewById(R.id.mytv);
        op = (TextView)findViewById(R.id.optv);
        editText = (EditText)findViewById(R.id.gameedit);
        myiv = (ImageView)findViewById(R.id.myiv);
        opiv = (ImageView)findViewById(R.id.opiv);
        myiv2 = (ImageView)findViewById(R.id.myiv2);
        opiv2 = (ImageView)findViewById(R.id.opiv2);
        myiv3 = (ImageView)findViewById(R.id.myiv3);
        opiv3 = (ImageView)findViewById(R.id.opiv3);

        gameHandle = new GameHandle();
        AndroidInterface.getInstance().gameHandle =  gameHandle;

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                if(msg != null)
                    AndroidInterface.getInstance().sengMessage(msg);
                editText.setText("");
            }
        });

        giveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidInterface.getInstance().giveUp();
            }
        });

        retract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidInterface.getInstance().retract();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gamemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.handup:
                AndroidInterface.getInstance().handUp();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


        @Override
    protected void onResume(){
        super.onResume();
        AndroidInterface.getInstance().isInTableActivity = false;
        AndroidInterface.getInstance().isInGameActivity = true;
//        ((MyApplication)getApplication()).gameHandle = new GameHandle();
    }

    @Override
    protected  void onStop(){
        super.onStop();
        AndroidInterface.getInstance().quitTable();
        AndroidInterface.getInstance().isInGameActivity = false;
    }

    public class GameHandle extends Handler{
        public GameHandle(){
            super();
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Bundle bundle = msg.getData();
                    String myname = bundle.getString("myname", "empty");
                    int myscroe = bundle.getInt("myscroe", 0);
                    String opponentname = bundle.getString("opponentname", "empty");
                    int opponentscroe = bundle.getInt("opponentscroe", 0);
                    int[][] board = (int[][])(bundle.getSerializable("board"));
                    boolean ifMyHandUp = bundle.getBoolean("ifMyHandUp", false);
                    boolean ifOpponentHandUp = bundle.getBoolean("ifOpponentHandUp", false);
                    boolean isPlaying = bundle.getBoolean("isPlaying", false);
                    boolean isBlack = bundle.getBoolean("isBlack", false);
                    boolean isMyTurn = bundle.getBoolean("isMyTurn", false);
                    if(isBlack){
                        myiv2.setImageResource(R.drawable.blacksamll);
                        opiv2.setImageResource(R.drawable.whitesmall);
                    }else{
                        opiv2.setImageResource(R.drawable.blacksamll);
                        myiv2.setImageResource(R.drawable.whitesmall);
                    }
                    if(isPlaying) {
                        if (isMyTurn) {
                            myiv3.setImageResource(R.drawable.green);
                            opiv3.setImageResource(0);
                        } else {
                            opiv3.setImageResource(R.drawable.green);
                            myiv3.setImageResource(0);
                        }
                    }
                    if(board != null)
                        gameView.board = board;
                    gameView.isBlack = isBlack;
                    gameView.isPlaying = isPlaying;
                    gameView.isMyTurn = isMyTurn;
                    my.setText(myname + "  " + "分数: " + myscroe);
                    if(ifMyHandUp)
                       myiv.setVisibility(View.VISIBLE);
                    else
                        myiv.setVisibility(View.INVISIBLE);
                    op.setText(opponentname + "  " + "分数: " + opponentscroe);
                    if(ifOpponentHandUp)
                        opiv.setVisibility(View.VISIBLE);
                    else
                        opiv.setVisibility(View.INVISIBLE);
                    gameView.invalidate();
                    break;
                case 1:
                    Bundle bundle1 = msg.getData();
                    String message = bundle1.getString("msg");
                    if(message != null)
                        Toast.makeText(GameActivity.this,"对手: "+message,Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    new ConfirmDialogFragment().show(getFragmentManager(), "对手想要悔棋,是否同意");
                    break;
                case 4:
                    Bundle bundle2 = msg.getData();
                    if(bundle2.getBoolean("ifAgree",false)){
                        new TipDialogFragment().show(getFragmentManager(), "对手拒绝了你的悔棋");
                    }
                    break;
                case 5:
                    Bundle bundle3 = msg.getData();
                    String message1 = bundle3.getString("message");
                    new TipDialogFragment().show(getFragmentManager(), message1);
                    break;
                case 6:
                    Bundle bundle4 = msg.getData();
                    AndroidInterface.getInstance().respondRetract(bundle4.getBoolean("reaction", false));
                    break;
                case 10:
                    Toast.makeText(GameActivity.this, "连接断开,请清除后台重新启动程序", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
