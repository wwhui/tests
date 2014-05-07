package com.sdhz.thread;

//import java.io.File;
//
//import com.hzsoft.web.RemoteService;
//
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Handler;
////import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//import android.view.Gravity;
//import android.widget.Toast;
//
////import com.hzsoft.dao.VersionDao;
////import com.hzsoft.dao.impl.VersionDaoImpl;
//import com.hzsoft.domain.Version;
//import com.hzsoft.util.DownloadUtil;
//import com.hzsoft.util.FileUtil;
//import com.hzsoft.util.JsonUtil;
//import com.hzsoft.wxty.MainActivity;
//import com.hzsoft.wxty.config.ConfigMainActivity;
//import com.hzsoft.wxty.message.Cache;
//
//public class VersionThread extends Thread
//{
//    // private VersionDao versionDao;
//    private RemoteService remoteService;
//    private String jsonVersion;
//    private Context context;
//    private int type;
//
//    public VersionThread(Context context, int type)
//    {
//        this.context = context;
//        this.type = type;
//    }
//
//    /*
//     * ��ȡ��ǰ����İ汾��
//     */
//    public static String getVersionName(Context context) throws Exception
//    {
//        // ��ȡpackagemanager��ʵ��
//        PackageManager packageManager = context.getPackageManager();
//        // getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
//        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//        return packInfo.versionName;
//    }
//
//    @Override
//    public void run()
//    {
//        // Looper looper = Looper.getMainLooper();//���̵߳�LOOPER����
//        remoteService = RemoteService.getInstance();
//        Message msg = Message.obtain();
//        jsonVersion = remoteService.getVersion();
//        Log.d("jiangqi", "jsonVersion = " + jsonVersion);
//        if (null != jsonVersion && !"".equals(jsonVersion))
//        {
//            Version ver = JsonUtil.getRemoteVersionFromJSON(jsonVersion);
//            msg.obj = ver.getRemoteVersion();
//        } else
//        {
//            msg.obj = 0;
//        }
//        versionHandler.sendMessage(msg);
//    }
//
//    public Handler versionHandler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg)
//        {
//            super.handleMessage(msg);
//            int remoteVersion = (Integer) msg.obj;
//            int localVersion = 0;
//            // Version localVersion = getLocalVersion();
//
//            if (remoteVersion == 0)
//            {
//                Toast toast = Toast.makeText(context, "���ӷ�����ʧ��,���ȼ�����磡", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 180);
//                toast.show();
//                return;
//            }
//
//            // boolean flag1 = false;
//            // boolean flag2 = false;
//            // �жϱ��ذ汾�Ƿ�����
//            // boolean flag1 = isNewVersion(localVersion.getLocalVersion(),
//            // remoteVersion);
//
//            try
//            {
//                String versionName = getVersionName(context);
//                Float f1 = Float.parseFloat(versionName) * 10;
//                localVersion = (f1).intValue();
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//            boolean hasNewVersion = (localVersion < remoteVersion ? true : false);
//            if (hasNewVersion)
//            {
//                Cache.getInstance().setIsAutoLogin("false");
//                
//                if (type == 1)
//                {
//                    MainActivity m = (MainActivity) context;
//                    m.showUpdateBuilder(remoteVersion);
//                } else
//                {
//                    ConfigMainActivity c = (ConfigMainActivity) context;
//                    c.showUpdateBuilder(remoteVersion);
//                }
//
//            } else
//            {
//                Cache.getInstance().setIsAutoLogin("true");
//                
//                if (type == 2)
//                {
//                    Toast toast = Toast.makeText(context, "��ǰ�����°汾", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 180);
//                    toast.show();
//                }
//            }
//            
//            // �Ƿ��һ�ΰ�װ
//            boolean flag2 = localVersion == 0;
//            if (!hasNewVersion)
//            {
//                if (!flag2)
//                {
//                    if (type == 1)
//                    {
//                        MainActivity m = (MainActivity) context;
//                        m.showUpdateBuilder(remoteVersion);
//                    } else
//                    {
//                        ConfigMainActivity c = (ConfigMainActivity) context;
//                        c.showUpdateBuilder(remoteVersion);
//                    }
//                } else
//                {
//                    Version ver = new Version();
//                    ver.setLocalVersion(remoteVersion);
//                    ver.setRemoteVersion(remoteVersion);
//                    ver.setIsNew(1);
//                    // addVersion(ver);
//                }
//            } else
//            {
//                if (type == 2)
//                {
//                    Toast toast = Toast.makeText(context, "��ǰ�����°汾", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 180);
//                    toast.show();
//                }
//            }
//        }
//    };
//    
//    public void showUpdateBuilder(final int remoteVersion) {
//        Builder builder = new Builder(context);
//        builder.setIcon(android.R.drawable.ic_dialog_info);
//        String version = String.valueOf(remoteVersion/10f);
//        builder.setTitle("Ŀǰ���°汾,�Ƿ�����?");
//        builder.setMessage("���°汾(Ver."+version+")");
//        builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                progressDialogShow(context);
//                new ConfigDownApk().execute(remoteVersion);
//            }
//        });
//        builder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
//    }
//    
//    class ConfigDownApk extends AsyncTask<Integer, Integer, String> {
//        @Override
//        protected String doInBackground(Integer... params) {
//            DownloadUtil downloader = new DownloadUtil(context, 2);
//            downloader.downFile("download/", "vod.apk");
//            return null;
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            progressDialogClose();
//            installApk("download/vod.apk");
//        }
//    }
//    
//    public void installApk(String fileName) {
//        Uri uri = Uri.fromFile(new File(FileUtil.SDPATH + fileName));
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(uri, "application/vnd.android.package-archive");
//        context.startActivity(intent);
//    }




    //
    // /** ��ȡ���ذ汾�� */
    // private Version getLocalVersion() {
    // versionDao = VersionDaoImpl.getInstance(context);
    // return versionDao.getVersion();
    // }
    /** �жϰ汾�Ƿ����� */
//    private boolean isNewVersion(int localVersion, int remoteVersion)
//    {
//        if (localVersion < remoteVersion)
//        {
//            return false;
//        }
//        return true;
//    }
    // /** ����汾�� */
    // private void addVersion(Version ver) {
    // versionDao = VersionDaoImpl.getInstance(context);
    // versionDao.addVersion(ver);
    // }
//}
