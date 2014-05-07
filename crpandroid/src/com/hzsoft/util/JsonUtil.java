package com.hzsoft.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sdhz.crpandroid.R;
import com.sdhz.domain.Image;
import com.sdhz.domain.Message;

public class JsonUtil
{
    public static ArrayList<ContentValues> getRemoteImageFromJSON(String jsonData)
    {
        // Log.d("JsonData", jsonData);
        ArrayList<ContentValues> imageList = new ArrayList<ContentValues>();
        try
        {
            JSONArray array = new JSONArray(jsonData);
            for (int i = 0; i < array.length(); i++)
            {
                ContentValues values = new ContentValues();

                JSONObject obj = array.getJSONObject(i);
                long imageId = obj.getLong("vid");
                String postdate = obj.getString("postdate");
                int categoryId = obj.getInt("class_id");
                String imageName = obj.getString("subject");// 标题
                String imageType = obj.getString("p_cat_name");// 分类
                String imageResource = obj.getString("pic");// 图片地址
                String synopsis = obj.getString("content");// 简介
                int type = obj.getInt("news_type");// 视频资源 0 或 图文资源 1

                values.put("imageId", imageId);
                values.put("postdate", postdate);
                values.put("categoryId", categoryId);
                values.put("imageName", imageName);
                values.put("imageType", imageType);
                values.put("imageResource", imageResource);
                values.put("synopsis", synopsis);
                values.put("type", type);
                imageList.add(values);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return imageList;
    }

    public static ArrayList<Image> getGalleryViewList(String jsonData, Context context)
    {
        // Log.d("JsonData", jsonData);
        ArrayList<Image> imageList = new ArrayList<Image>();
        try
        {
            JSONArray array = new JSONArray(jsonData);
            for (int i = 0; i < array.length(); i++)
            {
                Image values = new Image();

                JSONObject obj = array.getJSONObject(i);
                long imageId = obj.getLong("vid");
                int categoryId = obj.getInt("class_id");
                String imageName = obj.getString("subject");// 标题
                String imageType = obj.getString("p_cat_name");// 分类
                String imageResource = obj.getString("pic");// 图片地址
                String synopsis = obj.getString("content");// 简介
                // int type = obj.getInt("news_type");//视频资源 0 或 图文资源 1

                values.setImageId(imageId);
                values.setCategoryId(categoryId);
                values.setImageName(imageName);
                values.setImageType(imageType);
                values.setImageResource(imageResource);
                values.setSynopsis(synopsis);
                // values.setType(type);
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.default_gallery);
                values.setBitmap(bitmap);
                imageList.add(values);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return imageList;
    }

    public static ArrayList<ContentValues> getRemoteWebCalFromJSON(String jsonData)
    {
        ArrayList<ContentValues> webCalList = new ArrayList<ContentValues>();
        try
        {
            JSONArray array = new JSONArray(jsonData);
            for (int i = 0; i < array.length(); i++)
            {
                ContentValues values = new ContentValues();

                JSONObject obj = array.getJSONObject(i);
                long webCalId = obj.getLong("id");
                String webCalTitle = obj.getString("titleStr");// 标题
                String webCalContent = obj.getString("content");// 详细内容

                values.put("webCalId", webCalId);
                values.put("webCalTitle", webCalTitle);
                values.put("webCalContent", webCalContent);
                webCalList.add(values);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return webCalList;
    }

    public static List<Map<String, Object>> getRemoteWebCal1FromJSON(String jsonData)
    {
        ArrayList<Map<String, Object>> webCalList = new ArrayList<Map<String, Object>>();
        try
        {
            JSONArray array = new JSONArray(jsonData);
            for (int i = 0; i < array.length(); i++)
            {
                // ContentValues values = new ContentValues();
                Map<String, Object> values = new HashMap<String, Object>();

                JSONObject obj = array.getJSONObject(i);
                long webCalId = obj.getLong("id");
                String webCalTitle = obj.getString("titleStr");// 标题,时间,地点
                String webCalContent = obj.getString("content");// 详细内容
                String title = obj.getString("title");
                String startTime = obj.getString("startTime");
                String endTime = obj.getString("endTime");
                String webCalLocation = obj.getString("cal_location");
                String webPeople = obj.getString("participants");
                String webPeopleId = obj.getString("participant_ids");
                int isSelf = obj.getInt("is_self");
                int isAdmin = obj.getInt("is_admin");
                int important = obj.getInt("important");

                values.put("webCalId", webCalId);
                values.put("webCalTitle", webCalTitle);
                values.put("webCalContent", webCalContent);
                values.put("title", title);
                values.put("startTime", startTime);
                values.put("endTime", endTime);
                values.put("webCalLocation", webCalLocation);
                values.put("webPeople", webPeople);
                values.put("webPeopleId", webPeopleId);
                values.put("isSelf", isSelf);
                values.put("isAdmin", isAdmin);
                values.put("important", important);
                webCalList.add(values);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return webCalList;
    }

    public static List<Map<String, Object>> getRemoteWebCalPersonalWeekFromJSON(String jsonData)
    {
        ArrayList<Map<String, Object>> webCalList = new ArrayList<Map<String, Object>>();
        try
        {
            JSONArray array = new JSONArray(jsonData);
            for (int i = 0; i < array.length(); i++)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject obj = array.getJSONObject(i);
                String day = obj.getString("day_entry_date");
                String daych = obj.getString("day_entry_Week_CH");
                map.put("day", day);
                map.put("daych", daych);
                JSONArray titleArray = obj.getJSONArray("day_entry_title");
                ArrayList<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
                for (int j = 0; j < titleArray.length(); j++)
                {
                    Map<String, Object> values = new HashMap<String, Object>();
                    JSONObject obj1 = titleArray.getJSONObject(j);
                    String title = obj1.getString("titleStr");
                    values.put("title", title);
                    titleList.add(values);
                }
                map.put("titles", titleList);
                webCalList.add(map);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return webCalList;
    }

    public static List<Map<String, Object>> getRemoteWebCalTeamDayFromJSON(String jsonData)
    {
        ArrayList<Map<String, Object>> webCalList = new ArrayList<Map<String, Object>>();
        try
        {
            JSONArray array = new JSONArray(jsonData);
            for (int i = 0; i < array.length(); i++)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("name");
                String phone = obj.getString("cal_telephone");
                map.put("name", name);
                map.put("phone", phone);
                JSONArray titleArray = obj.getJSONArray("titleStr");
                ArrayList<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();
                for (int j = 0; j < titleArray.length(); j++)
                {
                    Map<String, Object> values = new HashMap<String, Object>();
                    JSONObject obj1 = titleArray.getJSONObject(j);
                    String title = obj1.getString("title");
                    values.put("title", title);
                    titleList.add(values);
                }
                map.put("titles", titleList);
                webCalList.add(map);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return webCalList;
    }

    public static List<Map<String, Object>> getRemoteWebCalTeamFromJSON(String jsonData)
    {
        ArrayList<Map<String, Object>> webCalList = new ArrayList<Map<String, Object>>();
        try
        {
            JSONArray array = new JSONArray(jsonData);
            for (int i = 0; i < array.length(); i++)
            {
                Map<String, Object> values = new HashMap<String, Object>();

                JSONObject obj = array.getJSONObject(i);
                long webCalViewId = obj.getLong("cal_view_id");
                String webCalName = obj.getString("cal_name");// 团队视图名称

                values.put("webCalViewId", webCalViewId);
                values.put("webCalName", webCalName);
                webCalList.add(values);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return webCalList;
    }

    // {"verdion":"12","sqliteversion":"1"}
//    public static Version getRemoteVersionFromJSON(String json)
//    {
//        Version ver = new Version();
//        try
//        {
//            JSONObject obj = new JSONObject(json);
//            int remoteVersion = obj.getInt("verdion");
//            ver.setRemoteVersion(remoteVersion);
//            return ver;
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//        return ver;
//    }

    public static List<Map<String, Object>> getParticipationPersonFromJson(String jsonData)
    {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try
        {
            JSONArray array = new JSONArray(jsonData);
            for (int i = 0; i < array.length(); i++)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject obj = array.getJSONObject(i);
                String orgName = obj.getString("orgname");
                int isGroupChecked = obj.getInt("is_group_checked");
                map.put("orgName", orgName);
                map.put("isGroupChecked", isGroupChecked);
                JSONArray users = obj.getJSONArray("users");
                ArrayList<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
                for (int j = 0; j < users.length(); j++)
                {
                    Map<String, Object> values = new HashMap<String, Object>();
                    JSONObject obj1 = users.getJSONObject(j);
                    String userName = obj1.getString("username");
                    String userId = obj1.getString("userid");
                    int isChecked = obj1.getInt("is_checked");
                    values.put("userName", userName);
                    values.put("userId", userId);
                    values.put("isChecked", isChecked);
                    userList.add(values);
                }
                map.put("users", userList);
                list.add(map);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public static Message getRemoteMessageFromJSON(String json)
    {
        // Log.d("MessageJson", json);
        Message mess = new Message();
        try
        {
            JSONObject obj = new JSONObject(json);
            int user_checkbox_4 = 0;
            int messageId = obj.getInt("id");
            int vid = obj.getInt("vid");
            int status = 0;
            String title = obj.getString("title");
            String content = obj.getString("content");
            String time = obj.getString("add_time");
            int type = obj.getInt("type");
            String phone = obj.getString("phone");
            int ru_id = obj.getInt("ru_id");
            int important = obj.getInt("important_flag");
            int urgency = obj.getInt("urgency_flag");
            String user_options_1 = obj.getString("user_options_1");
            String user_options_2 = obj.getString("user_options_2");
            String user_options_3 = obj.getString("user_options_3");
            if (obj.getString("user_checkbox_4") == null
                    || "".equals(obj.getString("user_checkbox_4")))
            {

            } else
            {
                user_checkbox_4 = obj.getInt("user_checkbox_4");
            }
            mess.setMessageId(messageId);
            mess.setVid(vid);
            mess.setStatus(status);
            mess.setTitle(title);
            mess.setContent(content);
            mess.setTime(time);
            mess.setType(type);
            mess.setPhone(phone);
            mess.setRu_id(ru_id);
            mess.setImportant(important);
            mess.setUrgency(urgency);
            mess.setUser_options_1(user_options_1);
            mess.setUser_options_2(user_options_2);
            mess.setUser_options_3(user_options_3);
            mess.setUser_checkbox_4(user_checkbox_4);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mess;
    }

    public static List<Message> getRemoteMessageListFromJSON(String jsons)
    {
        List<Message> msgs = new ArrayList<Message>();
        try
        {
            JSONArray jos = new JSONArray(jsons);
            for (int i = 0; i < jos.length(); i++)
            {
                JSONObject obj = jos.getJSONObject(i);
                Message mess = new Message();
                int user_checkbox_4 = 0;
                int messageId = obj.getInt("id");
                int vid = obj.getInt("vid");
                int status = 0;
                String title = obj.getString("title");
                String content = obj.getString("content");
                String time = obj.getString("add_time");
                int type = obj.getInt("type");
                String phone = obj.getString("phone");
                int ru_id = obj.getInt("ru_id");
                int important = obj.getInt("important_flag");
                int urgency = obj.getInt("urgency_flag");
                String user_options_1 = obj.getString("user_options_1");
                String user_options_2 = obj.getString("user_options_2");
                String user_options_3 = obj.getString("user_options_3");
                if (obj.getString("user_checkbox_4") == null
                        || "".equals(obj.getString("user_checkbox_4")))
                {

                } else
                {
                    user_checkbox_4 = obj.getInt("user_checkbox_4");
                }
                mess.setMessageId(messageId);
                mess.setVid(vid);
                mess.setStatus(status);
                mess.setTitle(title);
                mess.setContent(content);
                mess.setTime(time);
                mess.setType(type);
                mess.setPhone(phone);
                mess.setRu_id(ru_id);
                mess.setImportant(important);
                mess.setUrgency(urgency);
                mess.setUser_options_1(user_options_1);
                mess.setUser_options_2(user_options_2);
                mess.setUser_options_3(user_options_3);
                mess.setUser_checkbox_4(user_checkbox_4);
                msgs.add(mess);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return msgs;
    }
}