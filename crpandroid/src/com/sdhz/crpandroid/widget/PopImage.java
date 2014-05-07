package com.sdhz.crpandroid.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.hzsoft.util.AsyncBitmapLoader;
import com.hzsoft.util.AsyncBitmapLoader.ImageCallBack;
import com.hzsoft.util.cache.ImageLoader;
import com.sdhz.crpandroid.R;
import com.sdhz.view.dragimage.DragImageView;

public class PopImage {
	private Context context;
	private PopupWindow popupWindow;
	private DragImageView imageView;
	private int window_width, window_height;// 控件宽度
	private int state_height;// 状态栏的高度
	private ViewTreeObserver viewTreeObserver;
	public PopImage(final Context context) {
		this.context = context;
		View view = LayoutInflater.from(context)
				.inflate(R.layout.popimage, null);
		imageView=(DragImageView) view.findViewById(R.id.imageview);
		imageView.setContext(context);
		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		view.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		popupWindow.setContentView(view);
		/** 测量状态栏高度 **/
		viewTreeObserver = imageView.getViewTreeObserver();
		viewTreeObserver
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (state_height == 0) {
							// 获取状况栏高度
							Rect frame = new Rect();
							popupWindow.getContentView() .getWindowVisibleDisplayFrame(frame);
							state_height = frame.top;
							imageView.setScreen_H(imageView.getHeight()-state_height);
							imageView.setScreen_W(imageView.getWidth());
						}

					}
				});
		
	}
	public ImageView getImageView() {
		return imageView;
	}

	// 下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		popupWindow.setFocusable(true);
		popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏菜单
	public void dismiss() {
		if(popupWindow!=null && popupWindow.isShowing())
		{
		//	imageView.setImageBitmap(null);
			popupWindow.dismiss();
		}
	}

}
