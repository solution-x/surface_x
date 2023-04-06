package com.jfpal.crosspay.surface.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.google.gson.JsonObject;
import com.jfpal.crosspay.surface.payload.POSPMessage;
import com.jfpal.crosspay.surface.service.LoyaltyMessageService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("loyalty")
public class LoyaltyController {

	@Autowired
	private LoyaltyMessageService loyaltyMessageService;

	@GetMapping("/testg")
	public String index() {
		return "test get...";
	}

	//@PostMapping("/process")
	@PostMapping(value = "/process", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> processMessage(@Valid @RequestBody POSPMessage request) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		JsonObject response = loyaltyMessageService.processMessage(request);

		return new ResponseEntity<String>(response.toString(), responseHeaders, HttpStatus.OK);
	}

	@PostMapping(value = "/testp", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> testPost(@Valid @RequestBody POSPMessage request) {

		Logger logger = LoggerFactory.getLogger(LoyaltyController.class);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		JsonObject response = loyaltyMessageService.processMessage(request);

		logger.trace(request.toString());
		logger.trace(response.toString());

		return new ResponseEntity<String>(response.toString(), responseHeaders, HttpStatus.OK);
	}
}
