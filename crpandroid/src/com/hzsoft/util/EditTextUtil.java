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
        int index = getEditSelection();// ����λ��
        if (index < 0 || index >= getEditTextViewString().length()) {
            dia_et_pwd.append(sequence);
            //Log.i(TAG, "str===" + str);
        } else {
        	SpannableString spannableString=new SpannableString(sequence);
        	spannableString.setSpan(new ForegroundColorSpan(0xff0077ff),
					0, sequence.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            dia_et_pwd.getEditableText().insert(index, sequence);// �������λ�ò�������
        }
    }
 
    // ��ȡ��굱ǰλ��
    public int getEditSelection() {
        return dia_et_pwd.getSelectionStart();
    }
 
    // ��ȡ�ı��������
    public String getEditTextViewString() {
        return dia_et_pwd.getText().toString();
    }
 
    // ����ı����е�����
    public void clearText() {
        dia_et_pwd.getText().clear();
    }
 
    // ɾ��ָ��λ�õ��ַ�
    public void deleteEditValue(int index) {
        dia_et_pwd.getText().delete(index - 1, index);
    }
 
    // ���ù��λ��
    public void setEditSelectionLoc(int index) {
        dia_et_pwd.setSelection(index);
    }
}
