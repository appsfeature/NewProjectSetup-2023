package com.topcoaching.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.topcoaching.R;
import com.topcoaching.fragment.HomeFragment;


public class ListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_holder);
        initFragment();
    }

    private void initFragment() {
        final Bundle bundle = getIntent().getExtras();
        Runnable runnable = new Runnable() {
            public void run() {
                Fragment fragment = new HomeFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.content, fragment);
                transaction.commitAllowingStateLoss();
            }
        };
        setUpToolBar();
        new Handler().post(runnable);
    }

    private void setUpToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (!TextUtils.isEmpty(getProperty().getTitle())) {
                actionBar.setTitle(getProperty().getTitle());
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

