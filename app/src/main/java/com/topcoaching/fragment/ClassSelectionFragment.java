package com.topcoaching.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helper.callback.Response;
import com.topcoaching.R;
import com.topcoaching.adapter.ClassSelectionAdapter;
import com.topcoaching.entity.AppModel;
import com.topcoaching.presenter.CategoryViewModel;
import com.topcoaching.util.AppConstant;
import com.topcoaching.util.SupportUtil;

import java.util.ArrayList;
import java.util.List;


public class ClassSelectionFragment extends Fragment implements Response.Callback<AppModel>, Response.OnClickListener<AppModel> {

    private Activity activity;
    private CategoryViewModel model;
    private final List<AppModel> mList = new ArrayList<>();
    private ClassSelectionAdapter adapter;
    private View llNoData;
    private Response.OnClickListener<AppModel> clickListener;

    public static ClassSelectionFragment newInstance(Response.OnClickListener<AppModel> clickListener) {
        ClassSelectionFragment fragment = new ClassSelectionFragment();
        fragment.clickListener = clickListener;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_selection, container, false);
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
        mRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ClassSelectionAdapter(activity, mList, this);
        mRecyclerView.setAdapter(adapter);
    }

    private void loadData() {
        model.setCategoryId(AppConstant.DEFAULT_CATEGORY_ID);
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
        if (mList == null || mList.size() == 0) {
            SupportUtil.showNoData(llNoData, View.VISIBLE);
        }
    }

    @Override
    public void onItemClicked(View view, AppModel item) {
        if (clickListener != null) {
            clickListener.onItemClicked(view, item);
        }
    }
}
