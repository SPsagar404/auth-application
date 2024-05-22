package com.fin.auth.service.impl;

import com.fin.auth.config.security.jwt.IJwtConstant;
import com.fin.auth.config.security.jwt.JwtProvider;
import com.fin.auth.exception.UserException;
import com.fin.auth.model.User;
import com.fin.auth.repository.IUserRepository;
import com.fin.auth.request.LoginRequest;
import com.fin.auth.response.AuthResponse;
import com.fin.auth.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomeUserServiceImpl customUserDetails;

    @Override
    public String createUser(User user) throws UserException {
        String username = user.getUsername();
        String password = user.getPassword();


        User isEmailExist = userRepository.findByUsername(username);

        if (isEmailExist!=null) {

            throw new UserException("Username Is Already Used With Another Account");
        }

        // Create new user
        User createdUser = new User();
        createdUser.setUsername(username);
        createdUser.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(createdUser);
        if(savedUser != null){
            return "User registered...";
        }
        throw new IllegalArgumentException("Something went wrong...");
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login Success");
        authResponse.setJwt(jwt);
        authResponse.setExpiresAt(IJwtConstant.expiresAt);
        authResponse.setStatus(true);
        return authResponse;
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        System.out.println("sign in userDetails - " + userDetails);

        if (userDetails == null) {
            System.out.println("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
