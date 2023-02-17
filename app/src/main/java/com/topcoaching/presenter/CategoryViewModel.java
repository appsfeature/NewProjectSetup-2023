package com.topcoaching.presenter;

import android.app.Activity;

import com.helper.callback.Response;
import com.helper.task.TaskRunner;
import com.helper.util.BaseConstants;
import com.topcoaching.entity.AppModel;
import com.topcoaching.network.ApiConstants;
import com.topcoaching.network.NetworkManager;
import com.topcoaching.tasks.TaskGetCategories;
import com.topcoaching.util.AppConstant;

import java.util.List;


public class CategoryViewModel {

    private NetworkManager networkHandler;
    private Response.Callback<AppModel> callback;
    public int mCategoryId;
    public int mChildrenLevel = 1;

    private void initialize(Activity activity, Response.Callback<AppModel> callback) {
        this.callback = callback;
        networkHandler = getNetworkManager(activity);
    }

    public void setCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public CategoryViewModel(Activity activity, Response.Callback<AppModel> callback) {
        initialize(activity, callback);
    }


    private NetworkManager getNetworkManager(Activity activity) {
        if (networkHandler == null) {
            networkHandler = new NetworkManager(activity);
        }
        return networkHandler;
    }

    public void loadData() {
        isFirstHit = true;
        fetchFromCache(null,null);
    }

    private void fetchFromCache(final String imagePath, List<AppModel> catBeans) {
        if(mCategoryId == 0){
            callback.onFailure(new Exception(BaseConstants.Error.MSG_ERROR));
            return;
        }
        new TaskGetCategories(mCategoryId, imagePath, catBeans).execute(new TaskRunner.Callback<List<AppModel>>() {
            @Override
            public void onComplete(List<AppModel> response) {
                if (response != null && response.size() > 0) {
                    updateLiveData(response);
                } else {
                    if (!isFirstHit) {
                        callback.onFailure(new Exception(BaseConstants.NO_DATA));
                    }
                }
                if (isFirstHit) {
                    fetchDataFromServer();
                }
            }
        });
    }

    private boolean isFirstHit = true;

    private void fetchDataFromServer() {
        networkHandler.getSubCategories(ApiConstants.GET_SUB_CATEGORIES, mCategoryId, mChildrenLevel, new Response.CallbackImage<List<AppModel>>() {

            @Override
            public void onSuccess(List<AppModel> response, String imagePath, String pdfPath) {
                isFirstHit = false;
                fetchFromCache(imagePath, response);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    private void updateLiveData(List<AppModel> list) {
        AppModel examModel = new AppModel();
        examModel.setChildren(list);
        callback.onSuccess(examModel);
    }

}