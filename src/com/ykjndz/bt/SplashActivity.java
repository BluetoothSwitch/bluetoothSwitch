package com.ykjndz.bt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
/**
 * 闪屏
* @Description: 
* @author yougui_zheng 
* @date 2014-12-8 下午10:30:30 
*
 */
public class SplashActivity extends BaseActivity {
	private final int SPLASH_DISPLAY_LENGHT = 2000; // 延迟2秒
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable() {  
            public void run() {
            	
            	Intent intent=new Intent();
            	intent.setClass(SplashActivity.this, MainActivity.class);
            	startActivity(intent);  
                finish();  
            }  
        }, SPLASH_DISPLAY_LENGHT);
	}

}
