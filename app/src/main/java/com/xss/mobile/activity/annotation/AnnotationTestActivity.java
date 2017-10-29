package com.xss.mobile.activity.annotation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.xss.mobile.R;

public class AnnotationTestActivity extends AppCompatActivity {
    String TAG = AnnotationTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation_test);

        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content.setText(new Apple().getDesc());
        tv_content.append("\n" + FruitUtils.getFruitInfo(Apple.class));

        showTaste();
        showTaste("great");

    }

    @Deprecated
    public void showTaste() {
        Log.e(TAG, "The apple taste delicious");
    }

    public void showTaste(String taste) {
        Log.e(TAG, "The apple taste " + taste);
    }
}
