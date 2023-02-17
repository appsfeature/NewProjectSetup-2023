package com.topcoaching.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.util.BaseDynamicUrlCreator;
import com.helper.util.BaseUtil;
import com.helper.util.GsonParser;
import com.topcoaching.entity.Notification;

import java.util.HashMap;

public class DynamicUrlCreator extends BaseDynamicUrlCreator {

    public static final String TYPE_NOTIFICATION = "type_notification";
    private Response.Progress progressListener;

    public DynamicUrlCreator(Context context) {
        super(context);
    }

    public static void openActivity(Activity activity, Uri url, String extraData) {
        if (url != null && url.getQueryParameterNames() != null && url.toString().contains(ACTION_TYPE)) {
            if (url.getQueryParameter(ACTION_TYPE).equals(TYPE_NOTIFICATION)) {
                openNotification(activity, url, extraData);
            }
        }
    }

    private static void openNotification(Activity activity, Uri url, String extraData) {
        if(isValidUrlAndExtras(url, extraData)) {
            Notification mNotification = GsonParser.fromJsonAll(extraData, new TypeToken<Notification>(){});
//            ATUtility.onItemClickHandle(activity, mAnnouncement);
        }
    }

    public static boolean isValidUrlAndExtras(Uri url, String extraData) {
        return url != null && url.toString().contains(ACTION_TYPE) && url.getQueryParameter(ACTION_TYPE).equals(TYPE_NOTIFICATION) && !TextUtils.isEmpty(extraData);
    }

    public void shareNotification(Notification item, boolean isWhatsApp) {
        BaseUtil.showDialog(context, "Processing, Please wait...", true);
        HashMap<String, String> params = new HashMap<>();
        params.put(ACTION_TYPE, TYPE_NOTIFICATION);
        String extraData = toJson(item, new TypeToken<Notification>() {
        });
        generate(params, extraData, new DynamicUrlCallback() {
            @Override
            public void onDynamicUrlGenerate(String url) {
                if (progressListener != null) {
                    progressListener.onStopProgressBar();
                }
                BaseUtil.hideDialog();
                shareMe(url, item.getTitle(), isWhatsApp);
            }

            @Override
            public void onError(Exception e) {
                if (progressListener != null) {
                    progressListener.onStopProgressBar();
                }
                BaseUtil.hideDialog();
                Log.d(DynamicUrlCreator.class.getSimpleName(), "sharePdf:onError" + e.toString());
                shareMe(getPlayStoreLink(), item.getTitle(), isWhatsApp);
            }
        });
    }

    public DynamicUrlCreator addProgressListener(Response.Progress progressListener) {
        this.progressListener = progressListener;
        return this;
    }


    private String getPlayStoreLink() {
        return "http://play.google.com/store/apps/details?id=" + context.getPackageName();
    }

    @Override
    protected void onBuildDeepLink(@NonNull Uri deepLink, int minVersion, Context context, DynamicUrlCallback callback) {
        String uriPrefix = getDynamicUrl();
        if (!TextUtils.isEmpty(uriPrefix)) {
            DynamicLink.Builder builder = FirebaseDynamicLinks.getInstance()
                    .createDynamicLink()
                    .setLink(deepLink)
                    .setDomainUriPrefix(uriPrefix)
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
                            .setMinimumVersion(minVersion)
                            .build());

            // Build the dynamic link
            builder.buildShortDynamicLink().addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                @Override
                public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                    if (task.isComplete() && task.isSuccessful() && task.getResult() != null
                            && task.getResult().getShortLink() != null) {
                        callback.onDynamicUrlGenerate(task.getResult().getShortLink().toString());
                    } else {
                        callback.onError(new Exception(task.getException()));
                    }
                }
            });
        } else {
            callback.onError(new Exception("Invalid Dynamic Url"));
        }
    }

    @Override
    protected void onDeepLinkIntentFilter(Activity activity) {
        if (activity != null && activity.getIntent() != null) {
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(activity.getIntent())
                    .addOnSuccessListener(activity, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData linkData) {
                            if (resultCallBack != null && linkData != null && linkData.getLink() != null) {
                                resultCallBack.onDynamicUrlResult(linkData.getLink()
                                        , EncryptData.decode(linkData.getLink().getQueryParameter(PARAM_EXTRA_DATA)));
                            }
                        }
                    })
                    .addOnFailureListener(activity, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (resultCallBack != null) {
                                resultCallBack.onError(e);
                            }
                        }
                    });
        }
    }

    public void shareMe(String deepLink, String title, boolean isWhatsApp) {
        if (BaseUtil.isValidUrl(deepLink)) {
            Log.d(DynamicUrlCreator.class.getSimpleName(), deepLink);
            String text = title + "\n\n" + "Chick here to open : \n" + deepLink;

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.setType("text/plain");
            if ( isWhatsApp )
                intent.setPackage("com.whatsapp");

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(intent, "Share With")
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}