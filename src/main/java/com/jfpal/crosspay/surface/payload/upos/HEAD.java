package com.jfpal.crosspay.surface.payload.upos;

import jakarta.xml.bind.annotation.XmlElement;

public class HEAD {
	
	private String ClientID;
	private String ServerID;
	private String Result;

	public String getClientID() {
		return ClientID;
	}

	@XmlElement(name = "ClientID")
	public void setClientID(String clientID) {
		ClientID = clientID;
	}

	public String getServerID() {
		return ServerID;
	}

	@XmlElement(name = "ServerID")
	public void setServerID(String serverID) {
		ServerID = serverID;
	}

	public String getResult() {
		return Result;
	}

	@XmlElement(name = "Result")
	public void setResult(String result) {
		Result = result;
	}

	@Override
	public String toString() {
		return "HEAD [ClientID=" + ClientID + ", ServerID=" + ServerID + ", Result=" + Result + "]";
	}

	
}
