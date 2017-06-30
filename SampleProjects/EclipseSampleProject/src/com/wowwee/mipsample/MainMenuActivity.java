package com.wowwee.mipsample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.wowwee.bluetoothrobotcontrollib.MipCommandValues;
import com.wowwee.bluetoothrobotcontrollib.sdk.MipRobot;
import com.wowwee.bluetoothrobotcontrollib.sdk.MipRobotFinder;
import com.wowwee.bluetoothrobotcontrollib.minionmip.sdk.MinionMipRobot;
import com.wowwee.bluetoothrobotcontrollib.minionmip.sdk.MinionMipCommandValues;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
				Random r = new Random();
				// Distinguish different robot by mipRobotType
				if(mip.mipRobotType == MipRobot.MipType_MinionMip) {
					// cast MipRobot type to MinionMipRobot type to play minion sound
					switch(r.nextInt(4)) {
					case 0:
						((MinionMipRobot)mip).playMinionMipSoundWithIndex(MinionMipCommandValues.kMinionMipSoundFile_HAPPY_DMF_MINION_HEH_HEH_BELLO);
						break;
						
					case 1:
						((MinionMipRobot)mip).playMinionMipSoundWithIndex(MinionMipCommandValues.kMinionMipSoundFile_HAPPY_KEVIN_DIAL_096_LAUGH_TOCARINA_BOCALOO);
						break;
						
					case 2:
						((MinionMipRobot)mip).playMinionMipSoundWithIndex(MinionMipCommandValues.kMinionMipSoundFile_SCREAMS_DMF_MINION_LONG_SCREAM_WHEN_WHIPED_DM2);
						break;
						
					default:
						((MinionMipRobot)mip).playMinionMipSoundWithIndex(MinionMipCommandValues.kMinionMipSoundFile_NEW_FART_WET);
						break;
					}
				}
				else {
					switch(r.nextInt(4)) {
					case 0:
						mip.mipPlaySound(MipRobotSound.create(MipCommandValues.kMipSoundFile_MIP_IN_LOVE));
						break;
						
					case 1:
						mip.mipPlaySound(MipRobotSound.create(MipCommandValues.kMipSoundFile_ACTION_OUT_OF_BREATH));
						break;
						
					case 2:
						mip.mipPlaySound(MipRobotSound.create(MipCommandValues.kMipSoundFile_MIP_3));
						break;
						
					default:
						mip.mipPlaySound(MipRobotSound.create(MipCommandValues.kMipSoundFile_MIP_GOGOGO));
						break;						
					}
				}				
			}
		}
			break;
		case R.id.changechest:
		{
			List<MipRobot> mips = MipRobotFinder.getInstance().getMipsConnected();
			Random r = new Random();
			int colorIndex;
			switch(r.nextInt(4)) {
				case 0:
					colorIndex = getResources().getColor(R.color.green_color);
					break;
				case 1:
					colorIndex = getResources().getColor(R.color.red_color);
					break;
				case 2:
					colorIndex = getResources().getColor(R.color.orange_color);
					break;
				default:
					colorIndex = getResources().getColor(R.color.white_color);
					break;
			
			}
			for (MipRobot mip : mips) {
				// Pass RGB color to define what color you want the LED to turn
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
		case R.id.weight:
		{
			List<MipRobot> mips = MipRobotFinder.getInstance().getMipsConnected();
			for (MipRobot mip : mips) {
				mip.readMipSensorWeightLevel();
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
        	// Scan for MiP/Coder MiP/Turbo Dave
        	MipRobotFinder.getInstance().scanForAllRobots();
        	// Scan for MiP/Coder MiP
//        	MipRobotFinder.getInstance().scanForMips();
        	// Scan for Turbo Dave
//        	MipRobotFinder.getInstance().scanForMinions();
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
	public void mipRobotDidReceiveHardwareVersion(MipRobot mip,int mipHardwareVersion,
			int mipVoiceFirmwareVersion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotDidReceiveSoftwareVersion(Date mipFirmwareVersionDate,
			int mipFirmwareVersionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotDidReceiveVolumeLevel(MipRobot mip,int mipVolume) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotDidReceiveIRCommand(MipRobot mip,ArrayList<Byte> irDataArray,
			int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotDidReceiveWeightReading(MipRobot mip,byte value,
			boolean leaningForward) {
		final String weightLevel = "level " + value + "!";
		updateWeightButtonLable(weightLevel);
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
				}, 1);
				 
            }
        }
	};

	private final void updateWeightButtonLable(final String string)
	{
		 try {
	            runOnUiThread(new Runnable() {
	            final	Button button = (Button) findViewById(R.id.weight);
	                @Override
	                public void run() {
	            	  
	            		Log.d("MainMenuActivity", "mipRobotDidReceiveWeightReading " + string);
	            		
	            		button.setText(string);
	               
	            		Handler handler = new Handler();
	            		handler.postDelayed(new Runnable(){
	            		@Override
	            		      public void run(){
	            			 if(button.getText().toString().compareTo("Weight Level") !=0)
	            			 {
	            				 updateWeightButtonLable("Weight Level");
	            			 }
	            		   }
	            		}, 5);
	                }
	            });
	            Thread.sleep(300);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	}

	@Override
	public void mipRobotDidReceiveClapDetectionStatusIsEnabled(MipRobot arg0, boolean arg1, long arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotDidReceiveGesture(MipRobot arg0, byte arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mipRobotDidReceiveNumberOfClaps(MipRobot arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
