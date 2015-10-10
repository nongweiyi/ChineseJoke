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
	private GestureDetector detector; // ���Ƽ��

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
		return this.detector.onTouchEvent(event); // touch�¼��������ƴ���
	}

	/*
	 * ������һ����һ��Ц����ť��������¼�
	 * 
	 */
	private void setNextPrevBtnClickListener() {
		// ��һ��
		btn_previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPrevious();
			}

		});
		// ��һ��
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNext();
			}

		});

	}

	/**
	 * 
	 * ��һ��
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
			Toast.makeText(this, "���Ѿ��ǵ�һ����", 0).show();
			btn_previous.setVisibility(btn_previous.GONE);
		}

	}

	/**
	 * ���view��ViewFlipper��
	 * 
	 */
	private void addViewToViewFlipper() {
		View jokeDetialView = View.inflate(this, R.layout.joke_detail_layout, null);
		// ע������ͨ��jokeDetialView���ҵ��ؼ�ʵ��
		tv_jokeDetail_title = (TextView) jokeDetialView.findViewById(R.id.tv_jokeDetail_title_id);
		tv_jokeDetail_content = (TextView) jokeDetialView.findViewById(R.id.tv_jokeDetail_content_id);
		// ����ʾЦ����Ϣ�Ĳ�����ӵ�viewflipper��
		viewFlipper.addView(jokeDetialView);
	}

	/**
	 * ��һ��
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
			Toast.makeText(this, "���Ѿ������һ����", 0).show();
			btn_next.setVisibility(btn_next.GONE);
		}

	}

	/**
	 * ȡ�����ݹ��������ݲ�����textview��
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
	 * @Description:ͨ����ӿո�ı�������ʾ�ĸ�ʽ����Ч���������
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
	 * ��ʼ���ؼ�
	 * 
	 */
	private void initView() {
		btn_previous = (Button) this.findViewById(R.id.btn_previous_id);
		btn_next = (Button) this.findViewById(R.id.btn_next_id);
		viewFlipper = (ViewFlipper) this.findViewById(R.id.viewflipper_id);
		addViewToViewFlipper();

		// ����Ч��
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
