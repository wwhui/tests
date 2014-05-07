package com.sdhz.dao;

import java.util.List;
import java.util.Map;

import com.sdhz.domain.Message;

public interface MessageDao {
	public long addMsg(Message message);

	public List<Map<String, Object>> getMessages(String typeId,String phone);
	
	public List<Map<String, Object>> getCount(String phone);
	public List<Map<String, Object>> getNoticeCount(String phone,String typeId);
	
	public List<Map<String,Object >> getMessagesLimitFive(String phone,String typeId);
	
	public void updateStatus(String messageId);
	
	/** ��ȡ������Ϣ�б� */
	public List<Map<String, String>> getMessageList();
	/** ����������Ϣ */
	public void addMessage(Message message);
	/** ɾ��һ����Ϣ */
	public void deleteOneMessage(String messageId);
	/** ɾ��ȫ����Ϣ �������ͺ��ֻ���,ʣ��30��*/
	public void deleteAllMessageByTypeLeftThirty(String typeId,String phone);
	/** ɾ��ȫ����Ϣ �������ͺ��ֻ���*/
	public void deleteAllMessageByType(String typeId,String phone);
	
	public void update_message_send_num();
	/** ��ȡһ������*/
	public Map<String, Object> getOneGongGao(String messageid);
	/** ��ӻظ�����*/
	public void updateReply(String messageId,String reply);
	
	
}
