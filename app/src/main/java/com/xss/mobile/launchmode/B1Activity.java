package com.xss.mobile.launchmode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xss.mobile.R;

public class B1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1);

        Button btn = (Button) findViewById(R.id.btn_a1);
        btn.setText("标准模式启动 B2");
        findViewById(R.id.btn_a1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(B1Activity.this,
                        A2Activity.class);
                startActivity(intent);
            }
        });

    }
}
