package me.liaoheng.wallpaper.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.liaoheng.common.util.L;

import com.github.liaoheng.common.util.NetworkUtils;
import me.liaoheng.wallpaper.util.*;
import org.joda.time.LocalTime;

import me.liaoheng.wallpaper.widget.AppWidget_5x1;
import me.liaoheng.wallpaper.widget.AppWidget_5x2;

/**
 * 接收定时闹钟与开机自启事件
 *
 * @author liaoheng
 * @version 2016-09-19 15:49
 */
public class AutoSetWallpaperBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION = "me.liaoheng.wallpaper.ALARM_TASK_SCHEDULE";
    private final String TAG = AutoSetWallpaperBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        L.alog().d(TAG, "action : %s", intent.getAction());
        if (BingWallpaperUtils.isEnableLog(context)) {
            LogDebugFileUtils.get()
                    .i(TAG, "action  : %s", intent.getAction());
        }
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            AppWidget_5x1.start(context, null);
            AppWidget_5x2.start(context, null);

            if (BingWallpaperJobManager.isJobTypeDaemonService(context)) {
                BingWallpaperJobManager.startDaemonService(context);
                return;
            }

            LocalTime dayUpdateTime = BingWallpaperUtils.getDayUpdateTime(context);
            if (dayUpdateTime == null) {
                return;
            }
            BingWallpaperAlarmManager.enabled(context, dayUpdateTime);
            return;
        }
        if (ACTION.equals(intent.getAction())) {
            if (NetworkUtils.isConnected(context)) {
                return;
            }
            if (BingWallpaperUtils.getOnlyWifi(context)) {
                if (!NetworkUtils.isWifiConnected(context)) {
                    return;
                }
            }
            if (TasksUtils.isToDaysDoProvider(context, 1,
                    BingWallpaperIntentService.FLAG_SET_WALLPAPER_STATE)) {
                BingWallpaperIntentService.start(context,
                        BingWallpaperUtils.getAutoModeValue(context));
            }
        }
    }
}
