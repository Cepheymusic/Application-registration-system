package test.application.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.application.demo.entity.UserEntity;
import test.application.demo.repository.UserRepository;

public class AuthorizationService {
    private final UserDetailsService detailsService;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public AuthorizationService(UserDetailsService detailsService, PasswordEncoder encoder, UserRepository userRepository) {
        this.detailsService = detailsService;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }
    public boolean login(String firstName, String password) {
        UserDetails userDetails = detailsService.loadUserByUsername(firstName);
        return encoder.matches(password, userDetails.getPassword());
    }
    public boolean register(UserEntity register) {
        if (userRepository.existsByFirstName(register.getFirstName())) {
            return false;
        } else {
            userRepository.save(register);
        }
        return true;
    }
}
