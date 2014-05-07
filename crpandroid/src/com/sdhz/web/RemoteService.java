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
	 * @param account �ʺ�(�ֻ���)
	 * @param currentDate ָ������
	 * @return ָ�����ڵ���ͼ
	 */
	public String getWebCalPersonalDayData(String account, String currentDate, String req_phone) {
		String url = Constants.PERSONALDAY_URL.replace("currentDate", currentDate).replace("calPhone", account);
		if (null!=req_phone && !"".equals(req_phone)) {
			url = url + "&req_phone=" + req_phone;
		}
		return HttpUtils.getServerData(url);
	}
	
	/**
	 * @param account �ʺ�(�ֻ���)
	 * @return
	 */
	public String getWebCalPersonalWeekData(String account, String currentDate) {
		String url = Constants.PERSONALWEEK_URL.replace("currentDate", currentDate).replace("calPhone", account);
		return HttpUtils.getServerData(url);
	}
	
	/**
	 * �����û��ֻ����룬��ȡ�û����Ŷ�
	 * ���� cal_phone  ����
	 * ���� [{"cal_view_id":"3","cal_name":"����444"}] 
	 * @return
	 */
	public String getWebCalTeamData(String account) {
		return HttpUtils.getServerData(Constants.PERSONALTEAM_URL.replace("calPhone", account));
	}
	
	/**
	 * �����Ŷ���ͼID�����ڣ���ȡ��ǰ�Ŷ���ͼ�ڸ����������Ա��ͼ
	 * ���� cal_view_id ���� , cal_date ��ʽYYYYMMDD Ĭ��Ϊ���졣
	 * ����  [{"name":"����111","titleStr":[{"title":"09:00-12:00  ����222"},{"title":"  ����333"}]}]
	 * @return
	 */
	public String getWebCalTeamDayData(String viewId, String currentDate) {
		return HttpUtils.getServerData(Constants.TEAMDAY_URL.replace("currentDate", currentDate).replace("viewId", viewId));
	}
	
	/**
	 * �����ճ̣� ������ָ���ճ̣�Ĭ�ϵ���
	 * ����cal_phone �û��ֻ�����
	 * is_whole_day_entry ����ʱ����¼�  0��ʾ��ʱ�䣬1��ʱ��
	 * ����ʱ���¼���ʱ������ɲ�����
	 * p_cal_date       ָ�����ڣ�Ĭ��Ϊ����
	 * p_cal_hour       ָ����ʼʱ�䣨Сʱ��
	 * p_cal_minute     ָ����ʼʱ�䣨���ӣ�
	 * p_cal_hour_end   ָ������ʱ�䣨Сʱ�� Լ�� p_cal_hour_end >  p_cal_hour
	 * p_cal_minute_end ָ������ʱ�䣨���ӣ�
	 * cal_name ���� 
	 * cal_description ���ݣ�û��д������Ĭ�Ϻͱ���һ�£�
	 * cal_location �ص�
	 * ���� �ɹ���success, ʧ��: fail
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
	 * ɾ���ճ̣��û�ɾ�������ճ�
	 * ����cal_id �¼�ID  ����
	 * cal_phone �û��ֻ�����   ����
	 * @param calId
	 * @param account
	 * @return
	 */
	public String delWebCal(String calId, String account, String isSelf) {
		return HttpUtils.getServerData(Constants.WEBCALDEL_URL.replace("calId", calId).replace("calPhone", account).replace("isSelf", isSelf));
	}

	/**
	 * �޸�/�����ճ���ѡ�������
	 * @return
	 */
	public String getParticipationPerson(String calPhone, String calId) {
		return HttpUtils.getServerData(Constants.PARTICIPATION_URL.replace("calPhone", calPhone).replace("calId", calId));
	}
}
