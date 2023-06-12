package com.example.billingapp;

import com.parse.ParseObject;

public class MyParseClass extends ParseObject {

    public String getName() {
        return getString("shopName");
    }

    public void setName(String name) {
        put("shopName", name);
    }
}
