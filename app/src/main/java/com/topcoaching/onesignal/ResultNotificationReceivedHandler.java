package com.topcoaching.onesignal;

import android.content.Context;

import com.config.util.Logger;
import com.helper.task.TaskRunner;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;
import com.topcoaching.AppApplication;
import com.topcoaching.database.AppDatabase;
import com.topcoaching.database.DBInterface;
import com.topcoaching.database.DBManager;
import com.topcoaching.entity.Notification;
import com.topcoaching.onesignal.util.AppNotificationHandler;
import com.topcoaching.util.AppConstant;

import org.json.JSONObject;

import java.util.concurrent.Callable;

/**
 * Created by joginder on 1/7/17.
 */

public class ResultNotificationReceivedHandler implements OneSignal.OSRemoteNotificationReceivedHandler {

    private boolean isNotificationUnique = true;

    @Override
    public void remoteNotificationReceived(Context context, OSNotificationReceivedEvent osNotificationReceivedEvent) {
        TaskRunner.getInstance().executeAsync(() -> isNotificationUnique(osNotificationReceivedEvent), result -> {
            if (isNotificationUnique) {
                AppNotificationHandler.updateJson(osNotificationReceivedEvent.getNotification());
                handleNotificationData(osNotificationReceivedEvent, osNotificationReceivedEvent.getNotification().getAdditionalData());
                insertNotification(osNotificationReceivedEvent, osNotificationReceivedEvent.getNotification().getAdditionalData());
            } else {
                displayNotification(osNotificationReceivedEvent);
            }
        });
    }

    private boolean isNotificationUnique(OSNotificationReceivedEvent osNotificationReceivedEvent) {
        DBInterface dbHelper = AppApplication.getInstance().getAppDatabase();
        isNotificationUnique = !dbHelper.isNotificationExist(osNotificationReceivedEvent.getNotification().getNotificationId());
        return true;
    }

    private void handleNotificationData(OSNotificationReceivedEvent osNotificationReceivedEvent, JSONObject additionalData) {
//        int type = additionalData.optInt(AppConstant.TYPE, AppConstant.NOTIFICATION_TYPE_DEFAULT);
//        if ( type != AppConstant.NOTIFICATION_TYPE_USER_SELECT_ARTICLE) {
//            displayNotification(osNotificationReceivedEvent);
//        } else  if (type == AppConstant.NOTIFICATION_TYPE_USER_SELECT_ARTICLE){
//            int prefId = additionalData.optInt(AppConstant.PREF_ID, 0);
//            if ( prefId > 0 && UserPreferenceManager.isCategoryIdExistsInList(MyApplication.get(), prefId) ){
//                displayNotification(osNotificationReceivedEvent);
//            }else
//                ignoreNotification(osNotificationReceivedEvent);
//        }else
        displayNotification(osNotificationReceivedEvent);
    }

    private void insertNotification(OSNotificationReceivedEvent osNotificationReceivedEvent, JSONObject data) {
        Logger.e("OneSignalExample", "data: " + data);
        if (data != null) {
            int type = data.optInt(AppConstant.TYPE);
            insertNotificationDB(osNotificationReceivedEvent, data, type);
        }
    }

    private void displayNotification(OSNotificationReceivedEvent osNotificationReceivedEvent) {
        osNotificationReceivedEvent.complete(osNotificationReceivedEvent.getNotification());
    }

    private void ignoreNotification(OSNotificationReceivedEvent notification) {
        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
        // To omit displaying a notification, pass `null` to complete()
        notification.complete(null);
    }


    private void insertNotificationDB(OSNotificationReceivedEvent notification, JSONObject data, int type) {
        Notification item = new Notification();
        item.setNotificationId(notification.getNotification().getAndroidNotificationId());
        item.setType(type);
        item.setUuid(notification.getNotification().getNotificationId());
        item.setTitle(notification.getNotification().getTitle());
        item.setBody(notification.getNotification().getBody());
        item.setJsonData(data.toString());
        item.setUpdatedAt(System.currentTimeMillis() + "");
        try {
            TaskRunner.getInstance().executeAsync(() -> {
                AppApplication.getInstance().getAppDatabase().insertNotification(item);
                return null;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
