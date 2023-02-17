package com.topcoaching.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.topcoaching.entity.ExtraProperty;
import com.topcoaching.presenter.AppPresenter;
import com.topcoaching.util.AppConstant;

public class BaseFragment extends Fragment {

    private AppPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ViewModelProvider(requireActivity()).get(AppPresenter.class);
        presenter.initialize(getArguments());
    }

    public Bundle getDefaultBundle() {
        Bundle bundle = new Bundle();
        if(presenter.getCategoryProperty() != null) {
            ExtraProperty catProperty = presenter.getCategoryProperty().getClone();
            bundle.putSerializable(AppConstant.CATEGORY_PROPERTY, catProperty);
        }
        return bundle;
    }

    public ExtraProperty getCategoryProperty() {
        if(presenter.getCategoryProperty() != null) {
            return presenter.getCategoryProperty().getClone();
        }
        return null;
    }

    public ExtraProperty getProperty() {
        if(getCategoryProperty() == null){
            return new ExtraProperty();
        }
        return getCategoryProperty();
    }

}
