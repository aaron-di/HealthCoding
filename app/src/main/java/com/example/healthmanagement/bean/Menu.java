package com.example.healthmanagement.bean;

import java.io.Serializable;

/**
 * 菜谱
 */
public class Menu implements Serializable {
    private Integer id;//ID
    private Integer typeId;//类型
    private String title;//标题
    private String img;//图片
    private String url;//链接

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Menu(Integer id, Integer typeId, String title, String img, String url) {
        this.id = id;
        this.typeId = typeId;
        this.title = title;
        this.img = img;
        this.url = url;
    }
}
