package com.choubao.www.softwareengineeringproject.customview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.choubao.www.softwareengineeringproject.MainActivity;
import com.choubao.www.softwareengineeringproject.R;
import com.choubao.www.softwareengineeringproject.vo.HardWare;

/**
 * Created by choubao on 17/5/12.
 */

public class CustomPopupWindowGoodInformation extends PopupWindow implements View.OnClickListener{

    private View mPopView;
    private MainActivity mainActivity;
    private Context context;
    private HardWare mHardWare;

    private ImageView goods_information_imageView;
    private TextView goods_information_mingcheng,goods_information_xinghao,goods_information_jieshao,goods_information_jiage,goods_information_add;

    public CustomPopupWindowGoodInformation(Context context, HardWare hardWare) {
        super(context);
        this.context=context;
        mainActivity= (MainActivity) context;
        mHardWare=hardWare;
        init(context);
        initData(hardWare);
        setPopupWindow();
    }

    private void init(Context context) {
        LayoutInflater inflater=LayoutInflater.from(context);
        //绑定布局
        mPopView=inflater.inflate(R.layout.goods_information_popwindow,null);
        goods_information_imageView= (ImageView) mPopView.findViewById(R.id.goods_information_imageView);
        goods_information_mingcheng= (TextView) mPopView.findViewById(R.id.goods_information_mingcheng);
        goods_information_xinghao= (TextView) mPopView.findViewById(R.id.goods_information_xinghao);
        goods_information_jieshao= (TextView) mPopView.findViewById(R.id.goods_information_jieshao);
        goods_information_jiage= (TextView) mPopView.findViewById(R.id.goods_information_jiage);
        goods_information_add= (TextView) mPopView.findViewById(R.id.goods_information_add);
        goods_information_add.setOnClickListener(this);
    }

    private void initData(HardWare hardWare){
        goods_information_imageView.setImageBitmap(hardWare.getPicture());
        goods_information_mingcheng.setText(hardWare.getPinpai()+" "+hardWare.getHardware_Name());
        goods_information_xinghao.setText("型号:"+hardWare.getXinghao());
        String beizhu="";
        switch(hardWare.getHardware_Name()){
            case "处理器":
                beizhu="主频:";
                break;
            case "机械硬盘":
                beizhu="容量:";
                break;
            case "固态硬盘":
                beizhu="容量:";
                break;
            case "显示器":
                beizhu="尺寸:";
                break;
            case "内存":
                beizhu="容量:";
                break;
            case "机箱":
                beizhu=" ";
                break;
            case "主板":
                beizhu="不兼容CPU:";
                break;
            case "键盘":
                beizhu=" ";
                break;
            case "鼠标":
                beizhu=" ";
                break;
            case "光驱":
                beizhu=" ";
                break;
            case "显卡":
                beizhu="显存:";
                break;
        }
        goods_information_jieshao.setText(beizhu+hardWare.getBeizhu());
        goods_information_jiage.setText(hardWare.getJiage()+"");
    }

    private void setPopupWindow() {
        this.setContentView(mPopView);//设置View
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);//设置宽
//        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//设置高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //获取当前窗口焦点
        this.setFocusable(true);
        //设置动画
        this.setAnimationStyle(R.style.goodinformation_popwindow_anim_style);
        //设置背景
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //设置背景透明度
        //this.getBackground().setAlpha(50);
        darkenBackGround(0.5f);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.goods_information_add:
                System.out.println("加入到购物车");
                mainActivity.shoppingCars.add(mHardWare);
                mainActivity.setSumMoneyText();
                break;
        }
    }

    //设置Window灰度函数
    public void darkenBackGround(Float bgcolor){
        WindowManager.LayoutParams lp=mainActivity.getWindow().getAttributes();
        lp.alpha=bgcolor;
        mainActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mainActivity.getWindow().setAttributes(lp);
    }

}
