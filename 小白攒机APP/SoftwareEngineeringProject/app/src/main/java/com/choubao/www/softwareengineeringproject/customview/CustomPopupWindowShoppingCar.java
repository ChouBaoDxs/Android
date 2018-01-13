package com.choubao.www.softwareengineeringproject.customview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.choubao.www.softwareengineeringproject.MainActivity;
import com.choubao.www.softwareengineeringproject.R;
import com.choubao.www.softwareengineeringproject.adapter.ShoppingCarListAdapter;
import com.choubao.www.softwareengineeringproject.utils.ListItemClickHelp;
import com.choubao.www.softwareengineeringproject.vo.HardWare;

/**
 * Created by choubao on 17/5/12.
 */

public class CustomPopupWindowShoppingCar extends PopupWindow  implements ListItemClickHelp,View.OnClickListener{

    private View mPopView;
    private ListView listView_popupwindow_shopping_cart;
    private ShoppingCarListAdapter shoppingCarListAdapter;
    private LinearLayout clear_shopping_cart;
    private LinearLayout popupWindow_pack_up;
    private MainActivity mainActivity;
    private Context context;

    public CustomPopupWindowShoppingCar(Context context) {
        super(context);
        this.context=context;
        mainActivity= (MainActivity) context;
        init(context);
        setPopupWindow();

        shoppingCarListAdapter =new ShoppingCarListAdapter(mainActivity,mainActivity.shoppingCars,this);
        listView_popupwindow_shopping_cart.setAdapter(shoppingCarListAdapter);
    }

    //初始化布局
    private void init(Context context) {
        LayoutInflater inflater=LayoutInflater.from(context);
        //绑定布局
        mPopView=inflater.inflate(R.layout.popup_window_layout,null);
        listView_popupwindow_shopping_cart= (ListView) mPopView.findViewById(R.id.listView_popupwindow_shopping_cart);
        clear_shopping_cart=(LinearLayout) mPopView.findViewById(R.id.clear_shopping_cart);
        popupWindow_pack_up= (LinearLayout) mPopView.findViewById(R.id.popupWindow_pack_up);
        clear_shopping_cart.setOnClickListener(this);
        popupWindow_pack_up.setOnClickListener(this);
    }

    private void setPopupWindow() {
        this.setContentView(mPopView);//设置View
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);//设置宽
//        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//设置高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //不获取当前窗口焦点
        this.setFocusable(false);   //这样一来 就可以点击界面的其它地方了
        //设置动画
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        //设置背景
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //设置背景透明度
        //this.getBackground().setAlpha(50);
    }

    //实现ListItemClickHelp接口的onClick     点击添加按钮后将物品从购物车中删除
    @Override
    public void onClick(View item, View parent, int position, int which) {

        switch(which){
            case R.id.imageView_shoppingcar_remove:
                HardWare hardWare=mainActivity.shoppingCars.get(position);
                mainActivity.shoppingCars.remove(hardWare);
                for(HardWare hw:mainActivity.shoppingCars){
                    System.out.println(hw.toString());
                }
                //刷新列表
                shoppingCarListAdapter =new ShoppingCarListAdapter(mainActivity,mainActivity.shoppingCars,this);
                listView_popupwindow_shopping_cart.setAdapter(shoppingCarListAdapter);
                mainActivity.setSumMoneyText();
                break;
            default :

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.clear_shopping_cart:
                //清空数组
                mainActivity.shoppingCars.clear();
                //要刷新列表
                shoppingCarListAdapter =new ShoppingCarListAdapter(mainActivity,mainActivity.shoppingCars,this);
                listView_popupwindow_shopping_cart.setAdapter(shoppingCarListAdapter);
                mainActivity.setSumMoneyText();
                break;
            case R.id.popupWindow_pack_up:
                dismiss();
                break;
        }
    }
}
