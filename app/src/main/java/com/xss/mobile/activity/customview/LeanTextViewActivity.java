package com.xss.mobile.activity.customview;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.xss.mobile.R;
import com.xss.mobile.widget.LeanLabelTextView;
import com.xss.mobile.widget.LeanTextView;
import com.xss.mobile.widget.LeanTextWithThreeView;

import static com.xss.mobile.R.id.leanTextWithThreeView;

public class LeanTextViewActivity extends Activity {
    private LeanTextWithThreeView leanTextView, leanTextView0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lean_test);  // activity_lean_text_view

        leanTextView = (LeanTextWithThreeView) findViewById(R.id.leanTextWithThreeView);
        leanTextView0 = (LeanTextWithThreeView) findViewById(R.id.leanTextWithThreeView0);

        leanTextView.setText("强烈推荐", "碧桂园十里银滩", "100000元/平米");
        leanTextView0.setText("南山科技园", "1-4室 100-200平米");


//        LeanTextView leanTextView = (LeanTextView) findViewById(R.id.tv_lean);
//        leanTextView.setDegree(-45);
//
//        LeanTextView leanTextView1 = (LeanTextView) findViewById(R.id.tv_lean1);
//        leanTextView1.setDegree(-45);
//
//        LeanTextView leanTextView2 = (LeanTextView) findViewById(R.id.tv_lean2);
//        leanTextView2.setDegree(-45);
//
//        final LeanLabelTextView tv = (LeanLabelTextView) findViewById(R.id.tv);
//        tv.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.e("cc", tv.getHeight() + ", " + tv.getWidth());
//            }
//        });
    }


}
