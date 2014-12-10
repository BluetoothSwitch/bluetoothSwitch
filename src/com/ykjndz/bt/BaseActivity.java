package com.ykjndz.bt;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
/**
 * avtivity的基类
* @Description: 
* @author yougui_zheng 
* @date 2014-12-8 下午10:28:03 
*
 */
public class BaseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	

}
