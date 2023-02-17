package com.topcoaching.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.helper.util.BaseConstants;
import com.topcoaching.activity.ClassSelectionActivity;
import com.topcoaching.activity.ListActivity;
import com.topcoaching.activity.MainActivity;
import com.topcoaching.activity.NotificationActivity;
import com.topcoaching.entity.AppModel;
import com.topcoaching.entity.ExtraProperty;

public class ClassUtil {

    public static final int REQUEST_CLASS_SELECTION = 150;

    public static void openNotification(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, NotificationActivity.class)
                    .putExtra(BaseConstants.TITLE, "Notification"));
        }
    }

    public static void openClassSelection(Activity activity) {
        if (activity != null) {
            activity.startActivityForResult(new Intent(activity, ClassSelectionActivity.class), REQUEST_CLASS_SELECTION);
        }
    }

    public static void openListActivity(Context context, ExtraProperty property) {
        if (context != null) {
            context.startActivity(new Intent(context, ListActivity.class)
                    .putExtra(AppConstant.CATEGORY_PROPERTY, property));
        }
    }
}
