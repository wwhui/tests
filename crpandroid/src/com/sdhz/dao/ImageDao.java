package com.sdhz.dao;

import java.util.List;

import com.sdhz.domain.Image;

public interface ImageDao {
	/** ������Ƶ����,���� 1.�ɹ� 0.ʧ�� */
	public int addImageListData(String jsonData, String imageType);
	/** �����ͻ�ȡ�б� */
	public List<Image> getImageList(String imageType);
    public List<Image> getImageList(String imageType, int limit);
	/** �����ͷ�ҳ��ȡ�б�,limit��ʾ������,offset,�ӵڼ���֮��ʼ��ʾ */
	public List<Image> getImageList(String imgType, int limit, int offset);
	/** ��������� */
	public List<Image> searchImageList(String title);
//	/** ��ȡ�����������С��id */
//	public String getMaxId(String imageType);
//    public String getMinId(String imageType);
	/*** ��ȡ���һ�� **/
	public Image getLatest(String imageType);
	
	public void deleteList(String cid);
	public void deleteList(String cid, int maxNum);
	public int insertList(List<Image> imageList);
	public int getCount(String cid);
	
	public boolean openDatabase();
	
	public void closeDB();
}
