package com.em.jigsaw.bean;

/**
 * Time ： 2019/5/14 .
 * Author ： JN Zhang .
 * Description ： .
 */
public class PayEventBean {
    private String id;
    private String content;
    private String originalPrice;//原价
    private String presentPrice;//现价

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getPresentPrice() {
        return presentPrice;
    }

    public void setPresentPrice(String presentPrice) {
        this.presentPrice = presentPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
