package com.topcoaching.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.topcoaching.entity.AppModel;

import java.util.List;


@Dao
public interface AppDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertListRecords(List<AppModel> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertOnlySingleRecord(AppModel record);

    @Query("SELECT * FROM app_model order by ranking asc")
    List<AppModel> fetchAllData();

    @Query("SELECT * FROM app_model where parent_id=:parentId order by ranking asc")
    List<AppModel> fetchAllData(int parentId);

    @Query("DELETE FROM app_model WHERE parent_id = :parentId")
    void deleteByParentId(long parentId);
}