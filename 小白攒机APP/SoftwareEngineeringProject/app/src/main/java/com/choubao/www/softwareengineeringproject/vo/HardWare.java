package com.choubao.www.softwareengineeringproject.vo;

import android.graphics.Bitmap;

/**
 * Created by choubao on 17/4/22.
 */

public class HardWare{

    private String _id;//硬件的唯一标识
    private String Hardware_Name; //硬件名称：处理器、硬盘、固态硬盘、显示器、内存条、机箱、主板、键盘、鼠标、光驱、显卡
    private String pinpai;
    private String xinghao;
    private String beizhu;//详细信息
//    处理器：主频                         Cpu
//    硬盘：容量 硬盘转速                   Hard disk
//    固态硬盘：容量                       SSD
//    显示器：尺寸（英寸）                  Monitor
//    内存条：容量                        Memory bar
//    机箱：null                         Case
//    主板：不兼容的cpu                    Mother board
//    键盘：null                         Keyboard
//    鼠标：null                         Mouse
//    光驱：null                         CD drive
//    显卡：显存容量                       Graphics card
    private double jiage;
    private Bitmap picture;

    public String getHardware_Name() { return Hardware_Name; }

    public void setHardware_Name(String hardware_Name) { Hardware_Name = hardware_Name; }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPinpai() {
        return pinpai;
    }

    public void setPinpai(String pinpai) {
        this.pinpai = pinpai;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }


    public double getJiage() {
        return jiage;
    }

    public void setJiage(double jiage) {
        this.jiage = jiage;
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
    @Override
    public String toString() {
        return getPinpai()+" "+getXinghao()+" "+getJiage()+" "+getBeizhu();
    }

    public String geimingcheng() {
        return getPinpai()+" "+getXinghao();
    }

    //把字符串全都串起来，供查询用
    public String get_For_Search_String() {
        return getHardware_Name()+getPinpai()+getXinghao()+getBeizhu();
    }
}
