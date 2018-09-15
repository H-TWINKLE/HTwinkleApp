package com.twinkle.htwinkle.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UserInfoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (infoUpdate != null) {
            infoUpdate.onReceiveToUpdate();
        }

    }

    public interface onUserInfoUpdate {
        void onReceiveToUpdate();
    }

    private onUserInfoUpdate infoUpdate;

    public void setInfoUpdate(onUserInfoUpdate infoUpdate) {
        this.infoUpdate = infoUpdate;
    }
}
