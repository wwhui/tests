package com.hzsoft.util;

public class Constants {
	public static int SOCKET_PORT = 6000;
	//HOST服务器IP
//public static String HOST = "10.41.141.145";
public static String HOST = "221.131.129.230";

//public static String HOST = "221.131.129.230";
	
	private static String HTTP = "http://"+HOST+":8080/crpInterface/";
	/**
	 * 获取工单的类型  用来更新数据库t_task_type
	 * */
	public static String  Task_Type=HTTP+"workTypeList.action";
	/**
	 * 获取工单,主要用在TaskMainListView.class 
	 * */
	public static String Task_List=HTTP+"workFlowList.action";
	/*根据工单号查询工单的路由*/
	public static String  Route_Point_List=HTTP+"flowRouteList.action";
	/**/
	public static String User_List=HTTP+"userList.action";
	//发起博文
	public static String NewBlog=HTTP+"newBlog.action";
	//发起新的评论
	public static String NewReply=HTTP+"newReply.action";
	/*根据路由查询所有的有关博文*/
	public static String  Route_Blog_List=HTTP+"routeBlogList.action";
	/*根据博文。查询回复*/
	public static String  Blog_Reply_List=HTTP+"blogReplyList.action";
	/*关注 博文*/
	public static String 	Blog_Attention_Add=HTTP+"addAttentionBlog.action";
	/*我关注 的博文*/
	public static String 	FIND_Blog_Attention=HTTP+"attentionBlogList.action";
	public static final String Blog_Detail = HTTP+"blogDetail.action";
	///流程工单内容
	public static final String Task_Detail = HTTP+"findTaskDetail.action";
	public static String ForgetPwd=HTTP+"forgetPwd.action";
	//获取版本JSON
	public final static String VERSION_URL = HTTP + "checkVersion.action";
	public final static String Search_List=HTTP+"queryAll.action";
	
	//下载路径
	 public final static String DOWN_URL_NEW = HTTP + "download/";
		//客户端登录地址
	public final static String LOGIN_URL = HTTP + "login.action";
	//重置密码
	public final static String Forget_Pwd=HTTP+"forgetPwd.action";
	//校验短信验证码
	public static final String CheckSMSCode = HTTP+"checkSMSCode.action";
	//重置密码
	public static final String ResertPwd = HTTP+"resertPwd.action";
	
	public static final String ImageURL=HTTP+"uploadimage/";
	
	
	
    public final static String IMAGE_URL = HTTP + "interface/interface/getNews.php";
	public final static String FORGET_URL =  HTTP + "interface/interface/forgetpwd.php?uid=";
	public final static String MODPASS_URL = HTTP + "interface/interface/setpwd.php?uid=accountStr&pwd=passwordStr&oldpwd=oldpasswordStr";
	
	/**
	 * 日程模块接口
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

	//查询博文是否关注
	public final static String FINDATTENTION=HTTP+"findAttention.action";
	//查询at相关的用户
	public final static String FINDATUSER=HTTP+"findAtUser.action";
	//增加聊天内容
	public final static String ADDTALK=HTTP+"addTalk.action";
	//查找聊天内容
	public final static String FINDTALK=HTTP+"findTalk.action";
	//查找群组
	public final static String GROUPINFO=HTTP+"findAllGroup.action";
	//查找群组
	public final static String GROUPMEMBER=HTTP+"findGroupMember.action";
//	我发起的博文
	public final static String MYCREATE=HTTP+"findMyCreate.action";
//	与我相关
	public final static String ATMYSELF=HTTP+"findAtMyself.action";
//	查找好友或群组
	public final static String FINDALLFRIENDORGROUP=HTTP+"findAllFriendOrGroup.action";
//	查找好友
	public final static String FINDALLFRIEND=HTTP+"findAllFriend.action";
//	添加
	public final static String ADDFRIEND=HTTP+"addFriend.action";
//	更新详细资料
	public final static String UPDATEDETAIL=HTTP+"updateDetail.action";
//	查询详细资料
	public final static String FINDDETAIL=HTTP+"findDetail.action";
//	意见反馈
	public final static String SUGGEST=HTTP +"addSuggest.action";

}
