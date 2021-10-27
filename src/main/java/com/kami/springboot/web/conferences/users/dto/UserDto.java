
package com.kami.springboot.web.conferences.users.dto;

import com.kami.springboot.web.conferences.users.authorization.validation.email.ValidEmail;
import com.kami.springboot.web.conferences.users.authorization.validation.password.passwordMatches.PasswordMatches;
import com.kami.springboot.web.conferences.users.authorization.validation.password.validation.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class UserDto {
    @NotNull
    @Size(min = 1, message = "{size.firstname}")
    private String firstName;

    @NotNull
    @Size(min = 1, message = "{size.lastname}")
    private String lastName;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

    @ValidEmail
    @NotNull
    private String email;

    @NotNull
    @NotEmpty
    private String role;

}
