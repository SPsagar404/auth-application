package com.fin.auth.controller;

import com.fin.auth.exception.UserException;
import com.fin.auth.model.User;
import com.fin.auth.request.LoginRequest;
import com.fin.auth.response.AuthResponse;
import com.fin.auth.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private IAuthService authService;

        @PostMapping("/signup")
        public ResponseEntity<String> createUserHandler(
                @RequestBody User user) throws UserException {
            String response = authService.createUser(user);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }

        @PostMapping(value = "/signin")
        public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
            AuthResponse authResponse = authService.login(loginRequest);
            return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
        }


}
