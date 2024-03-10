package test.application.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import test.application.demo.dto.UserRole;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;
    @OneToMany(mappedBy = "author")
    private List<RequestEntity> requests;

}
