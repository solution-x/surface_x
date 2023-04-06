package com.jfpal.crosspay.surface.utils;

import java.io.StringReader;
import java.io.StringWriter;


import com.jfpal.crosspay.surface.payload.upos.UPOSRequest;
import com.jfpal.crosspay.surface.payload.upos.UPOSResponse;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;


public class LoyaltyXMLParser {

	public String generateXMLRequest(UPOSRequest request) throws Exception {

		System.out.println("xml passing..");
		
		JAXBContext context = JAXBContext.newInstance(UPOSRequest.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		marshaller.marshal(request, sw);
		return sw.toString();
	}

	public UPOSResponse extractUPOSResponse(String xml) throws Exception {

		JAXBContext context = JAXBContext.newInstance(UPOSResponse.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		
		StringReader reader = new StringReader(xml);
		UPOSResponse response = (UPOSResponse) unmarshaller.unmarshal(reader);
		return response;
	}

}
