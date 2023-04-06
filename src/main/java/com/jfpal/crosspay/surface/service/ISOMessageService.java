package com.jfpal.crosspay.surface.service;

import org.jpos.iso.ISOMsg;

import com.google.gson.JsonObject;
import com.jfpal.crosspay.surface.payload.POSPMessage;

public interface ISOMessageService {

	public String packMessage(POSPMessage request) throws Exception;
	
	public ISOMsg unpackMessage(byte[] isoMessage) throws Exception; 
	
}
