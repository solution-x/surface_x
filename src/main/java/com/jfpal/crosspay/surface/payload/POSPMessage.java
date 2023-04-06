package com.jfpal.crosspay.surface.payload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class POSPMessage {

	@NotNull(message = "messageTypeIdentification required.")
	@NotBlank(message = "messageTypeIdentification cannot be blank.")
	private String messageTypeIdentification;

	@NotNull(message = "processingCode required.")
	@NotBlank(message = "processingCode cannot be blank.")
	private String processingCode;

	@NotNull(message = "terminalID required.")
	@NotBlank(message = "terminalID cannot be blank.")
	private String terminalID;

	@NotNull(message = "merchantID required.")
	@NotBlank(message = "merchantID cannot be blank.")
	private String merchantID;

	@Valid
	private IsoData IsoDataObject;

	public String getMessageTypeIdentification() {
		return messageTypeIdentification;
	}

	public String getProcessingCode() {
		return processingCode;
	}

	public String getTerminalID() {
		return terminalID;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public IsoData getIsoData() {
		return IsoDataObject;
	}

	public void setMessageTypeIdentification(String messageTypeIdentification) {
		this.messageTypeIdentification = messageTypeIdentification;
	}

	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}

	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public void setIsoData(IsoData isoDataObject) {
		this.IsoDataObject = isoDataObject;
	}

	@Override
	public String toString() {
		return "POSPMessage [messageTypeIdentification=" + messageTypeIdentification + ", processingCode="
				+ processingCode + ", terminalID=" + terminalID + ", merchantID=" + merchantID + ", IsoDataObject="
				+ IsoDataObject + "]";
	}
	
	
}
