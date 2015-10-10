package com.example.chinesejoke.customView;

import android.view.MotionEvent;

class DistanceUtils {
	private float downX;
	private float downY;
	private float upX;
	private float upY;
	private float distance;
	private MotionEvent event;
	private int mark;
	private static final int TOUCH_DISTANCE = 0;
	private static final int POINT_DISTANCE = 1;

	// 初始化
	public DistanceUtils(float startX, float startY, float endX, float endY) {
		super();
		this.downX = startX;
		this.downY = startY;
		this.upX = endX;
		this.upY = endY;
	}

	public DistanceUtils() {

	}

	// 计算滑动距离，每次有onTouchEnvent事件发生的时候，都要调用此方法
	public void calculate(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downX = event.getX();
			downY = event.getY();
			upX = downX;
			upY = downY;
		}
		/**
		 * if (event.getAction() == MotionEvent.ACTION_UP) { upX = event.getX();
		 * upY = event.getY(); return; }
		 */
		else {
			upX = event.getX();
			upY = event.getY();
		}
	}

	// 获得在Y方向上的滑动距离
	public float getDistanceByY() {
		return upY - downY;
	}

	// 获得在X方向上的滑动距离
	public float getDistanceByX() {
		return upX - downX;
	}

	// 获得在XY方向上的滑动距离
	public float getDistanceByXY() {
		double d = (downX - upX) * (downX - upX) + (downY - upY) * (downY - upY);
		return (float) Math.sqrt(d);
	}

	// 获得在X方向上的滑动距离
	public float getMovedDistanceX() {
		return upX - downX;
	}

	// 获得在Y方向上的没去距离
	public float getMovedDistanceY() {
		return upY - downY;
	}

}
