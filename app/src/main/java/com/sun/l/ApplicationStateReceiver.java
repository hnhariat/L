package com.sun.l;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sunje on 2016-03-14.
 */
public class ApplicationStateReceiver extends BroadcastReceiver {

    public interface OnApplicationStateListener {
        void onInstall();

        void onAdd();

        void onRemove();
    }

    OnApplicationStateListener mOnApplicationStateListener;

    public void setOnApplicationStateListener(OnApplicationStateListener l) {
        mOnApplicationStateListener = l;
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        // TODO Auto-generated method stub
        if (mOnApplicationStateListener == null) {
            try {
                throw new Exception("Listener is not seted");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("L.br", "action : " + arg1.getAction());
        if (arg1.getAction().equals(Intent.ACTION_INSTALL_PACKAGE)) {
            mOnApplicationStateListener.onInstall();
        } else if (arg1.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            mOnApplicationStateListener.onAdd();
        } else if (arg1.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            mOnApplicationStateListener.onRemove();
        }
    }
}
