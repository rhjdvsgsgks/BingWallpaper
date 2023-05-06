package me.liaoheng.wallpaper.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.MalformedJsonException;

import com.bumptech.glide.load.engine.GlideException;
import com.github.liaoheng.common.util.L;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import me.liaoheng.wallpaper.BuildConfig;
import me.liaoheng.wallpaper.R;
import me.liaoheng.wallpaper.model.Config;

/**
 * @author liaoheng
 * @version 2018-04-23 23:25
 */
@SuppressLint("MissingPermission")
public class CrashReportHandle {
    private static boolean isFirebaseAnalytics;

    public static void init(Context context) {
    }

    private static void initFirebaseAnalytics() {
    }

    public static void enable(Context context) {
    }

    public static void disable(Context context) {
    }

    public static String loadFailed(Context context, String TAG, Throwable throwable) {
        String error = context.getString(R.string.network_request_error);
        if (throwable == null) {
            L.alog().e(TAG, error);
        } else {
            if (throwable instanceof GlideException) {
                GlideException e = (GlideException) throwable;
                error = context.getString(R.string.load_image_error);
                List<Throwable> causes = e.getCauses();
                if (causes != null) {
                    for (Throwable t : causes) {
                        if (t instanceof SocketTimeoutException) {
                            error = context.getString(R.string.connection_timed_out);
                            break;
                        }
                    }
                }
            } else {
                if (throwable instanceof SocketTimeoutException) {
                    error = context.getString(R.string.connection_timed_out);
                }
            }
            L.alog().e(TAG, throwable);
            collectException(context, TAG, throwable);
        }
        return error;
    }

    public static void saveWallpaper(Context context, String TAG, Throwable t) {
        L.alog().e(TAG, t, "save wallpaper error");
        if (BingWallpaperUtils.isEnableLogProvider(context)) {
            LogDebugFileUtils.get().e(TAG, "Save wallpaper error: %s", t);
        }
        collectException(context, TAG, t);
    }

    public static void collectException(Context context, String TAG, Throwable t) {
        collectException(context, TAG, null, t);
    }

    public static void collectException(Context context, String TAG, Config config, Throwable t) {
        if (check(context)) {
            return;
        }
        if (t instanceof LockSetWallpaperException) {
            return;
        }
        if (t instanceof SSLHandshakeException) {
            return;
        }
        if (t instanceof MalformedJsonException) {
            return;
        }
        if (t instanceof ConnectException) {
            return;
        }
        if (t instanceof SocketException) {
            return;
        }
        if (t instanceof SocketTimeoutException) {
            return;
        }
        if (t instanceof UnknownHostException) {
            return;
        }
    }

    private static boolean check(Context context) {
        return !Settings.isCrashReport(context) || BuildConfig.DEBUG;
    }
}
