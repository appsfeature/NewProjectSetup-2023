package com.topcoaching.util;

import com.helper.util.BasePrefUtil;
import com.topcoaching.AppApplication;

public class AppPreferences extends BasePrefUtil {

    private static final String SELECTED_CLASS_ID = "selected_class_id";
    private static final String SELECTED_CLASS_NAME = "selected_class_name";

    public static int getSelectedClassId() {
        return getInt(AppApplication.getInstance(), SELECTED_CLASS_ID, 0);
    }
    public static void setSelectedClassId(int value) {
        setInt(AppApplication.getInstance(), SELECTED_CLASS_ID, value);
    }

    public static String getSelectedClassName() {
        return getString(AppApplication.getInstance(), SELECTED_CLASS_ID, "");
    }
    public static void setSelectedClassName(String value) {
        setString(AppApplication.getInstance(), SELECTED_CLASS_NAME, value);
    }
}
