package com.marwaeltayeb.souq.receiver;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.marwaeltayeb.souq.utils.InternetUtils.isNetworkConnected;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.marwaeltayeb.souq.adapter.OrderAdapter;
import com.marwaeltayeb.souq.databinding.ActivityOrdersBinding;
import com.marwaeltayeb.souq.localdatabase.DBHelper;
import com.marwaeltayeb.souq.model.Order;
import com.marwaeltayeb.souq.utils.OnNetworkListener;

import java.sql.SQLException;
import java.util.ArrayList;

public class NetworkChangeReceiver extends BroadcastReceiver {

    OnNetworkListener onNetworkListener;


    public void setOnNetworkListener(OnNetworkListener onNetworkListener) {
        this.onNetworkListener = onNetworkListener;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isNetworkConnected(context)) {
            onNetworkListener.onNetworkDisconnected();




        } else {
            onNetworkListener.onNetworkConnected();

        }
    }






}
