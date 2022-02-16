package com.deliiv.server.controllers;

import com.deliiv.server.model.User;
import com.deliiv.server.payload.request.RegisterRequest;
import com.deliiv.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequest user, BindingResult bindingResult){
        return userService.registerUser(user, bindingResult);
    }
}
