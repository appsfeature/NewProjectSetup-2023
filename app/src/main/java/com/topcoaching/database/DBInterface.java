package com.topcoaching.database;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.topcoaching.entity.AppModel;
import com.topcoaching.entity.Notification;

import java.util.List;

public interface DBInterface {
    void deleteAppDataByParentId(int parentId);

    List<Long> insertAppDataList(List<AppModel> list);

    List<AppModel> fetchAppData(int categoryId);

    void fetchNotificationCount(@NonNull LifecycleOwner owner, Observer<Integer> observer);

    void fetchNotificationData(@NonNull LifecycleOwner owner, Observer<List<Notification>> observer);

    void deleteNotification(String uuid);

    void insertNotificationTest();

    void insertNotification(Notification notification);

    void hasReadNotification(String uuid);

    boolean isNotificationExist(String uuid);
}
