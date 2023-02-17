package com.topcoaching.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.helper.callback.Response;
import com.topcoaching.R;
import com.topcoaching.adapter.ViewPagerAdapter;
import com.topcoaching.entity.AppModel;
import com.topcoaching.entity.ExtraProperty;
import com.topcoaching.presenter.CategoryViewModel;
import com.topcoaching.util.AppConstant;
import com.topcoaching.util.SupportUtil;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment implements Response.Callback<AppModel>{

    private Activity activity;
    private CategoryViewModel model;
    private final List<AppModel> mList = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private View llNoData;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);
        if (getActivity() != null) {
            activity = getActivity();
            model = new CategoryViewModel(activity, this);
        }
        initView(view);
        loadData();
        return view;
    }


    private void initView(View view) {
        llNoData = view.findViewById(com.helper.R.id.ll_no_data);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
    }

    private void setupViewPager() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (AppModel item : mList){
            Fragment fragment = new ListFragment();
            ExtraProperty property = getProperty();
            property.setId(item.getId());
            property.setTitle(item.getTitle());
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppConstant.CATEGORY_PROPERTY, property);
            fragment.setArguments(bundle);
            adapter.addFrag(fragment, property.getTitle());
        }

        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(mList.size());
    }

    private void loadData() {
        model.setCategoryId(getProperty().getId());
        model.loadData();
    }

    private void loadList(List<AppModel> list) {
        if (list != null && list.size() > 0) {
            mList.clear();
            mList.addAll(list);
            setupViewPager();
            SupportUtil.showNoData(llNoData, View.GONE);
        } else {
            SupportUtil.showNoData(llNoData, View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(AppModel response) {
        if (response != null && response.getChildren() != null && response.getChildren().size() > 0) {
            loadList(response.getChildren());
        }else {
            SupportUtil.showNoData(llNoData, View.VISIBLE);
        }
    }

    @Override
    public void onFailure(Exception error) {
        if (mList.size() == 0) {
            SupportUtil.showNoData(llNoData, View.VISIBLE);
        }
    }
}
