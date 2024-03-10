package test.application.demo.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import test.application.demo.dto.NewPassword;
import test.application.demo.dto.UpdateUser;
import test.application.demo.dto.UserRole;
import test.application.demo.entity.UserEntity;
import test.application.demo.security.CustomUserDetails;
import test.application.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserEntity> getUsers() {
        return userService.getUsers();
    }
    @PostMapping("/set_password")
    public ResponseEntity<String> setPassword(@RequestBody NewPassword newPassword,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.setPassword(newPassword, customUserDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PatchMapping("/me")
    public UpdateUser updateUser(@RequestBody UpdateUser updateUser,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.updateUser(updateUser, customUserDetails);
    }

    @PatchMapping("{id}/add_role")
    public UserEntity addRole(@PathVariable("id") Long id, @RequestPart("add_role") UserRole userRole,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.addRole(id, userRole, customUserDetails);
    }

}
