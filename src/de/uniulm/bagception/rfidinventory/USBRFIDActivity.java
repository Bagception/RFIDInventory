package de.uniulm.bagception.rfidinventory;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import de.philipphock.android.lib.services.ServiceUtil;
import de.philipphock.android.lib.services.observation.ServiceObservationActor;
import de.philipphock.android.lib.services.observation.ServiceObservationReactor;
import de.uniulm.bagception.rfidinventory.service.USBConnectionActor;
import de.uniulm.bagception.rfidinventory.service.USBConnectionReactor;

public abstract class USBRFIDActivity extends Activity implements ServiceObservationReactor, USBConnectionReactor{
	private static final String SERVICE_NAME = "de.uniulm.bagception.rfidapi.miniusbconnectionservice.service.USBConnectionService";
	private static final String USB_CONNECTION_BROADCAST_RESCAN = "de.uniulm.bagception.service.broadcast.usbconnection.rescan";

	private static final String BROADCAST_RFID_TAG_FOUND = "de.uniulm.bagception.rfid.broadcast.tagfound";
	private static final String BROADCAST_RFID_FINISHED = "de.uniulm.bagception.rfid.broadcast.endinventory";
	
	
	private ServiceObservationActor observationActor;
	private USBConnectionActor usbConnectionActor;
	

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		observationActor = new ServiceObservationActor(this,SERVICE_NAME);
		usbConnectionActor = new USBConnectionActor(this);
		IntentFilter f = new IntentFilter();
		f.addAction(BROADCAST_RFID_TAG_FOUND);
		registerReceiver(rfidTagReceiver, f);
	}

	


	
	@Override
	protected void onResume() {
		super.onResume();
		onServiceStopped(SERVICE_NAME);
		observationActor.register(this);
		usbConnectionActor.register(this);
		ServiceUtil.requestStatusForServiceObservable(this, SERVICE_NAME);

		Intent i = new Intent();
		i.setAction(USB_CONNECTION_BROADCAST_RESCAN);
		sendBroadcast(i);
		
	}

	
	@Override
	protected void onPause() {
		usbConnectionActor.unregister(this);
		observationActor.unregister(this);
		
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(rfidTagReceiver);
	}




	BroadcastReceiver rfidTagReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String id = intent
					.getStringExtra(BROADCAST_RFID_TAG_FOUND);
			Log.d("RFID", "RECV: " + id);
			onTagRecv(id);
		}

	};

	
	
	
	
	public abstract void onTagRecv(String tagname);
	
}
