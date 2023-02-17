package com.topcoaching.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.topcoaching.AppApplication;
import com.topcoaching.util.AppConstant;
import com.topcoaching.util.AppPreferences;

public class SplashActivity extends AppCompatActivity {

    private int mDelayTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppApplication.getInstance().init();
        super.onCreate(savedInstanceState);

        openHomeActivity();
    }

    private void openHomeActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity();
            }
        }, mDelayTime);
    }

    private void openMainActivity() {
        Intent intent = getPreviousIntent();
        if (AppPreferences.getSelectedClassId() == 0) {
            intent.setClass(this, ClassSelectionActivity.class);
            intent.putExtra(AppConstant.IS_OPEN_MAIN_ACTIVITY, true);
        } else {
            intent.setClass(this, MainActivity.class);
            intent.putExtras(getIntent());
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
        finishClass();
    }

    private void finishClass() {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishAffinity();
                }
            },500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Intent getPreviousIntent() {
        return getIntent() == null ? new Intent() : getIntent();
    }


}
