package com.example.chinesejoke.activity;

import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import com.example.chinesejoke.R;
import com.example.chinesejoke.R.layout;
import com.example.chinesejoke.R.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class WelcomActivity extends Activity {
	private Boolean intenetConettedFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcom);
	}

	/**
	 * 检查当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */

	public boolean isNetworkAvailable(Activity activity) {
		Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	protected void onResume() {

		super.onResume();
		intenetConettedFlag = isNetworkAvailable(this);
		if (intenetConettedFlag) {
			Timer timer = new Timer();
			TimerTask timerTask = new TimerTask() {

				@Override
				public void run() {
					// 跳转到笑话主页
					Intent intent = new Intent(WelcomActivity.this, MainActivity.class);
					startActivity(intent);
					WelcomActivity.this.finish();
				}
			};
			timer.schedule(timerTask, 2000);

		} else {

			Toast.makeText(getApplicationContext(), "当前没有可用网络！请先设置", Toast.LENGTH_LONG).show();
			Timer timer = new Timer();
			TimerTask timerTask = new TimerTask() {

				@Override
				public void run() {
					// 跳转到网络设置页面
					Intent intent = new Intent();
					intent.setClassName("com.android.settings", "com.android.settings.Settings");
					startActivity(intent);
				}
			};
			timer.schedule(timerTask, 2000);
		}
	}

}
