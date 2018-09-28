package com.twinkle.htwinkle.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.twinkle.htwinkle.R;
import com.twinkle.htwinkle.entity.Eol;
import com.twinkle.htwinkle.entity.List;
import com.twinkle.htwinkle.entity.User;
import com.twinkle.htwinkle.net.Twinkle;
import com.twinkle.htwinkle.receiver.SubjectReceiver;

import java.util.Objects;

import cn.bmob.v3.BmobUser;

public class SubjectService extends Service implements Twinkle.JwglListener {


    public SubjectService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        toGetSubject();
        alarmTime();
        return super.onStartCommand(intent, flags, startId);
    }

    private void toGetSubject() {
        Twinkle.INSTANCE.setJwglListener(this);
        Twinkle.INSTANCE.getEol(BmobUser.getCurrentUser(User.class));
    }


    @Override
    public void onJwglListenerSuccess(Object t) {

        Eol j = (Eol) t;


        if (j.getList() == null) {
            return;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        boolean isShow = sp.getBoolean("notifications_new_message", true);

        boolean vibrate = sp.getBoolean("notifications_new_message_vibrate", true);

        if (isShow) {

            NotificationManager manager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            for (java.util.List<List> aList : j.getList()) {
                for (List list1 : aList) {
                    Notification notification = new NotificationCompat.Builder(this, String.valueOf(list1.getId()))
                            .setSubText(list1.getTitle() + "")
                            .setContentTitle(list1.getSubject() + "")
                            .setContentText("截止日期：" + list1.getAbort())
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setAutoCancel(true)
                            .setDefaults(vibrate ? Notification.DEFAULT_ALL : Notification.DEFAULT_SOUND)
                            .build();

                    if (manager != null) {
                        manager.notify(list1.getId(), notification);
                    }

                }
            }

        }


    }

    @Override
    public void onJwglListenerFailure(String text) {

    }


    private void alarmTime() {

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String time = sp.getString("sync_frequency", "180");

        int anHour = Integer.parseInt(time) * 1000; // 这是的毫秒数

        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;

        Intent i = new Intent(this, SubjectReceiver.class);

        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        Objects.requireNonNull(manager).setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

    }

}
