package shu.gobang.androidclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/2/27.
 */
public class RegisterActivity extends AppCompatActivity {
    Button bt_confirm;
    EditText et_user, et_password;
    RegisterHandle registerHandle;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        bt_confirm = (Button)findViewById(R.id.bt_confirm);
        et_user = (EditText)findViewById(R.id.newpasswordedit);
        et_password = (EditText)findViewById(R.id.newuseredit);

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = et_user.getText().toString();
                String password = et_password.getText().toString();
                if(user != null &&password  != null && !user.equals("") && !password.equals("")){
                    if(((MyApplication)getApplication()).androidInterface != null)
                        ((MyApplication)getApplication()).androidInterface.register(user, password);
                }else{
                    Toast.makeText(RegisterActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        registerHandle = new RegisterHandle();
        ((MyApplication)getApplication()).registerHandle = registerHandle;
    }

    @Override
    public void onPause(){
        super.onPause();
        ((MyApplication)getApplication()).registerHandle = null;
    }

    public class RegisterHandle extends Handler {
        public RegisterHandle(){
            super();
        }

        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    //注册成功
                    break;
                case 2:
                    Bundle bundle = msg.getData();
                    String reason = bundle.getString("reason", "未知错误");
                    Toast.makeText(RegisterActivity.this,reason,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
