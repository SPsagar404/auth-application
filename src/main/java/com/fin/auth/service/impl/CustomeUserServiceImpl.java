package com.fin.auth.service.impl;

import com.fin.auth.model.User;
import com.fin.auth.repository.IUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CustomeUserServiceImpl implements UserDetailsService {

    private IUserRepository userRepository;

    public CustomeUserServiceImpl(IUserRepository userRepository) {
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if(user==null) {

            throw new UsernameNotFoundException("user not found with username  - "+username);
        }

        List<GrantedAuthority> authorities=new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPassword(),authorities);
    }

}
