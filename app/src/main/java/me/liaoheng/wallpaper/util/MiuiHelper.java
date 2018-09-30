package me.liaoheng.wallpaper.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.liaoheng.common.util.ShellUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author liaoheng
 * @version 2018-09-19 15:57
 */
public class MiuiHelper {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setLockScreenWallpaper(Context context, File bitmap) throws IOException {
        if (BingWallpaperUtils.isMiuiLockScreenSupport(context) && ShellUtils.hasRootPermission()) {
            ShellUtils.CommandResult commandResult = ShellUtils.execCommand(
                    "cp " + bitmap.getAbsolutePath() + " /data/system/theme/lock_wallpaper", true, true);
            if (commandResult.result == 0) {
                ShellUtils.execCommand("chmod 755 /data/system/theme/lock_wallpaper", true);
            }
        } else {
            BingWallpaperUtils.setLockScreenWallpaper(context, bitmap);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setBothWallpaper(Context context, File bitmap) throws IOException {
        BingWallpaperUtils.setBothWallpaper(context, bitmap);
        setLockScreenWallpaper(context, bitmap);
    }
}
