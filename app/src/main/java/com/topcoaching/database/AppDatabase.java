package com.topcoaching.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.topcoaching.AppApplication;
import com.topcoaching.entity.AppModel;
import com.topcoaching.entity.CommonModel;
import com.topcoaching.entity.Notification;


@Database(entities = {CommonModel.class, Notification.class, AppModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends DBManager {

    private static final String DB_NAME = "top-coaching-db";
    private static AppDatabase appDatabase;

    public static AppDatabase getInstance() {
        if (appDatabase == null) appDatabase = AppDatabase.create(AppApplication.getInstance());
        return appDatabase;
    }

    public abstract CommonDAO commonDAO();
    public abstract AppDAO appDAO();
    public abstract NotificationDAO notificationDAO();

    public static AppDatabase create(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }
}