package com.dn.foldtextview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kikt.FoldTextView.FoldTextView;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    protected FoldTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (FoldTextView) findViewById(R.id.tv);

        tv.setDuration(200);
        //        tv.setMax(2000);//不设置则自动wrap_content，设置则max根据设置
        tv.setMin(0);
        tv.setText("hello world\n\ndsljfl\ndsfjds");

        tv.setOnFoldChangeListener(new FoldTextView.OnFoldChangeListener() {
            @Override
            public void onFoldEnd(FoldTextView textView, boolean isFold) {
                Log.d("MainActivity", "isFold:" + isFold);
            }
        });
    }

    public void fold(View view) {
        tv.fold();
    }
}
