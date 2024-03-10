package test.application.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import test.application.demo.entity.UserEntity;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends User {
    private Long id;
    public CustomUserDetails(UserEntity userEntity) {
        super(userEntity.getFirstName(), userEntity.getPassword(), List.of(userEntity.getUserRole()));
        this.id = userEntity.getId();
    }
    public Long getId() {
        return id;
    }
}
