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
	 * 将text中@某人、#某主题、http://网址的字体加亮，匹配的表情文字以表情显示
	 * 
	 * @param text
	 * @param context
	 * @return
	 */
	public static SpannableString formatContent(CharSequence text,
			final Context context, List<UserInfo> userList) {
		SpannableString spannableString = new SpannableString(text);
		/*
		 * @[^\\s:：]+[:：\\s] 匹配@某人 #([^\\#|.]+)# 匹配#某主题 http://t\\.cn/\\w+ 匹配网址
		 * @[^\\s:：]+[:：\\s]|#([^\\#|.]+)#|http://t\\.cn/\\w
		 */
		Pattern pattern = Pattern
				.compile("@[^\\s]+[\\s]");
		Matcher matcher = pattern.matcher(spannableString);
		final Context mcontext = context;
		while (matcher.find()) {
			final String match = matcher.group();
			if (match.startsWith("@")) { // @某人，加亮字体
				spannableString.setSpan(new ClickableSpan() {
					// 在onClick方法中可以编写单击链接时要执行的动作
					@Override
					public void onClick(View widget) {
						String username = match;
						username = username.replace("@", "");
						//username = username.replace(":", "");
						username = username.trim();
					
//						for(  UserInfo user:userList){
//							if(username.equals(user.getName())){
//							//	Intent intent = new Intent(mcontext, XXX.class);
//							//	mcontext.startActivity(intent);// 跳转到用户信息界面
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
			} else if (match.startsWith("#")) { // #某主题
				spannableString.setSpan(new ClickableSpan() {
					// 在onClick方法中可以编写单击链接时要执行的动作
					@Override
					public void onClick(View widget) {
						String theme = match;
						theme = theme.replace("#", "");
						theme = theme.trim();
//						ConstantsUtil.clickName = theme;
//						Intent intent = new Intent(mcontext, XXX.class);
//						mcontext.startActivity(intent);// 跳转到话题信息界面
					}
				}, matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (match.startsWith("http://")) { // 匹配网址
				spannableString.setSpan(new ClickableSpan() {
					// 在onClick方法中可以编写单击链接时要执行的动作
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
		 * @[^\\s:：]+[:：\\s] 匹配@某人 #([^\\#|.]+)# 匹配#某主题 http://t\\.cn/\\w+ 匹配网址
		 */
//		Pattern pattern = Pattern
//				.compile("@[^\\s:：]+[:：\\s]|#([^\\#|.]+)#|http://t\\.cn/\\w");
		Pattern pattern = Pattern
				.compile("@[^\\s]+[\\s]");
		Matcher matcher = pattern.matcher(spannableString);
		while (matcher.find()) {
			final String match = matcher.group();
			if (match.startsWith("@")) { // @某人，加亮字体
				spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (match.startsWith("#")) { // #某主题
				spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (match.startsWith("http://")) { // 匹配网址
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
