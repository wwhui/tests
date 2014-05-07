package com.hzsoft.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.sdhz.domain.group.UserInfo;

public class ContentUtil {
	/**
	 * ��text��@ĳ�ˡ�#ĳ���⡢http://��ַ�����������ƥ��ı��������Ա�����ʾ
	 * 
	 * @param text
	 * @param context
	 * @return
	 */
	public static SpannableString formatContent(CharSequence text,
			final Context context, List<UserInfo> userList) {
		SpannableString spannableString = new SpannableString(text);
		/*
		 * @[^\\s:��]+[:��\\s] ƥ��@ĳ�� #([^\\#|.]+)# ƥ��#ĳ���� http://t\\.cn/\\w+ ƥ����ַ
		 * @[^\\s:��]+[:��\\s]|#([^\\#|.]+)#|http://t\\.cn/\\w
		 */
		Pattern pattern = Pattern
				.compile("@[^\\s]+[\\s]");
		Matcher matcher = pattern.matcher(spannableString);
		final Context mcontext = context;
		while (matcher.find()) {
			final String match = matcher.group();
			if (match.startsWith("@")) { // @ĳ�ˣ���������
				spannableString.setSpan(new ClickableSpan() {
					// ��onClick�����п��Ա�д��������ʱҪִ�еĶ���
					@Override
					public void onClick(View widget) {
						String username = match;
						username = username.replace("@", "");
						//username = username.replace(":", "");
						username = username.trim();
					
//						for(  UserInfo user:userList){
//							if(username.equals(user.getName())){
//							//	Intent intent = new Intent(mcontext, XXX.class);
//							//	mcontext.startActivity(intent);// ��ת���û���Ϣ����
//								Toast.makeText(context, "sss", Toast.LENGTH_SHORT).show();
//								 break ;
//							}
//						}
						Toast.makeText(context, "sss", Toast.LENGTH_SHORT).show();
					}
				}, matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (match.startsWith("#")) { // #ĳ����
				spannableString.setSpan(new ClickableSpan() {
					// ��onClick�����п��Ա�д��������ʱҪִ�еĶ���
					@Override
					public void onClick(View widget) {
						String theme = match;
						theme = theme.replace("#", "");
						theme = theme.trim();
//						ConstantsUtil.clickName = theme;
//						Intent intent = new Intent(mcontext, XXX.class);
//						mcontext.startActivity(intent);// ��ת��������Ϣ����
					}
				}, matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (match.startsWith("http://")) { // ƥ����ַ
				spannableString.setSpan(new ClickableSpan() {
					// ��onClick�����п��Ա�д��������ʱҪִ�еĶ���
					@Override
					public void onClick(View widget) {
						Uri uri = Uri.parse(match);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						mcontext.startActivity(intent);
					}
				}, matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannableString;
	}

	public static SpannableString formatContentNoClick(CharSequence text) {
		SpannableString spannableString = new SpannableString(text);
		/*
		 * @[^\\s:��]+[:��\\s] ƥ��@ĳ�� #([^\\#|.]+)# ƥ��#ĳ���� http://t\\.cn/\\w+ ƥ����ַ
		 */
//		Pattern pattern = Pattern
//				.compile("@[^\\s:��]+[:��\\s]|#([^\\#|.]+)#|http://t\\.cn/\\w");
		Pattern pattern = Pattern
				.compile("@[^\\s]+[\\s]");
		Matcher matcher = pattern.matcher(spannableString);
		while (matcher.find()) {
			final String match = matcher.group();
			if (match.startsWith("@")) { // @ĳ�ˣ���������
				spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (match.startsWith("#")) { // #ĳ����
				spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (match.startsWith("http://")) { // ƥ����ַ
				spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannableString;
	}

	public static long calculateWeiboLength(CharSequence c) {
		double len = 0;
		for (int i = 0; i < c.length(); i++) {
			int temp = (int) c.charAt(i);
			if (temp > 0 && temp < 127) {
				len += 0.5;
			} else {
				len++;
			}
		}
		return Math.round(len);
	}
}
