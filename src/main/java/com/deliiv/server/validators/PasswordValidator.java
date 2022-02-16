package com.deliiv.server.validators;

import com.deliiv.server.payload.request.RegisterRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return RegisterRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        RegisterRequest user = (RegisterRequest) object;

        if (user.getPassword().length() < 6) {
            errors.rejectValue("password", "Length", "Password must be at least 6 characters");
        }
        if(user.getPhoneNumber().isEmpty()){
            errors.rejectValue("phoneNumber","required","PhoneNumber is required");
        }
        if(user.getEmail().isEmpty()){
            errors.rejectValue("email","required","email is required");
        }


    }
}