package com.jfpal.crosspay.surface.service;

import com.google.gson.JsonObject;
import com.jfpal.crosspay.surface.payload.POSPMessage;

public interface LoyaltyMessageService {

	public JsonObject processMessage(POSPMessage request);
}
