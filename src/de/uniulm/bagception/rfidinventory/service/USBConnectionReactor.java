package de.uniulm.bagception.rfidinventory.service;

import de.philipphock.android.lib.Reactor;

public interface USBConnectionReactor extends Reactor{

	public void onUSBConnected();
	public void onUSBDisconnected();
}
