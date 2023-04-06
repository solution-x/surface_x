package com.jfpal.crosspay.surface.payload.upos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "post_data")
public class UPOSRequest {

	private String ClientID;
	private String Relay;
	private String ISO;
	private String Cipher;

	public String getClientID() {
		return ClientID;
	}

	@XmlElement(name = "ClientID")
	public void setClientID(String clientID) {
		ClientID = clientID;
	}

	public String getRelay() {
		return Relay;
	}

	@XmlElement(name = "Relay")
	public void setRelay(String relay) {
		Relay = relay;
	}

	public String getISO() {
		return ISO;
	}

	@XmlElement(name = "ISO")
	public void setISO(String iSO) {
		ISO = iSO;
	}

	public String getCipher() {
		return Cipher;
	}

	@XmlElement(name = "Cipher")
	public void setCipher(String cipher) {
		Cipher = cipher;
	}

	@Override
	public String toString() {
		return "UPOSRequest [ClientID=" + ClientID + ", Relay=" + Relay + ", ISO=" + ISO + ", Cipher=" + Cipher + "]";
	}
	
	

}
