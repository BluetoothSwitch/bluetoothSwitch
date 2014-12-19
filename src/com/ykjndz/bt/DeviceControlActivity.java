package com.ykjndz.bt;

import java.util.List;

import com.ykjndz.bt.tool.Constants;
import com.ykjndz.bt.tool.Utils;
import com.ykjndz.bt.BluetoothLeService;

import android.R.string;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DeviceControlActivity extends BaseActivity {

	private final static String TAG = DeviceControlActivity.class.getSimpleName();
	private TextView tvBluetoothName;
	private ImageButton ibtControlLight;
	private ImageButton ibtControlLamp;
	private ImageButton ibtControlAlarm;
	private Button btnReturn;
	private TextView tvStatus;
	private TextView switchtxt;
	private TextView lighttxt;
	private TextView alarmtxt;
	private static BluetoothLeService mBluetoothLeService;
	protected static String EXTRAS_DEVICE_NAME = "DEVICE_NAME";;
	protected static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	protected static String EXTRAS_DEVICE_RSSI = "RSSI";

	private Bundle bundle;
	private String strDeviceName;
	private String strDeviceAddress;
	private String strRssi;
	private String swidthstr = null;
	private String lightstr = null;
	private String alarmstr = null;

	private boolean mConnected = false;
	private static BluetoothGattCharacteristic target_chara=null;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_control);
		mHandler = new Handler();
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
		switchtxt = (TextView) findViewById(R.id.switch_control);
		lighttxt = (TextView) findViewById(R.id.light_control);
		alarmtxt = (TextView) findViewById(R.id.alarm_control);
		tvStatus = (TextView) findViewById(R.id.control_blueth_status);
		tvStatus = (TextView) findViewById(R.id.control_blueth_status);
		bundle = getIntent().getExtras();
		strDeviceName = bundle.getString(EXTRAS_DEVICE_NAME);
		strDeviceAddress = bundle.getString(EXTRAS_DEVICE_ADDRESS);
		strRssi = bundle.getString(EXTRAS_DEVICE_RSSI);
		tvBluetoothName.setText(strDeviceName);
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		
		swidthstr = switchtxt.getText().toString();
		lightstr = lighttxt.getText().toString();
		alarmstr = alarmtxt.getText().toString();
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
	private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				mConnected = true;
				updateConnectionState(tvStatus,"Connected");
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
				mConnected = false;
				updateConnectionState(tvStatus,"Disconnected");
			}else if(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)){
			 // Show all the supported services and characteristics on the user interface.
				displayGattServices(mBluetoothLeService.getSupportedGattServices());
			 }else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)){//接收数据
				 String value = intent.getExtras().getString(BluetoothLeService.EXTRA_DATA);
				 checkData(value);
			 }
		}
	};
	
	private void checkData(String data){
		if(Constants.RES_ALARM_CLOSE.equals(data)){
			updateConnectionState(alarmtxt,alarmstr + ":" + data + "-->关");
		}else if(Constants.RES_ALARM_OPEN.equals(data)){
			updateConnectionState(alarmtxt,alarmstr + ":" + data + "-->开");
		}else if(Constants.RES_LIGHT_CLOSE.equals(data)){
			updateConnectionState(lighttxt,lightstr + ":" + data + "-->关");
		}else if(Constants.RES_LIGHT_OPEN.equals(data)){
			updateConnectionState(lighttxt,lightstr + ":" + data + "-->开");
		}else if(Constants.RES_SWITCH_CLOSE.equals(data)){
			updateConnectionState(switchtxt,swidthstr + ":" + data + "-->关");
		}else if(Constants.RES_SWITCH_OPEN.equals(data)){
			updateConnectionState(switchtxt,swidthstr + ":" + data + "-->开");
		}
	}
	/**
	 * 
	 * @Description: 获取Characteristics的数据信息
	 * @param gattServices
	 * @author: huangwb 
	 * @date: 2014年12月15日 下午9:04:25
	 * @throws:
	 */
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        for (BluetoothGattService gattService : gattServices) {
        	//-----Service的字段信息-----//
        	int type = gattService.getType();
            
            //-----Characteristics的字段信息-----//
            List<BluetoothGattCharacteristic> gattCharacteristics =gattService.getCharacteristics();
            for (final BluetoothGattCharacteristic  gattCharacteristic: gattCharacteristics) {
                int permission = gattCharacteristic.getPermissions();
                Log.d(TAG,"---->char permission:"+Utils.getCharPermission(permission));
                
                int property = gattCharacteristic.getProperties();
                Log.d(TAG,"---->char property:"+Utils.getCharPropertie(property));

                byte[] data = gattCharacteristic.getValue();
        		if (data != null && data.length > 0) {
        			Log.d(TAG,"---->char value:"+new String(data));
        		}
        		
        		if(gattCharacteristic.getUuid().toString().equals(SampleGattAttributes.HEART_RATE_MEASUREMENT)){
        			target_chara = gattCharacteristic;
        			
        			mHandler.postDelayed(new Runnable() {
                        @Override  
                        public void run() {  
                        	mBluetoothLeService.readCharacteristic(gattCharacteristic);
                        }  
                    }, 500); 
        			
        			mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
        			List<BluetoothGattDescriptor> descriptors= gattCharacteristic.getDescriptors();
	                for(BluetoothGattDescriptor descriptor:descriptors)
	                {
	                	mBluetoothLeService.getCharacteristicDescriptor(descriptor);
	                }
        		}
            }
        }//
    }

	private void updateConnectionState(final TextView view,final String status) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				view.setText(status);
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
				write(Constants.SWITCH_OPEN);
			}
		});
		
		ibtControlLight.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				write(Constants.SWITCH_CLOSE);
				return true;
			}
		});

		// 控制小夜灯
		ibtControlLamp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				write(Constants.LIGHT_OPEN);
			}
		});
		
		ibtControlLamp.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				write(Constants.LIGHT_CLOSE);
				return true;
			}
		});
		// 控制报警功能
		ibtControlAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				write(Constants.ALARM_OPEN);
			}
		});
		
		ibtControlAlarm.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				write(Constants.ALARM_CLOSE);
				return true;
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
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}

	// Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		if (mBluetoothLeService != null) {
			final boolean result = mBluetoothLeService.connect(strDeviceAddress);
			Log.d(TAG, "Connect request result=" + result);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mGattUpdateReceiver);
		mBluetoothLeService = null;
	}
	
	/**
	 * 
	 * @Description:写数据
	 * @param s
	 * @author: huangwb 
	 * @date: 2014年12月16日 下午1:51:38
	 * @throws:
	 */
	public static void write(String s)
    {
    	final int charaProp = target_chara.getProperties();				
		if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
			target_chara.setValue(s);
			mBluetoothLeService.writeCharacteristic(target_chara);
		}
    }
	
	private static BluetoothLeService.OnDataAvailableListener mOnDataAvailable = new BluetoothLeService.OnDataAvailableListener(){

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
            	Log.d(TAG,"onCharacteristicRead "+gatt.getDevice().getName()
						+" read "
						+characteristic.getUuid().toString()
						+" -> "
						+Utils.bytesToHexString(characteristic.getValue()));
            	mBluetoothLeService.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, characteristic);
            }
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
                	mBluetoothLeService.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, characteristic);
                	Log.d(TAG,"onCharacteristicWrite "+gatt.getDevice().getName()
        					+" write "
        					+characteristic.getUuid().toString()
        					+" -> "
        					+new String(characteristic.getValue()));
			
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			Log.d(TAG,"onCharacteristicChanged "+gatt.getDevice().getName()
					+" write "
					+characteristic.getUuid().toString()
					+" -> "
					+new String(characteristic.getValue()));
			
		}
		
    };
	
	/**
	 * 
	 * @Description:读数据 后续完善
	 * @author: huangwb 
	 * @date: 2014年12月16日 下午1:52:41
	 * @throws:
	 */
    public static void read()
    {
    	mBluetoothLeService.setOnDataAvailableListener(mOnDataAvailable);
    	mBluetoothLeService.readCharacteristic(target_chara);
    }

}
