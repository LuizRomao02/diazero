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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private TokenService tokenService;

	@PostMapping
	public ResponseEntity<?> logIn(@RequestBody @Valid AuthenticationUserDTO data) {
		var token = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var authentication = manager.authenticate(token);
        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new DataTokenJWT(tokenJWT));
	}
}
