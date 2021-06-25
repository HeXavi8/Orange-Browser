package com.example.fruit.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

public class QuickPage {
    private String url;
    private String title;
    private int imgPathId;//本地图片就是ID
    private String imgPathUrl;//网页图片就是string
    private boolean isAddIcon;//判断是添加的图标还是编辑图标

    public QuickPage(String title, String url,  int imgPathId, String imgPathUrl, boolean isAddIcon) {
        this.url = url;
        this.title = title;
        this.imgPathId = imgPathId;
        this.imgPathUrl = imgPathUrl;
        this.isAddIcon = isAddIcon;
    }

    public QuickPage() {
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
    public int getImgPathId() {
        return this.imgPathId;
    }
    public void setImgPathId(int imgPathId) {
        this.imgPathId = imgPathId;
    }
    public String getImgPathUrl() {
        return this.imgPathUrl;
    }
    public void setImgPathUrl(String imgPathUrl) {
        this.imgPathUrl = imgPathUrl;
    }

    public boolean getIsAddIcon() {
        return this.isAddIcon;
    }
    public void setIsAddIcon(boolean isAddIcon) {
        this.isAddIcon = isAddIcon;
    }

}
