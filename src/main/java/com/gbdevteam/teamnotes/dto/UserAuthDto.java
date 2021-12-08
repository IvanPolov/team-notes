package com.gbdevteam.teamnotes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDto {

    @Email
    @NotBlank(message = "Email must not be blank")
    private String email;
    @NotBlank
    private String password;

}
