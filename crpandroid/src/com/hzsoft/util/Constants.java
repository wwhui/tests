package com.hzsoft.util;

public class Constants {
	public static int SOCKET_PORT = 6000;
	//HOST������IP
//public static String HOST = "10.41.141.145";
public static String HOST = "221.131.129.230";

//public static String HOST = "221.131.129.230";
	
	private static String HTTP = "http://"+HOST+":8080/crpInterface/";
	/**
	 * ��ȡ����������  �����������ݿ�t_task_type
	 * */
	public static String  Task_Type=HTTP+"workTypeList.action";
	/**
	 * ��ȡ����,��Ҫ����TaskMainListView.class 
	 * */
	public static String Task_List=HTTP+"workFlowList.action";
	/*���ݹ����Ų�ѯ������·��*/
	public static String  Route_Point_List=HTTP+"flowRouteList.action";
	/**/
	public static String User_List=HTTP+"userList.action";
	//������
	public static String NewBlog=HTTP+"newBlog.action";
	//�����µ�����
	public static String NewReply=HTTP+"newReply.action";
	/*����·�ɲ�ѯ���е��йز���*/
	public static String  Route_Blog_List=HTTP+"routeBlogList.action";
	/*���ݲ��ġ���ѯ�ظ�*/
	public static String  Blog_Reply_List=HTTP+"blogReplyList.action";
	/*��ע ����*/
	public static String 	Blog_Attention_Add=HTTP+"addAttentionBlog.action";
	/*�ҹ�ע �Ĳ���*/
	public static String 	FIND_Blog_Attention=HTTP+"attentionBlogList.action";
	public static final String Blog_Detail = HTTP+"blogDetail.action";
	///���̹�������
	public static final String Task_Detail = HTTP+"findTaskDetail.action";
	public static String ForgetPwd=HTTP+"forgetPwd.action";
	//��ȡ�汾JSON
	public final static String VERSION_URL = HTTP + "checkVersion.action";
	public final static String Search_List=HTTP+"queryAll.action";
	
	//����·��
	 public final static String DOWN_URL_NEW = HTTP + "download/";
		//�ͻ��˵�¼��ַ
	public final static String LOGIN_URL = HTTP + "login.action";
	//��������
	public final static String Forget_Pwd=HTTP+"forgetPwd.action";
	//У�������֤��
	public static final String CheckSMSCode = HTTP+"checkSMSCode.action";
	//��������
	public static final String ResertPwd = HTTP+"resertPwd.action";
	
	public static final String ImageURL=HTTP+"uploadimage/";
	
	
	
    public final static String IMAGE_URL = HTTP + "interface/interface/getNews.php";
	public final static String FORGET_URL =  HTTP + "interface/interface/forgetpwd.php?uid=";
	public final static String MODPASS_URL = HTTP + "interface/interface/setpwd.php?uid=accountStr&pwd=passwordStr&oldpwd=oldpasswordStr";
	
	/**
	 * �ճ�ģ��ӿ�
	 */
	public final static String PERSONALDAY_URL   = HTTP + "test_cal/webcal/interface/view_3_personal_day_temp.php?cal_date=currentDate&cal_phone=calPhone";
	public final static String PERSONALWEEK_URL  = HTTP + "test_cal/webcal/interface/view_4_personal_week_temp.php?cal_date=currentDate&cal_phone=calPhone";
	public final static String PERSONALTEAM_URL  = HTTP + "test_cal/webcal/interface/view_5_getTeamByPhone_temp.php?cal_phone=calPhone";
	public final static String TEAMDAY_URL       = HTTP + "test_cal/webcal/interface/view_6_team_day_temp.php?cal_view_id=viewId&cal_date=currentDate";
	public final static String WEBCALADD_URL     = HTTP + "test_cal/webcal/interface/wxty_7_add_personal_entry_temp.php";
	public final static String WEBCALDEL_URL     = HTTP + "test_cal/webcal/interface/wxty_8_del_personal_entry.php?cal_id=calId&cal_phone=calPhone&is_self=isSelf";
	public final static String WEBCALMOD_URL     = HTTP + "test_cal/webcal/interface/wxty_7_edit_personal_entry_temp.php";
	public final static String PARTICIPATION_URL = HTTP + "test_cal/webcal/interface/wxty_10_participation.php?cal_phone=calPhone&cal_id=calId";

	
	
	
	public final static String MY_BROADCAST = "com.hzsoft.action.MY_BROADCAST";
	public final static int BUFFER_SIZE = 400 * 1024;

	//��ѯ�����Ƿ��ע
	public final static String FINDATTENTION=HTTP+"findAttention.action";
	//��ѯat��ص��û�
	public final static String FINDATUSER=HTTP+"findAtUser.action";
	//������������
	public final static String ADDTALK=HTTP+"addTalk.action";
	//������������
	public final static String FINDTALK=HTTP+"findTalk.action";
	//����Ⱥ��
	public final static String GROUPINFO=HTTP+"findAllGroup.action";
	//����Ⱥ��
	public final static String GROUPMEMBER=HTTP+"findGroupMember.action";
//	�ҷ���Ĳ���
	public final static String MYCREATE=HTTP+"findMyCreate.action";
//	�������
	public final static String ATMYSELF=HTTP+"findAtMyself.action";
//	���Һ��ѻ�Ⱥ��
	public final static String FINDALLFRIENDORGROUP=HTTP+"findAllFriendOrGroup.action";
//	���Һ���
	public final static String FINDALLFRIEND=HTTP+"findAllFriend.action";
//	���
	public final static String ADDFRIEND=HTTP+"addFriend.action";
//	������ϸ����
	public final static String UPDATEDETAIL=HTTP+"updateDetail.action";
//	��ѯ��ϸ����
	public final static String FINDDETAIL=HTTP+"findDetail.action";
//	�������
	public final static String SUGGEST=HTTP +"addSuggest.action";

}
