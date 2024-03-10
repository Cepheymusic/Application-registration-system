package test.application.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import test.application.demo.entity.UserEntity;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private Long id;
    private RequestStatus requestStatus;
    private String userText;
    private String phoneNumber;
    private String requestName;
    private UserEntity author;
}
