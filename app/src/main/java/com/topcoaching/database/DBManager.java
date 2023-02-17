package com.topcoaching.database;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.RoomDatabase;

import com.helper.task.TaskRunner;
import com.helper.util.BaseUtil;
import com.topcoaching.AppApplication;
import com.topcoaching.entity.AppModel;
import com.topcoaching.entity.Notification;
import com.topcoaching.fragment.NotificationFragment;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class DBManager extends RoomDatabase implements DBInterface{

    @Override
    public List<AppModel> fetchAppData(int categoryId) {
        return AppDatabase.getInstance().appDAO().fetchAllData(categoryId);
    }

    @Override
    public void deleteAppDataByParentId(int parentId) {
        AppDatabase.getInstance().appDAO().deleteByParentId(parentId);
    }

    @Override
    public List<Long> insertAppDataList(List<AppModel> list) {
        return AppDatabase.getInstance().appDAO().insertListRecords(list);
    }

    @Override
    public void fetchNotificationCount(@NonNull LifecycleOwner owner, Observer<Integer> observer) {
        AppDatabase.getInstance().notificationDAO().getCount().observe(owner, observer);
    }

    @Override
    public void fetchNotificationData(@NonNull LifecycleOwner owner, Observer<List<Notification>> observer) {
        AppDatabase.getInstance().notificationDAO().fetchAllData().observe(owner, observer);
    }

    @Override
    public void deleteNotification(String uuid) {
        AppDatabase.getInstance().notificationDAO().deleteRecord(uuid);
    }

    @Override
    public void insertNotificationTest() {
        Notification notification = new Notification();
        notification.setUuid(System.currentTimeMillis() + "");
        notification.setTitle("Notification " + BaseUtil.getDatabaseTimeStamp());
        notification.setUpdatedAt(BaseUtil.getDatabaseTimeStamp());
        insertNotification(notification);
    }

    @Override
    public void insertNotification(Notification notification) {
        AppDatabase.getInstance().notificationDAO().insertOnlySingleRecord(notification);
    }

    @Override
    public void hasReadNotification(String uuid) {
        TaskRunner.getInstance().executeAsync(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return AppDatabase.getInstance().notificationDAO().setHasRead(uuid);
            }
        });
    }

    @Override
    public boolean isNotificationExist(String uuid) {
        Notification notification = AppDatabase.getInstance().notificationDAO().isExist(uuid);
        return notification != null;
    }
}
