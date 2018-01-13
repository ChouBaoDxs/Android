package com.choubao.www.softwareengineeringproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.choubao.www.softwareengineeringproject.utils.Constant;
import com.choubao.www.softwareengineeringproject.vo.User;

import java.lang.reflect.Field;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText et_username, et_password;
    private Button btn_login;
    private TextView tv_forget_password, tv_register, tv_No_register_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_layout);
        //Bmob初始化
        Bmob.initialize(this, "35669d0369968e36b36ac00f814a8da8");

        app = (AssembleAComputerApplication) getApplication();
        int loginState = app.sp.getInt(Constant.LOGIN_STATE, 3);//缺省值为false

        //初始化组件
        initWidget();

        boolean bool=isNetworkConnected(this);
        if (bool) {
//			Toast.makeText(this, "网络可用", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "当前无网络", Toast.LENGTH_SHORT).show();
        }
    }

    private void initWidget() {
        et_username = (EditText) findViewById(R.id.username);
        et_password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.login);
        tv_forget_password = (TextView) findViewById(R.id.forget_password);
        tv_register = (TextView) findViewById(R.id.register);
        tv_No_register_login = (TextView) findViewById(R.id.No_register_login);

        btn_login.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_No_register_login.setOnClickListener(this);
    }

    public AssembleAComputerApplication app;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:            //登录
                btn_login.setEnabled(false);//为了避免用户重复点击 设置为不可点击
                final String username = et_username.getText().toString();
                final String password = et_password.getText().toString();
                if ((username.length() > 0) && (password.length() > 0)) {
                    login(username,password);
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }
                btn_login.setEnabled(true);//恢复按钮的点击功能
                break;
            case R.id.No_register_login:    //免注册登录
                SharedPreferences.Editor editor = app.sp.edit();
                editor.putInt(Constant.LOGIN_STATE, 1);
                editor.commit();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.forget_password:      //忘记密码

                break;
            case R.id.register:             //注册
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("新用户注册");

//                builder2.setIcon(R.mipmap.ic_launcher);
//              builder.setView(R.layout.register_layout);//API21 Android5.0支持
                final View view2 = getLayoutInflater().inflate(R.layout.register_layout, null);
                builder2.setView(view2);

                //负面的按钮
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Field filed = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                            filed.setAccessible(true);
                            filed.set(dialogInterface, true);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        dialogInterface.dismiss();
                    }
                });

                //正面的按钮
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText editText_username = (EditText) view2.findViewById(R.id.editText_username);
                        EditText editText_password = (EditText) view2.findViewById(R.id.editText_password);
                        EditText editText_password2 = (EditText) view2.findViewById(R.id.editText_password_2);

                        final String username = editText_username.getText().toString();
                        final String password = editText_password.getText().toString();
                        String password2 = editText_password2.getText().toString();

                        System.out.println(username + password + password2);

                        if (username.length() == 0 || password.length() == 0 || password2.length() == 0) {
                            Toast.makeText(LoginActivity.this, "信息未填写完", Toast.LENGTH_SHORT).show();
                            try {
                                Field filed = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                filed.setAccessible(true);
                                filed.set(dialogInterface, false);
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } else if (!password.equals(password2)) {
                            Toast.makeText(LoginActivity.this, "两次输入的密码不同！", Toast.LENGTH_SHORT).show();
                            try {
                                Field filed = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                filed.setAccessible(true);
                                filed.set(dialogInterface, false);
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } else {

                            User user=new User();
                            user.setUsername(username);
                            user.setPassword(password);
                            user.signUp(new SaveListener<User>() {
                                @Override
                                public void done(User user, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        login(username,password);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            try {
                                Field filed = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
                                filed.setAccessible(true);
                                filed.set(dialogInterface, true);
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            dialogInterface.dismiss();

                        }
                    }
                });

                builder2.show();

                break;
        }
    }

    //在登录界面的登录函数
    private void login(final String username, final String password) {
        User bmobUser = new User();
        bmobUser.setUsername(username);
        bmobUser.setPassword(password);
        bmobUser.login(new SaveListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = app.sp.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putInt(Constant.LOGIN_STATE, 2);
                    editor.commit();
                    putInformationToSP();//把信息存到SP里
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    switch(e.getErrorCode()){
                        case 101:
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            break;
                        case 9016:
                            Toast.makeText(LoginActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });
    }

    private void putInformationToSP() {
        User user= BmobUser.getCurrentUser(User.class);
        String name="";
        String phonenumber="";
        String address="";
        if (User.getObjectByKey("name") == null) {
            name = "";
        } else {
            name= (String) User.getObjectByKey("name");
        }

        if (User.getObjectByKey("phonenumber") == null) {
            phonenumber = "";
        } else {
            phonenumber= (String) User.getObjectByKey("phonenumber");
        }

        if (User.getObjectByKey("address") == null) {
            address = "";
        } else {
            address= (String) User.getObjectByKey("address");
        }

        SharedPreferences.Editor editor = app.sp.edit();
        if (name.length() > 0 && phonenumber.length() > 0 && address.length() > 0) {
            editor.putBoolean(Constant.USERINFORMATIONISCOMPLETE, true);
            System.out.println(true);
        }
        editor.putString(Constant.NAME, name);
        editor.putString(Constant.PHONENUMBER, phonenumber);
        editor.putString(Constant.ADDRESS, address);
        System.out.println(name+phonenumber+address);
        editor.commit();
    }

    //检查网络是否可用
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo=mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
