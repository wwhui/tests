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
 * �����µĲ���
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
	private EditText editText; // �����²�������
	private Button saveBtn; // ���水ť
	private RequestParams params; // �첽�ύ���ݴ�����
	private ProgressDialog progressDialog; // ���ؽ�����
	private String filePath = "";
	private CompressImage com;
	private static final int CAMERA_WITH_DATA = 3023;
	private Bitmap bitmap = null;
	private File imageFile;
	private Uri photoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ����û�б���
		setContentView(R.layout.crp_info_detail_view_activity);
		params = new RequestParams();
		initView();// ��ʼ��view
		initBlog();// ��ʼ������
		// ���þ۽�
		editText.setFocusable(true);
		editText.requestFocus();
		// ���ü���
		leftButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		atImageView.setOnClickListener(this);
		main_comment_cammera.setOnClickListener(this);
		takephoto.setOnClickListener(this);
		// ��ʼ��uri��ContentValues����Ƭ���浽����Ĭ�ϵ�ַ
		ContentValues values = new ContentValues();
	}

	// ��ʼ��view
	private void initView() {
		leftButton = (Button) findViewById(R.id.left_button);
		leftButton.setText("ȡ��");
		leftButton.setTextColor(Color.WHITE);
		rightButton = (Button) findViewById(R.id.right_button);
		rightButton.setText("����");
		rightButton.setVisibility(View.GONE);
		saveBtn = (Button) findViewById(R.id.save);
		title = (TextView) findViewById(R.id.title_bar);
		atImageView = (ImageView) findViewById(R.id.main_comment_at);
		main_comment_cammera = (ImageView) findViewById(R.id.main_comment_cammera);
		imageview = (ImageView) findViewById(R.id.imageview);
		editText = (EditText) findViewById(R.id.comment_title);
		takephoto = (ImageView) findViewById(R.id.takephoto);
	}

	// ��ʼ������
	private void initBlog() {
		Object object = getIntent().getSerializableExtra(GlobalParams.Data);
		title.setTextColor(Color.WHITE);
		if (object != null) {
			if (object instanceof TaskInfo) {
				TaskInfo info = (TaskInfo) object;
				params.put("type", "2");// ���̲���
				params.put("flow_id", info.getFlow_id());
				params.put("route_id", "");
				title.setText(info.getProject_name());
			} else if (object instanceof TaskRoute) {
				TaskRoute route = (TaskRoute) object;
				params.put("type", "1");// ·�ɲ���
				params.put("route_id", route.getRoute_id());
				params.put("flow_id", route.getFlow_id());
				title.setText(route.getCur_task());
			} else if (object instanceof Blog) {
				Blog blog = (Blog) object;
				params.put("type", String.valueOf(blog.getType()));// ·�ɲ���
				params.put("blog_id", blog.getBlog_id());
				params.put("is_direct", "1");
				title.setText("ת��:" + blog.getSend_content());
				main_comment_cammera.setVisibility(View.GONE);
				editText.setText(" " + "\n\n" + "ת��:" + blog.getSend_content());
				editText.setGravity(Gravity.TOP | Gravity.LEFT);
				editText.setSelection(1);
			}
		} else {
			params.put("type", "3");// ��������
			title.setText("�����µ�����");
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
			// �����ѡȡͼƬ
			Intent pic = new Intent(Intent.ACTION_GET_CONTENT);
			pic.setType("image/*");
			startActivityForResult(pic, IMAGE_CODE);
			break;
		case R.id.takephoto:
			// �����������
			filePath="";
			imageview.setImageBitmap(null); 
			getFile();
			break;

		default:
			break;
		}
	}

	// �ύ����
	public void submitInfo() {
		saveBtn.setClickable(false);
		progressDialog = ProgressDialog.show(this, "��ȴ�", "�����ύ������......",
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
								"���ӷ�����ʧ��,�������ύ", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(String result) {
						saveBtn.setClickable(true);
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
						if ("1".equals(result)) {
							Toast.makeText(CreateNewComentActivity.this,
									"�����ɹ�", Toast.LENGTH_SHORT).show();
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
		if (resultCode != RESULT_OK) { // �˴��� RESULT_OK ��ϵͳ�Զ����һ������
			Log.e("TAG->onresult", "ActivityResult resultCode error");
			return;
		}
		// at������
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
					params.put("refer_opertor_id", refer_opertor);// ����@������Ա
				}
			}
		}
		// �������
		if (requestCode == IMAGE_CODE && resultCode == Activity.RESULT_OK
				&& null != data) {
			try {
				Uri originalUri = data.getData(); // ���ͼƬ��uri
				bitmap = MediaStore.Images.Media.getBitmap(resolver,
						originalUri);
				((ImageView) findViewById(R.id.imageview))
						.setImageBitmap((bitmap));// �Եõ�bitmapͼƬ
				String[] proj = { MediaColumns.DATA };
				// ������android��ý�����ݿ�ķ�װ�ӿڣ�����Ŀ�Android�ĵ�
				Cursor cursor = managedQuery(originalUri, proj, null, null,
						null);
				// ���Ҹ������ ����ǻ���û�ѡ���ͼƬ������ֵ
				int column_index = cursor
						.getColumnIndexOrThrow(MediaColumns.DATA);
				// �����������ͷ ���������Ҫ����С�ĺ���������Խ��
				cursor.moveToFirst();
				// ����������ֵ��ȡͼƬ·��
				filePath = cursor.getString(column_index);
				// showToast("·��" + filePath);
				com = new CompressImage(this, filePath);
				CompressImageTask asytask=new CompressImageTask();
				asytask.execute(null);
				cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// �������
		if (requestCode == CAMERA_WITH_DATA) {
			if (resultCode == RESULT_OK) {
				if (imageFile != null && imageFile.exists()) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					bitmap = BitmapFactory.decodeFile(imageFile.getPath(),
							options);
					((ImageView) findViewById(R.id.imageview))
							.setImageBitmap(bitmap);// ��ͼƬ��ʾ��ImageView��
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

	// �������
	private void destoryBimap() {
		if (bitmap != null && bitmap.isRecycled()) {
			bitmap.isRecycled();
			bitmap = null;
		}
	}

	/**
	 * ͼƬ��ѹ������
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
					"ͼƬ����", "���ڴ���ͼƬ");
			super.onPreExecute();
		}

	};

	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	/**
	 * �õ�ǰʱ���ȡ�õ�ͼƬ����
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

	// ����Gallery����
	protected void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, 1);
		} catch (Exception e) {

		}
	}

	// ��װ����Gallery��intent
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
			// ����galleryȥ���������Ƭ
			final Intent intent = getCropImageIntent(Uri.fromFile(f));
			startActivityForResult(intent, 1);
		} catch (Exception e) {

		}
	}

	/**
	 * Constructs an intent for image cropping. ����ͼƬ��������
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
