package com.deliiv.server.service.user;

import com.deliiv.server.exceptions.AlreadyExistsException;
import com.deliiv.server.exceptions.AppException;
import com.deliiv.server.model.Role;
import com.deliiv.server.model.RoleName;
import com.deliiv.server.model.User;
import com.deliiv.server.payload.request.RegisterRequest;
import com.deliiv.server.payload.response.ApiResponse;
import com.deliiv.server.repository.UserRepository;
import com.deliiv.server.repository.RoleRepository;
import com.deliiv.server.validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordValidator passwordValidator;


    @Override
    public ResponseEntity registerUser(RegisterRequest user, BindingResult bindingResult) {

        passwordValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {

            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }

        User itsexisting = userRepository.findUserByEmail(user.getEmail());
        User itsExisting2 = userRepository.findUserByPhoneNumber(user.getEmail());
        if(itsexisting != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AlreadyExistsException(String.format("User with %s already exist", user.getEmail())));
        }
        if(itsExisting2 != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppException(String.format("User with %s already exist", user.getPhoneNumber()),null));
        }

        User newUserObject = new User();
        newUserObject.setEmail(user.getEmail());
        newUserObject.setPassword(passwordEncoder.encode(user.getPassword()));
        newUserObject.setFirstName(user.getFirstName());
        newUserObject.setLastName(user.getLastName());
        newUserObject.setPhoneNumber(user.getPhoneNumber());
        Role role;
        if(user.getRole().equalsIgnoreCase("admin")){
            newUserObject.setActive(true);
            role = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new AppException("User role not set",null));
        }else if(user.getRole().equalsIgnoreCase("rider")){
            role = roleRepository.findByName(RoleName.ROLE_RIDER).orElseThrow(()-> new AppException("User role not set", null));
        }else if(user.getRole().equalsIgnoreCase("rider_agency")){
            role = roleRepository.findByName(RoleName.ROLE_AGENCY_RIDER).orElseThrow(()-> new AppException("User role not set", null));
        }
        else {
            role = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User role not set",null));
        }
        newUserObject.setRole(Collections.singleton(role));
        User createdUser =  userRepository.save(newUserObject);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/{username}")
                .buildAndExpand(createdUser.getEmail()).toUri();

        return ResponseEntity.created(uri).body(new ApiResponse(true, String.format("An otp has been sent to %s and verification code has been sent to %s", user.getPhoneNumber(), user.getEmail()),newUserObject));
    }
}
