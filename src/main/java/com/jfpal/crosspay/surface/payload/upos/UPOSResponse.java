package com.jfpal.crosspay.surface.payload.upos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PAYMENT")
public class UPOSResponse {

	private TRANS TRANS;
	private String AESUpdate;

	public TRANS getTRANS() {
		return TRANS;
	}

	@XmlElement(name = "TRANS")
	public void setTRANS(TRANS tRANS) {
		TRANS = tRANS;
	}

	public String getAESUpdate() {
		return AESUpdate;
	}

	@XmlElement(name = "AESUpdate")
	public void setAESUpdate(String aESUpdate) {
		AESUpdate = aESUpdate;
	}

	@Override
	public String toString() {
		return "UPOSResponse [TRANS=" + TRANS + ", AESUpdate=" + AESUpdate + "]";
	}
	
}
