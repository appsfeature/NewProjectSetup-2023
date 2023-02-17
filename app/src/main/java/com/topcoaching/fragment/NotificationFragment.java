package com.topcoaching.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.helper.callback.Response;
import com.helper.task.TaskRunner;
import com.helper.util.BaseUtil;
import com.helper.widget.ItemDecorationCardMargin;
import com.topcoaching.AppApplication;
import com.topcoaching.R;
import com.topcoaching.entity.Notification;
import com.topcoaching.adapter.NotificationAdapter;
import com.topcoaching.onesignal.util.AppNotificationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class NotificationFragment extends Fragment implements Response.OnDeleteListener {

    private Activity activity;
    private NotificationAdapter mAdapter;
    private final List<Notification> mList = new ArrayList<>();
    private View llNoData;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        initLayouts(view);

        BaseUtil.loadBanner(view.findViewById(R.id.rlBannerAds), activity);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchHomeAnnouncementsFromDatabase();
    }

    private void initLayouts(View view) {
        llNoData = view.findViewById(com.helper.R.id.ll_no_data);
        RecyclerView recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.addItemDecoration(new ItemDecorationCardMargin(activity));
        mAdapter = new NotificationAdapter(activity, mList, new Response.OnListClickListener<>() {
            @Override
            public void onItemClicked(View view, Notification item) {
                AppNotificationHandler.open(activity, item.getJsonData());
            }

            @Override
            public void onDeleteClicked(View view, int position, Notification item) {
                if (mList.size() > 0 && mList.size() > position && position >= 0) {
                    mList.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    mAdapter.notifyItemRangeChanged(position, mList.size());
                } else {
                    BaseUtil.showNoData(llNoData, View.VISIBLE);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRemoveItemFromList(int position) {
        TaskRunner.getInstance().executeAsync(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                AppApplication.getInstance().getAppDatabase().deleteNotification(mList.get(position).getUuid());
                return null;
            }
        }, new TaskRunner.Callback<Object>() {
            @Override
            public void onComplete(Object result) {
                if(position >=0 && mList.size() > position) {
                    mList.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    mAdapter.notifyItemRangeChanged(position, mList.size());
                }
                if(mList.size() <= 0){
                    BaseUtil.showNoData(llNoData, View.VISIBLE);
                }
            }
        });
    }


    private void fetchHomeAnnouncementsFromDatabase() {
        AppApplication.getInstance().getAppDatabase().fetchNotificationData(NotificationFragment.this, notifications -> {
            if (notifications != null && notifications.size() > 0) {
                handleObserveData(notifications);
            }else {
                handleObserveData(null);
            }
        });
    }

    private void handleObserveData(List<Notification> list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
        if (mList.size() > 0) {
            BaseUtil.showNoData(llNoData, View.GONE);
        } else {
            showNoData();
        }
    }

    private void showNoData() {
        if (mList.size() <= 0) {
            BaseUtil.showNoData(llNoData, "No Notification", View.VISIBLE);
        }
    }
}