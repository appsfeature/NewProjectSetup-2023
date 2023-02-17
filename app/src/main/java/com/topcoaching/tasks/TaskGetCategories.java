package com.topcoaching.tasks;

import com.helper.task.TaskRunner;
import com.topcoaching.AppApplication;
import com.topcoaching.entity.AppModel;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;


public class TaskGetCategories {
    private final String imagePath;
    private final List<AppModel> catBeans;
    private final int categoryId;

    public TaskGetCategories(int mCategoryId, String imagePath, List<AppModel> catBeans) {
        this.categoryId = mCategoryId;
        this.imagePath = imagePath;
        this.catBeans = catBeans;
    }

    public void execute(TaskRunner.Callback<List<AppModel>> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<List<AppModel>>() {
            @Override
            public List<AppModel> call() throws Exception {
                if (catBeans != null && catBeans.size() > 0) {
                    for (AppModel item : catBeans) {
                        item.setParentId(categoryId);
                        if (item.getImage() != null && !item.getImage().equals("")) {
                            item.setImage(imagePath + item.getImage());
                        }
                        if (item.getChildren() != null) {
                            for (AppModel child : item.getChildren()) {
                                child.setParentId(item.getId());
                                if (child.getImage() != null && !child.getImage().equals("")) {
                                    child.setImage(imagePath + child.getImage());
                                }
                                if (child.getChildren() != null) {
                                    for (AppModel subChild : child.getChildren()) {
                                        if (subChild.getImage() != null && !subChild.getImage().equals("")) {
                                            subChild.setImage(imagePath + subChild.getImage());
                                        }
                                        subChild.setParentId(child.getId());
                                    }
                                    insertList(child.getChildren(), child.getId());
                                }
                            }
                            insertList(item.getChildren(), item.getId());
                        }
                    }
                    insertList(catBeans, categoryId);
                }

                List<AppModel> list = AppApplication.getInstance().getAppDatabase().fetchAppData(categoryId);
                List<AppModel> savedList = null;
                if (list != null && list.size() > 0) {
                    for (AppModel item : list) {
                        if (savedList != null)
                            for (AppModel savedItem : savedList) {
                                if (item.getId() == savedItem.getId()) {
                                    item.setRanking(savedItem.getRanking());
                                }
                            }
                        item.setChildren(AppApplication.getInstance().getAppDatabase().fetchAppData(item.getId()));
                        for (AppModel subItem : item.getChildren()) {
                            subItem.setChildren(AppApplication.getInstance().getAppDatabase().fetchAppData(subItem.getId()));
                            sortArrayList(subItem.getChildren());
                        }
                        sortArrayList(item.getChildren());
                    }
                    sortArrayList(list);
                }
                return list;
            }
        }, callback);
    }

    private void sortArrayList(List<AppModel> list) {
        Collections.sort(list, (item, item2) -> {
            Integer value = item.getRanking();
            Integer value2 = item2.getRanking();
            return value.compareTo(value2);
        });
    }

    private void insertList(List<AppModel> list, int parentId) {
        try {
            AppApplication.getInstance().getAppDatabase().deleteAppDataByParentId(parentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<Long> longs = AppApplication.getInstance().getAppDatabase().insertAppDataList(list);
//            if ( longs != null && longs.size() > 0 ) {
//                Logger.log("longs : " + longs.size());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
