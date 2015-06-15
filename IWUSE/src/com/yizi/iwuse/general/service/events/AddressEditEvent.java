package com.yizi.iwuse.general.service.events;

import com.yizi.iwuse.constants.GeneralConst;

public class AddressEditEvent {
	public int eventType = GeneralConst.EVENTTYPE_EDITADDR;
	public String address;
	
}
