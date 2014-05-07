package com.sdhz.domain.blog;

import java.io.Serializable;

public class Attention implements Serializable
{ 
	private String blog_id;

	public String getBlog_id()
	{
		return blog_id;
	}

	public void setBlog_id(String blog_id)
	{
		this.blog_id = blog_id;
	}
}
