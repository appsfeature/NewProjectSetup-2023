package com.topcoaching.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.helper.callback.Response;
import com.topcoaching.R;
import com.topcoaching.entity.AppModel;
import com.topcoaching.util.AppPreferences;

import java.util.List;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CommonViewModel> {

    private final Response.OnClickListener<AppModel> callback;
    private final Context context;
    private List<AppModel> items;


    public ListAdapter(Context context, List<AppModel> items, Response.OnClickListener<AppModel> callback) {
        this.context = context;
        this.items = items;
        this.callback = callback;
    }

    @Override
    @NonNull
    public CommonViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_class_selection, parent, false);
        return new CommonViewModel(item);
    }

    @Override
    public void onBindViewHolder(final CommonViewModel holder, final int position) {
        final AppModel item = items.get(position);

        holder.tvName.setText(item.getTitle());

        if (item.getId() == AppPreferences.getSelectedClassId()) {
            holder.mainView.setBackgroundResource(R.drawable.bg_class_selected);
            holder.tvName.setTextColor(Color.WHITE);
        } else {
            holder.mainView.setBackgroundResource(R.drawable.bg_class_default);
            holder.tvName.setTextColor(ContextCompat.getColor(context, com.helper.R.color.themeTextColor));
        }
        holder.itemView.setOnClickListener(v -> callback.onItemClicked(v, item));
    }

    private String getTag(String title) {
        try {
            if(!TextUtils.isEmpty(title)) {
                String[] mSplit = title.split(" ");
                if(mSplit.length >= 2){
                    return (mSplit[0].substring(0, 1) + mSplit[1].substring(0, 1)).toUpperCase();
                }else {
                    if (title.length() > 2) {
                        return title.substring(0, 2).toUpperCase();
                    } else {
                        return title;
                    }
                }
            }else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    class CommonViewModel extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final View mainView;

        private CommonViewModel(final View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_title);
            mainView = v.findViewById(R.id.main_view);
        }
    }


}

