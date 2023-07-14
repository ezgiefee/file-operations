package com.motus.fileoperations.service;

import com.motus.fileoperations.dto.UserDto;
import com.motus.fileoperations.model.UserEntity;
import com.motus.fileoperations.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto findByUserName(String userName){
        UserEntity user = userRepository.findByUsername(userName).orElse(null);
        if(user != null){
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setPassword(user.getPassword());
            userDto.setEmail(user.getEmail());
            return userDto;
        }
        else {
            return null;
        }
    }

    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public void saveUser(UserDto userDto){
        UserEntity user = new UserEntity();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);;
    }
}
