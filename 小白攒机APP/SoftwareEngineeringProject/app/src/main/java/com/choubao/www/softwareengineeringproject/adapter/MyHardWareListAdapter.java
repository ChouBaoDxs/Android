package com.choubao.www.softwareengineeringproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.choubao.www.softwareengineeringproject.R;
import com.choubao.www.softwareengineeringproject.utils.ListItemClickHelp;
import com.choubao.www.softwareengineeringproject.vo.HardWare;

import java.util.ArrayList;

/**
 * Created by choubao on 17/4/22.
 */

public class MyHardWareListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HardWare> HardWares;
    private ListItemClickHelp callback;

    public MyHardWareListAdapter(Context context, ArrayList<HardWare> HardWares,ListItemClickHelp callback) {
        this.context=context;
        this.HardWares = HardWares;
        this.callback=callback;
    }

    public ArrayList<HardWare> getHardWares() {
        return HardWares;
    }

    public void setHardWares(ArrayList<HardWare> hardWares) {
        this.HardWares = hardWares;
    }

    @Override
    public int getCount() {
        return HardWares.size();
    }

    @Override
    public Object getItem(int i) {
        return HardWares.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view= LayoutInflater.from(context).inflate(R.layout.list_item_layout,null);
            vh=new ViewHolder();
            vh.textView_mingcheng= (TextView) view.findViewById(R.id.textView_mingcheng);
            vh.textView_jieshao= (TextView) view.findViewById(R.id.textView_jieshao);
            vh.textView_jiage= (TextView) view.findViewById(R.id.textView_jiage);
            vh.imageView_picture= (ImageView) view.findViewById(R.id.imageView_picture);
            vh.add_to_shopping_car= (ImageView) view.findViewById(R.id.add_to_shopping_car);
            view.setTag(vh);
        }
        vh= (ViewHolder) view.getTag();
        final HardWare hardWare = HardWares.get(i);
        vh.textView_mingcheng.setText(hardWare.geimingcheng());

        String beizhu=" ";
        switch(hardWare.getHardware_Name()){
            case "处理器":
                beizhu="主频:";
                vh.textView_jieshao.setText(beizhu+hardWare.getBeizhu());
                break;
            case "机械硬盘":
                beizhu="容量:";
                vh.textView_jieshao.setText(beizhu+hardWare.getBeizhu());
                break;
            case "固态硬盘":
                beizhu="容量:";
                vh.textView_jieshao.setText(beizhu+hardWare.getBeizhu());
                break;
            case "显示器":
                beizhu="尺寸:";
                vh.textView_jieshao.setText(beizhu+hardWare.getBeizhu());
                break;
            case "内存":
                beizhu="容量:";
                vh.textView_jieshao.setText(beizhu+hardWare.getBeizhu());
                break;
            case "机箱":
                beizhu=" ";
                vh.textView_jieshao.setText(beizhu);
                break;
            case "主板":
                beizhu="不兼容CPU:";
                vh.textView_jieshao.setText(beizhu+hardWare.getBeizhu());
                break;
            case "键盘":
                beizhu=" ";
                vh.textView_jieshao.setText(beizhu);
                break;
            case "鼠标":
                beizhu=" ";
                vh.textView_jieshao.setText(beizhu);
                break;
            case "光驱":
                beizhu=" ";
                vh.textView_jieshao.setText(beizhu);
                break;
            case "显卡":
                beizhu="显存:";
                vh.textView_jieshao.setText(beizhu+hardWare.getBeizhu());
                break;
        }
        vh.textView_jiage.setText(hardWare.getJiage()+"");
        vh.imageView_picture.setImageBitmap(hardWare.getPicture());

        final int which=vh.add_to_shopping_car.getId();
        //点击加入购物车事件
        vh.add_to_shopping_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(view,viewGroup,i,which);
            }
        });

        final int which_picture=vh.imageView_picture.getId();
        //点击弹出详细信息窗口
        vh.imageView_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(view,viewGroup,i,which_picture);
            }
        });

        return view;
    }

    static class ViewHolder {
        ImageView imageView_picture,add_to_shopping_car;
        TextView textView_mingcheng,textView_jieshao,textView_jiage;
    }
}
