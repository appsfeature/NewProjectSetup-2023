package com.topcoaching.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.helper.util.DayNightPreference;
import com.helper.util.SocialUtil;
import com.helper.util.StyleUtil;
import com.topcoaching.AppApplication;
import com.topcoaching.R;
import com.topcoaching.entity.ExtraProperty;
import com.topcoaching.fragment.HomeFragment;
import com.topcoaching.listeners.AppCallback;
import com.topcoaching.onesignal.util.AppNotificationHandler;
import com.topcoaching.util.AppConstant;
import com.topcoaching.util.AppPreferences;
import com.topcoaching.util.ClassUtil;

import java.util.Locale;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupBottomNavigation();
        initUi();
        onNewIntent(getIntent());
        setBottomNavigationViewToHome();
    }

    private void initUi() {
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemTextColor(StyleUtil.getBottomBarColor(this));
        navigationView.setItemIconTintList(StyleUtil.getBottomBarColor(this));

        MenuItem mDayNight = navigationView.getMenu().findItem(R.id.nav_day_night);
        SwitchCompat dayNight = (SwitchCompat) mDayNight.getActionView();
        dayNight.setChecked(DayNightPreference.isNightModeEnabled(this));
        dayNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DayNightPreference.setNightMode(MainActivity.this, isChecked);
            }
        });
    }

    private void setBottomNavigationViewToHome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBottomNavigationView != null) {
                    mBottomNavigationView.setSelectedItemId(R.id.menu_home);
                }
            }
        }, 100);
    }

    private void setupBottomNavigation() {
        mBottomNavigationView = findViewById(R.id.navigation);
        if (mBottomNavigationView != null) {
            mBottomNavigationView.setItemIconTintList(null);
            mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    updateTitle(item.getTitle().toString());
                    if (itemId == R.id.menu_home) {
                        fragmentMapping(getHomeFragment());
                        return true;
                    } else if (itemId == R.id.menu_library) {
//                        fragmentMapping(getUpdatesFragment());
                        return true;
                    } else if (itemId == R.id.menu_saved) {
//                        fragmentMapping(getNotificationFragment());
                        return true;
                    } else if (itemId == R.id.menu_profile) {
//                        fragmentMapping(new ProfileFragment());
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private HomeFragment fragmentHome;

    private Fragment getHomeFragment() {
        if(fragmentHome == null) fragmentHome = newHomeFragment();
        return fragmentHome;
    }

    private HomeFragment newHomeFragment() {
        fragmentHome = new HomeFragment();
        Bundle bundle = getDefaultBundle();
        ExtraProperty property = new ExtraProperty();
        property.setId(AppPreferences.getSelectedClassId());
        bundle.putSerializable(AppConstant.CATEGORY_PROPERTY, property);
        fragmentHome.setArguments(bundle);
        return fragmentHome;
    }

    private void fragmentMapping(Fragment fragment ) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commitAllowingStateLoss();
    }

    private void updateTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_action_drawer);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Home");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ClassUtil.REQUEST_CLASS_SELECTION){
            if(resultCode == Activity.RESULT_OK){
                newHomeFragment();
                setBottomNavigationViewToHome();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initDataFromArgs();
    }

    boolean isAppNotificationHandlerNotInitialize = true;

    private void initDataFromArgs() {
        if(isAppNotificationHandlerNotInitialize) {
            AppNotificationHandler.init(this, getIntent());
            isAppNotificationHandlerNotInitialize = false;
        }
    }


    private TextView tvMenuNotificationCount;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);

        View view = menu.findItem(R.id.menu_item_notification).getActionView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassUtil.openNotification(MainActivity.this);
            }
        });
        tvMenuNotificationCount = view.findViewById(R.id.tv_menu_notification);
        addNotificationCountListener();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNotificationCountListener() {
        AppApplication.getInstance().addNotificationCountListener(this, new AppCallback.Notification() {
            @Override
            public void onNotificationReceived(int count) {
                if(tvMenuNotificationCount != null) {
                    if (count > 0) {
                        tvMenuNotificationCount.setText(String.format(Locale.ENGLISH, "%d", count));
                        tvMenuNotificationCount.setVisibility(View.VISIBLE);
                    } else {
                        tvMenuNotificationCount.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle app_navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_share) {
            SocialUtil.share(this, "");
        } else if (id == R.id.nav_rate_us) {
            SocialUtil.rateUs(this);
        }else if (id == R.id.nav_change_class) {
            ClassUtil.openClassSelection(this);
        }
//        else if (id == R.id.nav_more_apps) {
//            SocialUtil.moreApps(this, getString(R.string.account_name));
//        } else if (id == R.id.nav_leader_board) {
//            AppApplication.getInstance().openMockLeaderBoard(this);
//        } else if (id == R.id.privacy_policy) {
//            if (SupportUtil.isConnected(this)) {
//                SocialUtil.openLinkInAppBrowser(this, "Privacy Policy", getString(R.string.privacy_policy_url));
//            } else {
//                SupportUtil.showToast(this, AppConstant.NO_INTERNET_CONNECTION);
//            }
//        } else if (id == R.id.nav_bookmark_que) {
//            AppApplication.getInstance().openMCQBookmark(this);
//        } else if (id == R.id.nav_downloaded_books) {
//            PDFViewer.openPdfDownloadedListActivity(this);
//        } else if (id == R.id.nav_bookmark_pdf) {
//            PDFViewer.openPdfBookmarkActivity(this);
//        } else if (id == R.id.nav_login) {
//            UserProfileActivity.open(MainActivity.this, true, true);
//        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}