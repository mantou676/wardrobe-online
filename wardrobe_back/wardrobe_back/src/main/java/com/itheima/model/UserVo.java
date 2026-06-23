package com.itheima.model;

public class UserVo extends User {
    // 前端提交的新密码
    private String newpsw;

    public String getNewpsw() {
        return newpsw;
    }

    public void setNewpsw(String newpsw) {
        this.newpsw = newpsw;
    }
}