package com.choubao.www.softwareengineeringproject.vo;

import cn.bmob.v3.BmobObject;

/**
 * Created by choubao on 17/5/14.
 */

//订单类
public class Order extends BmobObject{
    private String username;
    private String order_id;
    private String order_content;
    private double order_amount;
    private String name;
    private String phonenumber;
    private String address;
    private boolean isPay;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = String.valueOf(order_id);
    }

    public String getOrder_content() {
        return order_content;
    }

    public void setOrder_content(String order_content) {
        this.order_content = order_content;
    }

    public double getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(double order_amount) {
        this.order_amount = order_amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    @Override
    public String toString() {
        return "Order{" +
                "username='" + username + '\'' +
                ", order_id='" + order_id + '\'' +
                ", order_content='" + order_content + '\'' +
                ", order_amount=" + order_amount +
                ", name='" + name + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", address='" + address + '\'' +
                ", isPay=" + isPay +
                '}';
    }
}
