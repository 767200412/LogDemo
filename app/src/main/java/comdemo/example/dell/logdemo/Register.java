package comdemo.example.dell.logdemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Register extends AppCompatActivity {
    private TextView mTvUrl;
    private TextView mTvLog;
    private Button mBtnNext;
    private EditText mEtNumber;
    private ImageView mIvExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mTvUrl = (TextView)findViewById(R.id.tv_URL);
        mTvLog = (TextView)findViewById(R.id.textView10);
        mBtnNext = (Button)findViewById(R.id.bt_log);
        mEtNumber = (EditText)findViewById(R.id.et_PhoneNumber);
        mIvExit = (ImageView)findViewById(R.id.imageView4);

        //限制按钮激活
        new SomeMonitorEditText().SetMonitorEditText(mBtnNext, mEtNumber);

        //跳转到登录界面
        mTvLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //跳转到短信验证界面
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,MessageVerification.class);
                intent.putExtra("phone",mEtNumber.getText().toString());
                intent.putExtra("type","register");
                startActivity(intent);
            }
        });

        mIvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Register.this,MainActivity.class);
                intent.putExtra(MainActivity.TAG_EXIT, true);
                startActivity(intent);
            }
        });

        //设置《辅城用户注册协议》超链接
        SpannableStringBuilder ssb = new SpannableStringBuilder("注册即代表您同意《辅城用户注册协议》");
        ssb.setSpan(new URLSpan("https://github.com/CaMnter"), 8, mTvUrl.getText().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(0xff4694ff), 8, mTvUrl.getText().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mTvUrl.setText(ssb);
        // 在单击链接时凡是有要执行的动作，都必须设置MovementMethod对象
        mTvUrl.setMovementMethod(LinkMovementMethod.getInstance());
       // 设置点击后的颜色，这里涉及到ClickableSpan的点击背景
        mTvUrl.setHighlightColor(0xff8FABCC);

    }

}
