package com.yesorno.zgq.breathview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BreathView breathView1 = (BreathView) findViewById(R.id.breath1);
        ((Button) findViewById(R.id.control)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breathView1.stop();
            }
        });
        breathView1.setTargetTitle(1);
        breathView1.start();
    }
}
