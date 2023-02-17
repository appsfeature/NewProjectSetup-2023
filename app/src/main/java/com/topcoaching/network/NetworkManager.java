package com.topcoaching.network;

import android.content.Context;

import com.config.config.ConfigConstant;
import com.config.config.ConfigManager;
import com.config.util.ConfigUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.model.base.BaseDataModel;
import com.helper.network.BaseNetworkManager;
import com.helper.util.BaseConstants;
import com.helper.util.BaseUtil;
import com.topcoaching.AppApplication;
import com.topcoaching.database.AppDatabase;
import com.topcoaching.database.DBInterface;
import com.topcoaching.entity.AppModel;
import com.topcoaching.entity.CommonModel;
import com.topcoaching.util.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkManager extends BaseNetworkManager {


    private final DBInterface dbManager;
    private String host = ConfigConstant.HOST_MAIN;

    public NetworkManager(Context context) {
        dbManager = AppApplication.getInstance().getAppDatabase();
    }

    public void fetchAnnouncements(Context context, int maxContentId, Response.Callback<List<CommonModel>> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("max_id", "" + maxContentId);
        ConfigManager.getInstance().getData(ConfigConstant.CALL_TYPE_GET, host
                , AppConstant.GET_ANNOUNCEMENTS, map,  (status, data) -> {
                    if (status && !BaseUtil.isEmptyOrNull(data)) {
                        parseConfigData(data, "announcements", new TypeToken<List<CommonModel>>() {
                        }.getType(), new ParserConfigData<List<CommonModel>>() {
                            @Override
                            public void onSuccess(List<CommonModel> homeBean, String imagePath, String pdfPath) {
                                callback.onSuccess(homeBean);
                            }

                            @Override
                            public void onFailure(Exception error) {
                                callback.onFailure(error);
                            }
                        });
                    } else {
                        callback.onFailure(new Exception(BaseConstants.NO_DATA));
                    }
                });
    }

    public void getSubCategories(String methodName, int categoryId, int childrenLevel, Response.CallbackImage<List<AppModel>> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("id", "" + categoryId);
        map.put("parent_id", "" + categoryId);
        map.put("max_id", "" + 0);
        map.put("children_level", "" + childrenLevel);
        map.put("level", "" + childrenLevel);

        ConfigManager.getInstance().getData(ConfigConstant.CALL_TYPE_GET, ApiConstants.HOST_MAIN
                , methodName , map, (status, data) -> {
                    if (status && !ConfigUtil.isEmptyOrNull(data)) {
                        parseConfigData(data, "data", new TypeToken<List<AppModel>>() {
                        }.getType(), new ParserConfigData<List<AppModel>>() {
                            @Override
                            public void onSuccess(List<AppModel> homeBean, String imagePath, String pdfPath) {
                                callback.onSuccess(homeBean, imagePath, pdfPath);
                            }

                            @Override
                            public void onFailure(Exception error) {
                                callback.onFailure(error);
                            }
                        });
                    } else {
                        callback.onFailure(new Exception("No Data"));
                    }
                });
    }

}




