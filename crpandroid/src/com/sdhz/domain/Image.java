package com.sdhz.domain;

import java.io.Serializable;

import android.graphics.Bitmap;
/** ��Ƶ��Դ,ͼ����Դ */
public class Image implements Serializable 
{
    private static final long serialVersionUID = -2468485308398586389L;
    
    private long vid;
	private String postdate;
	private int class_id;//����ID
	private String subject;//��Ƶ����
	private String p_cat_name;//��Ƶ����
	private String pic;//ͼƬ��ַ
	private String content;//���
	private Bitmap bitmap;
	private int news_type;//��Ƶ��Դ  0  ��  ͼ����Դ  1 

	public long getImageId() {
		return vid;
	}
	public void setImageId(long imageId) {
		this.vid = imageId;
	}
	public int getCategoryId() {
		return class_id;
	}
	public void setCategoryId(int categoryId) {
		this.class_id = categoryId;
	}
	public String getImageName() {
		return subject;
	}
	public void setImageName(String imageName) {
		this.subject = imageName;
	}
	public String getImageType() {
		return p_cat_name;
	}
	public void setImageType(String imageType) {
		this.p_cat_name = imageType;
	}
	public String getImageResource() {
		return pic;
	}
	public void setImageResource(String imageResource) {
		this.pic = imageResource;
	}
	public String getSynopsis() {
		return content;
	}
	public void setSynopsis(String synopsis) {
		this.content = synopsis;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public int getType() {
		return news_type;
	}
	public void setType(int type) {
		this.news_type = type;
	}
    public String getPostdate()
    {
        return postdate;
    }
    public void setPostdate(String postdate)
    {
        this.postdate = postdate;
    }
}
