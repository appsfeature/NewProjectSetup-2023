package com.topcoaching.listeners;

public interface AppCallback {

    interface OnNotificationReceived {
        void onNotificationReceived();
    }

    interface Notification{
        void onNotificationReceived(int count);
    }
}
