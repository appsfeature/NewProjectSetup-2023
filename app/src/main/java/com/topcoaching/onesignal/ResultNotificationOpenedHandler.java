package com.topcoaching.onesignal;

import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.topcoaching.AppApplication;
import com.topcoaching.onesignal.util.AppNotificationHandler;

import org.json.JSONObject;

/**
 * Created by joginder on 1/7/17.
 */

public class ResultNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {
    private static final String TAG = "notificationOpened";

    @Override
    public void notificationOpened(OSNotificationOpenedResult osNotificationOpenedResult) {
        AppNotificationHandler.updateJson(osNotificationOpenedResult.getNotification());
        handleNotificationClick(osNotificationOpenedResult);
    }

    public static void handleNotificationClick(OSNotificationOpenedResult osNotificationOpenedResult) {
        JSONObject data = osNotificationOpenedResult.getNotification().getAdditionalData();
        AppApplication.getInstance()
                .updateNotificationReadStatus(
                        osNotificationOpenedResult.getNotification().getNotificationId());
        AppNotificationHandler.openSplashActivity(AppApplication.getInstance(), data);
    }
}