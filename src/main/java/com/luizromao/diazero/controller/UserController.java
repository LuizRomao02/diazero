package com.luizromao.diazero.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.luizromao.diazero.domain.user.service.UserService;
import com.luizromao.diazero.domain.user.*;
import com.luizromao.diazero.domain.user.dto.DetailedUserDataDTO;
import com.luizromao.diazero.domain.user.dto.RegisterUserDTO;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    @Transactional
    public ResponseEntity<?> register(@RequestBody @Valid RegisterUserDTO accountDto, UriComponentsBuilder uriBuilder) {
        User user = service.registerNewUserAccount(accountDto);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(new DetailedUserDataDTO(user));
    }
}
