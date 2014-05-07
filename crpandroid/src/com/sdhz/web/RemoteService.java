package com.sdhz.web;

import java.util.Map;
import java.util.Random;

import android.graphics.Bitmap;

import com.hzsoft.util.Base64;
import com.hzsoft.util.Constants;
import com.hzsoft.util.HttpUtils;

public class RemoteService {
	private static RemoteService instance = null;
	
	synchronized public static RemoteService getInstance() {
		if(instance == null) {
			instance = new RemoteService();
		}
		return instance;
	}
	
	public String login(String account, String password) {
		Random random = new Random();
		String ran = "";
		for(int j=0; j<4; j++) {
			Integer s = random.nextInt(9);
			ran += s.toString();
		}
		String passRan = password+ran;
		String base64pass = Base64.encode(passRan.getBytes());
		String url = Constants.LOGIN_URL.replace("account", account).replace("password", base64pass);
		String result = HttpUtils.getServerData(url);
		return result;
	}
	
	public String getVersion() {
		return HttpUtils.getServerData(Constants.VERSION_URL);
	}
	
	public String getImageData(String imageType,String maxId) {
//		return HttpUtils.getServerData(Constants.IMAGE_URL.replace("imageType", imageType).replace("maxIdStr", maxId));
        return HttpUtils.getServerData(Constants.IMAGE_URL + 
                "?cid=" + imageType +
                "&vid=" + maxId);
	}

    public String getLatestImageData(String cid, long vid, int limit) {
        return HttpUtils.getServerData(Constants.IMAGE_URL + 
                "?cid=" + cid +
                "&comp=gt" +
                "&vid=" + vid +
                "&num=" + limit
                );
    }

    public String getNextImageData(String cid, long vid, int limit) {
        return HttpUtils.getServerData(Constants.IMAGE_URL + 
                "?cid=" + cid +
                "&comp=lt" + 
                "&vid=" + vid +
                "&num=" + limit
                );
    }

	public Bitmap getRmoteImage(String imageUrl) {
		return HttpUtils.loadRmoteImage(imageUrl);
	}
	
	public String getPasswordSMS(String account) {
		return HttpUtils.getServerData(Constants.FORGET_URL + account);
	}
	
	public String modPassword(String account, String oldpassword, String password) {
		Random random = new Random();
		String ran = "";
		for(int j=0;j<4;j++) {
			Integer s = random.nextInt(9);
			ran += s.toString();
		}
		String oldpassRan = oldpassword+ran;
		String base64oldpass = Base64.encode(oldpassRan.getBytes());
		String passRan = password+ran;
		String base64pass = Base64.encode(passRan.getBytes());
		String url = Constants.MODPASS_URL.replace("accountStr", account).replace("oldpasswordStr", base64oldpass).replace("passwordStr", base64pass);
		return HttpUtils.getServerData(url);
	}
	
	/**
	 * @param account 帐号(手机号)
	 * @param currentDate 指定日期
	 * @return 指定日期的视图
	 */
	public String getWebCalPersonalDayData(String account, String currentDate, String req_phone) {
		String url = Constants.PERSONALDAY_URL.replace("currentDate", currentDate).replace("calPhone", account);
		if (null!=req_phone && !"".equals(req_phone)) {
			url = url + "&req_phone=" + req_phone;
		}
		return HttpUtils.getServerData(url);
	}
	
	/**
	 * @param account 帐号(手机号)
	 * @return
	 */
	public String getWebCalPersonalWeekData(String account, String currentDate) {
		String url = Constants.PERSONALWEEK_URL.replace("currentDate", currentDate).replace("calPhone", account);
		return HttpUtils.getServerData(url);
	}
	
	/**
	 * 根据用户手机号码，获取用户的团队
	 * 参数 cal_phone  必填
	 * 返回 [{"cal_view_id":"3","cal_name":"测试444"}] 
	 * @return
	 */
	public String getWebCalTeamData(String account) {
		return HttpUtils.getServerData(Constants.PERSONALTEAM_URL.replace("calPhone", account));
	}
	
	/**
	 * 根据团队视图ID和日期，获取当前团队视图在该天的所有人员视图
	 * 参数 cal_view_id 必填 , cal_date 格式YYYYMMDD 默认为当天。
	 * 返回  [{"name":"测试111","titleStr":[{"title":"09:00-12:00  测试222"},{"title":"  测试333"}]}]
	 * @return
	 */
	public String getWebCalTeamDayData(String viewId, String currentDate) {
		return HttpUtils.getServerData(Constants.TEAMDAY_URL.replace("currentDate", currentDate).replace("viewId", viewId));
	}
	
	/**
	 * 新增日程： 可新增指定日程，默认当天
	 * 参数cal_phone 用户手机号码
	 * is_whole_day_entry 有无时间的事件  0表示无时间，1有时间
	 * （无时间事件，时间参数可不传）
	 * p_cal_date       指定日期，默认为当天
	 * p_cal_hour       指定开始时间（小时）
	 * p_cal_minute     指定开始时间（分钟）
	 * p_cal_hour_end   指定结束时间（小时） 约束 p_cal_hour_end >  p_cal_hour
	 * p_cal_minute_end 指定结束时间（分钟）
	 * cal_name 标题 
	 * cal_description 内容（没填写内容是默认和标题一致）
	 * cal_location 地点
	 * 返回 成功：success, 失败: fail
	 * @param rawParams
	 * @return
	 */
	public String saveWebCalPersonalDayData(Map<String, String> rawParams) {
		return HttpUtils.postRequest(Constants.WEBCALADD_URL, rawParams);
	}
	
	public String modWebCalPersonalDayData(Map<String, String> rawParams) {
		return HttpUtils.postRequest(Constants.WEBCALMOD_URL, rawParams);
	}
	
	/**
	 * 删除日程：用户删除本人日程
	 * 参数cal_id 事件ID  必填
	 * cal_phone 用户手机号码   必填
	 * @param calId
	 * @param account
	 * @return
	 */
	public String delWebCal(String calId, String account, String isSelf) {
		return HttpUtils.getServerData(Constants.WEBCALDEL_URL.replace("calId", calId).replace("calPhone", account).replace("isSelf", isSelf));
	}

	/**
	 * 修改/创建日程中选择参与人
	 * @return
	 */
	public String getParticipationPerson(String calPhone, String calId) {
		return HttpUtils.getServerData(Constants.PARTICIPATION_URL.replace("calPhone", calPhone).replace("calId", calId));
	}
}
