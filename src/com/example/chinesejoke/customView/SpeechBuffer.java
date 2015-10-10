package com.example.chinesejoke.customView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

/**
 * @author lhw
 * @version 2015-07-21 速度缓冲器
 */
public class SpeechBuffer {
	private View view;
	private MarginLayoutParams layoutParams = null;
	private int left;
	private int right;
	private int top;
	private int bottom;
	private int speed = 200;
	private SpeechBuffer mSpeechBuffer;
	private int sleepTime = 50;
	/**
	 * 表示View滑动的方向
	 */
	private int orientation;
	public static final int ORIENTATION_LEFT = 0;
	public static final int ORIENTATION_RIGHT = 1;
	public static final int ORIENTATION_DOWN = 2;
	public static final int ORIENTATION_UP = 3;
	private int MAXGIN_LEFT_MAX = 0;
	private int MAXGIN_RIGHT_MAX = 0;
	private int MAXGIN_DOWN_MAX = 0;
	private int MAXGIN_UP_MAX = 0;
	private SideslipView sv = null;
	private int update_speed = 500;

	public SpeechBuffer(View v, MarginLayoutParams m, int speed, int begin) {
		view = v;
		layoutParams = m;
		this.speed = speed;
		mSpeechBuffer = this;
		left = begin;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setSleepTime(int time) {
		this.sleepTime = time;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public void setMAXGIN_LEFT_MAX(int mAXGIN_LEFT_MAX) {
		MAXGIN_LEFT_MAX = mAXGIN_LEFT_MAX;
	}

	public void setMAXGIN_RIGHT_MAX(int mAXGIN_RIGHT_MAX) {
		MAXGIN_RIGHT_MAX = mAXGIN_RIGHT_MAX;
	}

	public void setMAXGIN_DOWN_MAX(int mAXGIN_DOWN_MAX) {
		MAXGIN_DOWN_MAX = mAXGIN_DOWN_MAX;
	}

	public void setMAXGIN_UP_MAX(int mAXGIN_UP_MAX) {
		MAXGIN_UP_MAX = mAXGIN_UP_MAX;
	}

	public void setLeft(int begin) {
		left = begin;
	}

	public void setRight(int begin) {
		right = begin;
	}

	public void setTop(int begin) {
		top = begin;
	}

	public void setBottom(int begin) {
		bottom = begin;
	}

	public void setSideslipView(SideslipView s) {
		this.sv = s;
	}

	private Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (left < 0) {
				left = 0;
			}
			if (left > MAXGIN_LEFT_MAX && orientation == ORIENTATION_RIGHT) {
				left = MAXGIN_LEFT_MAX;
			}
			layoutParams.setMargins(left, top, right, bottom);
			view.setLayoutParams(layoutParams);
		}

		;
	};

	/**
	 * 开始执行动画
	 */
	public void start() {
		new Thread() {
			public void run() {

				if (sv != null) {
					sv.setAnimationOnOrOff(true);
				}
				if (orientation == ORIENTATION_LEFT) {
					while (left > 0) {
						left = left - speed;
						h.sendEmptyMessage(left);
						try {
							Thread.sleep(sleepTime);
						} catch (Exception e) {
							// TODO: handle exception
						}
						updateSpeep();
					}
				} else if (orientation == ORIENTATION_RIGHT) {
					while (left < MAXGIN_LEFT_MAX) {
						left = left + speed;
						h.sendEmptyMessage(left);
						try {
							Thread.sleep(sleepTime);
						} catch (Exception e) {
							// TODO: handle exception
						}
						updateSpeep();
					}
				}
				try {
					Thread.sleep(sleepTime);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (orientation == ORIENTATION_LEFT) {
					left = 0;
				}
				if (orientation == ORIENTATION_RIGHT) {
					left = MAXGIN_LEFT_MAX;
				}
				h.sendEmptyMessage(0);
				if (sv != null) {
					sv.setAnimationOnOrOff(false);
				}
				sv = null;

			}

			;
		}.start();
	}

	/**
	 * 改变速度变化
	 */
	private void updateSpeep() {
		speed += update_speed;
	}
}
