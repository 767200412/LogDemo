package comdemo.example.dell.logdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MessageVerification extends AppCompatActivity implements TextWatcher {

    private EditText editText1,editText2,editText3,editText4;
    private MyDialog2 myDialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_verification);
        editText1 = (EditText)findViewById(R.id.editText6);
        editText2 = (EditText)findViewById(R.id.editText7);
        editText3 = (EditText)findViewById(R.id.editText8);
        editText4 = (EditText)findViewById(R.id.editText9);
        editText1.addTextChangedListener(this);
        editText2.addTextChangedListener(this);
        editText3.addTextChangedListener(this);
        editText4.addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.toString().length() == 1){
            if(editText1.isFocused())
            {
                editText1.clearFocus();
                editText2.requestFocus();
            }
            else if(editText2.isFocused())
            {
                editText2.clearFocus();
                editText3.requestFocus();
            }

            else if(editText3.isFocused())
            {
                editText3.clearFocus();
                editText4.requestFocus();
            }
            else
            {
                editText4.clearFocus();
                //验证，跳转到下一个页面设置密码
                boolean ok  = true;
                if(ok)
                {
                    //跳转到设置密码
                    Intent intent = new Intent(MessageVerification.this,Password.class);
                    startActivity(intent);
                }
                else
                {
                       //错误提示框
                    SomeWrongDialog();
                }
            }
        }
    }



    private void SomeWrongDialog(){
        myDialog2=new MyDialog2(MessageVerification.this,R.style.MyDialog);
        //myDialog.setTitle("警告！");
        myDialog2.setMessage("验证码错误，请重新输入");
        myDialog2.setYesOnclickListener("确定", new MyDialog2.onYesOnclickListener() {
            @Override
            public void onYesOnclick() {
                myDialog2.dismiss();
            }
        });

        myDialog2.show();
    }
}
