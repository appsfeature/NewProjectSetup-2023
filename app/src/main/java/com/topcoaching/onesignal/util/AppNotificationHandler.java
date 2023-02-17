package com.topcoaching.onesignal.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.helper.util.BaseConstants;
import com.helper.util.BaseDynamicUrlCreator;
import com.helper.util.BaseUtil;
import com.helper.util.GsonParser;
import com.helper.util.SocialUtil;
import com.onesignal.OSNotification;
import com.topcoaching.AppApplication;
import com.topcoaching.R;
import com.topcoaching.activity.SplashActivity;
import com.topcoaching.util.AppConstant;
import com.topcoaching.util.DynamicUrlCreator;

import org.json.JSONObject;

public class AppNotificationHandler {

    public static final String TAG = AppNotificationHandler.class.getSimpleName();

    public static void init(Activity activity, Intent intent) {
        if(intent != null) {
            if (intent.getData() != null && intent.getData().getAuthority().equals(activity.getString(R.string.url_public_domain_host_manifest))) {
                registerDynamicLinks(activity);
            } else {
                AppNotificationHandler.open(activity, intent);
            }
        }
    }

    public static void open(Activity activity, OneSignalModel item) {
        if (activity != null && item != null) {
            if (item.getType() != NotificationType.TYPE_DEFAULT) {
                int type = item.getType();
                if (type == NotificationType.TYPE_BROWSER_IN_APP) {
                    if(BaseUtil.isValidUrl(item.getUrl())) {
                        SocialUtil.openLinkInBrowserApp(activity, item.getTitle(), item.getUrl());
                    }
                } else if (type == NotificationType.TYPE_BROWSER_OUTSIDE) {
                    if(BaseUtil.isValidUrl(item.getUrl())) {
                        SocialUtil.openLinkInBrowserChrome(activity, item.getUrl());
                    }
                } else if (type == NotificationType.TYPE_PLAY_STORE_URL) {
                    if(!TextUtils.isEmpty(item.getAppId())) {
                        SocialUtil.openAppInPlayStore(activity, item.getAppId());
                    }else if(!TextUtils.isEmpty(item.getUrl())) {
                        SocialUtil.openLinkInBrowserChrome(activity, item.getUrl());
                    }
                }
            }
        }
    }

    public static void open(Activity activity, Intent intent) {
        if (activity != null && intent != null && intent.getSerializableExtra(BaseConstants.CATEGORY_PROPERTY) != null) {
            if (intent.getSerializableExtra(BaseConstants.CATEGORY_PROPERTY) instanceof OneSignalModel) {
                OneSignalModel oneSignal = (OneSignalModel) intent.getSerializableExtra(BaseConstants.CATEGORY_PROPERTY);
                open(activity, oneSignal);
            }
        }
    }

    public static void open(Activity activity, JSONObject jsonData) {
        open(activity, getModel(jsonData));
    }

    public static void open(Activity activity, String jsonData) {
        open(activity, getModel(jsonData));
    }

    public static OneSignalModel getModel(JSONObject jsonData) {
        return GsonParser.fromJsonAll(jsonData.toString(), OneSignalModel.class);
    }

    public static OneSignalModel getModel(String jsonData) {
        return GsonParser.fromJsonAll(jsonData, OneSignalModel.class);
    }

    public static void openSplashActivity(Context context, JSONObject data) {
        try {
            if (data != null) {
                Intent intent = new Intent(context, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(BaseConstants.CATEGORY_PROPERTY, AppNotificationHandler.getModel(data));
                context.startActivity(intent);
            } else {
                com.helper.util.Logger.e(TAG, "Notification Data is Null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            com.helper.util.Logger.e(TAG, e.toString());
        }
    }

    public static void updateJson(OSNotification notification) {
        try {
            notification.getAdditionalData().putOpt(AppConstant.BODY , notification.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void registerDynamicLinks(Activity activity) {
        if (AppApplication.getInstance().isEnableOpenDynamicLink()) {
            new DynamicUrlCreator(activity)
                    .register(new BaseDynamicUrlCreator.DynamicUrlResult() {
                        @Override
                        public void onDynamicUrlResult(Uri uri, String extraData) {
                            DynamicUrlCreator.openActivity(activity, uri, extraData);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("PDFDynamicShare", "onError" + e.toString());
                        }
                    });
        }
        AppApplication.getInstance().setEnableOpenDynamicLink(true);
    }
}
