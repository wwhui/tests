package com.sdhz.crpandroid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hzsoft.util.Constants;
import com.sdhz.web.RemoteService;

public class UpdateManager
{
	public final static int	SHOW_CHECK_UPDATE_NOTICE_YES	= 0;
	public final static int	SHOW_CHECK_UPDATE_NOTICE_NO		= 1;

	/* 保存解析的XML信息 */
	// HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String			mSavePath;
	/* 是否取消更新 */
	private boolean			mCancelUpdate					= false;
	// 是否强制更新
	private boolean			mbForce							= false;

	// private int mVersionRemote;
	private int				mVersionLocal;
	// private String mVersionNameLocal;
	// private String mVersionNameRemote;
	private Version			mVersion;

	private Context			mContext;
	/* 更新进度条 */
	private ProgressBar		mProgress;
	private TextView		mTextProgress;
	private Dialog			mDownloadDialog;

	private int getVersionCode(Context context)
	{
		try
		{
			// 获取软件版本号
			mVersionLocal = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;

		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
			return -1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		return mVersionLocal;
	}

	private class CheckUpdateTask extends AsyncTask<Void, Boolean, Boolean>
	{
		int				mShowNewVersionNotice	= SHOW_CHECK_UPDATE_NOTICE_NO;
		ProgressDialog	mPD						= null;

		CheckUpdateTask(int iShowNewVersionNotice)
		{
			mShowNewVersionNotice = iShowNewVersionNotice;
		}

		@Override
		protected Boolean doInBackground(Void... params)
		{

			publishProgress(true);

			boolean result = isUpdate();

			publishProgress(false);

			return result;
		}

		@Override
		protected void onProgressUpdate(Boolean... b)
		{
			if (mShowNewVersionNotice == SHOW_CHECK_UPDATE_NOTICE_NO)
			{
				return;
			}

			if (b[0])
			{
				mPD = ProgressDialog.show(mContext, null, "正在查询最新版本", true,
						false);
			}
			else
			{
				if (mPD != null)
				{
					mPD.dismiss();
				}
			}
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			if (result)
			{
				showUpdateDialog();
				// mIsUpdate = false;
			}
			else
			{
				if (mShowNewVersionNotice == SHOW_CHECK_UPDATE_NOTICE_YES)
				{
					showNoUpdateDialog();
				}
			}
		}
	}

	public UpdateManager(Context context)
	{
		this.mContext = context;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate(int iShowNewVersionNotice)
	{
		Log.d("jiangqi", "Check update");
		mbForce = false;
		new CheckUpdateTask(iShowNewVersionNotice).execute();
	}

	public void checkUpdate(int iShowNewVersionNotice, boolean bForce)
	{
		Log.d("jiangqi", "Check update");
		mbForce = bForce;
		new CheckUpdateTask(iShowNewVersionNotice).execute();
	}

	/**
	 * 检查软件是否有更新版本
	 * 
	 * @return
	 */
	public boolean isUpdate()
	{
		// 获取当前软件版本
		mVersionLocal = getVersionCode(mContext);

		String jsonVersion = RemoteService.getInstance().getVersion();
		Log.d("jiangqi", "jsonVersion = " + jsonVersion);
		if (null != jsonVersion && !"".equals(jsonVersion))
		{
			mVersion=new Version();
			try {
				JSONObject object = new JSONObject(jsonVersion);
				mVersion.version_code=   object.getInt("VERSION_CODE");
				mVersion.version_name=object.getString("VERSION_NAME");
				mVersion.filename=object.getString("FILENAME");
				mVersion.remark=object.getString("REMARK");
				mVersion.release_date=object.getString("RELEASE_DATE");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			return false;
			}
		}
		else
		{
			return false;
		}

		if ((mVersionLocal > 0 && mVersionLocal < mVersion.version_code)
				|| mbForce)
		{
			Log.d("jiangqi", "Version = " + mVersion.toString());
			return true;
		}
		else
		{
			return false;
		}

	}

	/**
	 * 显示无需更新对话框
	 */
	private void showNoUpdateDialog()
	{
		// 构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("提示");
		builder.setMessage("已经是最新版本，无需更新");
		builder.setPositiveButton("确定", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});

		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示软件更新对话框
	 */
	public void showUpdateDialog()
	{
		// 构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件更新");
		// builder.setMessage("检测到新版本"+ mVersion.versionName +"，是否立即更新？");

		builder.setMessage("检测到新版本" + mVersion.version_name + "("
				+ mVersion.release_date + "发布)，是否立即更新？\n\n" + " 更新内容：\n"
				+ mVersion.remark);

		// 更新
		builder.setPositiveButton("立即更新", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
			}
		});
		// 稍后更新
		builder.setNegativeButton("下次再说", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("正在更新");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.dialog_software_update, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		mTextProgress = (TextView) v.findViewById(R.id.textViewProgress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状态
				mCancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();

		// 下载文件
		new DownloadTask().execute();
	}

	private class DownloadTask extends AsyncTask<Void, Integer, Boolean>
	{
		@Override
		protected Boolean doInBackground(Void... params)
		{
			return downloadApk();
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			// 下载失败
			if (!result)
			{
				// 不是用户主动取消（可能是网络错误）
				if (!mCancelUpdate)
				{
					AlertDialog.Builder builder = new Builder(mContext);
					builder.setTitle("错误");
					builder.setMessage("程序下载失败");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog,
										int which)
								{
									dialog.dismiss();
								}
							});

					builder.create().show();
				}

				return;
			}

			String fileName = mVersion.filename;
			File apkfile = new File(mSavePath, fileName);
			if (!apkfile.exists())
			{
				return;
			}
			// 通过Intent安装APK文件
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
					"application/vnd.android.package-archive");
			mContext.startActivity(i);
		}

		@Override
		protected void onProgressUpdate(Integer... progresses)
		{
			mProgress.setProgress(progresses[0]);
			mTextProgress.setText(progresses[0] + "%");
		}

		/**
		 * 下载apk文件
		 */
		private boolean downloadApk()
		{
			int length = 0;
			int count = 0;

			try
			{
				// 获得存储卡的路径
				mSavePath = Environment.getExternalStorageDirectory().getPath()
						+ "/kx";

				String fileName = mVersion.filename;
				URL url = new URL(Constants.DOWN_URL_NEW + fileName);
				// 创建连接
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				// 获取文件大小
				length = conn.getContentLength();
				// 创建输入流
				InputStream is = conn.getInputStream();

				File file = new File(mSavePath);
				// 判断文件目录是否存在
				if (!file.exists())
				{
					file.mkdir();
				}
				Log.d("jiangqi", "mSavePath: " + mSavePath);
				File apkFile = new File(mSavePath, fileName);
				FileOutputStream fos = new FileOutputStream(apkFile);
				// 缓存
				byte buf[] = new byte[1024];
				// 写入到文件中
				do
				{
					// 写入文件
					int numread = is.read(buf);
					if (numread <= 0)
					{
						// 下载完成
						break;
					}
					fos.write(buf, 0, numread);

					// 更新进度
					count += numread;
					publishProgress((int) (((float) count / length) * 100));

				}
				while (!mCancelUpdate);// 点击取消就停止下载.
				fos.close();
				is.close();

			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();

				return false;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}

			// 取消下载对话框显示
			mDownloadDialog.dismiss();

			// 比较文件大小，判断是否下载完全
			if (count < length)
			{
				return false;
			}

			return true;
		}
	}

	public static class Version
	{
		public int		version_code;
		public String	version_name;
		public String	release_date;
		public String	filename;
		public String	remark;

		@Override
		public String toString()
		{
			return "Version [version_code=" + version_code + ", version_name="
					+ version_name + ", release_date=" + release_date
					+ ", filename=" + filename + ", remark=" + remark + "]";
		}

	}

}