package com.topcoaching.onesignal.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OneSignalModel implements Serializable {

    @Expose
    @SerializedName(value="id")
    private int id;
    @Expose
    @SerializedName(value="cat_id")
    private int catId;
    @Expose
    @SerializedName(value="type")
    private int type = NotificationType.TYPE_DEFAULT;
    @Expose
    @SerializedName(value="title")
    private String title;
    @Expose
    @SerializedName(value="body")
    private String body;
    @Expose
    @SerializedName(value="url", alternate={"web_url"})
    private String url;
    @Expose
    @SerializedName(value="app_id")
    private String appId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
