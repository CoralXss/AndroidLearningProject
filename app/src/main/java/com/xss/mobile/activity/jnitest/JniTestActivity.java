//package com.xss.mobile.activity.jnitest;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import com.xss.mobile.R;
//
//public class JniTestActivity extends AppCompatActivity {
//    private TextView textView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_jni_test);
//
//        textView = (TextView) findViewById(R.id.textView);
//        textView.setText(NdkUtil.getCLanguageString());
//
//        // C调用Java方法入口
//        NdkUtil.callJavaMethodFromJni();
//    }
//}
