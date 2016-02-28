package shu.gobang.androidclient;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import Client.AndroidInterface;

public class LoginActivity extends AppCompatActivity {
	Button bt_login, bt_registe;
	EditText et_user, et_password;
	AndroidInterface androidInterface;
	LoginHandle loginHandle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		setTitle("登入");
		bt_login = (Button)findViewById(R.id.bt_login);
		bt_registe = (Button)findViewById(R.id.bt_registe);
		et_user = (EditText)findViewById(R.id.passwordedit);
		et_password = (EditText)findViewById(R.id.useredit);

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						androidInterface = AndroidInterface.getInstance();
					} catch (Exception e) {
						androidInterface = null;
						Toast.makeText(LoginActivity.this, "当前网络不可用", Toast.LENGTH_LONG);
					}
				}
			}).start();

		bt_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String user = et_user.getText().toString();
				String password = et_password.getText().toString();
				if(user != null &&password  != null && !user.equals("") && !password.equals("")){
					et_password.setText("");
					et_user.setText("");
					AndroidInterface.getInstance().login(user, password);
				}else{
					Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT);
				}
			}
		});

		bt_registe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume(){
		super.onResume();
		loginHandle = new LoginHandle();
		AndroidInterface.getInstance().loginHandle = loginHandle;
		AndroidInterface.getInstance().isInLoginActivity = true;
	}

	@Override
	public void onStop(){
		super.onStop();
		AndroidInterface.getInstance().isInLoginActivity = false;
	}

	public class LoginHandle extends Handler{
		public LoginHandle(){
			super();
		}

		@Override
		public void handleMessage(Message msg){
			switch (msg.what){
				case 1:
					Intent intent = new Intent(LoginActivity.this,TableActivity.class);
					startActivity(intent);
					finish();
					break;
				case 0:
					Bundle bundle = msg.getData();
					String reason = bundle.getString("reason", "未知错误");
					Toast.makeText(LoginActivity.this,reason,Toast.LENGTH_LONG).show();
					break;
				case 10:
					Toast.makeText(LoginActivity.this,"连接断开,请清除后台重新启动程序",Toast.LENGTH_SHORT).show();
			}
		}
	}
}
