package com.luizromao.diazero.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import com.luizromao.diazero.DiazeroApplication;
import com.luizromao.diazero.domain.user.User;
import com.luizromao.diazero.domain.user.repository.UserRepository;

@SpringBootTest(classes = DiazeroApplication.class)
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    AuthenticationService authenticationService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("Should load user by username")
    void loadUserByUsername200Test() {
        String username = "testuser";
        User user = new User();
        user.setLogin(username);
        user.setPassword("password123"); 

        when(repository.findByLogin(anyString())).thenReturn(user);

        UserDetails userDetails = authenticationService.loadUserByUsername(username);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void loadUserByUsernameUser404Test() {
        String username = "nonexistentuser";

        when(repository.findByLogin(anyString())).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> authenticationService.loadUserByUsername(username));

        assertThat(exception.getMessage()).isEqualTo("User not found with username: " + username);
    }
}
