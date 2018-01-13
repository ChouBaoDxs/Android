package com.choubao.www.softwareengineeringproject.vo;

import cn.bmob.v3.BmobUser;

/**
 * Created by choubao on 17/5/14.
 */

public class User extends BmobUser {
    private String name;
    private String phonenumber;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}

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
}
