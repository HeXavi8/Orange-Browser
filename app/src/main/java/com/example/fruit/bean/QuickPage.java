package com.example.fruit.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

public class QuickPage {
    private String url;
    private String title;
    private int imgPath;
    private boolean isAddIcon;//判断是添加的图标还是编辑图标

    public QuickPage(String title, String url,  int imgPath, boolean isAddIcon) {
        this.url = url;
        this.title = title;
        this.imgPath = imgPath;
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
    public int getImgPath() {
        return this.imgPath;
    }
    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }
    public boolean getIsAddIcon() {
        return this.isAddIcon;
    }
    public void setIsAddIcon(boolean isAddIcon) {
        this.isAddIcon = isAddIcon;
    }

}
