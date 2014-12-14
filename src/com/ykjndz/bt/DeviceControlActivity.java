package com.ykjndz.bt;

import com.ykjndz.bt.BluetoothLeService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DeviceControlActivity extends BaseActivity {

	private final static String TAG = DeviceControlActivity.class
			.getSimpleName();
	private TextView tvBluetoothName;
	private ImageButton ibtControlLight;
	private ImageButton ibtControlLamp;
	private ImageButton ibtControlAlarm;
	private Button btnReturn;
	private TextView tvStatus;

	private static BluetoothLeService mBluetoothLeService;
	protected static String EXTRAS_DEVICE_NAME = "DEVICE_NAME";;
	protected static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	protected static String EXTRAS_DEVICE_RSSI = "RSSI";

	private Bundle bundle;
	private String strDeviceName;
	private String strDeviceAddress;
	private String strRssi;

	private boolean mConnected = false;

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
		tvStatus = (TextView) findViewById(R.id.control_blueth_status);
		bundle = getIntent().getExtras();
		strDeviceName = bundle.getString(EXTRAS_DEVICE_NAME);
		strDeviceAddress = bundle.getString(EXTRAS_DEVICE_ADDRESS);
		strRssi = bundle.getString(EXTRAS_DEVICE_RSSI);
		tvBluetoothName.setText(strDeviceName);
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}

	// Code to manage Service lifecycle.
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			mBluetoothLeService.connect(strDeviceAddress);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}

	};
	// Handles various events fired by the Service.
	// ACTION_GATT_CONNECTED: connected to a GATT server.
	// ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
	// ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
	// ACTION_DATA_AVAILABLE: received data from the device. This can be a
	// result of read
	// or notification operations.
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				mConnected = true;
				updateConnectionState("Connected");
				System.out.println("BroadcastReceiver :" + "device connected");

			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				mConnected = false;
				updateConnectionState("Disconnected");
				System.out.println("BroadcastReceiver :"
						+ "device disconnected");

			}
			// else if
			// (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action))
			// {
			// // Show all the supported services and characteristics on the
			// user interface.
			// displayGattServices(mBluetoothLeService.getSupportedGattServices());
			// System.out.println("BroadcastReceiver :"+"device SERVICES_DISCOVERED");
			// }
			// else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action))
			// {
			// //displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
			// System.out.println("BroadcastReceiver onData:"+intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
			// }
		}
	};

	private void updateConnectionState(final String status) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvStatus.setText(status);
			}
		});
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

		// 控制小夜灯
		ibtControlLamp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		// 控制报警功能
		ibtControlAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// 返回
		btnReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
			}
		});
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}

	// Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		if (mBluetoothLeService != null) {
			final boolean result = mBluetoothLeService
					.connect(strDeviceAddress);
			Log.d(TAG, "Connect request result=" + result);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mGattUpdateReceiver);
		mBluetoothLeService = null;
	}

}
