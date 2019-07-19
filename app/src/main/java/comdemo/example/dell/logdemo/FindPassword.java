package comdemo.example.dell.logdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class FindPassword extends AppCompatActivity {



    private EditText mEditPhoneNumber;
    private Button mButtonLog;
    private ImageView mIvExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        mEditPhoneNumber = (EditText)findViewById(R.id.et_PhoneNumber);
        mButtonLog = (Button)findViewById(R.id.bt_log);
        mIvExit = (ImageView)findViewById(R.id.imageView4);

        Intent intent = getIntent();//声明一个对象，并获得跳转过来的Intent对象
        String phone = intent.getStringExtra("phone");//从intent对象中获得数据
        mEditPhoneNumber.setText(phone);

        if(mEditPhoneNumber.getText().toString().equals("")) {
            //限制按钮激活
            new SomeMonitorEditText().SetMonitorEditText(mButtonLog, mEditPhoneNumber);
        }
        else{
            mButtonLog.setEnabled(true);
            mButtonLog.setBackgroundResource(R.drawable.btn_shape_normal);
        }
        mButtonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //前往验证码界面
                Intent intent = new Intent(FindPassword.this,MessageVerification.class);
                intent.putExtra("phone",mEditPhoneNumber.getText().toString());
                intent.putExtra("type","find");
                startActivity(intent);
            }
        });

        mIvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FindPassword.this,MainActivity.class);
                intent.putExtra(MainActivity.TAG_EXIT, true);
                startActivity(intent);
            }
        });
    }
}
