package com.topcoaching.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.helper.callback.Response;
import com.topcoaching.R;
import com.topcoaching.entity.AppModel;
import com.topcoaching.fragment.ClassSelectionFragment;
import com.topcoaching.util.AppConstant;
import com.topcoaching.util.AppPreferences;


public class ClassSelectionActivity extends AppCompatActivity {

    private boolean isOpenMainActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_holder);
        initFragment();
    }

    private void initFragment() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isOpenMainActivity = bundle.getBoolean(AppConstant.IS_OPEN_MAIN_ACTIVITY, false);
        }
        Runnable runnable = new Runnable() {
            public void run() {
                Fragment fragment = ClassSelectionFragment.newInstance(new Response.OnClickListener<AppModel>() {
                    @Override
                    public void onItemClicked(View view, AppModel item) {
                        onExamSelected(item);
                    }
                });
//                    fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.content, fragment);
                transaction.commitAllowingStateLoss();
            }
        };
        new Handler().post(runnable);
    }

    private void onExamSelected(AppModel item) {
        AppPreferences.setSelectedClassId(item.getId());
        AppPreferences.setSelectedClassName(item.getTitle());
        if(isOpenMainActivity){
            Intent intent = getPreviousIntent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        }else {
            setResult(Activity.RESULT_OK);
        }
        finish();
    }

    private Intent getPreviousIntent() {
        return getIntent() == null ? new Intent() : getIntent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
    }
}