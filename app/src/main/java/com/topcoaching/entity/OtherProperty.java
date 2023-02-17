package com.topcoaching.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OtherProperty implements Serializable {

    @Expose
    @SerializedName(value="update_id")
    private String updateId;

    @Expose
    @SerializedName(value="parent_id")
    private int parentId;

    @Expose
    @SerializedName(value="exam_id")
    private int[] examId;

    @Expose
    @SerializedName(value="cat_id")
    private int catId;

    @SerializedName(value="is_sub_cat", alternate={"isSubCat"})
    @Expose
    private boolean isSubCat = true;

    @SerializedName(value="is_see_ans", alternate={"isSeeAns"})
    @Expose
    private boolean isSeeAns = true;

    @SerializedName(value="host")
    @Expose
    private String host;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int[] getExamId() {
        return examId;
    }

    public void setExamId(int[] examId) {
        this.examId = examId;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public boolean isSubCat() {
        return isSubCat;
    }

    public void setSubCat(boolean subCat) {
        isSubCat = subCat;
    }

    public boolean isSeeAns() {
        return isSeeAns;
    }

    public void setSeeAns(boolean seeAns) {
        isSeeAns = seeAns;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }
}
