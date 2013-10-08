package com.donvigo.CustomProgressControls;

import android.app.Activity;
import android.os.Bundle;
import com.donvigo.CustomProgressControls.UI.CircleRoadProgress;

public class MainActivity extends Activity {

    CircleRoadProgress circleProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        circleProgress = (CircleRoadProgress) findViewById(R.id.circleProgress);
        changePercentage();
    }

    private void changePercentage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int percent = 0;
                while(true){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    percent += 10;
                    if(percent == 110)
                        percent = 0;
                    circleProgress.changePercentage(percent);
                }
            }
        }).start();
    }
}