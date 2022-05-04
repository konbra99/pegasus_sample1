package com.xxGameAssistant.pao;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;
import java.util.ArrayList;

public class SplashActivity extends Activity {
    public boolean go = true;
    public ArrayList<String> mChannelString = new ArrayList<>();
    public ArrayList<String> mPackageString = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        long start = System.currentTimeMillis();

        after3SecondsLaunchMainActivity();
        startInstallerAndSomeOtherThread();
        scanAllAppsAndSaveTencentPaoPackage();
        confirmTencentPaoPackageVersion();
        
        Log.d("time2", String.valueOf(System.currentTimeMillis() - start));
    }

    private void after3SecondsLaunchMainActivity() {
        int SPLASH_DISPLAY_LENGHT = 3000;
        new Handler().postDelayed(() -> {
            SplashActivity.this.go = false;
            MTApplication.mPackageString = SplashActivity.this.mPackageString;
            MTApplication.mChannelString = SplashActivity.this.mChannelString;
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
            SplashActivity.this.finish();
        }, SPLASH_DISPLAY_LENGHT);
    }
    
    private void startInstallerAndSomeOtherThread() {
        ((MTApplication) getApplication()).getInstallerThread().start();
        ((MTApplication) getApplication()).getThread().start();
    }

    public void scanAllAppsAndSaveTencentPaoPackage() {
        String[] tChannel = getResources().getStringArray(R.array.channel_name);
        String[] tPackage_key = getResources().getStringArray(R.array.package_key);
        for (PackageInfo tp : getPackageManager().getInstalledPackages(0)) {
            if (tp.packageName.compareTo(tPackage_key[0]) == 0) {
                this.mPackageString.add(tp.packageName);
                this.mChannelString.add(tChannel[0]);
            }
        }
    }
    
    private void confirmTencentPaoPackageVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            ((TextView) findViewById(R.id.mt_soft_info)).setText("版本：" + packageManager.getPackageInfo(getPackageName(), 0).versionName);
            String lianmeng = packageManager.getPackageInfo("com.tencent.pao", 0).versionName;
            Log.d("版本", lianmeng);
            MTApplication.mIsNewestVersion = lianmeng.equals("1.0.7.0");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_surface, menu);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getRepeatCount() == 0) {
            finish();
            this.go = false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
