package com.topcoaching.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helper.callback.Response;
import com.topcoaching.R;
import com.topcoaching.adapter.ListAdapter;
import com.topcoaching.entity.AppModel;
import com.topcoaching.entity.ExtraProperty;
import com.topcoaching.presenter.CategoryViewModel;
import com.topcoaching.util.ClassUtil;
import com.topcoaching.util.SupportUtil;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends BaseFragment implements Response.Callback<AppModel>{

    private Activity activity;
    private CategoryViewModel model;
    private final List<AppModel> mList = new ArrayList<>();
    private ListAdapter adapter;
    private View llNoData;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        if (getActivity() != null) {
            activity = getActivity();
            model = new CategoryViewModel(activity, this);
        }
        initView(view);
        loadData();
        return view;
    }


    private void initView(View view) {
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        llNoData = view.findViewById(com.helper.R.id.ll_no_data);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ListAdapter(activity, mList, new Response.OnClickListener<AppModel>() {
            @Override
            public void onItemClicked(View view, AppModel item) {
                ExtraProperty property = getProperty();
                property.setId(item.getId());
                property.setTitle(item.getTitle());
                ClassUtil.openListActivity(view.getContext(), property);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private void loadData() {
        model.setCategoryId(getProperty().getId());
        model.loadData();
    }

    private void loadList(List<AppModel> list) {
        if (list != null && list.size() > 0) {
            mList.clear();
            mList.addAll(list);
            adapter.notifyDataSetChanged();
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
