package com.ykjndz.bt;

import java.util.ArrayList;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ykjndz.bt.adapter.DeviceListAdapter;
/**
 * 主界面
* @Description: 
* @author yougui_zheng 
* @date 2014-12-8 下午10:28:55 
*
 */
public class MainActivity extends BaseActivity {
    private ListView listView;
	
    BluetoothAdapter mBluetoothAdapter;
    int REQUEST_ENABLE_BT=1;
    
    private boolean mScanning;
    private Handler mHandler;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    
    private DeviceListAdapter deviceListAdapter;
    private ArrayList<Integer> rssis;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		registerListener();
	}
    /**
     * 初始化组件
    * @Description: 
    * @param      
    * @return void
     */
	private void init(){
		listView = (ListView) findViewById(R.id.main_listview);
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
		    Toast.makeText(this, "不支持BLE", Toast.LENGTH_SHORT).show();
		    finish();
		}
		
		// Initializes Bluetooth adapter.
		final BluetoothManager bluetoothManager =
		        (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		
		// Ensures Bluetooth is available on the device and it is enabled. If not,
		// displays a dialog requesting user permission to enable Bluetooth.
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
		mHandler=new Handler();
		rssis =new ArrayList<Integer>();
		deviceListAdapter=new DeviceListAdapter(MainActivity.this,rssis);
		
		listView.setAdapter(deviceListAdapter);
		//开始扫描
		scanLeDevice(true);
	}
	
	/**
	 * 注册监听
	* @Description: 
	* @param      
	* @return void
	 */
	private void registerListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				final BluetoothDevice device = deviceListAdapter.getDevice(position);
		        if (device == null) return;
		        final Intent intent = new Intent(MainActivity.this, DeviceControlActivity.class);
		        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
		        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
		        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_RSSI, rssis.get(position).toString());
		        if (mScanning) {
		            mBluetoothAdapter.stopLeScan(mLeScanCallback);
		            mScanning = false;
		        }
		        startActivity(intent);
			}
		});
	}
	
	/**
	 * 扫描设备
	* @Description: 
	* @param @param enable     
	* @return void
	 */
	private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
	
	private BluetoothAdapter.LeScanCallback mLeScanCallback =
	        new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
			runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	deviceListAdapter.addDevice(device,rssi);
                	deviceListAdapter.notifyDataSetChanged();
                }
            });
		}
	};
}
