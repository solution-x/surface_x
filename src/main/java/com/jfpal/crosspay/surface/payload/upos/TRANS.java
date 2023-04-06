package com.jfpal.crosspay.surface.payload.upos;

import jakarta.xml.bind.annotation.XmlElement;

public class TRANS {
	
	private String Rsp;
	private HEAD HEAD;
	private String ISO;

	public String getRsp() {
		return Rsp;
	}

	@XmlElement(name = "Rsp")
	public void setRsp(String rsp) {
		Rsp = rsp;
	}

	public HEAD getHEAD() {
		return HEAD;
	}

	@XmlElement(name = "HEAD")
	public void setHEAD(HEAD hEAD) {
		HEAD = hEAD;
	}

	public String getISO() {
		return ISO;
	}

	@XmlElement(name = "ISO")
	public void setISO(String iSO) {
		ISO = iSO;
	}

	@Override
	public String toString() {
		return "TRANS [Rsp=" + Rsp + ", HEAD=" + HEAD + ", ISO=" + ISO + "]";
	}

	
}
