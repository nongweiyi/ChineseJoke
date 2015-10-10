package com.example.chinesejoke.activity;

import android.os.Bundle;
import java.util.List;

import com.example.chinesejoke.R;
import com.example.chinesejoke.R.id;
import com.example.chinesejoke.R.layout;
import com.example.chinesejoke.entity.Joke;
import com.example.chinesejoke.util.LogUtil;

import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class JokeDetailActivity extends Activity implements OnGestureListener {
	private TextView tv_jokeDetail_title;
	private TextView tv_jokeDetail_content;
	private ViewFlipper viewFlipper;
	private Button btn_previous, btn_next;

	private int jokeListPosition;
	private List<Joke> jokeList;
	private GestureDetector detector; // 手势检测

	Animation leftInAnimation;
	Animation leftOutAnimation;
	Animation rightInAnimation;
	Animation rightOutAnimation;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joke_detail);

		detector = new GestureDetector(this);
		initView();
		setDataToTextView();
		setNextPrevBtnClickListener();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return this.detector.onTouchEvent(event); // touch事件交给手势处理。
	}

	/*
	 * 设置上一则，下一则笑话按钮点击监听事件
	 * 
	 */
	private void setNextPrevBtnClickListener() {
		// 上一则
		btn_previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPrevious();
			}

		});
		// 下一则
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNext();
			}

		});

	}

	/**
	 * 
	 * 上一则
	 */
	private void showPrevious() {
		btn_next.setVisibility(btn_next.VISIBLE);
		addViewToViewFlipper();

		if (jokeListPosition > 0) {
			jokeListPosition--;
			Joke joke = jokeList.get(jokeListPosition);
			tv_jokeDetail_title.setText(joke.getJokeTitle());
			tv_jokeDetail_content.setText(joke.getJokeContent());
			viewFlipper.showPrevious();
		} else {
			/* jokeListPosition=0; */
			Toast.makeText(this, "这已经是第一则啦", 0).show();
			btn_previous.setVisibility(btn_previous.GONE);
		}

	}

	/**
	 * 添加view到ViewFlipper中
	 * 
	 */
	private void addViewToViewFlipper() {
		View jokeDetialView = View.inflate(this, R.layout.joke_detail_layout, null);
		// 注意这里通过jokeDetialView查找到控件实例
		tv_jokeDetail_title = (TextView) jokeDetialView.findViewById(R.id.tv_jokeDetail_title_id);
		tv_jokeDetail_content = (TextView) jokeDetialView.findViewById(R.id.tv_jokeDetail_content_id);
		// 将显示笑话信息的布局添加到viewflipper中
		viewFlipper.addView(jokeDetialView);
	}

	/**
	 * 下一则
	 * 
	 */
	private void showNext() {
		btn_previous.setVisibility(btn_previous.VISIBLE);
		addViewToViewFlipper();

		if (jokeListPosition < jokeList.size() - 1) {
			jokeListPosition++;
			Joke joke = jokeList.get(jokeListPosition);
			tv_jokeDetail_title.setText(joke.getJokeTitle());
			tv_jokeDetail_content.setText(joke.getJokeContent());
			viewFlipper.showNext();
		} else {
			Toast.makeText(this, "这已经是最后一则啦", 0).show();
			btn_next.setVisibility(btn_next.GONE);
		}

	}

	/**
	 * 取出传递过来的数据并放在textview中
	 * 
	 */
	private void setDataToTextView() {
		Intent intent = getIntent();
		jokeList = (List<Joke>) intent.getSerializableExtra("jokeList");
		int position = intent.getIntExtra("jokeListPositon", 0);
		jokeListPosition = position;
		Joke joke = jokeList.get(jokeListPosition);
		String content=formatContentData(joke);
		
		tv_jokeDetail_title.setText(joke.getJokeTitle());
		tv_jokeDetail_content.setText(content);

	}

	/**
	 * @Description:通过添加空格改变内容显示的格式（无效？待解决）
	 *
	 * @return
	 */
	private String formatContentData(Joke joke) {
		String content=joke.getJokeContent();
		LogUtil.i("nongweiyi", "content======"+content);
		System.out.println("content======"+content);
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("    ");
		for(int index=0;index<content.length();index++){
			if("\\r\\n".equals(content.charAt(index))){
				stringBuilder.append(content.charAt(index));
				stringBuilder.append("    ");
			}else{
				stringBuilder.append(content.charAt(index));
			}
			
		}
		return stringBuilder.toString();
	}

	/**
	 * 初始化控件
	 * 
	 */
	private void initView() {
		btn_previous = (Button) this.findViewById(R.id.btn_previous_id);
		btn_next = (Button) this.findViewById(R.id.btn_next_id);
		viewFlipper = (ViewFlipper) this.findViewById(R.id.viewflipper_id);
		addViewToViewFlipper();

		// 动画效果
		leftInAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
		leftOutAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
		rightInAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
		rightOutAnimation = AnimationUtils.loadAnimation(this, R.anim.right_out);

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (e1.getX() - e2.getX() > 120) {
			viewFlipper.setInAnimation(leftInAnimation);
			viewFlipper.setOutAnimation(leftOutAnimation);
			showNext();
			return true;
		} else if (e1.getX() - e2.getY() < -120) {
			viewFlipper.setInAnimation(rightInAnimation);
			viewFlipper.setOutAnimation(rightOutAnimation);
			showPrevious();

			return true;
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

}
