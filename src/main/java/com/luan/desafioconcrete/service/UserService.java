package com.luan.desafioconcrete.service;

import com.luan.desafioconcrete.domain.User;
import com.luan.desafioconcrete.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {


    @Autowired
    UserRepository userRepository;


    public User findById(UUID uuid){
        return userRepository.findById(uuid).orElse(null);
    }

    public User findByEmail(String email){
        return  userRepository.findByEmail(email);
    }

    public User findByEmailAndPassword(String email, String password){
        return  userRepository.findByEmailAndPassword(email,password);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public long count(){
        return userRepository.count();
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
