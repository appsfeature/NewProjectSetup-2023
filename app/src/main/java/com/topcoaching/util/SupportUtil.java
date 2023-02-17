package com.topcoaching.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.helper.util.BaseUtil;

public class SupportUtil extends BaseUtil {

    public static String getManifestMetaData(Context context, String key) {
        try {
            ApplicationInfo ai= context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if(value != null){
                return value.toString();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
