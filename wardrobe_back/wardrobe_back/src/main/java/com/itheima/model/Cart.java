package com.itheima.model;

public class Cart {
    private Integer id;
    private Integer clothId;
    private String clothSize;
    private Integer amount;
    private Integer userId;
    private String date;
    // 关联对象
    private Clothes clothes;
    private User user;

    public Cart() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getClothId() { return clothId; }
    public void setClothId(Integer clothId) { this.clothId = clothId; }
    public String getClothSize() { return clothSize; }
    public void setClothSize(String clothSize) { this.clothSize = clothSize; }
    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public Clothes getClothes() { return clothes; }
    public void setClothes(Clothes clothes) { this.clothes = clothes; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
