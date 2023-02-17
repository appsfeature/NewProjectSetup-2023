package com.topcoaching.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.topcoaching.entity.CommonModel;

import java.util.List;

@Dao
public interface CommonDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertListRecords(List<CommonModel> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertOnlySingleRecord(CommonModel record);

    @Query("SELECT * FROM common_data order by updated_at desc LIMIT 3")
    List<CommonModel> fetchHomeAnnouncements();

    @Query("SELECT id FROM common_data order by updated_at desc LIMIT 1")
    int getMaxContentId();

    @Query("SELECT * FROM common_data order by updated_at desc")
    LiveData<List<CommonModel>> fetchAllData();

    @Query("SELECT COUNT(*) FROM common_data where has_read=0")
    LiveData<Integer> getCount();

    @Query("SELECT id FROM common_data where has_read=1")
    List<Integer> getHasReadList();

    @Query("UPDATE common_data SET has_read='1' WHERE id=:contentId")
    int setHasRead(int contentId);

}