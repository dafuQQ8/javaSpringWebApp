package com.kami.springboot.web.conferences.users.authorization.registration.RegistrationService;

import com.kami.springboot.web.conferences.users.dao.User;
import com.kami.springboot.web.conferences.users.dao.repository.RoleRepository;
import com.kami.springboot.web.conferences.users.dao.repository.UserRepository;
import com.kami.springboot.web.conferences.users.dto.UserDto;
import com.kami.springboot.web.conferences.webapp.exceptions.UserAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Collections;

@AllArgsConstructor
@Service
public class RegistrationImplementation implements RegistrationService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public void registerNewUserAccount(final UserDto accountDto) {
        if (emailExists(accountDto.getEmail())) {
            throw new UserAlreadyExistsException("There is an account with this email address: " + accountDto.getEmail());
        }
        final User user = new User();

        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setRoles(Collections.singleton(roleRepository.findByName(accountDto.getRole())));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    public void autoAuthentication(HttpServletRequest request, UserDto accountDto) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(accountDto.getEmail(), accountDto.getPassword());
        authToken.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
