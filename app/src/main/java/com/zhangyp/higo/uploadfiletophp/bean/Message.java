package com.zhangyp.higo.uploadfiletophp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangyipeng on 16/2/18.
 */
public class Message implements Serializable{

    private int code;
    private String message;
    private List<String> imgUrls;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }
}
