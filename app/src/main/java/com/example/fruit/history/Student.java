package com.example.fruit.history;

public class Student {
    private String name;//姓名
    private int age;//年龄
    private String headImgUrl;//头像
    private boolean isSelected;//是否选中
    private int headImgId;//新加头像图片id，加载本地的图片

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getHeadImgId() {
        return headImgId;
    }

    public void setHeadImgId(int headImgId) {
        this.headImgId = headImgId;
    }

    public Student() {

    }

    public Student(String name, int age, String headImgUrl, boolean isSelected, int headImgId) {
        this.name = name;
        this.age = age;
        this.headImgUrl = headImgUrl;
        this.isSelected = isSelected;
        this.headImgId = headImgId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", isSelected=" + isSelected +
                ", headImgId=" + headImgId +
                '}';
    }
}
