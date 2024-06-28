package com.luizromao.diazero.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusApplication {

	@GetMapping
	public String status() throws UnknownHostException {
		return "STATUS OK Application: " + Instant.now() + " Server [" + InetAddress.getLocalHost() + "]"; 
	}
}
