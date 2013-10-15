package de.uniulm.bagception.rfidinventory;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import de.philipphock.android.lib.services.ServiceUtil;
import de.philipphock.android.lib.services.observation.ServiceObservationActor;
import de.philipphock.android.lib.services.observation.ServiceObservationReactor;
import de.uniulm.bagception.broadcastconstants.BagceptionBroadcastContants;
import de.uniulm.bagception.rfidinventory.service.USBConnectionActor;
import de.uniulm.bagception.rfidinventory.service.USBConnectionReactor;

public abstract class USBRFIDActivity extends Activity implements ServiceObservationReactor, USBConnectionReactor{
	private static final String SERVICE_NAME = "de.uniulm.bagception.rfidapi.miniusbconnectionservice.service.USBConnectionService";
	
	
	private ServiceObservationActor observationActor;
	private USBConnectionActor usbConnectionActor;
	

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		observationActor = new ServiceObservationActor(this,SERVICE_NAME);
		usbConnectionActor = new USBConnectionActor(this);
		

		
	}

	


	
	@Override
	protected void onResume() {
		super.onResume();
		onServiceStopped(SERVICE_NAME);
		
		{
		IntentFilter f = new IntentFilter();
		f.addAction(BagceptionBroadcastContants.BROADCAST_RFID_TAG_FOUND);
		registerReceiver(rfidTagReceiver, f);
		}
		
		{
		IntentFilter f = new IntentFilter();
		f.addAction(BagceptionBroadcastContants.BROADCAST_RFID_NOTCONNECTED);
		registerReceiver(rfidNotConnected, f);
		}
		
		
		observationActor.register(this);
		usbConnectionActor.register(this);
		ServiceUtil.requestStatusForServiceObservable(this, SERVICE_NAME);

		Intent i = new Intent();
		i.setAction(BagceptionBroadcastContants.USB_CONNECTION_BROADCAST_RESCAN);
		sendBroadcast(i);
		
	}

	
	@Override
	protected void onPause() {
		usbConnectionActor.unregister(this);
		observationActor.unregister(this);
		unregisterReceiver(rfidTagReceiver);
		unregisterReceiver(rfidNotConnected);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}




	BroadcastReceiver rfidTagReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String id = intent
					.getStringExtra(BagceptionBroadcastContants.BROADCAST_RFID_TAG_FOUND);
			onTagRecv(id);
		}

	};

	
	BroadcastReceiver rfidNotConnected = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(context, "Error: RFID reader not connected", Toast.LENGTH_SHORT).show();
		}
		
	};
	
	
	public abstract void onTagRecv(String tagname);
	public abstract void onReaderNotConnected();
	
}
