package com.example.fruit.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Collection {
    String name;
    String url;
    String title;
    @Generated(hash = 512964657)
    public Collection(String name, String url, String title) {
        this.name = name;
        this.url = url;
        this.title = title;
    }
    @Generated(hash = 1149123052)
    public Collection() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
