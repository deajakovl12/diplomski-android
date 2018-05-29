package com.diplomski.ui.wifiactivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.diplomski.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class WifiActivity extends AppCompatActivity implements WifiRecyclerViewAdapter.Listener {

    private WifiManager mWifiManager;

    private WifiRecyclerViewAdapter wifiRecyclerViewAdapter;

    @BindView(R.id.wifi_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Odabir WiFi mreže");
        }

        wifiRecyclerViewAdapter = new WifiRecyclerViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wifiRecyclerViewAdapter.setListener(this);
        recyclerView.setAdapter(wifiRecyclerViewAdapter);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mWifiScanReceiver);
        } catch (Exception e) {
            Timber.e("not register" + e.getMessage());
        }
    }

    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    wifiRecyclerViewAdapter.setData(mWifiManager.getScanResults());
                }
            }
        }
    };

    @Override
    public void onWifiClicked(ScanResult scanResult, int position) {
        showDialog(scanResult);
    }

    public void showDialog(ScanResult scanResult) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_enter_password);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        EditText lozinka = dialog.findViewById(R.id.lozinka);
        dialogButton.setOnClickListener(v -> {
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = String.format("\"%s\"", scanResult.SSID);
            wifiConfig.preSharedKey = String.format("\"%s\"", lozinka.getText().toString());

            int netId = mWifiManager.addNetwork(wifiConfig);
            mWifiManager.disconnect();
            mWifiManager.enableNetwork(netId, true);
            mWifiManager.reconnect();
            dialog.dismiss();
        });

        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
