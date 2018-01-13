package com.choubao.www.softwareengineeringproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.choubao.www.softwareengineeringproject.utils.Constant;
import com.choubao.www.softwareengineeringproject.vo.HardWare;
import com.choubao.www.softwareengineeringproject.vo.HardWareBmobFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends Activity {
    private static final int START_MAIN = 0x1;//进入主界面
    private static final int START_LOGIN = 0x2;//进入登录
    public AssembleAComputerApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        Bmob.initialize(this, "35669d0369968e36b36ac00f814a8da8");
        //进行数据初始化操作
        //getDataFromInternet();

        app = (AssembleAComputerApplication) getApplication();

        int loginState = app.sp.getInt(Constant.LOGIN_STATE, 3);//缺省值为false
        if (loginState != 3) {
            handler.sendEmptyMessageDelayed(START_MAIN, 1000);  //如果已经登录的，就去主界面
        } else {
            handler.sendEmptyMessageDelayed(START_LOGIN, 1000); //如果还没登录的，就去登录界面
        }


    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case START_MAIN:
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    break;
                case START_LOGIN:
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                    break;
                case SUCCEED:
//                    All_HardWares=(ArrayList<HardWare>) msg.obj;
//                    Intent intent=new Intent(SplashActivity.this, MainActivity.class);
//                    intent.putParcelableArrayListExtra("All_HardWares",All_HardWares);
//                    startActivity(intent);
                    break;
            }
        }
    };


    //没用没用***************************************************************************************************
    private static final int SUCCEED = 0x3; //获取数据成功的标志
    private ArrayList<HardWare> All_HardWares;
    private void getDataFromInternet(){
        BmobQuery<HardWareBmobFile> bmobQuery = new BmobQuery<HardWareBmobFile>();
        bmobQuery.addWhereEqualTo("isDelete",false);
        bmobQuery.setLimit(100);//返回100条，不写的话默认返回10条

        bmobQuery.findObjects(new FindListener<HardWareBmobFile>() {
            @Override
            public void done(List<HardWareBmobFile> list, BmobException e) {
                if (e == null) {

                    ArrayList<HardWare> l = new ArrayList<HardWare>();
                    for(HardWareBmobFile hardWareBmobFile:list)
                    {
                        HardWare h=new HardWare();
                        h.set_id(hardWareBmobFile.getId());
                        h.setHardware_Name(hardWareBmobFile.getHardWare_Name());
                        h.setPinpai(hardWareBmobFile.getPinpai());
                        h.setXinghao(hardWareBmobFile.getXinghao());
                        h.setBeizhu(hardWareBmobFile.getBeizhu());
                        h.setJiage(hardWareBmobFile.getJiage());
                        String picture_url=hardWareBmobFile.getPicture().getFileUrl();
                        try {
                            URL url = new URL(picture_url);
                            InputStream in=url.openStream();
                            Bitmap bitmap= BitmapFactory.decodeStream(in);
                            h.setPicture(bitmap);
                            l.add(h);
                        } catch (MalformedURLException e1) {
                            e1.printStackTrace();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }

                    Message message = handler.obtainMessage();
                    message.what = SUCCEED;
                    message.obj = l;
                    handler.sendMessage(message);

                }else {
                    Toast.makeText(SplashActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
