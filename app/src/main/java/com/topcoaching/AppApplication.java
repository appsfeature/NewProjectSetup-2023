package com.topcoaching;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.config.config.ConfigManager;
import com.config.util.ConfigUtil;
import com.config.util.CryptoUtil;
import com.helper.application.ActivityLifecycleObserver;
import com.helper.application.BaseApplication;
import com.helper.callback.ActivityLifecycleListener;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.topcoaching.database.AppDatabase;
import com.topcoaching.database.DBInterface;
import com.topcoaching.database.DBManager;
import com.topcoaching.listeners.AppCallback;
import com.topcoaching.onesignal.NotificationWillShowInForegroundHandler;
import com.topcoaching.onesignal.ResultNotificationOpenedHandler;
import com.topcoaching.util.SupportUtil;


public class AppApplication extends BaseApplication {

    private static AppApplication _instance;
    private ConfigManager configManager;
    private DBInterface antDatabase;

    public static AppApplication getInstance() {
        return _instance;
    }

    public ConfigManager getConfigManager() {
        if (configManager == null) {
            configManager = ConfigManager.getInstance(this, ConfigUtil.getSecurityCode(this), CryptoUtil.getUuidEncrypt(this), isDebugMode())
                    .setSecurityCodeEnc(true)
                    .setRequestTimeout(2)
                    .setEnableStatistics(false);
        }
        return configManager;
    }

    @Override
    public boolean isDebugMode() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        initOneSignal();
        ActivityLifecycleObserver.getInstance().register(this);
    }

    private boolean isLoaded = false;

    public void init() {
        if (!isLoaded) {
            configManager = getConfigManager();
            loadConfigFromServer();
            initRemoteConfig();
            setEnableCurrentActivityLifecycle(true);

            addActivityLifecycleListener(hashCode(), new ActivityLifecycleListener() {
                @Override
                public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                }

                @Override
                public void onActivityResumed(@NonNull Activity activity) {
                    mCurrentActivity = activity;
                }
            });
            isLoaded = true;
        }
    }

    public DBInterface getAppDatabase() {
        if (antDatabase == null) {
            antDatabase = AppDatabase.getInstance();
        }
        return antDatabase;
    }

    public void loadConfigFromServer(){
        if ((!TextUtils.isEmpty(CryptoUtil.getUuidEncrypt(this)) || !getConfigManager().isSecurityCodeEnc) && !getConfigManager().isConfigLoaded()) {
            configManager.loadConfig();
        }else if(TextUtils.isEmpty(CryptoUtil.getUuidEncrypt(this))){
            initRemoteConfig();
        }
    }


    private void initRemoteConfig() {
        CryptoUtil.initRemoteConfig(getInstance(), new CryptoUtil.onRemoteConfigLoad() {
            @Override
            public void onRemoteConfigLoad() {
                if (configManager != null){
                    configManager.setSecurityCodeEnc(CryptoUtil.getUuidEncrypt(getInstance()));
                    if (!configManager.isConfigLoaded()) {
                        loadConfigFromServer();
                    }
                }
            }
        });
    }

    private boolean isEnableOpenDynamicLink = true;

    public void setEnableOpenDynamicLink(boolean fromNightMode) {
        isEnableOpenDynamicLink = fromNightMode;
    }

    public boolean isEnableOpenDynamicLink() {
        return isEnableOpenDynamicLink;
    }

    public void addNotificationCountListener(AppCompatActivity activity, AppCallback.Notification listener) {
        AppApplication.getInstance().getAppDatabase().fetchNotificationCount(activity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                listener.onNotificationReceived(count);
            }
        });
    }


    private void initOneSignal() {
        String oneSignalId = "";

        if (TextUtils.isEmpty(oneSignalId))
            oneSignalId = getOneSignalApiKey();
        if (TextUtils.isEmpty(oneSignalId)) {
            throw new NullPointerException("oneSignalId is Null in String resource. Please add the one signal id for the current Flavour");
        }

        Log.e("initOneSignal", oneSignalId);

        OneSignal.initWithContext(this);
        OneSignal.setAppId(oneSignalId);
        OneSignal.setNotificationOpenedHandler(new ResultNotificationOpenedHandler());

        OneSignal.setNotificationWillShowInForegroundHandler(getNotificationWillShowInForegroundHandler());

        if (isDebugMode())
            OneSignal.sendTag("DebugUser", "DebugUser");

        if ( OneSignal.getDeviceState() != null ) {
            String UUID =  OneSignal.getDeviceState().getUserId();
            Log.e( "OneSignal", "getDeviceState : " + UUID  );
        }
    }

    public String getOneSignalApiKey() {
        return SupportUtil.getManifestMetaData(this, "com.apikey.onesignal");
    }

    public NotificationWillShowInForegroundHandler getNotificationWillShowInForegroundHandler() {
        if ( notificationWillShowInForegroundHandler == null ){
            notificationWillShowInForegroundHandler = new NotificationWillShowInForegroundHandler();
        }
        return notificationWillShowInForegroundHandler;
    }

    private NotificationWillShowInForegroundHandler notificationWillShowInForegroundHandler;


    private Activity mCurrentActivity;

    @Nullable
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void updateNotificationReadStatus(String uuid) {
        AppApplication.getInstance().getAppDatabase().hasReadNotification(uuid);
    }

    public Picasso getPicasso() {
        return Picasso.get();
    }
}
