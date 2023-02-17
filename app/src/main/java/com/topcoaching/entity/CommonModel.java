package com.topcoaching.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.adssdk.BaseAdModelClass;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "common_data")
public class CommonModel extends BaseAdModelClass {

    public CommonModel() {
    }

    public CommonModel(int id, String title, String dateTime) {
        this.id = id;
        this.title = title;
        this.updatedAt = dateTime;
    }


    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    @PrimaryKey
    private int id;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @SerializedName("item_type")
    @Expose
    @ColumnInfo(name = "item_type")
    private int itemType;
    @SerializedName("type")
    @Expose
    @ColumnInfo(name = "type")
    private int type;

    @SerializedName("wb_category_id")
    @Expose
    @ColumnInfo(name = "wb_category_id")
    private int categoryId;

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("created_at")
    @Expose
    @ColumnInfo(name = "created_at")
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    @ColumnInfo(name = "updated_at")
    private String updatedAt;

    @SerializedName("app_id_android")
    @Expose
    @ColumnInfo(name = "app_id_android")
    private String apIdAndroid;

    @SerializedName("app_id_ios")
    @Expose
    @ColumnInfo(name = "app_id_ios")
    private String appIdIos;

    @SerializedName("web_url")
    @Expose
    @ColumnInfo(name = "web_url")
    private String webUrl;

    @SerializedName("pdf_link")
    @Expose
    @ColumnInfo(name = "pdf_link")
    private String pdfLink;

    @ColumnInfo(name = "has_read")
    private boolean hasRead;

    @Ignore
    private String jsonData;
    @Ignore
    private String description;

    @Ignore
    private String uuid ;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getApIdAndroid() {
        return apIdAndroid;
    }

    public void setApIdAndroid(String apIdAndroid) {
        this.apIdAndroid = apIdAndroid;
    }

    public String getAppIdIos() {
        return appIdIos;
    }

    public void setAppIdIos(String appIdIos) {
        this.appIdIos = appIdIos;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
