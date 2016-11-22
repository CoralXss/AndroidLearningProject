package com.xss.mobile.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xss.mobile.R;

/**
 * Created by xss on 2016/11/2.
 * 自定义组合控件
 * TextView  EditText
 */
public class ComposeLabelEditView extends LinearLayout {
    private static final String TAG = ComposeLabelEditView.class.getSimpleName();

    private Context mContext;

    private TextView mTextView;
    private EditText mEditText;

    /** TextView属性 **/
    ColorStateList labelTextColor = null;
    int labelTextSize = 15;
    CharSequence labelText = "";
    int labelTextStyle = -1;

    Drawable drawableLeft = null, drawableTop = null, drawableRight = null,
            drawableBottom = null;
    int drawablePadding = 0;
    int labelBackgroundResourceId = -1;
    ColorStateList labelBackgroundColor = null;

    int ellipsize = -1;
    boolean singleLine = false;
    int maxLength = -1;

    /** EditText属性 **/
    ColorStateList editTextColor = null;
    int editTextSize = 15;
    CharSequence editText = "";
    CharSequence editTextHint = "";
    ColorStateList editTextHintColor = null;
    int inputType = 0;

    int labelMarginRight = 10;
    int edtPaddingLeft = 10;
    int edtPaddingTop;
    int edtPaddingRight;
    int edtPaddingBottom;
    int edtBackgroundResourceId = -1;
    ColorStateList edtBackgroundColor = null;

    private static final int INPUT_TYPE_TEXT = 0;
    private static final int INPUT_TYPE_PHONE = 1;
    private static final int INPUT_TYPE_PASSWORD = 2;
    private static final int INPUT_TYPE_NUMBER = 3;

    public ComposeLabelEditView(Context context) {
        this(context, null);
    }

    public ComposeLabelEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainStyledAttributes(context, attrs, 0, 0);
    }

    public ComposeLabelEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttributes(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ComposeLabelEditView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        obtainStyledAttributes(context, attrs, defStyleAttr, defStyleRes);
    }

    private void obtainStyledAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.composeLabelEditViewStyle, defStyleAttr, defStyleRes);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.composeLabelEditViewStyle_labelTextColor:
                    labelTextColor = a.getColorStateList(attr);
                    break;
                case R.styleable.composeLabelEditViewStyle_labelTextSize:
                    labelTextSize = a.getDimensionPixelSize(attr, labelTextSize);
                    break;
                case R.styleable.composeLabelEditViewStyle_labelText:
                    labelText = a.getText(attr);
                    break;
                case R.styleable.composeLabelEditViewStyle_labelTextStyle:
                    labelTextStyle = a.getInt(attr, 0);
                    break;
                case R.styleable.composeLabelEditViewStyle_drawableLeft:
                    drawableLeft = a.getDrawable(attr);
                    break;
                case R.styleable.composeLabelEditViewStyle_drawableTop:
                    drawableTop = a.getDrawable(attr);
                    break;
                case R.styleable.composeLabelEditViewStyle_drawableRight:
                    drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.composeLabelEditViewStyle_drawableBottom:
                    drawableBottom = a.getDrawable(attr);
                    break;
                case R.styleable.composeLabelEditViewStyle_drawablePadding:
                    drawablePadding = a.getDimensionPixelSize(attr, -1);
                    break;

                case R.styleable.composeLabelEditViewStyle_myEditTextColor:
                    editTextColor = a.getColorStateList(attr);
                    break;
                case R.styleable.composeLabelEditViewStyle_myEditTextSize:
                    editTextSize = a.getDimensionPixelSize(attr, editTextSize);
                    break;
                case R.styleable.composeLabelEditViewStyle_myEditText:
                    editText = a.getText(attr);
                    break;
                case R.styleable.composeLabelEditViewStyle_myEditTextHint:
                    editTextHint = a.getText(attr);
                    break;
                case R.styleable.composeLabelEditViewStyle_myEditTextHintColor:
                    editTextHintColor = a.getColorStateList(attr);
                    break;
                case R.styleable.composeLabelEditViewStyle_myEditInputType:
                    inputType = a.getInt(attr, inputType);
                    break;

                case R.styleable.composeLabelEditViewStyle_labelMarginRight:
                    labelMarginRight = a.getDimensionPixelSize(attr, labelMarginRight);
                    break;
                case R.styleable.composeLabelEditViewStyle_edtPaddingLeft:
                    edtPaddingLeft = a.getDimensionPixelSize(attr, edtPaddingLeft);
                    break;
                case R.styleable.composeLabelEditViewStyle_edtPaddingTop:
                    edtPaddingTop = a.getDimensionPixelSize(attr, edtPaddingTop);
                    break;
                case R.styleable.composeLabelEditViewStyle_edtPaddingRight:
                    edtPaddingRight = a.getDimensionPixelSize(attr, edtPaddingRight);
                    break;
                case R.styleable.composeLabelEditViewStyle_edtPaddingBottom:
                    edtPaddingBottom = a.getDimensionPixelSize(attr, edtPaddingBottom);
                    break;

                case R.styleable.composeLabelEditViewStyle_labelBackgroundResource:
                    labelBackgroundResourceId = a.getResourceId(attr, Color.parseColor("#ffffff"));
                    break;
                case R.styleable.composeLabelEditViewStyle_myEditTextBackgroundResource:
                    edtBackgroundResourceId = a.getResourceId(attr, Color.parseColor("#ffffff"));
                    break;
                default:
                    break;

                // to be continued...
            }
        }
        a.recycle();

        init();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        // 这里使用inflater，当加载的布局有merge时ViewGroup参数必须有效不为null 以及 attachToRoot需要为 false
        inflater.inflate(R.layout.layout_compose_label_edit_view, this, true);

        ViewGroup root = (ViewGroup) getChildAt(0);
        if (root != null) {
            mTextView = (TextView) root.getChildAt(0);
            mEditText = (EditText) root.getChildAt(1);

            mTextView.setText(labelText);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, labelTextSize);
            mTextView.setTextColor(labelTextColor != null ? labelTextColor : ColorStateList.valueOf(0xFF000000));

            // 设置TextView左上右下图片
            mTextView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
            mTextView.setCompoundDrawablePadding(drawablePadding);

            mEditText.setText(editText);
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, editTextSize);
            mEditText.setTextColor(editTextColor != null ? editTextColor : ColorStateList.valueOf(0xFF000000));
            mEditText.setHint(editTextHint);
