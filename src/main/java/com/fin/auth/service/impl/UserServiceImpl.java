package com.fin.auth.service.impl;


import com.fin.auth.config.security.jwt.JwtProvider;
import com.fin.auth.exception.UserException;
import com.fin.auth.model.User;
import com.fin.auth.repository.IUserRepository;
import com.fin.auth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository repository;

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String userName= JwtProvider.getUserNameFromJwtToken(jwt);


        User user = repository.findByUsername(userName);

        if(user==null) {
            throw new UserException("user not exist with email "+user.getUsername());
        }
        return user;
    }
}
