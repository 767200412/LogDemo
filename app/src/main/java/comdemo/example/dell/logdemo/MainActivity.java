package comdemo.example.dell.logdemo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText mEditTextPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditTextPhoneNumber = (EditText)findViewById(R.id.et_PhoneNumber);
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
                    mEditTextPhoneNumber.setText(ssb);

                    //mEditTextPhoneNumber.setText(stringBuilder.toString());
                    mEditTextPhoneNumber.setTextColor(Color.BLACK);
                    mEditTextPhoneNumber.setSelection(index);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


}
