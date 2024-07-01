package com.luizromao.diazero.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.luizromao.diazero.DiazeroApplication;
import com.luizromao.diazero.domain.incident.Incident;
import com.luizromao.diazero.domain.user.User;
import com.luizromao.diazero.domain.user.dto.RegisterUserDTO;
import com.luizromao.diazero.domain.user.repository.UserRepository;
import com.luizromao.diazero.infra.exception.LoginExistsException;

@SpringBootTest(classes = DiazeroApplication.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test register new user account - success case")
    void registerNewUserAccount200Test() {
        RegisterUserDTO accountDto = new RegisterUserDTO("testuser", "user@email.com", "password");

        when(userRepository.findByLogin("user@email.com")).thenReturn(null); // Simulate login does not exist
        
        User user = new User(accountDto, passwordEncoder);
        
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerNewUserAccount(accountDto);

        verify(userRepository, times(1)).findByLogin("user@email.com");
        verify(userRepository, times(1)).save(any(User.class));

        assertNotNull(result);
        assertEquals("user@email.com", result.getLogin()); 
    }

    @Test
    @DisplayName("Test register new user account - login exists")
    void registerNewUserAccount400Test() {
        RegisterUserDTO accountDto = new RegisterUserDTO("existinguser", "user@email.com", "password");
        when(userRepository.findByLogin("user@email.com")).thenReturn(new UserDetailsImpl());

        assertThrows(LoginExistsException.class, () -> userService.registerNewUserAccount(accountDto));        
    }
}
