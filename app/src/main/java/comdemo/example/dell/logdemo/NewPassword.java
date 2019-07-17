package comdemo.example.dell.logdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class NewPassword extends AppCompatActivity {
    private Button mButtonLog;
    private ImageButton mImageBtn,mImageBtn2, mImageBtn3;
    private EditText mEditTextPassword,mEditTextPassword2;
    private MyDialog2 myDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        mEditTextPassword = (EditText)findViewById(R.id.et_PassWord);
        mEditTextPassword2 = (EditText)findViewById(R.id.et_PassWord2);
        mButtonLog = (Button)findViewById(R.id.bt_log);
        mImageBtn = (ImageButton)findViewById(R.id.imageButton);
        mImageBtn2 = (ImageButton)findViewById(R.id.imageButton_2);
        mImageBtn3 = (ImageButton)findViewById(R.id.imageButton2);

        //限制按钮激活
        new SomeMonitorEditText().SetMonitorEditText(mButtonLog, mEditTextPassword,mEditTextPassword2);


        //
        //设置输入框的提示字符hint
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString("请输入新密码，6位以上");
        SpannableString ss2 = new SpannableString("请再次确认密码");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(20,true);
        AbsoluteSizeSpan ass2 = new AbsoluteSizeSpan(20,true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(ass2, 0, ss2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        mEditTextPassword.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
        mEditTextPassword2.setHint(new SpannableString(ss2));
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

        //密码的显示/隐藏
        mImageBtn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //重新设置按下时的背景图片
                    //((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.android_btn_pressed));
                    mEditTextPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    //再修改为抬起时的正常图片
                    // ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.ic_little_eye_hide));
                    mEditTextPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return false;
            }
        });


        mImageBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mButtonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  String p1 = mEditTextPassword.getText().toString();
                  String p2 = mEditTextPassword2.getText().toString();

                  if(!p1.equals(p2))
                  {
                      //两次密码不一致，提示弹窗
                      wrong("两次密码输入不一致","确定");

                  }
                  else if(p1.length()<6)
                 {
                      //密码少于6位
                     wrong("密码少于6位","确定");
                 }
                 else if(!p1.equals("^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)(?![a-zA-z\\d]+$)(?![a-zA-z!@#$%^&*]+$)(?![\\d!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$"))
                {
                     //
                    wrong("仅支持数字、字母或符号","确定");
                }
                else
                  {
                      //跳转到首页
                  }
            }
        });
    }

    private void wrong(String message,String button){
        myDialog2=new MyDialog2(NewPassword.this,R.style.MyDialog);
        //myDialog.setTitle("警告！");
        myDialog2.setMessage(message);
        myDialog2.setYesOnclickListener(button, new MyDialog2.onYesOnclickListener() {
            @Override
            public void onYesOnclick() {
                myDialog2.dismiss();
            }
        });

        myDialog2.show();
    }
}
