package com.luizromao.diazero.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizromao.diazero.domain.user.User;
import com.luizromao.diazero.domain.user.dto.AuthenticationUserDTO;
import com.luizromao.diazero.domain.user.dto.DataTokenJWT;
import com.luizromao.diazero.infra.security.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private TokenService tokenService;

	@Operation(description = "LogIn", responses = {
        @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseEntity.class)), responseCode = "200")})
	@PostMapping
	public ResponseEntity<?> logIn(@RequestBody @Valid AuthenticationUserDTO data) {
		var token = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var authentication = manager.authenticate(token);
        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new DataTokenJWT(tokenJWT));
	}
}
