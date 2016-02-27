package shu.gobang.androidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;

import Client.AndroidInterface;

public class LoginActivity extends AppCompatActivity {
	Button bt_login, bt_registe;
	EditText et_user, et_password;
	AndroidInterface androidInterface;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		bt_login = (Button)findViewById(R.id.bt_login);
		bt_registe = (Button)findViewById(R.id.bt_registe);
		et_user = (EditText)findViewById(R.id.passwordedit);
		et_password = (EditText)findViewById(R.id.useredit);

		try {
			androidInterface = new AndroidInterface(InetAddress.getByName("yidea.xyz"),4000);
		}catch (UnknownHostException e){
			androidInterface = null;
			Toast.makeText(this,"当前网络不可用",Toast.LENGTH_LONG);
		}
		((MyApplication)getApplication()).androidInterface = androidInterface;

		bt_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String user = et_user.getText().toString();
				String password = et_password.getText().toString();
				if(user != null &&password  != null && !user.equals("") && !password.equals("")){
					((MyApplication)getApplication()).androidInterface.login(user, password);
				}else{
					Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT);
				}
			}
		});

		bt_registe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}
}
