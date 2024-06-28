package com.luizromao.diazero.infra.exception;

public class LoginExistsException extends RuntimeException{

    public LoginExistsException(String message){
        super(message);
    }
}
