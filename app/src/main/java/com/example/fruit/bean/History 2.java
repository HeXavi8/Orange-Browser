package com.example.fruit.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class History {
    private String title;
    private String url;
    private String time;
    @Generated(hash = 1019327556)
    public History(String title, String url, String time) {
        this.title = title;
        this.url = url;
        this.time = time;
    }
    @Generated(hash = 869423138)
    public History() {
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
