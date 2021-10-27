package com.kami.springboot.web.conferences;

import com.kami.springboot.web.conferences.users.dao.Privilege;
import com.kami.springboot.web.conferences.users.dao.Role;
import com.kami.springboot.web.conferences.users.dao.User;
import com.kami.springboot.web.conferences.users.dao.repository.PrivilegeRepository;
import com.kami.springboot.web.conferences.users.dao.repository.RoleRepository;
import com.kami.springboot.web.conferences.users.dao.repository.UserRepository;
import com.kami.springboot.web.conferences.webapp.conferences.dao.Status;
import com.kami.springboot.web.conferences.webapp.exceptions.UserAlreadyExistsException;
import com.kami.springboot.web.conferences.webapp.conferences.dao.Conferences;
import com.kami.springboot.web.conferences.webapp.conferences.dao.repository.ConferencesRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@AllArgsConstructor
public class OnSetupLoader implements
        ApplicationListener<ContextRefreshedEvent> {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;

    private final ConferencesRepository conferencesRepository;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        // create initial privileges
        final Privilege getPrivilege = createPrivilegeIfNotFound("GET_PRIVILEGE");
        final Privilege postPrivilege = createPrivilegeIfNotFound("POST_PRIVILEGE");
        final Privilege putPrivilege = createPrivilegeIfNotFound("PUT_PRIVILEGE");
        final Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        // create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(getPrivilege, postPrivilege, putPrivilege, deletePrivilege));
        final List<Privilege> speakerPrivileges = new ArrayList<>(Arrays.asList(getPrivilege, putPrivilege));
        final List<Privilege> attendeePrivileges = new ArrayList<>(List.of(getPrivilege, putPrivilege));
        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        final Role speakerRole = createRoleIfNotFound("ROLE_SPEAKER", speakerPrivileges);
        final Role attendeeRole = createRoleIfNotFound("ROLE_ATTENDEE", attendeePrivileges);

        createUserIfNotFound("admin@stuff.com", "sonicare", new ArrayList<>(List.of(adminRole)));
        createUserIfNotFound("speaker@domain.com", "sonicare", new ArrayList<>(List.of(speakerRole)));
        createUserIfNotFound("attendee@domain.com", "sonicare", new ArrayList<>(List.of(attendeeRole)));

        createConference("GOTO","Berlin","Germany","2020-10-26", "2021-10-29","https://trifork.com/wp-content/uploads/2019/09/goto-logo.png","canceled");
        createConference("Devoxx","Krakow","Poland","2020-10-26", "2021-10-21","https://res-1.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco/obqwbc7vwrvadrdiitmo","canceled");
        createConference("Øredev","Malmö","Sweden","2020-11-04", "2021-10-25","https://cyberhades.ams3.cdn.digitaloceanspaces.com/imagenes/103753647_640jpg_8212112459_o_opt.jpg","canceled");
        createConference("Google I/O", "Mountain View, CA", "USA", "2022-05-12", "2022-10-26", "https://events.google.com/io/assets/io_social_asset.jpg", "online");
        createConference("DevFest Live", "Online", "World", "2021-05-21", "2021-10-26", "https://pbs.twimg.com/profile_images/1320729709534187520/-urnzdiO_400x400.jpg", "online");
        createConference("DevProdEng Showdown Android at Scale S1E4","Online","World","2021-05-29", "2021-10-26","https://cayote.openmtx.com/pic/profile_banners%2F1344418514795335681%2F1609369773%2F1500x500","online");
        createConference("Speaking About","Online","World","2021-05-26", "2021-10-21","https://pbs.twimg.com/profile_images/1286385560890806279/Bqu5Wsre_400x400.jpg","online");
        createConference("GOTO","Amsterdam","Netherlands","2021-06-15", "2021-10-26","https://pbs.twimg.com/profile_images/1159497155289260040/rCqZxpYX_400x400.jpg","online");
        createConference("ADDC","Barcelona","Spain","2021-06-23", "2021-10-26","https://cdn.dribbble.com/users/76556/screenshots/4273518/post.png?compress=1&resize=800x600","online");
        createConference("360|AnDev","Online","World","2021-07-22", "2021-10-28","https://speakers.360andev.com/images/logo-banner.png","online");
        createConference("Droidcon","Lagos","Nigeria","2021-07-30", "2021-10-26","https://pbs.twimg.com/profile_images/1285509156410208256/BX81FwSp_400x400.jpg","online");
        createConference("Mobiconf","Krakow","Poland","2020-10-01", "2021-10-28","https://papercallio-production.s3.amazonaws.com/uploads/event/logo/2314/900x400.png","canceled");
        createConference("Android Summit","Online","World","2020-10-08", "2021-10-28","https://developer.android.com/events/dev-summit/images/twitter-card-image.png","online");
        createConference("GOTO","Berlin","Germany","2020-10-26", "2021-10-26","https://trifork.com/wp-content/uploads/2019/09/goto-logo.png","canceled");
        createConference("Devoxx","Krakow","Poland","2020-10-26", "2021-10-26","https://res-1.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco/obqwbc7vwrvadrdiitmo","canceled");
        createConference("Øredev","Malmö","Sweden","2020-11-04", "2021-10-26","https://cyberhades.ams3.cdn.digitaloceanspaces.com/imagenes/103753647_640jpg_8212112459_o_opt.jpg","canceled");
        createConference("Google I/O", "Mountain View, CA", "USA", "2022-05-18", "2022-10-26", "https://events.google.com/io/assets/io_social_asset.jpg", "online");
        createConference("DevFest Live", "Online", "World", "2021-05-22", "2021-10-26", "https://pbs.twimg.com/profile_images/1320729709534187520/-urnzdiO_400x400.jpg", "online");
        createConference("DevProdEng Showdown Android at Scale S1E4","Online","World","2021-05-26", "2021-10-26","https://cayote.openmtx.com/pic/profile_banners%2F1344418514795335681%2F1609369773%2F1500x500","online");
        createConference("Speaking About","Online","World","2021-05-26", "2021-10-26","https://pbs.twimg.com/profile_images/1286385560890806279/Bqu5Wsre_400x400.jpg","online");
        createConference("GOTO","Amsterdam","Netherlands","2021-06-15", "2021-10-26","https://pbs.twimg.com/profile_images/1159497155289260040/rCqZxpYX_400x400.jpg","online");
        createConference("ADDC","Barcelona","Spain","2021-06-23", "2021-10-26","https://cdn.dribbble.com/users/76556/screenshots/4273518/post.png?compress=1&resize=800x600","online");
        createConference("360|AnDev","Online","World","2021-07-22", "2021-10-28","https://speakers.360andev.com/images/logo-banner.png","online");
        createConference("Droidcon","Lagos","Nigeria","2021-07-30", "2021-10-26","https://pbs.twimg.com/profile_images/1285509156410208256/BX81FwSp_400x400.jpg","online");
        createConference("Mobiconf","Krakow","Poland","2020-10-01", "2021-10-26","https://papercallio-production.s3.amazonaws.com/uploads/event/logo/2314/900x400.png","canceled");
        createConference("Android Summit","Online","World","2020-10-08", "2021-10-28","https://developer.android.com/events/dev-summit/images/twitter-card-image.png","online");
        createConference("DevFest Norway","Online","Norway","2020-10-15", "2021-10-26","https://devfest-norway-2020.firebaseapp.com/images/social_1.png","online");
        createConference("GOTO","Berlin","Germany","2020-10-26", "2021-10-28","https://trifork.com/wp-content/uploads/2019/09/goto-logo.png","canceled");
        createConference("Devoxx","Krakow","Poland","2020-10-26", "2021-10-26","https://res-1.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco/obqwbc7vwrvadrdiitmo","canceled");
        createConference("Øredev","Malmö","Sweden","2020-11-04", "2021-10-28","https://cyberhades.ams3.cdn.digitaloceanspaces.com/imagenes/103753647_640jpg_8212112459_o_opt.jpg","canceled");

    }

    @Transactional
    void createConference(final String name,
                          final String city,
                          final String country,
                          final String startDate,
                          final String finishDate,
                          final String logo,
                          final String type) {
        Conferences conference = new Conferences();
        conference.setName(name);
        conference.setCity(city);
        conference.setCountry(country);
        conference.setStartDate(stringDate(startDate));
        conference.setFinishDate(stringDate(finishDate));
        conference.setLogo(logo);
        conference.setType(type);
        conference.setStatus(Status.UPCOMING);
        conferencesRepository.save(conference);

    }

    @Transactional
    Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    void createUserIfNotFound(final String email, final String password, final Collection<Role> roles) {
        User user = userRepository.findByEmail(email);
        try {
            if (user == null) {
                user = new User();
                user.setPassword(passwordEncoder.encode(password));
                user.setEmail(email);
                user.setEnabled(true);
                user.setAccountNonExpired(true);
                user.setCredentialsNonExpired(true);
                user.setAccountNonLocked(true);
                user.setRoles(roles);
                userRepository.save(user);
            }
        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException("There is an account with this credentials:");
        }
    }

    private LocalDate stringDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
