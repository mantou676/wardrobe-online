package com.itheima.model;
import java.math.BigDecimal;
import java.util.List;

public class Clothes {
    private Integer id;
    private String clothName;
    private String image;
    private Integer typeId;
    private String typeName;
    private String style;
    private BigDecimal price;
    //新增尺码集合，用于接收对应类别尺码
    private List<Size> sizeList;

    //无参构造
    public Clothes(){}

    //get/set 全套
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getClothName() {return clothName;}
    public void setClothName(String clothName) {this.clothName = clothName;}
    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}
    public Integer getTypeId() {return typeId;}
    public void setTypeId(Integer typeId) {this.typeId = typeId;}
    public String getTypeName() {return typeName;}
    public void setTypeName(String typeName) {this.typeName = typeName;}
    public String getStyle() {return style;}
    public void setStyle(String style) {this.style = style;}
    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {this.price = price;}
    public List<Size> getSizeList() {return sizeList;}
    public void setSizeList(List<Size> sizeList) {this.sizeList = sizeList;}
}