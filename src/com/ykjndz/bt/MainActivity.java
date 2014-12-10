package com.ykjndz.bt;

import android.os.Bundle;
import android.view.Menu;
/**
 * 主界面
* @Description: 
* @author yougui_zheng 
* @date 2014-12-8 下午10:28:55 
*
 */
public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
