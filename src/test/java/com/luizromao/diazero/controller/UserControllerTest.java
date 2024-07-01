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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.luizromao.diazero.DiazeroApplication;
import com.luizromao.diazero.domain.user.User;
import com.luizromao.diazero.domain.user.dto.DetailedUserDataDTO;
import com.luizromao.diazero.domain.user.dto.RegisterUserDTO;
import com.luizromao.diazero.domain.user.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(classes = DiazeroApplication.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureJsonTesters
class UserControllerTest {

    @Mock
    UserService service;

    @InjectMocks
    UserController userController;

    @Autowired
    private JacksonTester<RegisterUserDTO> dataCreateUserJson;

    @Autowired
    private JacksonTester<DetailedUserDataDTO> detailUserDataJson;

    MockMvc mockMvc;

    @BeforeEach
    void setup(){
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Should return http code 200 when information is valid")
    void register200Test() throws Exception {
        var dataRegister = new RegisterUserDTO(
            "user", 
            "login@email.com", 
            "123"
        );

        User user = new User();
        user.setId(1L);
        user.setLogin("login@email.com");
        user.setName("user");
        user.setPassword("123");

        when(service.registerNewUserAccount(any(RegisterUserDTO.class))).thenReturn(user);

        var response = mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                    .content(dataCreateUserJson.write(dataRegister).getJson())).andReturn().getResponse();

        var detailUser = new DetailedUserDataDTO(
            1L, 
            dataRegister.name(), 
            dataRegister.login()
        );

        var jsonReturn = detailUserDataJson.write(detailUser).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonReturn);
    }

    @Test
    @DisplayName("Should return http code 400 when information is invalid")
    @WithMockUser
    void register400Test() throws Exception{
        var response = mockMvc.perform(post("/users")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
