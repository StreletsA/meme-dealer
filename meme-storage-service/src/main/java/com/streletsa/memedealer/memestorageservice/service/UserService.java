package com.streletsa.memedealer.memestorageservice.service;

import com.streletsa.memedealer.memestorageservice.model.User;
import com.streletsa.memedealer.memestorageservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public class UserService {

    @Autowired
    UserRepository userRepository;

    public boolean checkAccessToken(String token){
        return userRepository.findByToken(token).isPresent();
    }

    public Optional<User> getUserByToken(@Nullable String userToken){
        return userRepository.findByToken(userToken);
    }

    public void addUser(User user){
        userRepository.insert(user);
    }

    public void deleteUserById(String userId){
        userRepository.deleteById(userId);
    }

    public void deleteUserByToken(String token){
        userRepository.deleteByToken(token);
    }

}
