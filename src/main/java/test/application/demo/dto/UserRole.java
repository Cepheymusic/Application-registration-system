package test.application.demo.dto;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    USER("USER"), ADMIN("ADMIN"), OPERATOR("OPERATOR");
    UserRole(String value) {
        this.value = value;
    }
    private String value;

    @Override
    public String getAuthority() {
        return value;
    }
}

