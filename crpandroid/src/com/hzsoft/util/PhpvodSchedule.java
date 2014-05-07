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
// *从服务器上获取视频
// * */
//public class PhpvodSchedule extends TimerTask
//{
//    private Context context;
//    private RemoteService remoteService;
//    private ImageDao imageDao;
//    // 1 新闻聚焦 2 无线城市 3 最佳实践 4 光荣榜 28 我型我秀 29 生活万花筒 57 拍客频道
////    private String[] imageType = new String[] { "1", "2", "3", "4", "28", "29", "57" };
//    //73    新闻眼  67 媒体汇  75 工作秀  78 风采录  83 微生活  88 移知道  
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
//        System.out.println("执行phpvod");
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
//    // 同步服务器上的所有栏目下的所有帖子到本地数据库中。服务器上并不会处理maxId 
//    
//    //TODO 这里应该只获取最新的、本地没有的帖子
//    //不要插入数据库，仅仅是放在通知栏，等用户点击后，再载入具体内容并插入数据库
//    
//    //TODO 只支持订阅功能，只提醒用户设置的指定栏目下帖子的更新
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
//                System.out.println("获取视频vid:" + class_id + ", maxId : " + maxId);
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
