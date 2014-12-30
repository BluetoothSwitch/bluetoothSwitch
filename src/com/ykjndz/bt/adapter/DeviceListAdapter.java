package com.ykjndz.bt.adapter;

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ykjndz.bt.R;

public class DeviceListAdapter extends BaseAdapter {

	private ArrayList<BluetoothDevice> mLeDevices;

	private ArrayList<Integer> arrayList;
	private Context context;

	public DeviceListAdapter(Context context,ArrayList<Integer> arrayList) {
		super();
		this.arrayList = arrayList;
		mLeDevices = new ArrayList<BluetoothDevice>();
		this.context = context;
	}

	public void addDevice(BluetoothDevice device, int rssi) {
		if (!mLeDevices.contains(device)) {
			mLeDevices.add(device);
			arrayList.add(rssi);
		}
	}

	public BluetoothDevice getDevice(int position) {
		return mLeDevices.get(position);
	}

	public void clear() {
		mLeDevices.clear();
		arrayList.clear();
	}

	@Override
	public int getCount() {
		return mLeDevices.size();
	}

	@Override
	public Object getItem(int i) {
		return mLeDevices.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {

		

		view = LayoutInflater.from(context).inflate(R.layout.adapter_device, null);

		TextView deviceAddress = (TextView) view
				.findViewById(R.id.tv_deviceAddr);
		TextView deviceName = (TextView) view.findViewById(R.id.tv_deviceName);
		//TextView rssi = (TextView) view.findViewById(R.id.tv_rssi);

		BluetoothDevice device = mLeDevices.get(i);
		deviceAddress.setText(device.getAddress());
		deviceName.setText(device.getName());
		//rssi.setText("" + arrayList.get(i));

		return view;
	}

}
