package com.choubao.www.softwareengineeringproject.vo;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by choubao on 17/4/22.
 */

public class HardWareBmobFile extends BmobObject {

    private String id;//硬件的唯一标识
    private String HardWare_Name; //硬件名称：处理器、硬盘、固态硬盘、显示器、内存条、机箱、主板、键盘、鼠标、光驱、显卡
    private String pinpai;
    private String xinghao;
    private String beizhu;//详细信息
    private double jiage;
    private BmobFile picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHardWare_Name() {
        return HardWare_Name;
    }

    public void setHardWare_Name(String hardWare_Name) {
        HardWare_Name = hardWare_Name;
    }

    public String getPinpai() {
        return pinpai;
    }

    public void setPinpai(String pinpai) {
        this.pinpai = pinpai;
    }

    public String getXinghao() {
        return xinghao;
    }

    public void setXinghao(String xinghao) {
        this.xinghao = xinghao;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public double getJiage() {
        return jiage;
    }

    public void setJiage(double jiage) {
        this.jiage = jiage;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }
}
