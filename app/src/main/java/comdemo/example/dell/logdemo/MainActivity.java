package comdemo.example.dell.logdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.TimeUnit;

import comdemo.example.dell.logdemo.Beans.ResponseMessage;
import comdemo.example.dell.logdemo.Beans.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    public static final String TAG_EXIT = "exit";//退出程序标识

    private EditText mEditTextPhoneNumber;
    private EditText mEditTextPassword;
    private Button mButtonLog;
    private ImageButton mImageBtn,mImageBtn2;
    private MyDialog myDialog;
    private MyDialog2 myDialog2;
    private TextView mTvReg,mTvLog,mTvForget;
    private int wrongNumber =0;
    private String phoneNumber,passWord;
    private String url = "http://devapi.fccn.cc/Api/v1.1/Account/Login";
    private String TAG = "Success";
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        mEditTextPhoneNumber = (EditText)findViewById(R.id.et_PhoneNumber);
        mEditTextPassword = (EditText)findViewById(R.id.et_PassWord);
        mButtonLog = (Button)findViewById(R.id.bt_log);
        mImageBtn = (ImageButton)findViewById(R.id.imageButton);
        mImageBtn2 = (ImageButton)findViewById(R.id.imageView4);
        mTvReg = (TextView)findViewById(R.id.textView11);
        mTvLog = (TextView)findViewById(R.id.textView7);
        mTvForget = (TextView)findViewById(R.id.textView5);

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

        //限制按钮激活
        new SomeMonitorEditText().SetMonitorEditText(mButtonLog, mEditTextPassword,mEditTextPhoneNumber);

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
                    ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, stringBuilder.toString().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
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






        //点击登录
        mButtonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneNumber = mEditTextPhoneNumber.getText().toString().replaceAll(" ","");
                passWord = mEditTextPassword.getText().toString();
                Log.d("phoneNumber",phoneNumber);
                Log.d("passWord",passWord);

                login(phoneNumber,passWord);
                //Log.d("flag",String.valueOf(flag));

            }
        });

        //点击注册
       mTvReg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this,Register.class);
               startActivity(intent);
           }
       });

       //点击忘记密码
        mTvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FindPassword.class);
                intent.putExtra("phone",mEditTextPhoneNumber.getText().toString());
                startActivity(intent);
            }
        });

        //点击验证码登录
        mTvLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,VerificationLog.class);
                intent.putExtra("phone",mEditTextPhoneNumber.getText().toString());
                startActivity(intent);
            }
        });

        //退出程序
        mImageBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                intent.putExtra(MainActivity.TAG_EXIT, true);
                startActivity(intent);
            }
        });

    }



    private void SomeWrongDialog(){
        myDialog2=new MyDialog2(MainActivity.this,R.style.MyDialog);
        //myDialog.setTitle("警告！");
        myDialog2.setMessage("账号或者密码错误，请重新输入");
        myDialog2.setYesOnclickListener("确定", new MyDialog2.onYesOnclickListener() {
            @Override
            public void onYesOnclick() {
                myDialog2.dismiss();
            }
        });

        myDialog2.show();
    }

    private void FindPassWordDialog(){
        myDialog=new MyDialog(MainActivity.this,R.style.MyDialog);
        //myDialog.setTitle("警告！");
        myDialog.setMessage("密码错误，找回密码？");
        myDialog.setYesOnclickListener("找回密码", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesOnclick() {
                myDialog.dismiss();
                //前往密码忘记页面 带入手机值
                Intent intent = new Intent(MainActivity.this,FindPassword.class);
                intent.putExtra("phone",mEditTextPhoneNumber.getText().toString());
                startActivity(intent);
            }
        });
        myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                myDialog.dismiss();
                //返回
            }
        });
        myDialog.show();
    }


    //退出程序
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
            }
        }
    }


    //登录login
    /*
      返回的值 -1 正常
              -2  连接失败
              1-5 密码错误的次数
              -3 其他错误
     */
    private void login(String phoneNumber,String passWord){

        //输入的实体类
        User user = new User();
        user.setAccount(phoneNumber);
        user.setPassword(passWord);
        Log.e("account",user.getAccount());
        Log.e("passWord",user.getPassword());

        //使用Gson 添加 依赖 compile 'com.google.code.gson:gson:2.8.1'
        Gson gson = new Gson();
        //使用Gson将对象转换为json字符串
        String json = gson.toJson(user);

        //创建okHttpClient对象
        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        //设置request
        Request request = new Request.Builder()
                .url(url)//请求的url
                .post(requestBody)
                .build();

        //创建Call
        final Call call = okHttpClient.newCall(request);
        //同步操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    Response response = call.execute();
                    String result = response.body().string();
                    Log.d("Response",result);

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    ResponseMessage responseMessage = gson.fromJson(result, new TypeToken<ResponseMessage>() {
                    }.getType());


                    if(response.code() == 400){
                        //返回400 请求错误
                        Log.e("Status Code:400","请求错误");
                        // 读取错误信息
                        if(responseMessage.getCanFailedCount() != null) {
                            Log.d("CanFailedCount()", responseMessage.getCanFailedCount());
                            flag = Integer.parseInt(responseMessage.getCanFailedCount());
                        }

                    }
                    else if(response.code() == 200) {
                        //正确 跳转到主页并保存数据



                        //保存数据到SharedPreference
                        //步骤1：创建一个SharedPreferences对象
                        SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
                        //步骤2： 实例化SharedPreferences.Editor对象
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //步骤3：将获取过来的值放入文件
                        editor.putString("ResponseMessage",result);
                        //步骤4：提交
                        editor.commit();
                        flag = -1;


                    }
                    else {
                        //其他错误
                        flag = -3;
                    }

                }catch ( Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


        if(flag == -1)
        {
            Log.d("log","success");
        }
        else if(flag >=0 && flag <5 )
        {
            SomeWrongDialog();
        }
        else if(flag == 5)
        {
            //错误5次 找回密码提示框
            FindPassWordDialog();
        }
        else {
            //其他错误
        }
        /*
        //加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                flag[0] = -2;
                System.out.println("连接失败");
                Log.e("WRONG","连接失败");

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();

                Gson gson = new GsonBuilder().serializeNulls().create();
                ResponseMessage responseMessage = gson.fromJson(result, new TypeToken<ResponseMessage>() {
                }.getType());
                Log.d("result",result);

                if(response.code() == 400){
                    //返回400 请求错误
                    Log.e("Status Code:400","请求错误");
                    // 读取错误信息
                    if(responseMessage.getCanFailedCount() != null) {
                        Log.d("CanFailedCount()", responseMessage.getCanFailedCount());
                        flag[0] = Integer.parseInt(responseMessage.getCanFailedCount());
                    }

                }
                else if(response.code() == 200) {
                    //正确 跳转到主页并保存数据



                    //保存数据到SharedPreference
                    //步骤1：创建一个SharedPreferences对象
                    SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
                    //步骤2： 实例化SharedPreferences.Editor对象
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //步骤3：将获取过来的值放入文件
                    editor.putString("ResponseMessage",result);
                    //步骤4：提交
                    editor.commit();
                    flag[0] = -1;

                }
                else {
                    //其他错误
                    flag[0] = -3;
                }
            }
        });
        */

    }
}

