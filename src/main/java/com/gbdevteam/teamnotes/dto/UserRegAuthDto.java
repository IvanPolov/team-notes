package com.gbdevteam.teamnotes.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegAuthDto {

    @NotBlank(message = "Name must not be blank")
    private String username;

    @Email
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotNull
    @Size(min = 5, max = 15, message = "Password must be between 5 to 15 characters")
    private String password;

    @NotNull
    @Size(min = 5, max = 15, message = "Password must be between 5 to 15 characters")
    private String confirmPassword;

}
