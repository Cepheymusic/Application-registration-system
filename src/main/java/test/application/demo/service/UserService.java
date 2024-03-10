package test.application.demo.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import test.application.demo.dto.NewPassword;
import test.application.demo.dto.UpdateUser;
import test.application.demo.dto.UserRole;
import test.application.demo.entity.UserEntity;
import test.application.demo.exceptions.UserNotFoundException;
import test.application.demo.exceptions.VerificationException;
import test.application.demo.repository.UserRepository;
import test.application.demo.security.CustomUserDetails;

import java.util.List;
@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }
    public void setPassword(NewPassword newPassword, CustomUserDetails customUserDetails) {
        if (userRepository.existsByFirstName(customUserDetails.getUsername())) {
            UserEntity user = new UserEntity();
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("User is not found");
        }
    }
    public UpdateUser updateUser(UpdateUser updateUser, CustomUserDetails customUserDetails) {
        UserEntity userEntity = userRepository.findByFirstName(customUserDetails.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User is not found"));
            userEntity.setFirstName(updateUser.getFirstName());
            userEntity.setLastName(updateUser.getLastName());
            userRepository.save(userEntity);
            return updateUser;
    }

    public UserEntity addRole(Long userId, UserRole userRole, CustomUserDetails customUserDetails) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User is not found"));
        accessVerification(customUserDetails);
        userEntity.setUserRole(userRole);
        return userRepository.save(userEntity);
    }
    public void accessVerification(CustomUserDetails customUserDetails) {
        if (!customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new VerificationException("Verification error");
        }
    }
}
