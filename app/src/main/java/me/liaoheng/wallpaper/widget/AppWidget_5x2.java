package me.liaoheng.wallpaper.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.firebase.jobdispatcher.Constraint;
import com.github.liaoheng.common.util.L;

import me.liaoheng.wallpaper.R;
import me.liaoheng.wallpaper.data.BingWallpaperNetworkClient;
import me.liaoheng.wallpaper.model.BingWallpaperCoverStory;
import me.liaoheng.wallpaper.model.BingWallpaperImage;
import me.liaoheng.wallpaper.util.BingWallpaperUtils;
import me.liaoheng.wallpaper.util.Constants;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author liaoheng
 * @version 2018-06-11 09:38
 */
public class AppWidget_5x2 extends BaseAppWidget {

    public static void start(Context context, BingWallpaperImage bingWallpaperImage) {
        if (!getWidgetActive(context, Constants.PREF_APPWIDGET_5X2_ENABLE)) {
            return;
        }
        start(context, AppWidget_5x2.class, bingWallpaperImage);
    }

    protected void setWidgetActive(Context context, boolean active) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(Constants.PREF_APPWIDGET_5X2_ENABLE, active).apply();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        L.alog().d(TAG, "onReceive action: %s", intent.getAction());
        super.onReceive(context, intent);
    }

    @Override
    protected void setText(final Context context, final BingWallpaperImage image) {
        final RemoteViews remoteViews = getRemoteViews(context, R.layout.view_appwidget_5x2);

        if (image == null) {
            remoteViews.setTextViewText(R.id.app_widget_title, "failure !");
            return;
        }

        if (BingWallpaperUtils.isChinaLocale(context)) {
            BingWallpaperNetworkClient.getCoverStory()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Action1<BingWallpaperCoverStory>() {

                                @Override
                                public void call(BingWallpaperCoverStory bingWallpaperCoverStory) {
                                    remoteViews.setTextViewText(R.id.app_widget_title,
                                            bingWallpaperCoverStory.getTitle());
                                    remoteViews.setTextViewText(R.id.app_widget_content,
                                            bingWallpaperCoverStory.getPara1()
                                                    + bingWallpaperCoverStory.getPara2());

                                    update(context, AppWidget_5x2.class, remoteViews);
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {

                                }
                            });
        } else if (!TextUtils.isEmpty(image.getCaption())) {
            remoteViews.setTextViewText(R.id.app_widget_title, image.getCaption());
            remoteViews.setTextViewText(R.id.app_widget_content, image.getDesc());
            update(context, AppWidget_5x2.class, remoteViews);
        } else {
            remoteViews.setTextViewText(R.id.app_widget_title, image.getCopyright());
            remoteViews.setTextViewText(R.id.app_widget_content, "");
            update(context, AppWidget_5x2.class, remoteViews);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = getRemoteViews(context, R.layout.view_appwidget_5x2);

        addTitleClick(context, AppWidget_5x2.class, remoteViews);
        addContentClick(context, AppWidget_5x2.class, remoteViews);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }
}
