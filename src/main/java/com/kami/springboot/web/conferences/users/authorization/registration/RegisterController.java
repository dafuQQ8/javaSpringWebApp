package com.kami.springboot.web.conferences.users.authorization.registration;

import com.kami.springboot.web.conferences.users.authorization.registration.RegistrationService.RegistrationService;
import com.kami.springboot.web.conferences.users.dto.UserDto;
import com.kami.springboot.web.conferences.webapp.exceptions.UserAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Slf4j
@AllArgsConstructor
public class RegisterController {

    private final RegistrationService registrationService;


    @GetMapping("/registration")
    public String registrationPage(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userForm", userDto);
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("userForm") @Valid final UserDto accountDto,
                                      BindingResult bindingResult,
                                      HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.error(String.valueOf(bindingResult.getFieldError()));
            return "registration";
        } else {
            try {
                registrationService.registerNewUserAccount(accountDto);
                log.info("Registering user account with information: {}", accountDto);
                registrationService.autoAuthentication(request, accountDto);
                return "redirect:/home";
            } catch (UserAlreadyExistsException e) {
                bindingResult.addError((new FieldError("accountDto",
                        "email", "Email address is already in use!")));
                return "registration";
            }
        }
    }
}
