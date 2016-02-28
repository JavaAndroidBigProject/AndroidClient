package shu.gobang.androidclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Client.AndroidInterface;
import ServerInterface.TableInfo;

/**
 * Created by Administrator on 2016/2/27.
 */
public class TableActivity extends AppCompatActivity{
    ImageView[] table;
    Button[] bt;
    TextView[] tv;
    View.OnClickListener onClickListener;
    TableHandle tableHandle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        setTitle("大厅");
//        if(((MyApplication)getApplication()).androidInterface == null)
//            Log.e("xie","androidint wei null");
        table = new ImageView[8];
        bt = new Button[4];
        tv = new TextView[8];
        table[0] = (ImageView)findViewById(R.id.table1_left);
        tv[0] = (TextView)findViewById(R.id.table1_left_name);
        table[1] = (ImageView)findViewById(R.id.table1_right);
        tv[1] = (TextView)findViewById(R.id.table1_right_name);
        table[2] = (ImageView)findViewById(R.id.table2_left);
        tv[2] = (TextView)findViewById(R.id.table2_left_name);
        table[3] = (ImageView)findViewById(R.id.table2_right);
        tv[3] = (TextView)findViewById(R.id.table2_right_name);
        table[4] = (ImageView)findViewById(R.id.table3_left);
        tv[4] = (TextView)findViewById(R.id.table3_left_name);
        table[5] = (ImageView)findViewById(R.id.table3_right);
        tv[5] = (TextView)findViewById(R.id.table3_right_name);
        table[6] = (ImageView)findViewById(R.id.table4_left);
        tv[6] = (TextView)findViewById(R.id.table4_left_name);
        table[7] = (ImageView)findViewById(R.id.table4_right);
        tv[7] = (TextView)findViewById(R.id.table4_right_name);
        bt[0] = (Button)findViewById(R.id.add_table1);
        bt[1] = (Button)findViewById(R.id.add_table2);
        bt[2] = (Button)findViewById(R.id.add_table3);
        bt[3] = (Button)findViewById(R.id.add_table4);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(((MyApplication)getApplication()).androidInterface == null)
//                    Log.e("xie","err");
                if(v == bt[0])
                    AndroidInterface.getInstance().enterTable(1);
                else if(v == bt[1])
                    AndroidInterface.getInstance().enterTable(2);
                else if(v == bt[2])
                    AndroidInterface.getInstance().enterTable(3);
                else if(v == bt[3])
                    AndroidInterface.getInstance().enterTable(4);
            }
        };
        bt[0].setOnClickListener(onClickListener);
        bt[1].setOnClickListener(onClickListener);
        bt[2].setOnClickListener(onClickListener);
        bt[3].setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume(){
        super.onResume();
        AndroidInterface.getInstance().isInTableActivity = true;
        AndroidInterface.getInstance().isInGameActivity = false;
        tableHandle = new TableHandle();
        AndroidInterface.getInstance().tableHandle = tableHandle;
    }

    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    protected  void onStop(){
        super.onStop();
        if(AndroidInterface.getInstance().isInTableActivity)
            AndroidInterface.getInstance().quit();
        AndroidInterface.getInstance().isInTableActivity = false;
    }

    public class TableHandle extends Handler {
        public TableHandle(){
            super();
        }

        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    Bundle bundle = msg.getData();
                    TableInfo[] tableInfos = (TableInfo[])bundle.getSerializable("tableinfos");
                    for(int i =0;i<tableInfos.length;i++){
                        if(!tableInfos[i].player1.name.equals("empty")){
                            table[i*2].setImageResource(R.drawable.white);
                        }else {
                            table[i*2].setImageResource(0);
                        }
                        tv[i*2].setText(tableInfos[i].player1.name);
                        if(!tableInfos[i].player2.name.equals("empty")){
                            table[i*2+1].setImageResource(R.drawable.black);
                        }else
                            table[i*2+1].setImageResource(0);
                        tv[i*2+1].setText(tableInfos[i].player2.name);
                    }
                    break;
                case 1:
                    Bundle bundle1 = msg.getData();
                    int tableId = bundle1.getInt("tableId", 1);
                    boolean ifEntered = bundle1.getBoolean("ifEntered", false);
                    String reason = bundle1.getString("reason","未知");
                    if(!ifEntered)
                        Toast.makeText(TableActivity.this,reason,Toast.LENGTH_LONG).show();
                    else{
                        AndroidInterface.getInstance().isInTableActivity = false;
                        Intent intent = new Intent(TableActivity.this,GameActivity.class);
                        startActivity(intent);
                    }
                    break;
                case 10:
                    Toast.makeText(TableActivity.this,"连接断开,请清除后台重新启动程序",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
