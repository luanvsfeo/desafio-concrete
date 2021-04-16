package com.luan.desafioconcrete.service;

import java.util.ArrayList;
import com.luan.desafioconcrete.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if(userService.count() == 0 ){
            User user1 = new User();
            user1.setName("teste");
            user1.setPassword("123");
            user1.setEmail("teste");
            user1.changePassword();
            userService.save(user1);
        }

         User user = userService.findByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }else {
            return new org.springframework.security.core.userdetails.User(email, user.getPassword(),
                    new ArrayList<>());
        }
    }
}
