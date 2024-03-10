package test.application.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import test.application.demo.dto.RequestStatus;

import java.time.LocalDateTime;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private RequestStatus requestStatus;
    private String userText;
    private String phoneNumber;
    private String requestName;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;
    private LocalDateTime localDateTime;

}
