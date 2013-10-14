package de.uniulm.bagception.rfidinventory;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.philipphock.android.lib.services.ServiceUtil;
import de.philipphock.android.lib.services.observation.ServiceObservationActor;
import de.philipphock.android.lib.services.observation.ServiceObservationReactor;
import de.uniulm.bagception.R;
import de.uniulm.bagception.rfidapi.RFIDMiniMe;

import de.uniulm.bagception.rfidinventory.service.USBConnectionActor;
import de.uniulm.bagception.rfidinventory.service.USBConnectionReactor;

public class RFIDInventory extends USBRFIDActivity{

	
	private TextView tv_usbstate;

	private ArrayAdapter<String> adapter;
	private final ArrayList<String> ids = new ArrayList<String>();

	public void inventoryClicked(View v) {
		RFIDMiniMe.triggerInventory(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_usbstate = (TextView) findViewById(R.id.tv_usbstate);

	
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ids);
		ListView view = (ListView) findViewById(R.id.list);
		view.setAdapter(adapter);
		setUsbState(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	
	
	
	
	//** ServiceObservationReactor **// 
	@Override
	public void onServiceStarted(String serviceName) {
		//nothing to do here
		
	}

	@Override
	public void onServiceStopped(String serviceName) {
		tv_usbstate.setText("service disconnected");
		tv_usbstate.setTextColor(android.graphics.Color.YELLOW);
		
		
	}

	//** USBConnectionReactor* *//
	
	@Override
	public void onUSBConnected() {
		setUsbState(true);
	}

	@Override
	public void onUSBDisconnected() {
		
		setUsbState(false);	
	}

	private void setUsbState(boolean state) {
		if (state) {
			tv_usbstate.setText("USB connected");
			tv_usbstate.setTextColor(android.graphics.Color.GREEN);
		} else {
			tv_usbstate.setText("USB disconnected");
			tv_usbstate.setTextColor(android.graphics.Color.RED);
		}
	}

	
	
	// RFID 
	@Override
	public void onTagRecv(String tagname) {
		//adapter.add(tagname);
		//adapter.notifyDataSetChanged();		
	}
	
}
