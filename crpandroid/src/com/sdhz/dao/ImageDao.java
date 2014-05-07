package com.sdhz.dao;

import java.util.List;

import com.sdhz.domain.Image;

public interface ImageDao {
	/** 保存视频数据,返回 1.成功 0.失败 */
	public int addImageListData(String jsonData, String imageType);
	/** 按类型获取列表 */
	public List<Image> getImageList(String imageType);
    public List<Image> getImageList(String imageType, int limit);
	/** 按类型分页获取列表,limit显示的条数,offset,从第几条之后开始显示 */
	public List<Image> getImageList(String imgType, int limit, int offset);
	/** 搜索结果集 */
	public List<Image> searchImageList(String title);
//	/** 获取该类型最大最小的id */
//	public String getMaxId(String imageType);
//    public String getMinId(String imageType);
	/*** 获取最近一个 **/
	public Image getLatest(String imageType);
	
	public void deleteList(String cid);
	public void deleteList(String cid, int maxNum);
	public int insertList(List<Image> imageList);
	public int getCount(String cid);
	
	public boolean openDatabase();
	
	public void closeDB();
}
