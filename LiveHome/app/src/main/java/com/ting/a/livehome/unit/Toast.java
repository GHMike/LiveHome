package com.ting.a.livehome.unit;


import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ting.a.livehome.R;


public abstract class Toast {
	public static final int LENGTH_SHORT = android.widget.Toast.LENGTH_SHORT;
	public static final int LENGTH_LONG = android.widget.Toast.LENGTH_LONG;
	private static android.widget.Toast toast;
	private static View toastRoot;
	private static Handler handler = new Handler();
	private static Runnable run = new Runnable() {
		public void run() {
			toast.cancel();
		}
	};
	
	private static void toast(Context ctx, CharSequence msg, int duration) {
		toastRoot = LayoutInflater.from(ctx).inflate(R.layout.toast_view,null);
		handler.removeCallbacks(run); // handler的duration不能直接对应Toast的常量时长，在此针对Toast的常量相应定义时长
		switch (duration) {
		case LENGTH_SHORT:// Toast.LENGTH_SHORT值为0，对应的持续时间大概为1s
			duration = 1000;
			break;
		case LENGTH_LONG:// Toast.LENGTH_LONG值为1，对应的持续时间大概为3s
			duration = 3000;
			break;
		default:
			break;
		}
		if (null != toast) {
			TextView message = (TextView) toastRoot.findViewById(R.id.message);
			message.setText(msg);
			toast.setGravity(Gravity.BOTTOM, 0, 150);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(toastRoot);
//			toast.setText(msg);
			
		} else {
			toast = android.widget.Toast.makeText(ctx, msg, duration);
			TextView message = (TextView) toastRoot.findViewById(R.id.message);
			message.setText(msg);
			toast.setGravity(Gravity.BOTTOM, 0, 150);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(toastRoot);
		}
		handler.postDelayed(run, duration);
		toast.show();
	}

	/**
	 * * 弹出Toast * * @param ctx * 弹出Toast的上下文 * @param msg 弹出Toast的内容 * @param
	 * duration 弹出Toast的持续时间
	 */
	public static void show(Context ctx, CharSequence msg, int duration)
			throws NullPointerException {
		if (null == ctx) {
			throw new NullPointerException("The ctx is null!");
		}
		if (0 > duration) {
			duration = LENGTH_SHORT;
		}
		toast(ctx, msg, duration);
	}

	/**
	 * * 弹出Toast *
	 * 
	 * @param ctx
	 *            * 弹出Toast的上下文 * @param msg * 弹出Toast的内容的资源ID * @param duration
	 *            * 弹出Toast的持续时间
	 */
	public static void show(Context ctx, int resId, int duration)
			throws NullPointerException {
		if (null == ctx) {
			throw new NullPointerException("The ctx is null!");
		}
		if (0 > duration) {
			duration = LENGTH_SHORT;
		}
		toast(ctx, ctx.getResources().getString(resId), duration);
	}
}
