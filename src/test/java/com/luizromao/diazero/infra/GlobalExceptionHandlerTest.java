package com.luizromao.diazero.infra;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.luizromao.diazero.DiazeroApplication;
import com.luizromao.diazero.infra.exception.GlobalExceptionHandler;
import com.luizromao.diazero.infra.exception.IncidentException;
import com.luizromao.diazero.infra.exception.LoginExistsException;

@SpringBootTest(classes = DiazeroApplication.class)
@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    GlobalExceptionHandler handler;

    @Test
    void handleLoginExistsExceptionTest() {
        LoginExistsException ex = new LoginExistsException("Login already exists");
        ResponseEntity<String> response = handler.handleLoginExistsException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Login already exists");
    }

    @Test
    void handleIncidentExceptionTest() {
        IncidentException ex = new IncidentException("Incident error");
        ResponseEntity<String> response = handler.handleIncidentException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Incident error");
    }

    @Test
    void handleBadCredentialsExceptionTest() {
        ResponseEntity<String> response = handler.handleBadCredentialsException();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("Invalid Credentials");
    }

    @Test
    void handleGeneralExceptionTest() {
        Exception ex = new Exception("General error");
        ResponseEntity<String> response = handler.handleGeneralException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("An unexpected error occurred");
    }

    @Test
    void handleUsernameNotFoundExceptionTest() {
        UsernameNotFoundException ex = new UsernameNotFoundException("User not found");
        ResponseEntity<String> response = handler.handleUsernameNotFoundException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("User not found with username");
    }
}
