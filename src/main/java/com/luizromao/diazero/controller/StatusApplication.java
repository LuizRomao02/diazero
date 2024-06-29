package com.luizromao.diazero.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/status")
public class StatusApplication {

	@Operation(description = "Return application status", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseEntity.class)), responseCode = "200")})
	@GetMapping
	public String status() throws UnknownHostException {
		return "STATUS OK Application: " + Instant.now() + " Server [" + InetAddress.getLocalHost() + "]"; 
	}
}
