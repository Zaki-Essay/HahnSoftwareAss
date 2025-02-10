package com.example.employeemanagementsystemapi.controller;

import com.example.employeemanagementsystemapi.config.JwtService;
import com.example.employeemanagementsystemapi.config.UserInfoService;
import com.example.employeemanagementsystemapi.dto.JwtResponse;
import com.example.employeemanagementsystemapi.dto.LoginRequest;
import com.example.employeemanagementsystemapi.dto.SignupRequest;
import com.example.employeemanagementsystemapi.entity.UserInfo;
import com.example.employeemanagementsystemapi.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/signup")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @GetMapping("/hrPersonnel")
    @PreAuthorize("hasAuthority('ROLE_HR_PERSONNEL')")
    public String userProfile() {
        return "Welcome to HR_PERSONNEL";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public String adminProfile() {
        return "Welcome to Admin MANAGER";
    }

    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody LoginRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
