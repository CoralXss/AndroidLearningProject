package com.xss.mobile.cache.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefUtil {
    private static final String SP_FILE_NAME = "memorandum_xml";

    public static SharedPrefUtil instance = null;
    private static SharedPreferences sp;
    private static Context context = null;

    private static final String CONTENT_TXT_SIZE = "content_txt_size";
    private static final String CONTENT_TXT_COLOR = "content_txt_color";
    private static final String SEEK_BAR_PROGRESS = "seek_bar_progress";

    private static final String SP_KEY_LOCK_GESTURE = "sp_key_lock_gesture";


    private SharedPrefUtil(Context ctx) {
        this.context = ctx;
    }

    public static synchronized SharedPrefUtil getInstance(Context ctx) {
        Context context = ctx.getApplicationContext();
        if (null == instance || instance.context != context) {
            instance = new SharedPrefUtil(ctx);
        }
        return instance;
    }

    public static SharedPreferences getSp() {
        if (sp == null) {
            sp = context.getSharedPreferences(getSpFileName(),
                    Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE | Context.MODE_PRIVATE);
        }
        return sp;
    }

    public static Editor getEdit() {
        return getSp().edit();
    }

    private static String getSpFileName() {
        return SP_FILE_NAME;
    }

    public static String getSpKeyLockGesture() {
        return getSp().getString(SP_KEY_LOCK_GESTURE, "");
    }

    public static void setSpKeyLockGesture(String lockGesture) {
        getEdit().putString(SP_KEY_LOCK_GESTURE, lockGesture).commit();
    }

    public static int getContentTxtColor() {
       // return getSp().getInt(CONTENT_TXT_COLOR, context.getResources().getColor(R.color.color_000000));
        return getSp().getInt(CONTENT_TXT_COLOR, 0);
    }

    public static void setContentTxtColor(int content_txt_color) {
        getEdit().putInt(CONTENT_TXT_COLOR, content_txt_color).commit();
    }

    public static float getSeekBarProgress() {
        return getSp().getFloat(SEEK_BAR_PROGRESS, 0.0f);
    }

    public static void setSeekBarProgress(float progress) {
        getEdit().putFloat(SEEK_BAR_PROGRESS, progress).commit();
    }


}
