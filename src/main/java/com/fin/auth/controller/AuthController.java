package com.fin.auth.controller;

import com.fin.auth.config.security.jwt.IJwtConstant;
import com.fin.auth.config.security.jwt.JwtProvider;
import com.fin.auth.exception.UserException;
import com.fin.auth.model.User;
import com.fin.auth.repository.IUserRepository;
import com.fin.auth.request.LoginRequest;
import com.fin.auth.response.AuthResponse;
import com.fin.auth.service.impl.CustomeUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
        @Autowired
        private IUserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private CustomeUserServiceImpl customUserDetails;

        @PostMapping("/signup")
        public ResponseEntity<String> createUserHandler(
                @RequestBody User user) throws UserException {

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

            userRepository.save(savedUser);

//            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            Map<String,String> jwtData = JwtProvider.generateToken(authentication);
//
//            AuthResponse authResponse = new AuthResponse();
//            authResponse.setJwt(jwtData.get("jwt"));
//            authResponse.setExpiredAt(jwtData.get("expiredAt"));
//            authResponse.setStatus(true);
//            authResponse.setMessage("Register Success");

            return new ResponseEntity<>("User Registered", HttpStatus.OK);

        }

        @PostMapping(value = "/signin")
        public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {

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

            return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
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
