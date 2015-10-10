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
	 * ���ò˵���ѡ�����¼�����
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
	 * ��ʾѯ���Ƿ��˳��Ի���
	 * 
	 */
	private void showAskExitDialog() {
		// ����builder
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("��ʾ"); // ����
		builder.setMessage("ȷ���˳��ó�����");
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();
			}

		});
		builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.this.finish();
				
			}
		});
		builder.create();
		builder.show();
		
	}

	/*
	 * Ϊ�˵���listview���ѡ������
	 * 
	 */
	private void addDataToMenuListView() {
		List<String> data = getMenuListViewData();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
		menu_listview.setAdapter(adapter);
	}

	/*
	 * ��ȡ�˵���listviewѡ������
	 * 
	 */
	private List<String> getMenuListViewData() {
		List<String> list = new ArrayList<String>();

		list.add("�˳�");
		return list;
	}

	/*
	 * ����ˢ���¼���ť����
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
						/***************************** ��ȡ���� *********************************/
						String httpArg = "page=1";
						String jsonResult = HttpUtil.request(httpUrl, httpArg, apikey);
						/***************************** �������� *********************************/
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
	 * ����listview��Ŀ����¼�����
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
	 * ���ظ��ఴť�¼�����
	 * 
	 */
	private void setLoadMoreListener() {
		loadmore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadmore.setText("���ڼ���......");
				loadPage++;
				new Thread(new Runnable() {

					@Override
					public void run() {
						/***************************** ��ȡ���� *********************************/
						String httpArg = "page=" + loadPage;
						String jsonResult = HttpUtil.request(httpUrl, httpArg, apikey);
						/***************************** �������� *********************************/
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
	 * �첽��Ϣ���մ���
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
					Toast.makeText(MainActivity.this, "û�и�����", 0).show();
				} else {
					CustomAdapter adapter = new CustomAdapter(MainActivity.this, laodMoreJokeList);
					listview.setAdapter(adapter);
				}
				loadmore.setText("���ظ���");
			} else if (REFRESH_OK == msg.what) {

				loadingHint.setVisibility(loadingHint.GONE);
				btn_refresh.setBackgroundResource(R.drawable.titlebar_button_refresh);
				CustomAdapter adapter = new CustomAdapter(MainActivity.this, jokeList);
				listview.setAdapter(adapter);
				Toast.makeText(MainActivity.this, "ˢ�³ɹ�", 0).show();
			}
		};
	};

	/*
	 * �������̣߳��������ϻ�ȡ���ݲ�����
	 * 
	 */
	private void getDataByHttp() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				/***************************** ��ȡ���� *********************************/
				String httpArg = "page=1";
				String jsonResult = HttpUtil.request(httpUrl, httpArg, apikey);
				/***************************** �������� *********************************/
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
	 * ��ʼ���ؼ�
	 * 
	 */
	private void initView() {
		listview = (ListView) this.findViewById(R.id.listview_id);
		btn_refresh = (Button) this.findViewById(R.id.btn_refresh_id);
		loadmore = (Button) this.findViewById(R.id.loadmore_id);
		loadingHint = (LinearLayout) this.findViewById(R.id.loadingHint_id);
		/************************* ʹ���Զ�����ʵ�ֲ໬Ч�� *************************************/
		SideslipView sideslipView = (SideslipView) findViewById(R.id.sideslipView_id);
		View jokeShow = findViewById(R.id.jokeShow_id);
		View menu = findViewById(R.id.menu_id);
		sideslipView.setSideAndBaseView(jokeShow, menu);
		menu_listview = (ListView) this.findViewById(R.id.menu_listview_id);
	}

	/*
	 * ���ؼ����������������������
	 * 
	 */
	@Override
	public void onBackPressed() {

		backPressCount++;
		if (2 == backPressCount) {
			this.finish();
		} else {
			Toast.makeText(this, "�ٰ�һ���˳�����", 0).show();
		}

	}

}
