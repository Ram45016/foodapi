package com.trickytechies.foodapi.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.trickytechies.foodapi.model.User;
import com.trickytechies.foodapi.service.AuthenticationResponse;
import com.trickytechies.foodapi.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

   @PostMapping("/register")
   public ResponseEntity<AuthenticationResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User user) {
        return ResponseEntity.ok(authenticationService.login(user));
    }
    

}