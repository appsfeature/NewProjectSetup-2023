package com.topcoaching.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.helper.util.BaseConstants;
import com.topcoaching.fragment.NotificationFragment;


public class NotificationActivity extends AppCompatActivity {

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.config.R.layout.lib_activity_frag_holder);
        initFragment();
    }

    private void initFragment() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString(BaseConstants.TITLE);

            Runnable runnable = new Runnable() {
                public void run() {
                    Fragment fragment = new NotificationFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(com.config.R.id.content, fragment);
                    transaction.commitAllowingStateLoss();
                }
            };
            new Handler().post(runnable);
            setUpToolBar();
        } else {
            Toast.makeText(this, "Invalid bundle", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setUpToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (!TextUtils.isEmpty(title)) {
                actionBar.setTitle(title);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == android.R.id.home ){
            onBackPressed();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }
}
