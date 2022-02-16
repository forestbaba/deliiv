package com.deliiv.server.payload.request;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;

    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    @Size(min=3)
    private String password;

    private String role;

}
