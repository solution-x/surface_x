package com.jfpal.crosspay.surface.serviceImpl;

import jakarta.validation.constraints.NotNull;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.jfpal.crosspay.surface.payload.POSPMessage;
import com.jfpal.crosspay.surface.payload.upos.UPOSRequest;
import com.jfpal.crosspay.surface.payload.upos.UPOSResponse;
import com.jfpal.crosspay.surface.service.ISOMessageService;
import com.jfpal.crosspay.surface.service.LoyaltyMessageService;
import com.jfpal.crosspay.surface.utils.LoyaltyXMLParser;

import java.time.Duration;

@Service
public class LoyaltyMessageServiceImpl implements LoyaltyMessageService{
	JsonObject responseJson;
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ISOMessageService isoMessageService;

	public LoyaltyMessageServiceImpl(RestTemplateBuilder builder) {
		this.restTemplate = builder
				.setConnectTimeout(Duration.ofMillis(10*1000))
				.setReadTimeout(Duration.ofMillis(30*1000))
				.build();
	}
	@Override
	public JsonObject processMessage(POSPMessage request) {
		String requestISOMessage;
		try {
			requestISOMessage = isoMessageService.packMessage(request);
			unpackPrintIso("Unpacking(REQ)...", requestISOMessage);
		} catch (NoSuchFieldException noSuchFieldException) {
			return buildJsonObjError("QFLE", noSuchFieldException.getMessage());
		} catch (Exception exception) {
			return buildJsonObjError("QPIE", "Request ISO Packing Error");
		}

		//**************************************************
		UPOSRequest upos = new UPOSRequest();
		upos.setClientID(request.getTerminalID());
		upos.setRelay("");
		upos.setISO(requestISOMessage);

		//**************************************************
		String sReqXml;
		LoyaltyXMLParser xmlParser = new LoyaltyXMLParser();
		try {
			sReqXml = xmlParser.generateXMLRequest(upos);
			System.out.println("REQ[XML] = " + sReqXml);
		} catch (Exception e) {
			return buildJsonObjError("QPXE", "Request XML Packing Error");
		}

		//**************************************************
		//**** Transmit>>UPOS ******************************
		System.out.println("Sending xml requst to UPOS..");
		String sResXml = sDataExchangeUPOS(sReqXml);
		if (sResXml==null) {
			return this.responseJson;
		}
		System.out.println("RES[XML] = " + sResXml);

		//**************************************************
		UPOSResponse uposResponse;
		try {
			uposResponse = xmlParser.extractUPOSResponse(sResXml);
		} catch (Exception e) {
			return buildJsonObjError("SPXE", "Response XML Parsing Error");
		}

		//**************************************************
		if (uposResponse.getTRANS().getRsp().equals("00")==false) {
			return buildJsonObjError("00" + uposResponse.getTRANS().getRsp(), uposResponse.getTRANS().getHEAD().getResult());
		}
		String responseISOMessage = uposResponse.getTRANS().getISO();
		//****this is a bad response >> exception (TESTING)
		//responseISOMessage = "003860000000000210203801000E800000000000000XXX1205220825000031303832353030303033383831313431333730303130303030303233";
		unpackPrintIso("Unpacking(RES)...", responseISOMessage);

		//**************************************************
		ISOMsg isoMsg;
		try {
			isoMsg = isoMessageService.unpackMessage(ISOUtil.hex2byte(responseISOMessage.substring(14)));
			getJsonObjSuccess(isoMsg);
		} catch (ISOException e) {
			buildJsonObjError("SPIE", "Response ISO Parsing Error");
		} catch (Exception exception) {
			buildJsonObjError("SPGE", "Response Parsing General Error");
		}
		return this.responseJson;
	}

	private void procDe63AndFill(@NotNull String sDe63, JsonObject isoJson) {
		if (sDe63.length()!=62) {
			return;
		}

		isoJson.addProperty("transactionAmount", Long.parseLong(sDe63.substring(0, 12)));
		isoJson.addProperty("redeemedAmount", Long.parseLong(sDe63.substring(12, 24)));
		isoJson.addProperty("totalTransactionAmount", Long.parseLong(sDe63.substring(24, 36)));
		isoJson.addProperty("awardedAmount", Long.parseLong(sDe63.substring(36, 48)));
		isoJson.addProperty("nonAwardedAmount", Long.parseLong(sDe63.substring(48, 60)));
		isoJson.addProperty("loyaltyStatusCode", sDe63.substring(60, 62));
	}

