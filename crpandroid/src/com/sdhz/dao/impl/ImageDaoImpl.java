package com.sdhz.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hzsoft.util.JsonUtil;
import com.sdhz.dao.ImageDao;
import com.sdhz.dao.support.DaoSupport;
import com.sdhz.domain.Image;

public class ImageDaoImpl implements ImageDao
{
    private DaoSupport support;
    private SQLiteDatabase db;
    private static ImageDaoImpl instance = null;

    public ImageDaoImpl(Context context)
    {
        // support = new DaoSupport(context, DaoSupport.DATABASE_NAME);
        // db = support.getWritableDatabase();
        support = DaoSupport.getInstance(context);
    }

    synchronized public static ImageDao getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new ImageDaoImpl(context);
        }
        return instance;
    }

    @Override
    public int addImageListData(String jsonData, String imageType)
    {
        db = support.getWritableDatabase();

        db.delete("t_image", "categoryId=?", new String[] { imageType });

        ArrayList<ContentValues> imageList = JsonUtil.getRemoteImageFromJSON(jsonData);
        for (ContentValues values : imageList)
        {
            long i = db.insert("t_image", null, values);
            if (i == -1)
            {
                Log.e("jiangqi", "insert into t_image error, result = " + i);
                db.close();
                return 0;
            }
        }
        db.close();
        return 1;
    }
    
    @Override
    public void deleteList(String cid)
    {
        db = support.getWritableDatabase();
        db.delete("t_image", "categoryId=?", new String[] { cid });
        db.close();
    }
    
    @Override
    public void deleteList(String cid, int maxNum)
    {
        int limit = 1;
        int offset = maxNum - 1;
        long vid = 0;
        
        db = support.getWritableDatabase();
        
        //获取最后一个vid
        Cursor cursor = null;
        String sql = "select imageid from t_image where categoryid = ? order by imageId desc limit ? offset ?";
        cursor = db.rawQuery(sql, new String[] { cid, String.valueOf(limit), String.valueOf(offset)});
        if(cursor != null && cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                vid = cursor.getLong(0);
            }
        }
        
        //删除小于vid的所有记录
        db.delete("t_image", "categoryId=? and imageId < ?", new String[] { cid, String.valueOf(vid)});
        
        db.close();
    }

    @Override
    public int insertList(List<Image> imageList)
    {
        Log.d("jiangqi", "ImageDaoImpl insertList: " + imageList.size());
        db = support.getWritableDatabase();
        int cnt = 0;
        
        ContentValues cv = new ContentValues();
        
        for(Image img: imageList)
        {
            cv.put("imageId",    img.getImageId());
            cv.put("categoryId", img.getCategoryId());
            cv.put("postdate",   img.getPostdate());
            cv.put("imageName",  img.getImageName());
            cv.put("imageType",  img.getImageType());
            cv.put("imageResource", img.getImageResource());
            cv.put("synopsis",   img.getSynopsis());
            cv.put("type",       img.getType());
            
            long result = db.insert("t_image", null, cv);
            if(result >= 0)
            {
                cnt++;
            }
        }
        db.close();
        
        return cnt;
    }
    
    @Override
    public int getCount(String cid)
    {
        int cnt = -1;
        db = support.getReadableDatabase();
        Cursor cursor = null;
        String sql = "select count(1) cnt from t_image where categoryId=?";
        cursor = db.rawQuery(sql, new String[] { cid });
        if(cursor != null && cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                cnt = cursor.getInt(0);
            }
        }
        db.close();
        
        return cnt;
    }
    
    @Override
    public List<Image> getImageList(String category)
    {
        List<Image> imageList = new ArrayList<Image>();
        db = support.getReadableDatabase();
        Cursor cursor = null;
        if ("100".equals(category))
        {
            cursor = db.query("t_image", null, null, new String[] {}, null, null, "imageId desc");
        } else
        {
            cursor = db.query("t_image", null, "categoryId=?", new String[] { category }, null,
                    null, "imageId desc");
        }
        
        if (cursor != null && cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                Long imageId = cursor.getLong(cursor.getColumnIndex("imageId"));
                int categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
                String postdate = cursor.getString(cursor.getColumnIndex("postdate"));
                String imageName = cursor.getString(cursor.getColumnIndex("imageName"));
                String imageResource = cursor.getString(cursor.getColumnIndex("imageResource"));
                String imageType = cursor.getString(cursor.getColumnIndex("imageType"));
                String synopsis = cursor.getString(cursor.getColumnIndex("synopsis"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));

                Image image = new Image();
                image.setImageId(imageId);
                image.setCategoryId(categoryId);
                image.setPostdate(postdate);
                image.setImageName(imageName);
                image.setImageResource(imageResource);
                image.setImageType(imageType);
                image.setSynopsis(synopsis);
                image.setType(type);

                imageList.add(image);
            }
        }
        cursor.close();
        db.close();
        return imageList;
    }

    @Override
    public List<Image> getImageList(String category, int limit)
    {
        List<Image> imageList = new ArrayList<Image>();
        db = support.getReadableDatabase();
        Cursor cursor = null;
        if ("100".equals(category))
        {
            cursor = db.rawQuery("select * from t_image order by imageId desc limit" + limit, null);
        } else
        {
            cursor = db.rawQuery("select * from t_image where categoryId=? order by imageId desc limit " + limit, new String[] { category });
        }
        
        if (cursor != null && cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                Long imageId = cursor.getLong(cursor.getColumnIndex("imageId"));
                int categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
                String postdate = cursor.getString(cursor.getColumnIndex("postdate"));
                String imageName = cursor.getString(cursor.getColumnIndex("imageName"));
                String imageResource = cursor.getString(cursor.getColumnIndex("imageResource"));
                String imageType = cursor.getString(cursor.getColumnIndex("imageType"));
                String synopsis = cursor.getString(cursor.getColumnIndex("synopsis"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));

                Image image = new Image();
                image.setImageId(imageId);
                image.setCategoryId(categoryId);
                image.setPostdate(postdate);
                image.setImageName(imageName);
                image.setImageResource(imageResource);
                image.setImageType(imageType);
                image.setSynopsis(synopsis);
                image.setType(type);

                imageList.add(image);
            }
        }
        cursor.close();
        db.close();
        return imageList;
    }
    
    @Override
    public List<Image> getImageList(String imgType, int limit, int offset)
    {
        List<Image> imageList = new ArrayList<Image>();
        db = support.getReadableDatabase();
        String sql = "select * from t_image where imageType = ? limit " + limit + " offset " + offset;
        Cursor cursor = db.rawQuery(sql, new String[] { imgType });
        if (cursor != null && cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                Long imageId = cursor.getLong(cursor.getColumnIndex("imageId"));
                int categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
                String postdate = cursor.getString(cursor.getColumnIndex("postdate"));
                String imageName = cursor.getString(cursor.getColumnIndex("imageName"));
                String imageResource = cursor.getString(cursor.getColumnIndex("imageResource"));
                String imageType = cursor.getString(cursor.getColumnIndex("imageType"));
                String synopsis = cursor.getString(cursor.getColumnIndex("synopsis"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));

                Image image = new Image();
                image.setImageId(imageId);
                image.setCategoryId(categoryId);
                image.setPostdate(postdate);
                image.setImageName(imageName);
                image.setImageResource(imageResource);
                image.setImageType(imageType);
                image.setSynopsis(synopsis);
                image.setType(type);
                imageList.add(image);
            }
        }
        cursor.close();
        db.close();
        return imageList;
    }

    @Override
    public List<Image> searchImageList(String title)
    {
        List<Image> imageList = new ArrayList<Image>();
        db = support.getReadableDatabase();
        // Cursor cursor = db.query("t_image", null,
        // "imageName like ('%"+title+"%')", null, null, null, null);
        Cursor cursor = db.rawQuery("select * from t_image where imageName like '%" + title + "%'",
                null);
        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    Long imageId = cursor.getLong(cursor.getColumnIndex("imageId"));
                    int categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
                    String postdate = cursor.getString(cursor.getColumnIndex("postdate"));
                    String imageName = cursor.getString(cursor.getColumnIndex("imageName"));
                    String imageResource = cursor.getString(cursor.getColumnIndex("imageResource"));
                    String imageType = cursor.getString(cursor.getColumnIndex("imageType"));
                    String synopsis = cursor.getString(cursor.getColumnIndex("synopsis"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));

                    Image image = new Image();
                    image.setImageId(imageId);
                    image.setCategoryId(categoryId);
                    image.setPostdate(postdate);
                    image.setImageName(imageName);
                    image.setImageResource(imageResource);
                    image.setImageType(imageType);
                    image.setSynopsis(synopsis);
                    image.setType(type);
                    imageList.add(image);
                }
            }
        }
        cursor.close();
        db.close();
        return imageList;
    }

    @Override
    public boolean openDatabase()
    {
        db = support.getReadableDatabase();
        if (db != null)
        {
            return db.isOpen();
        } else
        {
            return false;
        }
    }

    @Override
    public void closeDB()
    {
        if (db != null)
        {
            if (db.isOpen())
            {
                db.close();
            }
        }
    }

