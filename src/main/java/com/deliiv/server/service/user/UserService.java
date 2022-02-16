package com.deliiv.server.service.user;

import com.deliiv.server.model.User;
import com.deliiv.server.payload.request.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface UserService {
    ResponseEntity registerUser(RegisterRequest user, BindingResult bindingResult);
}
