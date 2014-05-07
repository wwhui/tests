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
	
	/** 获取推送信息列表 */
	public List<Map<String, String>> getMessageList();
	/** 保存推送信息 */
	public void addMessage(Message message);
	/** 删除一条信息 */
	public void deleteOneMessage(String messageId);
	/** 删除全部信息 根据类型和手机号,剩余30条*/
	public void deleteAllMessageByTypeLeftThirty(String typeId,String phone);
	/** 删除全部信息 根据类型和手机号*/
	public void deleteAllMessageByType(String typeId,String phone);
	
	public void update_message_send_num();
	/** 获取一条公告*/
	public Map<String, Object> getOneGongGao(String messageid);
	/** 添加回复内容*/
	public void updateReply(String messageId,String reply);
	
	
}
