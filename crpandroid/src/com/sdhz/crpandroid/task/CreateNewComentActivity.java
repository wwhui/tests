package com.sdhz.crpandroid.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzsoft.util.CompressImage;
import com.hzsoft.util.Constants;
import com.hzsoft.util.ContentUtil;
import com.hzsoft.util.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sdhz.crpandroid.BaseActivity;
import com.sdhz.crpandroid.CrpaApplication;
import com.sdhz.crpandroid.R;
import com.sdhz.crpandroid.group.SelectUserActivity;
import com.sdhz.domain.Account;
import com.sdhz.domain.blog.Blog;
import com.sdhz.domain.group.UserInfo;
import com.sdhz.domain.task.TaskInfo;
import com.sdhz.domain.task.TaskRoute;

/**
 * 发表新的博文
 * 
 */
public class CreateNewComentActivity extends BaseActivity implements
		OnClickListener {
	private static final int GET_CODE = 0;
	private static final int IMAGE_CODE = 1;
	private Button leftButton;
	private Button rightButton;
	private TextView title; // titlebar
	private ImageView atImageView, main_comment_cammera, imageview, takephoto;
	private EditText editText; // 发表新博文区域
	private Button saveBtn; // 保存按钮
	private RequestParams params; // 异步提交数据带参数
	private ProgressDialog progressDialog; // 加载进度条
	private String filePath = "";
	private CompressImage com;
	private static final int CAMERA_WITH_DATA = 3023;
	private Bitmap bitmap = null;
	private File imageFile;
	private Uri photoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置没有标题
		setContentView(R.layout.crp_info_detail_view_activity);
		params = new RequestParams();
		initView();// 初始化view
		initBlog();// 初始化博文
		// 设置聚焦
		editText.setFocusable(true);
		editText.requestFocus();
		// 设置监听
		leftButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		atImageView.setOnClickListener(this);
		main_comment_cammera.setOnClickListener(this);
		takephoto.setOnClickListener(this);
		// 初始化uri用ContentValues把照片保存到拍照默认地址
		ContentValues values = new ContentValues();
	}

	// 初始化view
	private void initView() {
		leftButton = (Button) findViewById(R.id.left_button);
		leftButton.setText("取消");
		leftButton.setTextColor(Color.WHITE);
		rightButton = (Button) findViewById(R.id.right_button);
		rightButton.setText("保存");
		rightButton.setVisibility(View.GONE);
		saveBtn = (Button) findViewById(R.id.save);
		title = (TextView) findViewById(R.id.title_bar);
		atImageView = (ImageView) findViewById(R.id.main_comment_at);
		main_comment_cammera = (ImageView) findViewById(R.id.main_comment_cammera);
		imageview = (ImageView) findViewById(R.id.imageview);
		editText = (EditText) findViewById(R.id.comment_title);
		takephoto = (ImageView) findViewById(R.id.takephoto);
	}

	// 初始化博文
	private void initBlog() {
		Object object = getIntent().getSerializableExtra(GlobalParams.Data);
		title.setTextColor(Color.WHITE);
		if (object != null) {
			if (object instanceof TaskInfo) {
				TaskInfo info = (TaskInfo) object;
				params.put("type", "2");// 流程博文
				params.put("flow_id", info.getFlow_id());
				params.put("route_id", "");
				title.setText(info.getProject_name());
			} else if (object instanceof TaskRoute) {
				TaskRoute route = (TaskRoute) object;
				params.put("type", "1");// 路由博文
				params.put("route_id", route.getRoute_id());
				params.put("flow_id", route.getFlow_id());
				title.setText(route.getCur_task());
			} else if (object instanceof Blog) {
				Blog blog = (Blog) object;
				params.put("type", String.valueOf(blog.getType()));// 路由博文
				params.put("blog_id", blog.getBlog_id());
				params.put("is_direct", "1");
				title.setText("转发:" + blog.getSend_content());
				main_comment_cammera.setVisibility(View.GONE);
				editText.setText(" " + "\n\n" + "转发:" + blog.getSend_content());
				editText.setGravity(Gravity.TOP | Gravity.LEFT);
				editText.setSelection(1);
			}
		} else {
			params.put("type", "3");// 其他博文
			title.setText("发表新的贴子");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_button:
			this.finish();
			break;
		case R.id.save:
			submitInfo();
			break;
		case R.id.main_comment_at:
			Intent intent = new Intent(this, SelectUserActivity.class);
			startActivityForResult(intent, GET_CODE);
			break;
		case R.id.main_comment_cammera:
			imageview.setImageBitmap(null);
			// 从相册选取图片
			Intent pic = new Intent(Intent.ACTION_GET_CONTENT);
			pic.setType("image/*");
			startActivityForResult(pic, IMAGE_CODE);
			break;
		case R.id.takephoto:
			// 调用相机拍照
			filePath="";
			imageview.setImageBitmap(null); 
			getFile();
			break;

		default:
			break;
		}
	}

	// 提交数据
	public void submitInfo() {
		saveBtn.setClickable(false);
		progressDialog = ProgressDialog.show(this, "请等待", "正在提交数据中......",
				false);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(true);
		// progressDialog.s
		params.put("blog_content", editText.getText().toString());
		Account account =  ( (CrpaApplication)getApplication()).getAccount();
		params.put("send_operator", account.getAccount());
		if (imageFile != null) {
			try {
				params.put("file", imageFile);
			} catch (FileNotFoundException e) {
			}
		}
		HttpClient.post(Constants.NewBlog, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0) {
						saveBtn.setClickable(true);
						progressDialog.dismiss();
						Toast.makeText(CreateNewComentActivity.this,
								"连接服务器失败,请重新提交", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(String result) {
						saveBtn.setClickable(true);
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
						if ("1".equals(result)) {
							Toast.makeText(CreateNewComentActivity.this,
									"操作成功", Toast.LENGTH_SHORT).show();
							if (com != null) {
								com.delete();
							}
							CreateNewComentActivity.this.finish();
						}
					}

				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ContentResolver resolver = getContentResolver();
		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
			Log.e("TAG->onresult", "ActivityResult resultCode error");
			return;
		}
		// at到的人
		if (requestCode == GET_CODE) {
			if (resultCode == RESULT_OK) {
				if (data.getSerializableExtra("selectUserInfoList") != null) {
					String edit = "";
					String refer_opertor = "";
					List<UserInfo> list = (List) data
							.getSerializableExtra("selectUserInfoList");
					if (list != null && list.size() > 0) {
						for (UserInfo user : list) {
							edit += "@" + user.getName() + " ";
							refer_opertor += user.getOperator_id() + ",";
						}
					}
					editText.setText(ContentUtil.formatContentNoClick(edit
							+ editText.getText().toString()));
					params.put("refer_opertor_id", refer_opertor);// 传递@到的人员
				}
			}
		}
		// 调用相册
		if (requestCode == IMAGE_CODE && resultCode == Activity.RESULT_OK
				&& null != data) {
			try {
				Uri originalUri = data.getData(); // 获得图片的uri
				bitmap = MediaStore.Images.Media.getBitmap(resolver,
						originalUri);
				((ImageView) findViewById(R.id.imageview))
						.setImageBitmap((bitmap));// 显得到bitmap图片
				String[] proj = { MediaColumns.DATA };
				// 好像是android多媒体数据库的封装接口，具体的看Android文档
				Cursor cursor = managedQuery(originalUri, proj, null, null,
						null);
				// 按我个人理解 这个是获得用户选择的图片的索引值
				int column_index = cursor
						.getColumnIndexOrThrow(MediaColumns.DATA);
				// 将光标移至开头 ，这个很重要，不小心很容易引起越界
				cursor.moveToFirst();
				// 最后根据索引值获取图片路径
				filePath = cursor.getString(column_index);
				// showToast("路径" + filePath);
				com = new CompressImage(this, filePath);
				CompressImageTask asytask=new CompressImageTask();
				asytask.execute(null);
				cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 调用相机
		if (requestCode == CAMERA_WITH_DATA) {
			if (resultCode == RESULT_OK) {
				if (imageFile != null && imageFile.exists()) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					bitmap = BitmapFactory.decodeFile(imageFile.getPath(),
							options);
					((ImageView) findViewById(R.id.imageview))
							.setImageBitmap(bitmap);// 将图片显示在ImageView里
					com = new CompressImage(this, imageFile.getAbsolutePath());
					CompressImageTask asytask=new CompressImageTask();
					asytask.execute(null);
					Toast.makeText(this, imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this, "", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	public void getFile() {
		destoryBimap();
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			String saveDir = Environment.getExternalStorageDirectory()
					+ "/temple";
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			imageFile = new File(saveDir,getPhotoFileName() );
			if (!imageFile.exists()) {
				try {
					imageFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(this,
						"sss", Toast.LENGTH_LONG)
							.show();
					return;
				}
			}
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} else {
			Toast.makeText(this,
					" sd card ", Toast.LENGTH_LONG).show();
		}
	}

	// 清除缓存
	private void destoryBimap() {
		if (bitmap != null && bitmap.isRecycled()) {
			bitmap.isRecycled();
			bitmap = null;
		}
	}

	/**
	 * 图片的压缩处理
	 * 
	 * */
	class  CompressImageTask extends  AsyncTask {

		@Override
		protected Object doInBackground(Object... params) {
			if (com != null) {
				com.proess();
			}
			imageFile = com.getFile();
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(CreateNewComentActivity.this,
					"图片处理", "正在处理图片");
			super.onPreExecute();
		}

	};

	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	/**
	 * 用当前时间给取得的图片命名
	 * 
	 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	private Bitmap show(Intent data) {
		Bundle bundle = data.getExtras();
		Bitmap bitmap = (Bitmap) bundle.get("data");
		return bitmap;
	}

	// 请求Gallery程序
	protected void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, 1);
		} catch (Exception e) {

		}
	}

	// 封装请求Gallery的intent
	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
	}

	protected void doCropPhoto(File f) {
		try {
			// 启动gallery去剪辑这个照片
			final Intent intent = getCropImageIntent(Uri.fromFile(f));
			startActivityForResult(intent, 1);
		} catch (Exception e) {

		}
	}

	/**
	 * Constructs an intent for image cropping. 调用图片剪辑程序
	 */
	public static Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
	}
	
	
	   @Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		   if(imageFile!=null){
		   }
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		   if(imageFile!=null){
			   Log.e("errror", imageFile.getAbsolutePath());
		   }
		super.onRestoreInstanceState(savedInstanceState);
	}

	public void onConfigurationChanged(Configuration config) { 
		    super.onConfigurationChanged(config); 
		    } 
}
