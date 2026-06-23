package com.itheima.model;

import java.math.BigDecimal;

public class Order {
    private Integer id;
    private String clothesDetails;
    private BigDecimal price;
    private Integer status;
    private Integer userId;
    private String address;
    private String time;
    private String userName; // 后台订单查询关联用户名

    // 无参构造
    public Order(){}

    // getter & setter
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getClothesDetails() {return clothesDetails;}
    public void setClothesDetails(String clothesDetails) {this.clothesDetails = clothesDetails;}
    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}
    public Integer getStatus() {return status;}
    public void setStatus(Integer status) {this.status = status;}
    public Integer getUserId() {return userId;}
    public void setUserId(Integer userId) {this.userId = userId;}
    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}
    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}
}