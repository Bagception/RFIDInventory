package de.uniulm.bagception.rfidinventory.service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import de.philipphock.android.lib.BroadcastActor;

/**
 * 
 * @author phil
 *
 */
public class USBConnectionActor extends BroadcastActor<USBConnectionReactor>{
	private static final String USB_CONNECTION_BROADCAST_CONNECTED = "de.uniulm.bagception.service.broadcast.usbconnection.connected";
	private static final String USB_CONNECTION_BROADCAST_DISCONNECTED = "de.uniulm.bagception.service.broadcast.usbconnection.disconnected";
	

	public USBConnectionActor(USBConnectionReactor reactor) {
		super(reactor);
	}

	@Override
	public void register(Context context) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(USB_CONNECTION_BROADCAST_CONNECTED);
		filter.addAction(USB_CONNECTION_BROADCAST_DISCONNECTED);
		context.registerReceiver(this,filter);
		
	}


	@Override
	public void onReceive(Context context, Intent intent) {
		if (USB_CONNECTION_BROADCAST_CONNECTED.equals(intent.getAction())){
			reactor.onUSBConnected();
		} else if (USB_CONNECTION_BROADCAST_DISCONNECTED.equals(intent.getAction())){
			reactor.onUSBDisconnected();
		}
	}
	
	
}
