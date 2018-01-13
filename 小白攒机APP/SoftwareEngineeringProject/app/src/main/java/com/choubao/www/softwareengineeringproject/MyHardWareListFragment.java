package com.choubao.www.softwareengineeringproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.choubao.www.softwareengineeringproject.adapter.MyHardWareListAdapter;
import com.choubao.www.softwareengineeringproject.addtoshoppingcaranimation.AnimationUtils;
import com.choubao.www.softwareengineeringproject.customview.CustomPopupWindowGoodInformation;
import com.choubao.www.softwareengineeringproject.utils.DataUtils;
import com.choubao.www.softwareengineeringproject.utils.ListItemClickHelp;
import com.choubao.www.softwareengineeringproject.vo.HardWare;
import com.choubao.www.softwareengineeringproject.vo.HardWareBmobFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by choubao on 17/4/22.
 */

public class MyHardWareListFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener,ListItemClickHelp{

    private Spinner spinner_liebiaopaixu;
    private EditText editText_sousuo;
    private ListView listView_myCpu;
    private TextView textView_shaixuan;

    private ImageView imageView_delete;

    private ArrayList<HardWare> HardWares;//这是临时使用的硬件列表，供查询 排序用的
    private ArrayList<HardWare> All_HardWares;//这是当前名称全部的硬件列表

    private MyHardWareListAdapter myHardWareListAdapter;
    private MainActivity mainActivity;

    private TextView tv_nodata;//列表不显示数据的时候要显示的TextView

