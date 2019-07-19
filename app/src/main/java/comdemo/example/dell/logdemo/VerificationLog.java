package comdemo.example.dell.logdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class VerificationLog extends AppCompatActivity {
    private EditText mEditTextPassword;
    private Button mButtonLog;
    private ImageView mIvExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_log);
        mEditTextPassword = (EditText)findViewById(R.id.et_PhoneNumber);
        mButtonLog = (Button)findViewById(R.id.bt_log);
        mIvExit = (ImageView)findViewById(R.id.imageView4);


        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        String phone = intent.getStringExtra("phone");//从intent对象中获得数据
        mEditTextPassword.setText(phone);
        //限制按钮激活
        new SomeMonitorEditText().SetMonitorEditText(mButtonLog, mEditTextPassword);

        mButtonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到验证码界面
                Intent intent = new Intent(VerificationLog.this,MessageVerification.class);
                intent.putExtra("phone",mEditTextPassword.getText().toString());
                intent.putExtra("type","log");
                startActivity(intent);
            }
        });

        mIvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VerificationLog.this,MainActivity.class);
                intent.putExtra(MainActivity.TAG_EXIT, true);
                startActivity(intent);
            }
        });
    }
}
