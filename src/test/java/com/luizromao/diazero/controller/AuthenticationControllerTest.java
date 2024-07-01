package com.luizromao.diazero.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizromao.diazero.DiazeroApplication;
import com.luizromao.diazero.domain.user.User;
import com.luizromao.diazero.domain.user.dto.AuthenticationUserDTO;
import com.luizromao.diazero.infra.security.TokenService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

@SpringBootTest(classes = DiazeroApplication.class)
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
	AuthenticationManager manager;

	@Mock
	TokenService tokenService;

    @InjectMocks
    AuthenticationController authenticationController;

    MockMvc mockMvc;

    @BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
	}

    @Test
	@DisplayName("Request LoginIn return 200")
	void logIn200Test() throws Exception {
        var tokenJWT = "mockedToken";
        var user = new User();
        var authentication = org.mockito.Mockito.mock(Authentication.class);

        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn(tokenJWT);

        AuthenticationUserDTO authUser = new AuthenticationUserDTO("userteste@email.com", "123");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(authUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(tokenJWT));
    }
}
