package com.choubao.www.softwareengineeringproject.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.choubao.www.softwareengineeringproject.AssembleAComputerApplication;
import com.choubao.www.softwareengineeringproject.LoginActivity;
import com.choubao.www.softwareengineeringproject.MainActivity;
import com.choubao.www.softwareengineeringproject.R;
import com.choubao.www.softwareengineeringproject.utils.Constant;
import com.choubao.www.softwareengineeringproject.vo.User;

import java.lang.reflect.Field;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by choubao on 17/5/12.
 */

public class LeftMenu extends PopupWindow implements View.OnClickListener {

    private Button iwantlogin, iwantregister, userInformation, exitlogin;
    private LinearLayout leftmenu_pack_up;

    public AssembleAComputerApplication app;
    private View mPopView;
    private MainActivity mainActivity;
    private Context context;

    public LeftMenu(Context context) {
        super(context);
        this.context = context;
        mainActivity = (MainActivity) context;
        app = (AssembleAComputerApplication) mainActivity.getApplication();
        init(context);
        setPopupWindow();
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        mPopView = inflater.inflate(R.layout.leftmenu_layout, null);

        iwantlogin = (Button) mPopView.findViewById(R.id.iwantlogin);
        iwantregister = (Button) mPopView.findViewById(R.id.iwantregister);
        userInformation = (Button) mPopView.findViewById(R.id.userInformation);
        exitlogin = (Button) mPopView.findViewById(R.id.exitlogin);

        leftmenu_pack_up= (LinearLayout) mPopView.findViewById(R.id.leftmenu_pack_up);

        iwantlogin.setOnClickListener(this);
        iwantregister.setOnClickListener(this);
        userInformation.setOnClickListener(this);
        exitlogin.setOnClickListener(this);
        leftmenu_pack_up.setOnClickListener(this);

        int Login_State = app.sp.getInt(Constant.LOGIN_STATE, 1);//能到主界面的至少是免注册登录的人
        switch (Login_State) {
            case 1://免注册登录的，就把个人信息按钮藏起来
                userInformation.setVisibility(View.GONE);
                break;
            case 2://注册登录的，就把我要登录和我要注册按钮藏起来
                iwantlogin.setVisibility(View.GONE);
                iwantregister.setVisibility(View.GONE);
                break;
        }
    }

    private void setPopupWindow() {
        this.setContentView(mPopView);//设置View
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);//设置宽
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //获取当前窗口焦点
        this.setFocusable(true);
        //设置动画
        this.setAnimationStyle(R.style.leftmenu_popwindow_anim_style);
        //设置背景
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftmenu_pack_up:
                dismiss();
                break;
            case R.id.iwantlogin:
                SharedPreferences.Editor editor = app.sp.edit();
                editor.remove(Constant.LOGIN_STATE);
                editor.commit();
                mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
                //mainActivity.finish();
                break;
            case R.id.iwantregister:
                register();
                break;
            case R.id.userInformation:
                userInformationDialog();
                break;
            case R.id.exitlogin:
                SharedPreferences.Editor editor2 = app.sp.edit();
                //editor.clear();//清空数据
                editor2.clear().commit();
                BmobUser.logOut();//清除缓存对象
                mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
                mainActivity.finish();
                break;
        }
    }

    //个人信息函数 会弹出一个Dialog
    private void userInformationDialog() {

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(mainActivity);
        builder1.setTitle("编辑个人信息");
        final View view1 = mainActivity.getLayoutInflater().inflate(R.layout.user_information, null);
        builder1.setView(view1);

        //绑定控件
        final EditText editText_name = (EditText) view1.findViewById(R.id.editText_name);
        final EditText editText_phonenumber = (EditText) view1.findViewById(R.id.editText_phonenumber);
        final EditText editText_address = (EditText) view1.findViewById(R.id.editText_address);
        //先获取数据
        String name = app.sp.getString(Constant.NAME, "");
        String phonenumber = app.sp.getString(Constant.PHONENUMBER, "");
        String address = app.sp.getString(Constant.ADDRESS, "");

        if (name.length() > 0) {
            editText_name.setText(name);
        }
        if (phonenumber.length() > 0) {
            editText_phonenumber.setText(phonenumber);
        }
        if (address.length() > 0) {
            editText_address.setText(address);
        }

        //负面的按钮
        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        //正面的按钮
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText_name.getText().toString();
                String phonenumber = editText_phonenumber.getText().toString();
                String address = editText_address.getText().toString();
                SharedPreferences.Editor editor3 = app.sp.edit();
                editor3.putString(Constant.NAME, name);
                editor3.putString(Constant.PHONENUMBER, phonenumber);
                editor3.putString(Constant.ADDRESS, address);
                if (name.length() > 0 && phonenumber.length() > 0 && address.length() > 0) {
                    editor3.putBoolean(Constant.USERINFORMATIONISCOMPLETE, true);
                }
                editor3.commit();
                User newUser = new User();
                newUser.setName(name);
                newUser.setPhonenumber(phonenumber);
                newUser.setAddress(address);
                BmobUser bmobUser = BmobUser.getCurrentUser();
                newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(mainActivity, "更新信息成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mainActivity, "更新数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder1.show();
    }

    //注册函数 会弹出一个Dialog
    private void register() {
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(mainActivity);
        builder2.setTitle("新用户注册");
//                builder2.setIcon(R.mipmap.ic_launcher);
//              builder.setView(R.layout.register_layout);//API21 Android5.0支持
        final View view2 = mainActivity.getLayoutInflater().inflate(R.layout.register_layout, null);
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
                    Toast.makeText(mainActivity, "信息未填写完", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mainActivity, "两次输入的密码不同！", Toast.LENGTH_SHORT).show();
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

                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null) {
                                Toast.makeText(mainActivity, "注册成功", Toast.LENGTH_SHORT).show();
                                login(username, password);
                            } else {
                                Toast.makeText(mainActivity, "注册失败", Toast.LENGTH_SHORT).show();
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
    }

    //在主界面中的登录函数
    private void login(final String username, final String password) {
        User bmobUser = new User();
        bmobUser.setUsername(username);
        bmobUser.setPassword(password);
        bmobUser.login(new SaveListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                if (e == null) {
                    Toast.makeText(mainActivity, "自动登录成功", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = app.sp.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putInt(Constant.LOGIN_STATE, 2);
                    editor.commit();

                    putInformationToSP();//把信息存到SP里
//                    startActivity(new Intent(zhujiemianActivity.this, zhujiemianActivity.class));
//                    finish();
                    iwantlogin.setVisibility(View.GONE);
                    iwantregister.setVisibility(View.GONE);
                    userInformation.setVisibility(View.VISIBLE);

                } else {
                    switch(e.getErrorCode()){
                        case 101:
                            Toast.makeText(mainActivity, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            break;
                        case 9016:
                            Toast.makeText(mainActivity, "无网络连接", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(mainActivity, "未知错误", Toast.LENGTH_SHORT).show();
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
}
