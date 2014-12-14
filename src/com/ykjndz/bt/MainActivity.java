package com.ykjndz.bt;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 主界面
* @Description: 
* @author yougui_zheng 
* @date 2014-12-8 下午10:28:55 
*
 */
public class MainActivity extends BaseActivity {
    private TextView tvBluetoothName;
    private Button btnCntDev,btnStopDev;
    private ImageButton ibControl;
    private TextView tvMydata;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initComponent();
		registerListener();
	}
    /**
     * 初始化组件
    * @Description: 
    * @param      
    * @return void
     */
	private void initComponent(){
		tvBluetoothName = (TextView) findViewById(R.id.bluetooth_name_text);
		btnCntDev = (Button) findViewById(R.id.btn_cnt_dev);
		btnStopDev = (Button) findViewById(R.id.btn_stop_dev);
		ibControl = (ImageButton) findViewById(R.id.ib_control);
		tvMydata = (TextView) findViewById(R.id.mydata);
	}
	
	/**
	 * 注册监听
	* @Description: 
	* @param      
	* @return void
	 */
	private void registerListener() {
		//连接设备
		btnCntDev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		// 断开设备
		btnStopDev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//控制操作
		ibControl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		
	}

}
