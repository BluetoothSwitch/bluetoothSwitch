package com.ykjndz.bt;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DeviceControlActivity extends BaseActivity {
	private TextView tvBluetoothName;
	private ImageButton ibtControlLight;
	private ImageButton ibtControlLamp;
	private ImageButton ibtControlAlarm;
	private Button btnReturn;
	
	protected static String EXTRAS_DEVICE_NAME ="DEVICE_NAME";;
	protected static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	protected static String EXTRAS_DEVICE_RSSI = "RSSI";
	
	private Bundle bundle;
	private String strDeviceName;
	private String strDeviceAddress;
	private String strRssi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_control);
		init();
		registerListener();
	}
	/**
	 * 初始化组件
	 * 
	 * @Description:
	 * @param
	 * @return void
	 */
	private void init() {
		tvBluetoothName = (TextView) findViewById(R.id.control_blueth_name);
		ibtControlLight = (ImageButton) findViewById(R.id.ibt_control_light);
		ibtControlLamp = (ImageButton) findViewById(R.id.ibt_control_lamp);
		ibtControlAlarm = (ImageButton) findViewById(R.id.ibt_control_alarm);
		btnReturn = (Button) findViewById(R.id.control_finish);
		bundle=getIntent().getExtras();
		strDeviceName = bundle.getString(EXTRAS_DEVICE_NAME);
		strDeviceAddress = bundle.getString(EXTRAS_DEVICE_ADDRESS);
		strRssi = bundle.getString(EXTRAS_DEVICE_RSSI);
		tvBluetoothName.setText(strDeviceName);
	}

	/**
	 * 注册监听
	 * 
	 * @Description:
	 * @param
	 * @return void
	 */
	private void registerListener() {
		// 控制开关灯
		ibtControlLight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		
		//控制小夜灯
		ibtControlLamp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		//控制报警功能
		ibtControlAlarm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//返回
		btnReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
		});
	}
}
