package comdemo.example.dell.logdemo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private EditText mEditTextPhoneNumber;
    private EditText mEditTextPassword;
    private Button mButtonLog;
    private ImageButton mImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        mEditTextPhoneNumber = (EditText)findViewById(R.id.et_PhoneNumber);
        mEditTextPassword = (EditText)findViewById(R.id.et_PassWord);
        mButtonLog = (Button)findViewById(R.id.bt_log);
        mImageBtn = (ImageButton)findViewById(R.id.imageButton);
        mButtonLog.setEnabled(true);

        //设置输入框的提示字符hint
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString("请输入手机号");
        SpannableString ss2 = new SpannableString("请输入密码");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(20,true);
        AbsoluteSizeSpan ass2 = new AbsoluteSizeSpan(20,true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(ass2, 0, ss2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        mEditTextPhoneNumber.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
        mEditTextPassword.setHint(new SpannableString(ss2));

        //关键部分:自动分隔手机号码通过addTextChangedListener()实现
        mEditTextPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0)
                    return;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        stringBuilder.append(s.charAt(i));
                        if ((stringBuilder.length() == 4 || stringBuilder.length() == 9)
                                && stringBuilder.charAt(stringBuilder.length() - 1) != ' ') {
                            stringBuilder.insert(stringBuilder.length() - 1, ' ');
                        }
                    }
                }
                if (!stringBuilder.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (stringBuilder.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }

                    //设置粗体
                    SpannableStringBuilder ssb = new SpannableStringBuilder(stringBuilder.toString());
                    ssb.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, stringBuilder.toString().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    //ssb.setSpan(new ForegroundColorSpan(Color.BLACK), 0, stringBuilder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mEditTextPhoneNumber.setText(new SpannableString(ssb));

                    //mEditTextPhoneNumber.setText(stringBuilder.toString());
                    mEditTextPhoneNumber.setTextColor(Color.BLACK);
                    mEditTextPhoneNumber.setSelection(index);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //密码的显示/隐藏
        mImageBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //重新设置按下时的背景图片
                    //((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.android_btn_pressed));
                    mEditTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    //再修改为抬起时的正常图片
                    // ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.ic_little_eye_hide));
                    mEditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return false;
            }
        });

        //实现按钮的激活/未激活状态
        String e1 = null;
        String e2 = null;
        e1 = mEditTextPhoneNumber.getText().toString();
        e2 = mEditTextPassword.getText().toString();
        if(e1!= null && e2!=null){
            mButtonLog.setEnabled(true);
        }



    }

}
