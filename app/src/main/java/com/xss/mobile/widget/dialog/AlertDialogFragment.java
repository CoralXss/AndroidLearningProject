package com.xss.mobile.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xss.mobile.R;
import com.xss.mobile.utils.DensityUtil;

/**
 * Created by xss on 2016/10/10.
 */
public class AlertDialogFragment extends DialogFragment {
    private Builder builder;

    private TextView mContent;
    private TextView mTitle;
    private TextView mPosBtn;
    private TextView mNegBtn;
    private View mDivider;

    public AlertDialogFragment() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.alert_dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_alert, container, false);

        mContent = (TextView) v.findViewById(R.id.dialog_alert_content);
        mTitle = (TextView) v.findViewById(R.id.dialog_alert_title);
        mPosBtn = (TextView) v.findViewById(R.id.dialog_alert_positive);
        mNegBtn = (TextView) v.findViewById(R.id.dialog_alert_negative);
        mDivider = v.findViewById(R.id.view_btn_divider);

        if (builder.hasTitle) {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(builder.title);
        } else {
            mTitle.setVisibility(View.GONE);
        }

        mContent.setText(builder.content);
        mContent.setGravity(builder.contentAlign);

        if (builder.hasPositiveBtn) {
            mPosBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, builder.positiveBtnTxtSize);
            mPosBtn.setVisibility(View.VISIBLE);
            mPosBtn.setText(builder.posStr);
            mPosBtn.setTextColor(builder.posBtnColor);
            mPosBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (builder.posListener != null)
                        builder.posListener.onClick(view);
                    dismiss();
                }
            });
        } else {
            mPosBtn.setVisibility(View.GONE);
        }

        if (builder.hasNegativeBtn) {
            mNegBtn.setVisibility(View.VISIBLE);
            mNegBtn.setText(builder.negStr);
            mNegBtn.setTextColor(builder.negBtnColor);
            mNegBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (builder.negListener != null)
                        builder.negListener.onClick(view);
                    dismiss();
                }
            });
        } else {
            mNegBtn.setVisibility(View.GONE);
        }

        if (builder.hasNegativeBtn && builder.hasPositiveBtn) {
            mDivider.setVisibility(View.VISIBLE);

        } else {
            mDivider.setVisibility(View.GONE);
        }
        setCancelable(builder.cancelable);
        getDialog().setCanceledOnTouchOutside(false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 设置DialogFragment的宽和高
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout(builder.width, builder.height);
        }
    }

    public static class Builder {
        Context context;
        int width;
        int height;
        boolean hasTitle = false;
        boolean hasPositiveBtn = false;
        boolean hasNegativeBtn = false;
        boolean cancelable = true;
        int posBtnColor = 0xff157efb;
        int negBtnColor = 0xff157efb;
        int contentAlign = Gravity.CENTER;
        CharSequence posStr;
        CharSequence negStr;
        CharSequence title;
        CharSequence content;
        View.OnClickListener posListener, negListener;
        float positiveBtnTxtSize = 16;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            if (title.length() > 0) {
                this.title = title;
                hasTitle = true;
                return this;
            }
            return this;
        }

        public Builder setMessage(int messageId) {
            content = context.getText(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            content = message;
            return this;
        }

        public Builder setContentGravity(int align) {
            contentAlign = align;
            return this;
        }

        public Builder setButtonTxtSize(float txtSize) {
            this.positiveBtnTxtSize = txtSize;
            return this;
        }

        public Builder setPositiveButton(int textId,
                                         final View.OnClickListener listener) {
            posStr = context.getText(textId);
            posListener = listener;
            hasPositiveBtn = true;

            return this;
        }

        public Builder setPositiveButton(CharSequence text,
                                         final View.OnClickListener listener) {
            posStr = text;
            posListener = listener;
            hasPositiveBtn = true;
            return this;
        }

        public Builder setPositiveButton(int textId, int color,
                                         final View.OnClickListener listener) {
            posStr = context.getText(textId);
            posListener = listener;
            posBtnColor = color;
            hasPositiveBtn = true;

            return this;
        }

        public Builder setPositiveButton(CharSequence text, int color,
                                         final View.OnClickListener listener) {
            posStr = text;
            posListener = listener;
            posBtnColor = color;
            hasPositiveBtn = true;
            return this;
        }

        public Builder setNegativeButton(int textId,
                                         final View.OnClickListener listener) {
            negStr = context.getText(textId);
            negListener = listener;
            hasNegativeBtn = true;

            return this;
        }

        public Builder setNegativeButton(CharSequence text,
                                         final View.OnClickListener listener) {
            negStr = text;
            negListener = listener;
            hasNegativeBtn = true;
            return this;
        }

        public Builder setNegativeButton(int textId, int color,
                                         final View.OnClickListener listener) {
            negStr = context.getText(textId);
            negListener = listener;
            negBtnColor = color;
            hasNegativeBtn = true;

            return this;
        }

        public Builder setNegativeButton(CharSequence text, int color,
                                         final View.OnClickListener listener) {
            negStr = text;
            negListener = listener;
            negBtnColor = color;
            hasNegativeBtn = true;
            return this;
        }

        public Builder setCancelable(boolean cancel) {
            cancelable = cancel;
            return this;
        }


        public AlertDialogFragment create() {
            AlertDialogFragment f = new AlertDialogFragment();
            f.builder = this;
            return f;
        }

    }

}
