package com.util;


import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.mungbill.R;

import java.lang.reflect.Type;

public class KeyBoardUtils {
    private final Keyboard k1;  //自定义键盘
    private KeyboardView keyboardView;
    private EditText editText;

    public interface OnEnsureListenner{
        public void onEnsure();
    }
    OnEnsureListenner onEnsureListenner;

    public void setOnEnsureListenner(OnEnsureListenner onEnsureListenner){
        this.onEnsureListenner = onEnsureListenner;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText){
        this.keyboardView=keyboardView;
        this.editText=editText;
        this.editText.setInputType(InputType.TYPE_NULL);    //取消弹出系统键盘
        k1=new Keyboard(this.editText.getContext(), R.xml.key);

        this.keyboardView.setKeyboard(k1);  //设置要显示的键盘
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(listener);    //设置键盘按钮被点击了的监听
    }

    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int i) {
        }

        @Override
        public void onRelease(int i) {
        }

        @Override
        public void onKey(int i, int[] ints) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            switch(i){
                case Keyboard.KEYCODE_DELETE:
                    if (editable!=null && editable.length()>0){
                        if (start>0){
                            editable.delete(start-1,start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL:
                    editable.clear();
                    break;
                case Keyboard.KEYCODE_DONE:
                    onEnsureListenner.onEnsure();
                    break;
                default:
                    editable.insert(start, Character.toString((char)i));

            }
        }

        @Override
        public void onText(CharSequence charSequence) {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeUp() {
        }
    };

    public void showKeyboard(){
        int visibility = keyboardView.getVisibility();
        if (visibility == View.INVISIBLE || visibility == View.GONE){
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard(){
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE || visibility == View.INVISIBLE){
            keyboardView.setVisibility(View.GONE);
        }
    }
}
