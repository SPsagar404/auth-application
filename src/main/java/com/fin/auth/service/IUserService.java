package com.fin.auth.service;

import com.fin.auth.exception.UserException;
import com.fin.auth.model.User;

public interface IUserService {

    public User findUserProfileByJwt(String jwt) throws UserException;
}
