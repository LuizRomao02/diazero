package com.luizromao.diazero.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luizromao.diazero.domain.user.User;
import com.luizromao.diazero.domain.user.dto.RegisterUserDTO;
import com.luizromao.diazero.domain.user.repository.UserRepository;
import com.luizromao.diazero.infra.exception.LoginExistsException;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    public User registerNewUserAccount(RegisterUserDTO accountDto) {
        if (loginExist(accountDto.login())) {
            throw new LoginExistsException("There is an account with that login: " + accountDto.login());
        }
        
        User user = new User(accountDto, passwordEncoder);
        return repository.save(user);
    }

    private boolean loginExist(String login){
        return repository.findByLogin(login) != null;
    }
}
