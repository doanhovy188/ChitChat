package com.example.chitchat.activities;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.chitchat.R;

public class notification {

    public static AppCompatActivity appCompatActivity;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void ShowNotification(String username, String message) {
        NotificationManager notificationManager = (NotificationManager) appCompatActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "chitchat";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NotificationService", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("Sample Channel description");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(appCompatActivity, NOTIFICATION_CHANNEL_ID);

        Notification notification = notificationBuilder
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(true)
                .setContentTitle(username)
                .setContentText(message)
                .build();

        notificationManager.notify(1, notification);
    }
}
