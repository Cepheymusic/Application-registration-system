package test.application.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import test.application.demo.dto.UpdateUser;
import test.application.demo.dto.UserRole;
import test.application.demo.entity.UserEntity;
import test.application.demo.exceptions.UserNotFoundException;
import test.application.demo.repository.UserRepository;
import test.application.demo.security.CustomUserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService underTest;
    @Mock
    private UserRepository userRepository;

    static UserEntity user1 = new UserEntity(
            1L, "Test", "Testing", "", UserRole.ADMIN);
    static UserEntity user2 = new UserEntity(
            2L, "Test2", "Testing2", "", UserRole.ADMIN);
    static UpdateUser updateUser = new UpdateUser("Test3", "Testing3");
    static CustomUserDetails customUserDetails = new CustomUserDetails(user1);
    @BeforeEach
    void beforeEach() {
        underTest = new UserService(userRepository);
    }
    @Test
    void getUsers_findAndReturnAll() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        List<UserEntity> result = underTest.getUsers();
        assertEquals(List.of(user1, user2), result);

    }

    @Test
    void updateUser_userInRepository_updateAndReturnUpdateUser() {
        when(userRepository.findByFirstName(customUserDetails.getUsername())).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);
        UpdateUser result = underTest.updateUser(updateUser, customUserDetails);
        assertEquals(updateUser, result);
    }
    @Test
    void updateUser_userIsNotRepository_returnUserNotFoundException() {
        when(userRepository.findByFirstName(customUserDetails.getUsername())).thenReturn(Optional.empty());
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> underTest.updateUser(updateUser, customUserDetails));
        assertEquals("User is not found", ex.getMessage());
    }

    @Test
    void addRole_userIsInRepository_addRoleAndReturnUserEntity() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);
        UserEntity result = underTest.addRole(1L, UserRole.OPERATOR, customUserDetails);
        assertEquals(UserRole.OPERATOR, result.getUserRole());
    }
    @Test
    void addRole_userIsNotRepository_returnUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> underTest.addRole(1L, UserRole.OPERATOR, customUserDetails));
        assertEquals("User is not found", ex.getMessage());
    }
}