//    @Override
//    public String getMaxId(String imageType)
//    {
//        String result = "0";
//        db = support.getReadableDatabase();
//        String sql = "select max(imageId) imageId from t_image where 1=1 and categoryId=?";
//        Cursor cursor = db.rawQuery(sql, new String[] { imageType });
//        if (cursor != null)
//        {
//            if (cursor.getCount() > 0)
//            {
//                while (cursor.moveToNext())
//                {
//                    result = String.valueOf(cursor.getLong(cursor.getColumnIndex("imageId")));
//                }
//            } 
//        } 
//
//        cursor.close();
//        db.close();
//        return result;
//    }
//    
//    @Override
//    public String getMinId(String imageType)
//    {
//        String result = "0";
//        db = support.getReadableDatabase();
//        String sql = "select min(imageId) imageId from t_image where 1=1 and categoryId=?";
//        Cursor cursor = db.rawQuery(sql, new String[] { imageType });
//        if (cursor != null)
//        {
//            if (cursor.getCount() > 0)
//            {
//                while (cursor.moveToNext())
//                {
//                    result = String.valueOf(cursor.getLong(cursor.getColumnIndex("imageId")));
//                }
//            } 
//        } 
//
//        cursor.close();
//        db.close();
//        return result;
//    }


    @Override
    public Image getLatest(String imageType)
    {
        Image image = null;

        db = support.getReadableDatabase();
        String sql = "select * from t_image where imageid = (select max(imageId) imageId from t_image where categoryId=?)";
        Cursor cursor = db.rawQuery(sql, new String[] { imageType });
        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                while (cursor.moveToNext())
                {
                    Long imageId = cursor.getLong(cursor.getColumnIndex("imageId"));
                    int categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
                    String postdate = cursor.getString(cursor.getColumnIndex("postdate"));
                    String imageName = cursor.getString(cursor.getColumnIndex("imageName"));
                    String imageResource = cursor.getString(cursor.getColumnIndex("imageResource"));
                    String synopsis = cursor.getString(cursor.getColumnIndex("synopsis"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));

                    image = new Image();
                    image.setImageId(imageId);
                    image.setCategoryId(categoryId);
                    image.setPostdate(postdate);
                    image.setImageName(imageName);
                    image.setImageResource(imageResource);
                    image.setImageType(imageType);
                    image.setSynopsis(synopsis);
                    image.setType(type);
                }
            } else
            {
                image = null;
            }
        }

        cursor.close();
        db.close();
        
        return image;
    }

}