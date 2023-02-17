package com.topcoaching.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.helper.model.BaseCategoryProperty;

import java.util.List;


@Entity(tableName = "app_model")
public class AppModel extends BaseCategoryProperty implements Cloneable{

    @SerializedName(value="subcategories", alternate={"children"})
    @Expose
    @Ignore
    private List<AppModel> children;

    @Expose
    @Ignore
    private boolean changePosition = false;

    public List<AppModel> getChildren() {
        return children;
    }

    public void setChildren(List<AppModel> children) {
        this.children = children;
    }

    public boolean isChangePosition() {
        return changePosition;
    }

    public void setChangePosition(boolean changePosition) {
        this.changePosition = changePosition;
    }

    public OtherProperty getOtherProperty() {
        return getPropertyModel(new TypeToken<OtherProperty>() {});
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public AppModel getClone() {
        try {
            return (AppModel) clone();
        } catch (CloneNotSupportedException e) {
            return new AppModel();
        }
    }
}