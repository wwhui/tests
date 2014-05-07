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

	/* ���������XML��Ϣ */
	// HashMap<String, String> mHashMap;
	/* ���ر���·�� */
	private String			mSavePath;
	/* �Ƿ�ȡ������ */
	private boolean			mCancelUpdate					= false;
	// �Ƿ�ǿ�Ƹ���
	private boolean			mbForce							= false;

	// private int mVersionRemote;
	private int				mVersionLocal;
	// private String mVersionNameLocal;
	// private String mVersionNameRemote;
	private Version			mVersion;

	private Context			mContext;
	/* ���½����� */
	private ProgressBar		mProgress;
	private TextView		mTextProgress;
	private Dialog			mDownloadDialog;

	private int getVersionCode(Context context)
	{
		try
		{
			// ��ȡ����汾��
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
				mPD = ProgressDialog.show(mContext, null, "���ڲ�ѯ���°汾", true,
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
	 * ����������
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
	 * �������Ƿ��и��°汾
	 * 
	 * @return
	 */
	public boolean isUpdate()
	{
		// ��ȡ��ǰ����汾
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
	 * ��ʾ������¶Ի���
	 */
	private void showNoUpdateDialog()
	{
		// ����Ի���
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("��ʾ");
		builder.setMessage("�Ѿ������°汾���������");
		builder.setPositiveButton("ȷ��", new OnClickListener()
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
	 * ��ʾ������¶Ի���
	 */
	public void showUpdateDialog()
	{
		// ����Ի���
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("�������");
		// builder.setMessage("��⵽�°汾"+ mVersion.versionName +"���Ƿ��������£�");

		builder.setMessage("��⵽�°汾" + mVersion.version_name + "("
				+ mVersion.release_date + "����)���Ƿ��������£�\n\n" + " �������ݣ�\n"
				+ mVersion.remark);

		// ����
		builder.setPositiveButton("��������", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// ��ʾ���ضԻ���
				showDownloadDialog();
			}
		});
		// �Ժ����
		builder.setNegativeButton("�´���˵", new OnClickListener()
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
	 * ��ʾ������ضԻ���
	 */
	private void showDownloadDialog()
	{
		// ����������ضԻ���
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("���ڸ���");
		// �����ضԻ������ӽ�����
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.dialog_software_update, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		mTextProgress = (TextView) v.findViewById(R.id.textViewProgress);
		builder.setView(v);
		// ȡ������
		builder.setNegativeButton("ȡ��", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// ����ȡ��״̬
				mCancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();

		// �����ļ�
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
			// ����ʧ��
			if (!result)
			{
				// �����û�����ȡ�����������������
				if (!mCancelUpdate)
				{
					AlertDialog.Builder builder = new Builder(mContext);
					builder.setTitle("����");
					builder.setMessage("��������ʧ��");
					builder.setPositiveButton("ȷ��",
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
			// ͨ��Intent��װAPK�ļ�
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
		 * ����apk�ļ�
		 */
		private boolean downloadApk()
		{
			int length = 0;
			int count = 0;

			try
			{
				// ��ô洢����·��
				mSavePath = Environment.getExternalStorageDirectory().getPath()
						+ "/kx";

				String fileName = mVersion.filename;
				URL url = new URL(Constants.DOWN_URL_NEW + fileName);
				// ��������
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				// ��ȡ�ļ���С
				length = conn.getContentLength();
				// ����������
				InputStream is = conn.getInputStream();

				File file = new File(mSavePath);
				// �ж��ļ�Ŀ¼�Ƿ����
				if (!file.exists())
				{
					file.mkdir();
				}
				Log.d("jiangqi", "mSavePath: " + mSavePath);
				File apkFile = new File(mSavePath, fileName);
				FileOutputStream fos = new FileOutputStream(apkFile);
				// ����
				byte buf[] = new byte[1024];
				// д�뵽�ļ���
				do
				{
					// д���ļ�
					int numread = is.read(buf);
					if (numread <= 0)
					{
						// �������
						break;
					}
					fos.write(buf, 0, numread);

					// ���½���
					count += numread;
					publishProgress((int) (((float) count / length) * 100));

				}
				while (!mCancelUpdate);// ���ȡ����ֹͣ����.
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

			// ȡ�����ضԻ�����ʾ
			mDownloadDialog.dismiss();

			// �Ƚ��ļ���С���ж��Ƿ�������ȫ
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