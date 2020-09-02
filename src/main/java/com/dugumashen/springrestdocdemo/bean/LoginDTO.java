package com.dugumashen.springrestdocdemo.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * @author
 */
public class LoginDTO {

    private String name;
    private String pwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
