package com.fin.auth.service;

import com.fin.auth.exception.UserException;
import com.fin.auth.model.User;
import com.fin.auth.request.LoginRequest;
import com.fin.auth.response.AuthResponse;

public interface IAuthService {

    public String createUser(User user) throws UserException;

    public AuthResponse login(LoginRequest loginRequest);
}
