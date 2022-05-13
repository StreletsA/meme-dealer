package com.streletsa.memedealer.memestorageservice.service;

import com.streletsa.memedealer.memestorageservice.config.ConstantsConfig;
import com.streletsa.memedealer.memestorageservice.model.User;
import com.streletsa.memedealer.memestorageservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    private void init(){
        String adminToken = ConstantsConfig.ADMIN_TOKEN;
        Optional<User> adminOptional = getUserByToken(adminToken);

        if (adminOptional.isEmpty()){
            User admin = new User();
            admin.setName("Admin");
            admin.setToken(adminToken);
            addUser(admin);
        }
    }

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
