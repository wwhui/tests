package com.sdhz.dao;

import com.sdhz.domain.Config;

public interface ConfigDao {

	/** 保存设置信息 */
	public long addConfig(Config config);
	
	/** 获取推送设置信息:1.推送设置,2.推送声音设置,3.推送震动设置 */
	public Config getConfigWithConfigType(String configType);
	
	/**
	 * 修改设置信息
	 * @param config 修改的对象
	 * @param configType 设置类型(1.推送设置)
	 * @return 修改的行数
	 */
	public int modConfig(Config config, String configType);
}
