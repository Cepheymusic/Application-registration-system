package test.application.demo.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import test.application.demo.dto.DateSorting;
import test.application.demo.dto.RequestStatus;
import test.application.demo.dto.UpdateRequest;
import test.application.demo.entity.RequestEntity;
import test.application.demo.exceptions.RequestException;
import test.application.demo.exceptions.RequestStatusException;
import test.application.demo.exceptions.VerificationException;
import test.application.demo.repository.RequestRepository;

import test.application.demo.security.CustomUserDetails;

import java.util.List;

@Service
public class ProcessingService {
    private RequestRepository requestRepository;

    public ProcessingService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public RequestEntity createRequest(RequestEntity requestEntity, CustomUserDetails customUserDetails) {
        if (requestRepository.existsByName(requestEntity.getRequestName())) {
            throw new RequestException("The request with the same name exists");
        } else {
            accessVerification(customUserDetails);
            return requestRepository.save(requestEntity);
        }
    }

    public RequestEntity editRequest(Long id, UpdateRequest updateRequest, CustomUserDetails customUserDetails) {
        RequestEntity requestEntity = requestRepository.findById(id).orElseThrow(
                () -> new RequestException("The request does not exist"));
        if (requestEntity.getRequestStatus().getValue().equals("DRAFT") && requestEntity.getAuthor().getFirstName()
                .equals(customUserDetails.getUsername())) {
            accessVerification(customUserDetails);
            requestEntity.setRequestName(updateRequest.getRequestName());
            requestEntity.setPhoneNumber(updateRequest.getPhoneNumber());
            requestEntity.setUserText(updateRequest.getUserText());
            return requestRepository.save(requestEntity);
        } else {
            throw new RequestStatusException("Request does not draft");
        }
    }

    public List<RequestEntity> getMyRequests(DateSorting dateSorting, int page,
                                             CustomUserDetails customUserDetails) {
        Sort sort = Sort
                .by(dateSorting.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, "localDateTime");
        Pageable pageable = PageRequest.of(page, 5, sort);
        List<RequestEntity> requestEntities =
                requestRepository.findRequestsByUserNameAndOrderByCreatedAt(customUserDetails.getUsername(), pageable);
        return requestEntities;
    }

    public RequestEntity getRequestById(Long id, CustomUserDetails customUserDetails) {
        if (customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("USER"))) {
            throw new VerificationException("Verification error");
        } else {
            return requestRepository.findById(id).orElseThrow(
                    () -> new RequestException("The request does not exist"));
        }
    }

    public List<RequestEntity> getRequestsByStatus(RequestStatus requestStatus,
                                                   int page,
                                                   CustomUserDetails customUserDetails) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("localDateTime").descending());
        if (customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return requestRepository.findRequestsByRequestStatus(requestStatus, pageable);
        } else {
            throw new VerificationException("Verification error");
        }
    }

    public void accessVerification(CustomUserDetails customUserDetails) {
        if (!customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("USER"))) {
            throw new VerificationException("Verification error");
        }
    }


}
