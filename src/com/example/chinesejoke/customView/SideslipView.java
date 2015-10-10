package com.example.chinesejoke.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

public class SideslipView extends FrameLayout {
	private boolean ok = false;
	private View contentView;
	private View menuView;
	private Animation sideslipAnimation;// 动画
	private float maxSideslipLength;// 最大滑动距离
	private float sideslip = 0;
	private DistanceUtils du;
	private FrameLayout.LayoutParams layoutParams;
	private static final int STATE_LEFT = 0;//
	private static final int STATE_RIGHT = 1;
	private static final int STATE_MIDLE = 2;
	private int state = STATE_LEFT;
	private int MOVE_MAX = 120;
	private boolean CLICK_JUST = true;
	private boolean animation_on = false;
	private int firstLayoutParams = -1;

	public SideslipView(Context context) {
		super(context);
	}

	public SideslipView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SideslipView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setSideAndBaseView(View content, View menu) {
		contentView = content;
		menuView = menu;
		init();
	}

	// 初始化
	private void init() {
		ok = true;
		du = new DistanceUtils();
		layoutParams = (LayoutParams) contentView.getLayoutParams();
	}

	// 设置margin属性，实现动画效果
	private void sideslipMargin() {
		CLICK_JUST = false;
		layoutParams.setMargins((int) sideslip, 0, 0, 0);
		contentView.setLayoutParams(layoutParams);
	}

	private boolean processMargin() {
		if (sideslip <= 0) {
			if (sideslip > -MOVE_MAX) {
				CLICK_JUST = true;
				if (state == STATE_LEFT) {
					sideslip = 0;
				} else {
					sideslip = maxSideslipLength;
				}
			} else {
				CLICK_JUST = false;

				sideslip = sideslip + MOVE_MAX;

			}
		} else if (sideslip >= 0) {
			if (sideslip < MOVE_MAX) {
				CLICK_JUST = true;
				if (state == STATE_LEFT) {
					sideslip = 0;
				} else {
					sideslip = maxSideslipLength;
				}
			} else {
				CLICK_JUST = false;
				sideslip = sideslip - MOVE_MAX;
			}
		}
		if (CLICK_JUST) {
			return false;
		}
		if (state == STATE_LEFT) {
			if (sideslip < 0) {
				sideslip = 0;
				return false;
			}
		}
		if (state == STATE_RIGHT) {
			if (sideslip > 0) {
				sideslip = maxSideslipLength;
				return false;
			}
			sideslip = maxSideslipLength + sideslip;
			if (sideslip < 0)
				sideslip = 0;
		}
		if (sideslip > maxSideslipLength || sideslip < 0) {
			if (sideslip < 0) {
				sideslip = 0;
			}
			if (sideslip > maxSideslipLength) {
				sideslip = maxSideslipLength;
			}
			return false;
		}
		return true;
	}

	/**
	 * 启动动画
	 */
	private void startAnimation() {
		// animation_on=true;
		SpeechBuffer s = new SpeechBuffer(contentView, layoutParams, 50, 0);
		s.setSideslipView(this);
		s.setSleepTime(100);
		if (state == STATE_LEFT) {
			s.setOrientation(SpeechBuffer.ORIENTATION_LEFT);
			// s.setLeft((int) sideslip);
		} else {
			s.setOrientation(SpeechBuffer.ORIENTATION_RIGHT);
			s.setMAXGIN_LEFT_MAX((int) maxSideslipLength);
		}
		s.setLeft((int) (sideslip));
		s.start();

	}

	private boolean processTouchEvent(MotionEvent event) {
		if (!ok) {
			return false;
		}

		if (maxSideslipLength < 8) {
			maxSideslipLength = menuView.getWidth();
		}
		sideslip = du.getMovedDistanceX();
		processMargin();
		sideslipMargin();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (CLICK_JUST) {
				if (state == STATE_LEFT)
					sideslip = 0;
				else
					sideslip = maxSideslipLength;
				sideslipMargin();
				return true;
			}
			if (sideslip >= maxSideslipLength / 2) {
				state = STATE_RIGHT;
				startAnimation();
				sideslip = maxSideslipLength;
			} else {
				state = STATE_LEFT;
				startAnimation();
				sideslip = 0;
			}
			// return true;
		}

		return true;
	}

	/**
	 * 设置动画开关
	 * 
	 * @param onOrOff
	 */
	public void setAnimationOnOrOff(boolean onOrOff) {
		animation_on = onOrOff;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		super.dispatchTouchEvent(event);
		if (firstLayoutParams < 0) {
			firstLayoutParams++;
			layoutParams.width = contentView.getWidth();
			layoutParams.height = contentView.getHeight();
		}
		if (animation_on) {
			return true;
		}
		du.calculate(event);
		float my = du.getMovedDistanceY();
		if (my >= MOVE_MAX || -my >= MOVE_MAX) {
			if (CLICK_JUST) {
				return true;
			}
		}
		processTouchEvent(event);
		return true;
	}
}
