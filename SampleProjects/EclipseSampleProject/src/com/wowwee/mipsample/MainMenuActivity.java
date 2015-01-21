package com.wowwee.mipsample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.wowwee.bluetoothrobotcontrollib.MipCommandValues;
import com.wowwee.bluetoothrobotcontrollib.sdk.MipRobot;
import com.wowwee.bluetoothrobotcontrollib.sdk.MipRobotFinder;
import com.wowwee.bluetoothrobotcontrollib.MipRobotSound;
import com.wowwee.bluetoothrobotcontrollib.sdk.MipRobot.MipRobotInterface;
import com.wowwee.mipsample.R;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

public class MainMenuActivity extends FragmentActivity implements MipRobotInterface{

	private BluetoothAdapter mBluetoothAdapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main_menu);
		
		final BluetoothManager bluetoothManager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		
		// Set BluetoothAdapter to MipRobotFinder
		MipRobotFinder.getInstance().setBluetoothAdapter(mBluetoothAdapter);
		
		// Set Context to MipRobotFinder
		MipRobotFinder finder = MipRobotFinder.getInstance();
		finder.setApplicationContext(getApplicationContext());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		this.registerReceiver(mMipFinderBroadcastReceiver, MipRobotFinder.getMipRobotFinderIntentFilter());
		if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
           if (!mBluetoothAdapter.isEnabled()) {
               TextView noBtText = (TextView)this.findViewById(R.id.no_bt_text);
               noBtText.setVisibility(View.VISIBLE);
           }
		}
		
		// Search for mip
		MipRobotFinder.getInstance().clearFoundMipList();
		scanLeDevice(false);
//		updateMipList();
		scanLeDevice(true);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		this.unregisterReceiver(mMipFinderBroadcastReceiver);
		for(MipRobot mip : MipRobotFinder.getInstance().getMipsConnected()) {
			mip.readMipHardwareVersion();
			mip.disconnect();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		System.exit(0);
	}
	
	public void buttonOnclickHandler(View view){
		switch (view.getId()){
		case R.id.playsound:
		{
			List<MipRobot> mips = MipRobotFinder.getInstance().getMipsConnected();
			for (MipRobot mip : mips) {
				mip.mipPlaySound(MipRobotSound.create(MipCommandValues.kMipSoundFile_MIP_IN_LOVE));
			}
		}
			break;
		case R.id.changechest:
		{
			List<MipRobot> mips = MipRobotFinder.getInstance().getMipsConnected();
			int colorIndex = getResources().getColor(R.color.white_color);
			for (MipRobot mip : mips) {
				// Pass RGB color to define what color you want the Chest RGB to turn
				mip.setMipChestRGBLedWithColor((byte)Color.red(colorIndex), (byte)Color.green(colorIndex), (byte)Color.blue(colorIndex), (byte) 1);
			}
		}
			break;
		case R.id.fallover:
		{
			List<MipRobot> mips = MipRobotFinder.getInstance().getMipsConnected();
			for (MipRobot mip : mips) {
				mip.mipFalloverWithStyle(MipCommandValues.kMipPositionOnBack);
			}
		}
			break;
		case R.id.drive:
			DriveViewFragment fragment = new DriveViewFragment();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.add(R.id.view_id_drive_layout, fragment);
			transaction.attach(fragment);
			transaction.commit();
			break;
		}
	}

	private void scanLeDevice(final boolean enable) {
        if (enable) {
            MipRobotFinder.getInstance().scanForMips();
        } else {
            MipRobotFinder.getInstance().stopScanForMips();
        }
    }
	
	public void updateMipList()
	{
		//connect to first found mip
		List<MipRobot> mipFoundList = MipRobotFinder.getInstance().getMipsFoundList();
		for(MipRobot mipRobot : mipFoundList) {
			connectToMip(mipRobot);
			break;
		}
	}

	private void connectToMip(final MipRobot mip) {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mip.setCallbackInterface(MainMenuActivity.this);
				mip.connect(MainMenuActivity.this.getApplicationContext());
				TextView connectionView = (TextView)MainMenuActivity.this.findViewById(R.id.connect_text);
				connectionView.setText("Connecting: "+mip.getName());
			}
		});
		
	}
	
//	@Override
	public void mipDeviceReady(MipRobot sender) {
		final MipRobot robot = sender;
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TextView connectionView = (TextView)MainMenuActivity.this.findViewById(R.id.connect_text);
				connectionView.setText("Connected: "+robot.getName());
			}
		});
	}

	@Override
	public void mipRobotDidReceiveHardwareVersion(int mipHardwareVersion,
			int mipVoiceFirmwareVersion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotDidReceiveSoftwareVersion(Date mipFirmwareVersionDate,
			int mipFirmwareVersionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotDidReceiveVolumeLevel(int mipVolume) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotDidReceiveIRCommand(ArrayList<Byte> irDataArray,
			int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotDidReceiveWeightReading(byte value,
			boolean leaningForward) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipDeviceDisconnected(MipRobot sender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotDidReceiveBatteryLevelReading(MipRobot mip, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotDidReceivePosition(MipRobot mip, byte position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotIsCurrentlyInBootloader(MipRobot mip,
			boolean isBootloader) {
		// TODO Auto-generated method stub
		
	}

	private final BroadcastReceiver mMipFinderBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (MipRobotFinder.MipRobotFinder_MipFound.equals(action)) {
            	// Connect to mip
            	final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						 List<MipRobot> mipFoundList = MipRobotFinder.getInstance().getMipsFoundList();
						 if (mipFoundList != null && mipFoundList.size() > 0){
							 MipRobot selectedMipRobot = mipFoundList.get(0);
							  if (selectedMipRobot != null){
								  connectToMip(selectedMipRobot);
							  }
						 }
					}
				}, 3000);
				 
            }
        }
	};

}
