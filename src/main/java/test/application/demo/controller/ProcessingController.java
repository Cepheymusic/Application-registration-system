package test.application.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import test.application.demo.dto.DateSorting;
import test.application.demo.dto.RequestStatus;
import test.application.demo.dto.UpdateRequest;
import test.application.demo.entity.RequestEntity;
import test.application.demo.security.CustomUserDetails;
import test.application.demo.service.ProcessingService;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class ProcessingController {
    private ProcessingService processingService;

    public ProcessingController(ProcessingService processingService) {
        this.processingService = processingService;
    }

    @PostMapping("/create_request")
    public RequestEntity createRequest(@RequestBody RequestEntity requestEntity,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return processingService.createRequest(requestEntity, customUserDetails);
    }

    @PutMapping("{id}/update_request")
    public RequestEntity updateRequest(@PathVariable("id") Long id, @RequestBody UpdateRequest updateRequest,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return processingService.editRequest(id, updateRequest, customUserDetails);
    }

    @GetMapping("/sorting_rule/page")
    public List<RequestEntity> getMyRequests(@RequestPart DateSorting dateSorting,
                                             @RequestPart int page,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return processingService.getMyRequests(dateSorting, page, customUserDetails);
    }

    @GetMapping("{id}")
    public RequestEntity getRequestById(@PathVariable("id") Long id,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return processingService.getRequestById(id, customUserDetails);
    }

    @GetMapping("/status/page")
    public List<RequestEntity> getRequestsByStatus(@RequestPart RequestStatus requestStatus,
                                                   @RequestPart int page,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return processingService.getRequestsByStatus(requestStatus, page, customUserDetails);
    }
}
