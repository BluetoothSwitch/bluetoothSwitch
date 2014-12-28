package com.ykjndz.bt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ykjndz.bt.tool.Constants;
import com.ykjndz.bt.tool.Utils;
import com.ykjndz.bt.BluetoothLeService;

import android.R.integer;
import android.annotation.SuppressLint;
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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class DeviceControlActivity extends BaseActivity {

	private final static String TAG = DeviceControlActivity.class.getSimpleName();
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
	private MediaPlayer mPlayer;//播放声音

	private Bundle bundle;
	private String strDeviceName;
	private String strDeviceAddress;
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
		tvStatus = (TextView) findViewById(R.id.control_blueth_status);
		tvStatus = (TextView) findViewById(R.id.control_blueth_status);
		bundle = getIntent().getExtras();
		strDeviceName = bundle.getString(EXTRAS_DEVICE_NAME);
		strDeviceAddress = bundle.getString(EXTRAS_DEVICE_ADDRESS);
		tvBluetoothName.setText(strDeviceName);
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		
		initSound();
	}

	private final ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
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
				updateState(tvStatus,Constants.CONNECTED);
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
				mConnected = false;
				updateState(tvStatus,Constants.DISCONNECTED);
			}else if(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)){
				GetGattServices(mBluetoothLeService.getSupportedGattServices());
				searchState(0, true);
			 }else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)){//接收数据
				 String value = intent.getExtras().getString(BluetoothLeService.EXTRA_DATA);
				 checkData(value);
			 }
		}
	};
	/**
	 * 
	 * @Description:接收数据
	 * @param data
	 * @author: huangwb 
	 * @date: 2014年12月28日 下午1:07:47
	 * @throws:
	 */
	private void checkData(String data){
		Log.d(TAG,"接收到的数据:" + data);
		//点击返回
		if(Constants.RES_ALARM_CLOSE.equals(data) || Constants.ALARM_CLOSE.equals(data)){
			updateImage(ibtControlAlarm,false);
		}else if(Constants.RES_ALARM_OPEN.equals(data) || Constants.ALARM_OPEN.equals(data)){
			updateImage(ibtControlAlarm,true);
		}else if(Constants.RES_LIGHT_CLOSE.equals(data) || Constants.LIGHT_CLOSE.equals(data)){
			updateImage(ibtControlLamp,false);
		}else if(Constants.RES_LIGHT_OPEN.equals(data) || Constants.LIGHT_OPEN.equals(data)){
			updateImage(ibtControlLamp,true);
		}else if(Constants.RES_SWITCH_CLOSE.equals(data) || Constants.SWITCH_CLOSE.equals(data)){
			updateImage(ibtControlLight,false);
		}else if(Constants.RES_SWITCH_OPEN.equals(data) || Constants.SWITCH_OPEN.equals(data)){
			updateImage(ibtControlLight,true);
		}else if(Constants.ALARM.equals(data)){
			mPlayer.start();
		}	
	}
	
	/**
	 * @Description:查询灯的状态
	 * @author: huangwb 
	 * @date: 2014年12月24日 上午11:29:32
	 * @throws:
	 */
	private void searchState(int type,boolean isInit){
		int i = 0;
		String str = null;
		if(isInit){
			for(i = 0;i < Constants.PORT_ARRAY.length;i++){
				str = Constants.PORT_ARRAY[i];
				write(str);
			}
		}else{
			str = Constants.PORT_ARRAY[type];
			write(str);
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
    private void GetGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        for (BluetoothGattService gattService : gattServices) {
        	//-----Service的字段信息-----//
        	int type = gattService.getType();
        	Log.d(TAG,"---->type:" + type);
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
    /**
     * 
     * @Description:改变连接状态
     * @param view
     * @param status
     * @author: huangwb 
     * @date: 2014年12月28日 上午10:33:32
     * @throws:
     */
	private void updateState(final TextView view,final String status) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				view.setText(status);
			}
		});
	}
	
	/**
	 * 
	 * @Description:改变按钮的背景图片
	 * @param ib
	 * @param isOpen
	 * @author: huangwb 
	 * @date: 2014年12月28日 上午10:35:19
	 * @throws:
	 */
	private void updateImage(final ImageButton ib,final boolean isOpen){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(isOpen){
					ib.setImageDrawable(getResources().getDrawable(R.drawable.btn_first_l));
					ib.setTag(true);
				}else{
					ib.setImageDrawable(getResources().getDrawable(R.drawable.btn_first_h));
					ib.setTag(false);
				}
			}
		});
	}
	
	/**
	 * 
	 * @Description:初始化声音
	 * @author: huangwb 
	 * @date: 2014年12月28日 下午2:18:44
	 * @throws:
	 */
	private void initSound(){
		try {
			mPlayer = MediaPlayer.create(this, R.raw.alarm);
			mPlayer.setLooping(true);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	/**
	 * 
	 * @Description:停止播放声音
	 * @author: huangwb 
	 * @date: 2014年12月28日 下午1:32:44
	 * @throws:
	 */
	private void stopSounds(){
		if(mPlayer != null){
			mPlayer.stop();
			mPlayer.release();
		}
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
		ibtControlLight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ibtControlLight.getTag() == null) {
					ibtControlLight.setTag(false);
				}
			
				boolean flag = (Boolean)ibtControlLight.getTag();
				if(flag){
					write(Constants.SWITCH_CLOSE);
					ibtControlLight.setTag(false);
				}else{
					write(Constants.SWITCH_OPEN);
					ibtControlLight.setTag(true);
				}
			}
		});
		
		// 控制小夜灯
		ibtControlLamp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ibtControlLamp.getTag() == null) {
					ibtControlLamp.setTag(false);
				}
			
				boolean flag = (Boolean)ibtControlLamp.getTag();
				if(flag){
					write(Constants.LIGHT_CLOSE);
					ibtControlLamp.setTag(false);
				}else{
					write(Constants.LIGHT_OPEN);
					ibtControlLamp.setTag(true);
				}
			}
		});
		
		// 控制报警功能
		ibtControlAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ibtControlAlarm.getTag() == null) {
					ibtControlAlarm.setTag(false);
				}
			
				boolean flag = (Boolean)ibtControlAlarm.getTag();
				if(flag){
					write(Constants.ALARM_CLOSE);
					ibtControlLamp.setTag(false);
				}else{
					write(Constants.ALARM_OPEN);
					ibtControlLamp.setTag(true);
				}
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
		unbindService(mServiceConnection);
		mBluetoothLeService = null;
		stopSounds();
		//mServiceConnection.onServiceDisconnected(strDeviceAddress);
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
