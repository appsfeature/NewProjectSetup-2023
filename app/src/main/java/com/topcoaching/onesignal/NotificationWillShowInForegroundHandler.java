package com.topcoaching.onesignal;

import android.app.AlertDialog;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;
import com.topcoaching.AppApplication;
import com.topcoaching.onesignal.util.AppNotificationHandler;

import org.json.JSONObject;


public class NotificationWillShowInForegroundHandler implements OneSignal.OSNotificationWillShowInForegroundHandler{

    @Override
    public void notificationWillShowInForeground(OSNotificationReceivedEvent osNotificationReceivedEvent) {
        if ( AppApplication.getInstance() != null ){
            AppNotificationHandler.updateJson(osNotificationReceivedEvent.getNotification());
            handleForegroundNotificationUI(osNotificationReceivedEvent.getNotification());
            osNotificationReceivedEvent.complete(null);
        }else {
            osNotificationReceivedEvent.complete(osNotificationReceivedEvent.getNotification());
        }
    }

    private void handleForegroundNotificationUI(final OSNotification notification) {
        if(AppApplication.getInstance() == null || AppApplication.getInstance().getCurrentActivity() == null){
            return;
        }
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AppApplication.getInstance().getCurrentActivity(), com.helper.R.style.DialogTheme);
                    builder.setMessage(notification.getBody())
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> {
                                dialog.dismiss();
                                dialog.cancel();
                                if (notification.getAdditionalData() != null) {
                                    JSONObject data = notification.getAdditionalData();
                                    AppApplication.getInstance()
                                            .updateNotificationReadStatus(notification.getNotificationId());
                                    AppNotificationHandler.open(AppApplication.getInstance().getCurrentActivity() , notification.getAdditionalData() );
                                }
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.dismiss();
                                dialog.cancel();
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle(notification.getTitle());
                    alert.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        try {
            AppApplication.getInstance().getCurrentActivity().runOnUiThread(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
