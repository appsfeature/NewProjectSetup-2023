package com.topcoaching.presenter;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.topcoaching.entity.ExtraProperty;
import com.topcoaching.util.AppConstant;

public class AppPresenter extends ViewModel {

    private ExtraProperty extraProperty;

    public void initialize(@Nullable Intent intent) {
        if(intent != null && intent.getSerializableExtra(AppConstant.CATEGORY_PROPERTY) instanceof ExtraProperty) {
            extraProperty = (ExtraProperty) intent.getSerializableExtra(AppConstant.CATEGORY_PROPERTY);
        }
    }

    public void initialize(@Nullable Bundle bundle) {
        if(bundle != null && bundle.getSerializable(AppConstant.CATEGORY_PROPERTY) instanceof ExtraProperty) {
            extraProperty = (ExtraProperty) bundle.getSerializable(AppConstant.CATEGORY_PROPERTY);
        }
    }

    public ExtraProperty getCategoryProperty(){
        return extraProperty;
    }
}
