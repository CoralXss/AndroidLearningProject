package com.xss.mobile.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Desc：
 * Author: xss
 * Time：2016/2/17 11:02
 */
public abstract class BaseDialog {
    protected Dialog dialog;
    protected Context context;
    protected boolean isCanClose;

    public BaseDialog(Context context, boolean isCanClose) {

    }

    public BaseDialog(Context context, boolean isCanClose, int style) {
        this.context = context;
        dialog = new Dialog(context, style);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(isCanClose);

        setDialogContent();
    }

    public abstract void setDialogContent();

    protected void setDialogAttributes(float widthRate, float heightRate, int gravity) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (widthRate != -1) {
            lp.width = (int) (display.getWidth() * widthRate);
        }
        if (heightRate != -1) {
            lp.height = (int)(display.getHeight() * heightRate);
        }
        lp.gravity = gravity;
        window.setAttributes(lp);
    }

    /**
     * 显示对话框
     */
    public void showDialog() {
        if(dialog != null && !dialog.isShowing()) {
            if(!((Activity)context).isFinishing()) {
                dialog.show();
            }
        }
    }
    /**
     * 关闭对话框
     */
    public void closeDialog(){
        if(dialog != null && dialog.isShowing()){
            if(!((Activity)context).isFinishing()){
                dialog.dismiss();
            }
        }
    }

    /**
     * 是否正在显示
     * @return
     */
    public boolean isShowing(){
        return dialog.isShowing();
    }

    protected <T extends View> T $(int viewID) {
        return (T) dialog.findViewById(viewID);
    }
}
