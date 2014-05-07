package com.hzsoft.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

public class EditTextUtil {
	private EditText dia_et_pwd;
	
	public EditTextUtil(EditText dia_et_pwd) {
		super();
		this.dia_et_pwd = dia_et_pwd;
	}

	public void addString(String sequence) {
        int index = getEditSelection();// 光标的位置
        if (index < 0 || index >= getEditTextViewString().length()) {
            dia_et_pwd.append(sequence);
            //Log.i(TAG, "str===" + str);
        } else {
        	SpannableString spannableString=new SpannableString(sequence);
        	spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
					0, sequence.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            dia_et_pwd.getEditableText().insert(index, sequence);// 光标所在位置插入文字
        }
    }
 
    // 获取光标当前位置
    public int getEditSelection() {
        return dia_et_pwd.getSelectionStart();
    }
 
    // 获取文本框的内容
    public String getEditTextViewString() {
        return dia_et_pwd.getText().toString();
    }
 
    // 清除文本框中的内容
    public void clearText() {
        dia_et_pwd.getText().clear();
    }
 
    // 删除指定位置的字符
    public void deleteEditValue(int index) {
        dia_et_pwd.getText().delete(index - 1, index);
    }
 
    // 设置光标位置
    public void setEditSelectionLoc(int index) {
        dia_et_pwd.setSelection(index);
    }
}