	private JsonObject buildJsonObjError(String sStatusCode, String sStatusMessage) {
		this.responseJson = new JsonObject();
		this.responseJson.addProperty("statusCode", sStatusCode);
		this.responseJson.addProperty("statusMessage", sStatusMessage);
		return this.responseJson;
	}

	private JsonObject getJsonObjSuccess(ISOMsg isoMsg) {
		this.responseJson = new JsonObject();
		JsonObject transDataJson = new JsonObject();
		JsonObject isoDataJson = new JsonObject();

		if (isoMsg==null) {
			return buildJsonObjError("SFER", "Response ISO format error, unknown format");
		}

		try {
			transDataJson.addProperty("messageTypeIdentification", isoMsg.getMTI());
			for (int i = 1; i <= isoMsg.getMaxField(); i++) {
				if (isoMsg.hasField(i) == false) {
					continue;
				}
				switch (i) {
					case 3:
						transDataJson.addProperty("processingCode", isoMsg.getString(i));
						break;
					case 41:
						transDataJson.addProperty("terminalID", isoMsg.getString(i));
						break;
					case 42:
						transDataJson.addProperty("merchantID", isoMsg.getString(i));
						break;
					case 11:
						isoDataJson.addProperty("systemTraceNumber", Integer.parseInt(isoMsg.getString(i)));
						break;
					case 12:
						isoDataJson.addProperty("transactionTime", isoMsg.getString(i));
						break;
					case 13:
						isoDataJson.addProperty("transactionDate", isoMsg.getString(i));
						break;
					case 24:
						isoDataJson.addProperty("nii", isoMsg.getString(i));
						break;
					case 37:
						isoDataJson.addProperty("retrievalRefNumber", isoMsg.getString(i));
						break;
					case 38:
						isoDataJson.addProperty("authorizationCode", isoMsg.getString(i));
						break;
					case 39:
						isoDataJson.addProperty("respCode", isoMsg.getString(i));
						break;
					case 63:
						procDe63AndFill(isoMsg.getString(i), isoDataJson);
						break;
					default:
						break;
				}
			}

			this.responseJson.addProperty("statusCode", "0000");
			this.responseJson.addProperty("statusMessage", "Successful");
			transDataJson.add("isoJson", isoDataJson);
			this.responseJson.add("transData", transDataJson);
		} catch (Exception x) {
			this.responseJson = buildJsonObjError("SFIN", x.toString());
		}

		return this.responseJson;
	}

	private void unpackPrintIso(String sIntro, String sIsoData) {
		System.out.println("=======================================================");
		System.out.println(sIntro);
		System.out.println("[ISO] = " + sIsoData);

		try {
			ISOMsg isoMsg = isoMessageService.unpackMessage(ISOUtil.hex2byte(sIsoData.substring(14)));

			System.out.printf("MTI          = %s%n", isoMsg.getMTI());
			for (int i = 1; i <= isoMsg.getMaxField(); i++) {
				if (isoMsg.hasField(i)) {
					System.out.printf("Field (%02d) = %s%n", i, isoMsg.getString(i));
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		System.out.println("=======================================================");
	}
	
	private String sDataExchangeUPOS(String sReqXml) {
		String uposUrl = "https://testonline.payment.net.sg/TestUPOS_1/";
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<String> responseEntityStr;

		System.out.println("HDR[REQ]=" + headers.toString());
		System.out.println("XML[REQ]=" + sReqXml);

		HttpEntity<String> request = new HttpEntity<>(sReqXml,headers);

		try {
			responseEntityStr = restTemplate.exchange(uposUrl, HttpMethod.POST, request, String.class);

			System.out.println("MSG[RES]=" + responseEntityStr.toString());
			return responseEntityStr.getBody();
		} catch (HttpStatusCodeException httpStatusCodeException) {
			System.out.println("httpStatusCodeException = " + httpStatusCodeException.getMessage());
			buildJsonObjError("HTTE", "HTTP Error=" + httpStatusCodeException.getStatusCode().value());
		} catch (ResourceAccessException resourceAccessException) {
			System.out.println("resourceAccessException = " + resourceAccessException.getMessage());
			buildJsonObjError("ACCE", "Access Error");
		} catch (Exception exception) {
			System.out.println("exception = " + exception.getMessage());
			buildJsonObjError("COME", "Comm Error");
		}
		return null;
	}

}
