package com.melo.inventorymanagementsystem;

import java.util.Date;

public class productData {
    private  Integer product_id;
    private  String type;
    private  String brand;
    private  String productName;
    private  Double price;
    private  String status;
    private  String image;
    private  Date date;

    public productData(Integer product_id,String type, String brand, String productName, Double price, String status,String image, Date date){
        this.product_id =product_id;
        this.type=type;
        this.brand=brand;
        this.productName=productName;
        this.price=price;
        this.status=status;
        this.image=image;
        this.date=date;
    }
    public Integer getProduct_id(){
        return product_id;
    }
    public String getType(){
        return type;
    }
    public String getBrand(){
        return brand;
    }
    public String getProductName(){
        return  productName;
    }
    public Double getPrice(){
        return price;
    }
    public  String getStatus(){
        return status;
    }
    public String getImage(){
        return  image;
    }
    public Date getDate(){
        return  date;
    }

}
