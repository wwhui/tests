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
	private int window_width, window_height;// �ؼ����
	private int state_height;// ״̬���ĸ߶�
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
		/** ����״̬���߶� **/
		viewTreeObserver = imageView.getViewTreeObserver();
		viewTreeObserver
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (state_height == 0) {
							// ��ȡ״�����߶�
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

	// ����ʽ ���� pop�˵� parent ���½�
	public void showAsDropDown(View parent) {
		popupWindow.setFocusable(true);
		popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		// ˢ��״̬
		popupWindow.update();
	}

	// ���ز˵�
	public void dismiss() {
		if(popupWindow!=null && popupWindow.isShowing())
		{
		//	imageView.setImageBitmap(null);
			popupWindow.dismiss();
		}
	}

}
