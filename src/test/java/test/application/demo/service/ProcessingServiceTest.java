package test.application.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import test.application.demo.dto.DateSorting;
import test.application.demo.dto.RequestStatus;
import test.application.demo.dto.UpdateRequest;
import test.application.demo.dto.UserRole;
import test.application.demo.entity.RequestEntity;
import test.application.demo.entity.UserEntity;
import test.application.demo.exceptions.RequestException;
import test.application.demo.repository.RequestRepository;
import test.application.demo.security.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessingServiceTest {
    @InjectMocks
    private ProcessingService underTest;
    @Mock
    private RequestRepository requestRepository;
    static UserEntity user1 = new UserEntity(
            1L, "Test", "Testing", "", UserRole.USER, List.of());
    static UserEntity user2 = new UserEntity(
            2L, "Test", "Testing", "", UserRole.ADMIN, List.of());
    static RequestEntity request1 = new RequestEntity(
            1L, RequestStatus.DRAFT, "Test", "TestPhone", "Name", user1,
            LocalDateTime.now());
    static RequestEntity request2 = new RequestEntity(
            1L, RequestStatus.DRAFT, "Test", "TestPhone", "Name", user2,
            LocalDateTime.now());
    private UpdateRequest updateRequest = new UpdateRequest(
            "Text", "TestPhone", "Name");
    static CustomUserDetails customUserDetails = new CustomUserDetails(user1);
    static CustomUserDetails customUserDetails2 = new CustomUserDetails(user2);

    @BeforeEach
    void beforeEach() {
        underTest = new ProcessingService(requestRepository);
    }

    @Test
    void createRequest_createAndReturnRequest() {
        when(requestRepository.save(request1)).thenReturn(request1);
        RequestEntity result = underTest.createRequest(request1, customUserDetails);
        assertEquals(request1, result);
    }

    @Test
    void createRequest_requestExists_returnRequestException() {
        when(requestRepository.existsByName(request1.getRequestName())).thenReturn(true);
        RequestException ex =
                assertThrows(RequestException.class,
                        () -> underTest.createRequest(request1, customUserDetails));
        assertEquals("The request with the same name exists", ex.getMessage());
    }

    @Test
    void editRequest_editAndReturnRequest() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request1));
        when(requestRepository.save(request1)).thenReturn(request1);
        RequestEntity result = underTest.editRequest(1L, updateRequest, customUserDetails);
        assertEquals(request1, result);
    }
    @Test
    void editRequest_requestIsNotRepository_returnRequestException() {
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());
        RequestException ex =
                assertThrows(RequestException.class,
                        () -> underTest.editRequest(1L, updateRequest, customUserDetails));
        assertEquals("The request does not exist", ex.getMessage());
    }

    @Test
    void getMyRequests_returnListRequests() {
        Pageable pageable = PageRequest.of(
                1, 5, Sort.by(Sort.Direction.DESC, "localDateTime"));
        when(requestRepository.findRequestsByUserNameAndOrderByCreatedAt(customUserDetails.getUsername(), pageable))
                .thenReturn(List.of(request1));
                List<RequestEntity> result = underTest.getMyRequests(DateSorting.DESC, 1, customUserDetails);
        assertEquals(1, result.size());
    }

    @Test
    void getRequestById_findAndReturnRequest() {
        when(requestRepository.findById(2L)).thenReturn(Optional.of(request2));
        RequestEntity result = underTest.getRequestById(2L, customUserDetails2);
        assertEquals(request2, result);
    }
    @Test
    void getRequestById_requestIsNotRepository_returnRequestException() {
        when(requestRepository.findById(2L)).thenReturn(Optional.empty());
        RequestException ex = assertThrows(RequestException.class,
                () -> underTest.getRequestById(2L, customUserDetails2));
        assertEquals("The request does not exist", ex.getMessage());
    }

    @Test
    void getRequestsByStatus_findAndReturnListRequests() {
        Pageable pageable = PageRequest.of(
                1, 5, Sort.by("localDateTime").descending());
        when(requestRepository.findRequestsByRequestStatus(RequestStatus.DRAFT, pageable)).
                thenReturn(List.of(request2));
        List<RequestEntity> result = underTest.getRequestsByStatus(RequestStatus.DRAFT, 1, customUserDetails2);
        assertEquals(List.of(request2), result);

    }
}