//            mEditText.setHintTextColor(editTextHintColor != null ? editTextHintColor : ColorStateList.valueOf(0xFF000000));
            switch (inputType) {
                case INPUT_TYPE_TEXT:
                    mEditText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                    break;
                case INPUT_TYPE_PHONE:
                    mEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                    break;
                case INPUT_TYPE_PASSWORD:
                    mEditText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
                    break;
                case INPUT_TYPE_NUMBER:
                    mEditText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                    break;
                default:
                    break;
            }

//            // 设置标签右边距 ??? 无法通过getLayoutParams() 来获取
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, labelMarginRight, 0);
            mTextView.setLayoutParams(lp);
//
//            // todo 设置编辑框的内边距（无效？？？）
            mEditText.setPadding(edtPaddingLeft, edtPaddingTop, edtPaddingRight, edtPaddingBottom);

            // 设置背景图片resource
//            Drawable labelD = mContext.getDrawable(labelBackgroundResourceId);
//            if (labelD != null) {
//                if (labelD instanceof ColorDrawable) {
//                    mTextView.setBackground(new ColorDrawable(labelBackgroundResourceId));
//                } else {
//                    mTextView.setBackground(labelD);
//                }
//            }

            Drawable editD = mContext.getDrawable(edtBackgroundResourceId);
            if (editD instanceof ColorDrawable) {
                mEditText.setBackground(new ColorDrawable(edtBackgroundResourceId));
            } else {
                mEditText.setBackground(editD);
            }
        }
    }


    private void setDrawableBounds(Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
    }

    public void setLabelTextColor(ColorStateList labelTextColor) {
        this.labelTextColor = labelTextColor;
        mTextView.setTextColor(this.labelTextColor);
    }

    public void setLabelTextSize(int labelTextSize) {
        this.labelTextSize = labelTextSize;
        mTextView.setTextSize(this.labelTextSize);
    }

    public void setLabelText(CharSequence labelText) {
        this.labelText = labelText;
        mTextView.setText(this.labelText);
    }

    public void setLabelTextStyle(int labelTextStyle) {
        this.labelTextStyle = labelTextStyle;
    }

    public void setDrawableLeft(Drawable drawableLeft) {
        this.drawableLeft = drawableLeft;
    }

    public void setDrawableTop(Drawable drawableTop) {
        this.drawableTop = drawableTop;
    }

    public void setDrawableRight(Drawable drawableRight) {
        this.drawableRight = drawableRight;
    }

    public void setDrawableBottom(Drawable drawableBottom) {
        this.drawableBottom = drawableBottom;
    }

    public void setDrawablePadding(int drawablePadding) {
        this.drawablePadding = drawablePadding;
    }

    public void setEllipsize(int ellipsize) {
        this.ellipsize = ellipsize;
    }

    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setEditTextColor(ColorStateList editTextColor) {
        this.editTextColor = editTextColor;
        mEditText.setTextColor(editTextColor);
    }

    public void setEditTextSize(int editTextSize) {
        this.editTextSize = editTextSize;
        mEditText.setTextSize(this.editTextSize);
    }

    public void setEditText(CharSequence editText) {
        this.editText = editText;
        mEditText.setText(editText);
    }

    public void setEditTextHint(CharSequence editTextHint) {
        this.editTextHint = editTextHint;
        mEditText.setHint(this.editTextHint);
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
        mEditText.setInputType(this.inputType);
    }
}
