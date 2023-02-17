package com.topcoaching.entity;

import com.helper.model.BaseCategoryProperty;

import java.io.Serializable;

public class ExtraProperty extends BaseCategoryProperty implements Serializable, Cloneable{

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ExtraProperty getClone() {
        try {
            return (ExtraProperty) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return new ExtraProperty();
        }
    }
}
