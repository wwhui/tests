package com.sdhz.dao;

import com.sdhz.domain.Config;

public interface ConfigDao {

	/** ����������Ϣ */
	public long addConfig(Config config);
	
	/** ��ȡ����������Ϣ:1.��������,2.������������,3.���������� */
	public Config getConfigWithConfigType(String configType);
	
	/**
	 * �޸�������Ϣ
	 * @param config �޸ĵĶ���
	 * @param configType ��������(1.��������)
	 * @return �޸ĵ�����
	 */
	public int modConfig(Config config, String configType);
}
