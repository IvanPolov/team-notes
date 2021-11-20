package com.gbdevteam.teamnotes.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegAuthDto {

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    private Boolean isVerified;

    @NotNull
    @Size(min = 5, max = 15, message = "Password must be between 5 to 15 characters")
    private String password;

    @NotNull
    @Size(min = 5, max = 15, message = "Password must be between 5 to 15 characters")
    private String confirmPassword;

}
