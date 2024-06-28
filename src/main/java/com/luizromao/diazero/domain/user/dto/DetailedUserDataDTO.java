package com.luizromao.diazero.domain.user.dto;

import com.luizromao.diazero.domain.user.User;

public record DetailedUserDataDTO(Long id, String name, String login) {

    public DetailedUserDataDTO(User user){
        this(user.getId(), user.getName(), user.getLogin());
    }
}
