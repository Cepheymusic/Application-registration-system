package test.application.demo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import test.application.demo.dto.RequestStatus;
import test.application.demo.entity.RequestEntity;

import java.util.List;

public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    boolean existsByName(String requestName);
    List<RequestEntity> findRequestsByUserName(String userName);

    List<RequestEntity> findRequestsByRequestStatus(RequestStatus requestStatus, Pageable pageable);

    List<RequestEntity> findRequestsByUserNameAndOrderByCreatedAt(String name, Pageable page);
}
