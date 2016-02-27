package shu.gobang.androidclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ServerInterface.TableInfo;

/**
 * Created by Administrator on 2016/2/27.
 */
public class TableActivity extends AppCompatActivity{
    ImageView[] table;
    Button[] bt;
    TextView[] tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

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


    }

    @Override
    protected void onResume(){
        super.onResume();
        ((MyApplication)getApplication()).isInTableActivity = true;
        ((MyApplication)getApplication()).tableHandle = new TableHandle();
    }

    @Override
    public void onPause(){
        super.onPause();
        ((MyApplication)getApplication()).tableHandle = null;
    }
    @Override
    protected  void onStop(){
        super.onStop();
        ((MyApplication)getApplication()).isInTableActivity = false;
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
                        if(tableInfos[i].player1 != null){
                            table[i*2].setImageResource(R.drawable.white);
                            tv[i*2].setText(tableInfos[0].player1.name);
                        }
                        if(tableInfos[i].player2 != null){
                            table[i*2+1].setImageResource(R.drawable.black);
                            tv[i*2+1].setText(tableInfos[0].player2.name);
                        }
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

                    }
                    break;
            }

        }
    }
}
