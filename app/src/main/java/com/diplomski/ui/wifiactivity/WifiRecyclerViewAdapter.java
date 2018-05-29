package com.diplomski.ui.wifiactivity;


import android.net.wifi.ScanResult;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diplomski.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class  WifiRecyclerViewAdapter extends RecyclerView.Adapter<WifiRecyclerViewAdapter.WifiResultViewHolder> {

    public interface Listener {

        Listener EMPTY = (restaurantInfo, position) -> {
        };

        void onWifiClicked(ScanResult scanResult, int position);

    }


    private WifiRecyclerViewAdapter.Listener listener = WifiRecyclerViewAdapter.Listener.EMPTY;

    private final List<ScanResult> scanResults = new ArrayList<>();

    private long mLastClickTime = 0;

    @Override
    public WifiRecyclerViewAdapter.WifiResultViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_item, parent, false);
        return new WifiRecyclerViewAdapter.WifiResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final WifiRecyclerViewAdapter.WifiResultViewHolder holder, final int position) {
        holder.wifiName.setText(scanResults.get(position).SSID);
    }

    @Override
    public int getItemCount() {
        return scanResults.size();
    }

    public void setData(final List<ScanResult> data) {
        scanResults.clear();
        scanResults.addAll(data);
        notifyDataSetChanged();
    }

    public void setListener(final WifiRecyclerViewAdapter.Listener listener) {
        this.listener = listener != null ? listener : WifiRecyclerViewAdapter.Listener.EMPTY;
    }


    public class WifiResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.wifi_item_name)
        protected TextView wifiName;

        @OnClick(R.id.wifi_whole_layout)
        public void onRestaurantClicked() {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            listener.onWifiClicked(scanResults.get(getAdapterPosition()), getAdapterPosition());

        }


        public WifiResultViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}