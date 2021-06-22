package com.example.fruit.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

import java.sql.Blob;

@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String name;
    private String password;
    private String customizeName;
    @Generated(hash = 1661209600)
    public User(Long id, String name, String password, String customizeName) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.customizeName = customizeName;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getCustomizeName() {
        return this.customizeName;
    }
    public void setCustomizeName(String customizeName) {
        this.customizeName = customizeName;
    }
}
