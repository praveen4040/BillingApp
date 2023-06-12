package com.example.billingapp;

public class Report {
    private Double tot;
    private int id;
    private String date,shopName;
    public Report(){}

    public Report(int id,Double tot,String date,String shopName){
        this.id=id;
        this.tot=tot;
        this.date=date;
        this.shopName=shopName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Double getTot() {
        return tot;
    }

    public void setTot(Double tot) {
        this.tot = tot;
    }
}
