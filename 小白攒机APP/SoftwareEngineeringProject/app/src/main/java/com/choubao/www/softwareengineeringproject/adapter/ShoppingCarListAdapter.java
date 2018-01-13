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
 * Created by choubao on 17/5/10.
 */

public class ShoppingCarListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HardWare> HardWares;
    private ListItemClickHelp callback;

    public ShoppingCarListAdapter(Context context, ArrayList<HardWare> HardWares,ListItemClickHelp callback) {
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
    public View getView(final int i, View view,final ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view= LayoutInflater.from(context).inflate(R.layout.shopping_cart_list_item_layout,null);
            vh=new ViewHolder();
            vh.imageView_shoppingcar_add= (ImageView) view.findViewById(R.id.imageView_shoppingcar_add);
            vh.imageView_shoppingcar_remove= (ImageView) view.findViewById(R.id.imageView_shoppingcar_remove);
            vh.textView_shoppingcar_mingcheng= (TextView) view.findViewById(R.id.textView_shoppingcar_mingcheng);
            vh.textView_shoppingcar_jiage= (TextView) view.findViewById(R.id.textView_shoppingcar_jiage);
            vh.textView_shoppingcar_shumu= (TextView) view.findViewById(R.id.textView_shoppingcar_shumu);
            vh.textView_shoppingcar_Hardware_Name= (TextView) view.findViewById(R.id.textView_shoppingcar_Hardware_Name);
            view.setTag(vh);
        }
        vh=(ViewHolder) view.getTag();
        final HardWare hardWare = HardWares.get(i);
        vh.textView_shoppingcar_mingcheng.setText(hardWare.geimingcheng());
        vh.textView_shoppingcar_Hardware_Name.setText(hardWare.getHardware_Name());
        vh.textView_shoppingcar_jiage.setText(hardWare.getJiage()+"");

        final int which1=vh.imageView_shoppingcar_add.getId();
        //点击增加数目
        vh.imageView_shoppingcar_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(view,viewGroup,i,which1);
            }
        });
        //点击减少数目
        final int which2=vh.imageView_shoppingcar_remove.getId();
        vh.imageView_shoppingcar_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(view,viewGroup,i,which2);
            }
        });

        return view;
    }

    static class ViewHolder {
        ImageView imageView_shoppingcar_remove,imageView_shoppingcar_add;
        TextView textView_shoppingcar_mingcheng,textView_shoppingcar_jiage,textView_shoppingcar_shumu,textView_shoppingcar_Hardware_Name;
    }
}
