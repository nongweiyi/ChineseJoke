package com.example.chinesejoke.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.example.chinesejoke.R;
import com.example.chinesejoke.adapter.CustomAdapter;
import com.example.chinesejoke.entity.Joke;
import com.example.chinesejoke.util.HttpUtil;
import com.example.chinesejoke.util.JsonParser;
import com.example.chinesejoke.customView.SideslipView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView listview;
	private Button btn_refresh;
	private Button loadmore;
	private LinearLayout loadingHint;
	ListView menu_listview;

	private List<Joke> jokeList;
	private List<Joke> laodMoreJokeList;

	private static final int OK = 0x1111;
	private static final int LODEMORE_OK = 0x1112;
	private static final int REFRESH_OK = 0x1113;

	private final String httpUrl = "http://apis.baidu.com/hihelpsme/chinajoke/getjokelist";
	private final String apikey = "bc0f2fd93a5157aedf10e6fc439f4b75";
	private int loadPage = 1;
	private int backPressCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		getDataByHttp();
		addDataToMenuListView();
		setMenuListViewItemClickListener();
		setListViewItemClickListener();
		setLoadMoreListener();
		setRefreshListener();

	}

	/*
	 * 设置菜单栏选项点击事件监听
	 * 
	 */
	private void setMenuListViewItemClickListener() {
		menu_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showAskExitDialog();

			}

		});

	}

	/*
	 * 显示询问是否退出对话框
	 * 
	 */
	private void showAskExitDialog() {
		// 创建builder
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("提示"); // 标题
		builder.setMessage("确定退出该程序吗？");
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();
			}

		});
		builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.this.finish();
				
			}
		});
		builder.create();
		builder.show();
		
	}

	/*
	 * 为菜单栏listview添加选项数据
	 * 
	 */
	private void addDataToMenuListView() {
		List<String> data = getMenuListViewData();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
		menu_listview.setAdapter(adapter);
	}

	/*
	 * 获取菜单栏listview选项数据
	 * 
	 */
	private List<String> getMenuListViewData() {
		List<String> list = new ArrayList<String>();

		list.add("退出");
		return list;
	}

	/*
	 * 设置刷新事件按钮监听
	 * 
	 */
	private void setRefreshListener() {
		btn_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_refresh.setBackgroundResource(R.drawable.titlebar_button_refresh_selected);
				loadingHint.setVisibility(loadingHint.VISIBLE);
				new Thread(new Runnable() {

					@Override
					public void run() {
						/***************************** 获取数据 *********************************/
						String httpArg = "page=1";
						String jsonResult = HttpUtil.request(httpUrl, httpArg, apikey);
						/***************************** 解析数据 *********************************/
						jokeList = new ArrayList<Joke>();
						try {
							jokeList = JsonParser.parseJson(jsonResult);
							// success
							Message msg = Message.obtain();
							msg.what = REFRESH_OK;
							handler.sendMessage(msg);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}).start();

			}
		});
	}

	/*
	 * 设置listview条目点击事件监听
	 * 
	 */
	private void setListViewItemClickListener() {
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(MainActivity.this, JokeDetailActivity.class);
				intent.putExtra("jokeList", (Serializable) jokeList);
				intent.putExtra("jokeListPositon", position);
				startActivity(intent);
			}
		});

	}

	/*
	 * 加载更多按钮事件监听
	 * 
	 */
	private void setLoadMoreListener() {
		loadmore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadmore.setText("正在加载......");
				loadPage++;
				new Thread(new Runnable() {

					@Override
					public void run() {
						/***************************** 获取数据 *********************************/
						String httpArg = "page=" + loadPage;
						String jsonResult = HttpUtil.request(httpUrl, httpArg, apikey);
						/***************************** 解析数据 *********************************/
						laodMoreJokeList = new ArrayList<Joke>();
						try {
							laodMoreJokeList = JsonParser.parseJson(jsonResult);
							// success
							Message msg = Message.obtain();
							msg.what = LODEMORE_OK;
							handler.sendMessage(msg);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});

	}

	/*
	 * 异步消息接收处理
	 * 
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (OK == msg.what) {
				loadingHint.setVisibility(loadingHint.GONE);
				CustomAdapter adapter = new CustomAdapter(MainActivity.this, jokeList);
				listview.setAdapter(adapter);
			} else if (LODEMORE_OK == msg.what) {
				if (laodMoreJokeList.size() == jokeList.size()) {
					Toast.makeText(MainActivity.this, "没有更多了", 0).show();
				} else {
					CustomAdapter adapter = new CustomAdapter(MainActivity.this, laodMoreJokeList);
					listview.setAdapter(adapter);
				}
				loadmore.setText("加载更多");
			} else if (REFRESH_OK == msg.what) {

				loadingHint.setVisibility(loadingHint.GONE);
				btn_refresh.setBackgroundResource(R.drawable.titlebar_button_refresh);
				CustomAdapter adapter = new CustomAdapter(MainActivity.this, jokeList);
				listview.setAdapter(adapter);
				Toast.makeText(MainActivity.this, "刷新成功", 0).show();
			}
		};
	};

	/*
	 * 开启子线程，从网络上获取数据并解析
	 * 
	 */
	private void getDataByHttp() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				/***************************** 获取数据 *********************************/
				String httpArg = "page=1";
				String jsonResult = HttpUtil.request(httpUrl, httpArg, apikey);
				/***************************** 解析数据 *********************************/
				jokeList = new ArrayList<Joke>();
				try {
					jokeList = JsonParser.parseJson(jsonResult);
					// success
					Message msg = Message.obtain();
					msg.what = OK;
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	/*
	 * 初始化控件
	 * 
	 */
	private void initView() {
		listview = (ListView) this.findViewById(R.id.listview_id);
		btn_refresh = (Button) this.findViewById(R.id.btn_refresh_id);
		loadmore = (Button) this.findViewById(R.id.loadmore_id);
		loadingHint = (LinearLayout) this.findViewById(R.id.loadingHint_id);
		/************************* 使用自定义类实现侧滑效果 *************************************/
		SideslipView sideslipView = (SideslipView) findViewById(R.id.sideslipView_id);
		View jokeShow = findViewById(R.id.jokeShow_id);
		View menu = findViewById(R.id.menu_id);
		sideslipView.setSideAndBaseView(jokeShow, menu);
		menu_listview = (ListView) this.findViewById(R.id.menu_listview_id);
	}

	/*
	 * 返回键监听，按下两次提出程序
	 * 
	 */
	@Override
	public void onBackPressed() {

		backPressCount++;
		if (2 == backPressCount) {
			this.finish();
		} else {
			Toast.makeText(this, "再按一次退出程序", 0).show();
		}

	}

}
