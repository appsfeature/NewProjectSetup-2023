package com.topcoaching.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.adssdk.adapter.NativeAdsListAdapter;
import com.helper.callback.Response;
import com.helper.model.common.BaseTimeViewHolder;
import com.topcoaching.AppApplication;
import com.topcoaching.R;
import com.topcoaching.entity.Notification;
import com.topcoaching.util.DynamicUrlCreator;

import java.util.List;

public class NotificationAdapter extends NativeAdsListAdapter {

    private final List<Notification> mList;
    private final Context mContext;
    private final Response.OnListClickListener<Notification> listener;
    private final DynamicUrlCreator dynamicShare;

    public NotificationAdapter(Activity context, List<Notification> mList, Response.OnListClickListener<Notification> listener) {
        super(context , mList, com.adssdk.R.layout.ads_native_unified_card, null);
        this.mContext = context;
        this.mList = mList;
        this.listener = listener;
        this.dynamicShare = new DynamicUrlCreator(context);
    }


    @Override
    protected RecyclerView.ViewHolder onAbstractCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slot_notification, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onAbstractBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ViewHolder myViewHolder = (ViewHolder) holder;
        myViewHolder.tvTitle.setText(mList.get(position).getTitle());
        myViewHolder.tvDate.setText(myViewHolder.getTimeInDaysAgoFormat(mList.get(position).getUpdatedAt()));

        if(mList.get(position).isHasRead()) {
            myViewHolder.tvTitle.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), com.helper.R.color.themeHintColor));
        } else {
            myViewHolder.tvTitle.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), com.helper.R.color.themeTextColor));
        }

        if(!TextUtils.isEmpty(mList.get(position).getBody())){
            myViewHolder.tvDescription.setText(mList.get(position).getBody());
            myViewHolder.tvDescription.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.tvDescription.setVisibility(View.GONE);
        }
    }


    private class ViewHolder extends BaseTimeViewHolder implements View.OnClickListener{
        private final TextView tvTitle, tvDate, tvDescription;
        private final View ivDelete;

        ViewHolder(View v) {
            super(v);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDescription = itemView.findViewById(R.id.tv_description);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            itemView.setOnClickListener(this);
            (itemView.findViewById(R.id.iv_share)).setOnClickListener(this);
            (itemView.findViewById(R.id.iv_whatsapp)).setOnClickListener(this);

            ivDelete.setOnClickListener(this);
            ivDelete.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.iv_share){
                if (mList != null && mList.size() > getAbsoluteAdapterPosition()) {
                    dynamicShare.shareNotification(mList.get(getAbsoluteAdapterPosition()), false);
                }
            }else if(v.getId() == R.id.iv_whatsapp){
                dynamicShare.shareNotification(mList.get(getAbsoluteAdapterPosition()), true);
            }else if(v.getId() == R.id.iv_delete){
                listener.onDeleteClicked(v, getAbsoluteAdapterPosition(), mList.get(getAbsoluteAdapterPosition()));
            }else {
                AppApplication.getInstance().updateNotificationReadStatus(
                        mList.get(getAbsoluteAdapterPosition()).getUuid());
                listener.onItemClicked(v, mList.get(getAbsoluteAdapterPosition()));
            }
        }
    }
}