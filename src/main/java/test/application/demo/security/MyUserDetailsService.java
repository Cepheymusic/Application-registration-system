package test.application.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import test.application.demo.entity.UserEntity;
import test.application.demo.exceptions.UserNotFoundException;
import test.application.demo.repository.UserRepository;

public class MyUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String firstName) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByFirstName(firstName).orElseThrow(
                () -> new UserNotFoundException("User is not found"));
        return new CustomUserDetails(userEntity);
    }
}
