package com.topcoaching.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.topcoaching.entity.Notification;

import java.util.List;


@Dao
public interface NotificationDAO {

    //    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert
    Long insertOnlySingleRecord(Notification notificationModel);

    @Query("SELECT * FROM notification order by updatedAt desc LIMIT 2000")
    LiveData<List<Notification>> fetchAllData();

    @Query("SELECT * FROM notification WHERE uuid=:uuid LIMIT 1")
    Notification isExist(String uuid);

    @Query("SELECT * FROM notification where hasRead=0")
    List<Notification> getUnReadNotification();

    @Query("SELECT COUNT(*) FROM notification where hasRead=0")
    LiveData<Integer> getCount();

    @Query("DELETE FROM notification where id=:notificationId")
    int deleteRecord(int notificationId);

    @Query("DELETE FROM notification where uuid=:uuid")
    int deleteRecord(String uuid);

    @Query("UPDATE notification SET hasRead=1 WHERE notificationId=:notificationId")
    int setHasRead(int notificationId);

    @Query("UPDATE notification SET hasRead=1 WHERE uuid=:notificationId")
    int setHasRead(String notificationId);
}