package com.hzsoft.util;
//
//import java.util.TimerTask;
//
//import com.hzsoft.dao.ImageDao;
//import com.hzsoft.dao.impl.ImageDaoImpl;
//import com.hzsoft.web.RemoteService;
//
//import android.content.Context;
//
///*
// *�ӷ������ϻ�ȡ��Ƶ
// * */
//public class PhpvodSchedule extends TimerTask
//{
//    private Context context;
//    private RemoteService remoteService;
//    private ImageDao imageDao;
//    // 1 ���ž۽� 2 ���߳��� 3 ���ʵ�� 4 ���ٰ� 28 �������� 29 ������Ͳ 57 �Ŀ�Ƶ��
////    private String[] imageType = new String[] { "1", "2", "3", "4", "28", "29", "57" };
//    //73    ������  67 ý���  75 ������  78 ���¼  83 ΢����  88 ��֪��  
//    private static String[] imageType = new String[] { "73", "67", "75", "78", "83", "88"};
//
//    public PhpvodSchedule(Context context)
//    {
//        this.context = context;
//    }
//
//    @Override
//    public void run()
//    {
//        run_task();
//        System.out.println("ִ��phpvod");
//    }
//
//    public void run_task()
//    {
//        Thread t = new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                task_1();
//            }
//        });
//        t.start();
//    }
//
//    // ͬ���������ϵ�������Ŀ�µ��������ӵ��������ݿ��С��������ϲ����ᴦ��maxId 
//    
//    //TODO ����Ӧ��ֻ��ȡ���µġ�����û�е�����
//    //��Ҫ�������ݿ⣬�����Ƿ���֪ͨ�������û������������������ݲ��������ݿ�
//    
//    //TODO ֻ֧�ֶ��Ĺ��ܣ�ֻ�����û����õ�ָ����Ŀ�����ӵĸ���
//    public void task_1()
//    {
//        boolean net = HttpUtils.checkNetworkIsAvailable(this.context);
//        if (net)
//        {
//            remoteService = RemoteService.getInstance();
//            imageDao = ImageDaoImpl.getInstance(context);
//
//            for (String class_id : imageType)
//            {
//                String maxId = imageDao.getMaxId(class_id);
//                
//                System.out.println("��ȡ��Ƶvid:" + class_id + ", maxId : " + maxId);
//                String imageJson = remoteService.getImageData(class_id, maxId);
//                if (null != imageJson 
//                        && !"".equals(imageJson) 
//                        && !"[]".equals(imageJson))
//                {
//                    imageDao.addImageListData(imageJson, class_id);
//                }
//            }
//        }
//    }
//
//}
