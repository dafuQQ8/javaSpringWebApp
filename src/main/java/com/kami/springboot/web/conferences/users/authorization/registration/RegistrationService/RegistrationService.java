package com.kami.springboot.web.conferences.users.authorization.registration.RegistrationService;

import com.kami.springboot.web.conferences.users.dto.UserDto;

import javax.servlet.http.HttpServletRequest;

public interface RegistrationService {

    void registerNewUserAccount(UserDto accountDto);

    void autoAuthentication(HttpServletRequest request, UserDto accountDto);
}
