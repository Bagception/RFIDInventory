package de.uniulm.bagception.rfidinventory;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class RFIDInventory extends USBRFIDActivity{
	public static final String USB_CONNECTION_BROADCAST_RFIDSCAN = "de.uniulm.bagception.service.broadcast.usbconnection.rfidscan";

	
	private TextView tv_usbstate;

	private ArrayAdapter<String> adapter;
	private final ArrayList<String> ids = new ArrayList<String>();

	public void inventoryClicked(View v) {
		Log.d("RFID","br for rfid scan send");
		Intent i = new Intent();
		i.setAction(USB_CONNECTION_BROADCAST_RFIDSCAN);
		sendBroadcast(i);
		
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
		tv_usbstate.setTextColor(android.graphics.Color.RED);
		
		
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
