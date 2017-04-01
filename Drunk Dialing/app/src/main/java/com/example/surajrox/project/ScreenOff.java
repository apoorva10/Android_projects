package com.example.surajrox.project;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ScreenOff extends DeviceAdminReceiver {
    private void showToast(Context context, String msg) {
        String status = context.getString(R.string.admin_receiver_status, msg);
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, context.getString(R.string.admin_receiver_status_enabled));
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, context.getString(R.string.admin_receiver_status_disabled));
    }
}
