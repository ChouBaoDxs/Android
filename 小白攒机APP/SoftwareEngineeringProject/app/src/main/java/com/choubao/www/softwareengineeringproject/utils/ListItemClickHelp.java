package com.choubao.www.softwareengineeringproject.utils;

import android.view.View;

/**
 * Created by choubao on 17/4/23.
 */

public interface ListItemClickHelp {
    /*＊
     * 点击item条目中某个控件的回调方法
     * item:ListView中item布局的View对象
     * parent:父容器对象
     * position:item在ListView中所处的位置
     * which:item中要点击的控件的id
     */
    void onClick(View item,View parent,int position,int which);
}