    //private int position=0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity= (MainActivity) context;
    }

    public static MyHardWareListFragment newInstance(String Hardware_Name) {
        MyHardWareListFragment my = new MyHardWareListFragment();
        //System.out.println(yingjianmingcheng);
        Bundle b=new Bundle();
        b.putString("Hardware_Name",Hardware_Name);
        my.setArguments(b);
        return my;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_hardware_list_layout,null);
        spinner_liebiaopaixu= (Spinner) view.findViewById(R.id.spinner_liebiaopaixu);
        editText_sousuo= (EditText) view.findViewById(R.id.editText_sousuo);
        textView_shaixuan= (TextView) view.findViewById(R.id.textView_shaixuan);
        listView_myCpu= (ListView) view.findViewById(R.id.listView_myCpu);
        imageView_delete= (ImageView) view.findViewById(R.id.imageView_delete);

        tv_nodata= (TextView) view.findViewById(R.id.tv_nodata);

        listView_myCpu.setOnItemClickListener(this);
        textView_shaixuan.setOnClickListener(this);
        imageView_delete.setOnClickListener(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        //价格排序监听事件
        spinner_liebiaopaixu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //综合排序  价格由低到高  价格由高到低
                //System.out.println(adapterView.getItemAtPosition(i));
                switch(i){
                    case 0:
                        //综合排序先什么都不做
                        break;
                    case 1:
                        if (HardWares != null) {
                            HardWares=DataUtils.getSort(mainActivity,HardWares,1);
//                            for (HardWare h : HardWares) {
//                                System.out.println(h.toString());
//                            }
                            if(HardWares==null)return;
                            else fillList(HardWares);
//                            myHardWareListAdapter =new MyHardWareListAdapter(mainActivity, HardWares,this);
//                            listView_myCpu.setAdapter(myHardWareListAdapter);
                        }
                        break;
                    case 2:
                        if (HardWares != null) {
                            HardWares=DataUtils.getSort(mainActivity,HardWares,2);
//                            for (HardWare h : HardWares) {
//                                System.out.println(h.toString());
//                            }
                            if (HardWares == null)return;
                            else fillList(HardWares);
//                            myHardWareListAdapter =new MyHardWareListAdapter(mainActivity, HardWares,this);
//                            listView_myCpu.setAdapter(myHardWareListAdapter);
                        }
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //监听输入
        editText_sousuo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //if(editText_sousuo.getText().toString()!=null)imageView_delete.setVisibility(View.VISIBLE);
            }
        });

        //监听搜索框的回车键和确认键
        editText_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                //下面的语句会执行两次
                //return (keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER);

                //防止执行两次
                if (actionId== EditorInfo.IME_ACTION_SEND||(keyEvent!=null&&keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER)) {
                    switch (keyEvent.getAction()) {
                        case KeyEvent.ACTION_UP:{
                            //System.out.println(editText_sousuo.getText().toString());
                            //按下回车后要把软键盘收起来**********
                            InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm.isActive()) {
                                imm.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
                            }
                            //按下回车后要把软键盘收起来**********

                            if (All_HardWares != null) {
                                String search_Key=editText_sousuo.getText().toString();
                                HardWares =DataUtils.searchByKey(mainActivity, All_HardWares,search_Key);
//                                for (HardWare h : HardWares) {
//                                    System.out.println(h.toString());
//                                }
                                if (HardWares == null) {
                                    //如果是空的就移除列表
                                    listView_myCpu.setVisibility(View.GONE);
                                    tv_nodata.setVisibility(View.VISIBLE);
                                    return false;
                                } else {
                                    fillList(HardWares);
                                }
                                //myHardWareListAdapter = new MyHardWareListAdapter(mainActivity, HardWare_Search_Results);
                                //listView_myCpu.setAdapter(myHardWareListAdapter);
                            }
                        }
                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });

        loadData();
        return view;
    }

    public void loadData() {
        Bundle b=getArguments();
        String Hardware_Name=b.getString("Hardware_Name");
        getDataFromInternet(Hardware_Name);
        //System.out.println(Hardware_Name);

/*        All_HardWares = DataUtils.getData(mainActivity,Hardware_Name);
        if (All_HardWares == null) return;
        else {
            if(HardWares==null){
                HardWares=All_HardWares;
                fillList(All_HardWares);
            }
            else{
                fillList(HardWares);
            }
        }*/
    }

    public void fillList(ArrayList<HardWare> HardWares) {
        myHardWareListAdapter =new MyHardWareListAdapter(mainActivity,HardWares,this);
        if (HardWares.size() == 0) {
            tv_nodata.setVisibility(View.VISIBLE);
        }else{
            tv_nodata.setVisibility(View.GONE);
        }
        listView_myCpu.setAdapter(myHardWareListAdapter);
    }

    /* //隐藏键盘
        AppUtils.hideInputMethod(et_search_content);
        //获取输入的文字
        String key = et_search_content.getText().toString();*/

    @Override
    public void onClick(View view) {
        //只有textView_shaixuan用到
        switch(view.getId()){
            case R.id.imageView_delete:
                editText_sousuo.setText("");
                //imageView_delete.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    //实现ListItemClickHelp接口的onClick     点击添加按钮后将物品添加到购物车中
    @Override
    public void onClick(View item, View parent, int position, int which) {
        switch(which){
            case R.id.add_to_shopping_car:
                HardWare hardWare=HardWares.get(position);
                mainActivity.shoppingCars.add(hardWare);
                mainActivity.setSumMoneyText();
//                for(HardWare hw:mainActivity.shoppingCars){
//                    System.out.println(hw.toString());
//                }
                //添加到购物车动画
                AnimationUtils.setAnimation(item,mainActivity.RippleImageView_shopping_car,
                        (ViewGroup) mainActivity.main_Linearlayout,mainActivity).start();
                break;
            case R.id.imageView_picture:
                HardWare hardWare2=HardWares.get(position);
                final CustomPopupWindowGoodInformation mGoodPop=new CustomPopupWindowGoodInformation(mainActivity,hardWare2);
                //全屏显示
                View rootView= LayoutInflater.from(mainActivity).inflate(R.layout.activity_main,null);
                mGoodPop.showAtLocation(rootView, Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,0,0);
                mGoodPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mGoodPop.darkenBackGround(1f);
                    }
                });
                break;
        }
    }

    //从网络服务器获取数据的
    private static final int SUCCEED = 0x1;           //获取数据成功的标志
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCEED:
                    All_HardWares=(ArrayList<HardWare>) msg.obj;
                    if (All_HardWares == null) return;
                    else {
                        if(HardWares==null){
                            HardWares=All_HardWares;
                            fillList(All_HardWares);
                        }
                        else{
                            fillList(HardWares);
                        }
                    }
                    break;
            }
        }
    };

    private void getDataFromInternet(String Hardware_Name){

        BmobQuery<HardWareBmobFile> bmobQuery = new BmobQuery<HardWareBmobFile>();
        bmobQuery.addWhereEqualTo("HardWare_Name",Hardware_Name);
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
                        h.setBeizhu(hardWareBmobFile.getBeizhu()+" ");
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
                    //Toast.makeText(mainActivity, "数据获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
