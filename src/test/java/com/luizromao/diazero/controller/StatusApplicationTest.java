package com.luizromao.diazero.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.luizromao.diazero.DiazeroApplication;

@SpringBootTest(classes = DiazeroApplication.class)
@ExtendWith(MockitoExtension.class)
class StatusApplicationTest {


    @InjectMocks
	StatusApplicationController statusApplicationController;

    MockMvc mockMvc;

    @BeforeEach
    void setup(){
		mockMvc = MockMvcBuilders.standaloneSetup(statusApplicationController).build();
    }

    @Test
    @DisplayName("Should return status http code 200 when information is valid")
    void status200Test() throws Exception{
        mockMvc.perform(get("/status")).andExpect(status().isOk());
    }
}
