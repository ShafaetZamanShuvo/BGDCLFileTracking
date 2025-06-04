package com.bgdcl.bgdcl_file_tracking.controller;

import com.bgdcl.bgdcl_file_tracking.dto.*;
import com.bgdcl.bgdcl_file_tracking.model.FileRequest;
import com.bgdcl.bgdcl_file_tracking.model.FileStatus;
import com.bgdcl.bgdcl_file_tracking.model.FileTracking;
import com.bgdcl.bgdcl_file_tracking.model.FileTransaction;
import com.bgdcl.bgdcl_file_tracking.security.services.UserDetailsImpl;
import com.bgdcl.bgdcl_file_tracking.service.FileTrackingService;
import com.bgdcl.bgdcl_file_tracking.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/files")
public class FileTrackingController {

    @Autowired
    private FileTrackingService fileTrackingService;

    @Autowired
    private UserDetailsService userDetailsService;

    // Get file status
    @GetMapping("/{code}/status")
    public ResponseEntity<Map<String, Object>> getFileStatus(@PathVariable String code) {
        // Call the service to get the file status
        Map<String, Object> status = fileTrackingService.getFileStatus(code);

        // Check if the response contains an error (indicating file was not found)
        if (status.containsKey("error")) {
            // If the file is not found, return a 404 Not Found with the error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(status);
        }

        // Return a 200 OK status with the file status if found
        return ResponseEntity.ok(status);
    }


    // Helper method to get current user ID from security context
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    // Validate transfer request to ensure all necessary data is provided
    private void validateTransferRequest(TransferRequestDTO request) {
        if (request.getFileCode() == null || request.getFileCode().isEmpty()) {
            throw new IllegalArgumentException("File code is required");
        }
        if (request.getFromDepartmentId() == null) {
            throw new IllegalArgumentException("From department ID is required");
        }
        if (request.getToDepartmentId() == null) {
            throw new IllegalArgumentException("To department ID is required");
        }
        if (request.getToUserId() == null) {
            throw new IllegalArgumentException("To user ID is required");
        }
    }

    // Get file history
    @GetMapping("/{code}/history")
    public ResponseEntity<List<Map<String, Object>>> getFileHistory(@PathVariable String code) {
        List<Map<String, Object>> history = fileTrackingService.getFileHistory(code);
        return ResponseEntity.ok(history);
    }

    // Transfer file
    @PostMapping("/transfer")
    public ResponseEntity<FileTransaction> transferFile(@RequestBody TransferRequestDTO request) {
        // Check for any missing fields in the DTO
        validateTransferRequest(request);

        // Proceed with file transfer
        FileTransaction transaction = fileTrackingService.transferFile(
                request.getFileCode(),
                request.getFromDepartmentId(),
                request.getToDepartmentId(),
                request.getFromUserId(),
                request.getToUserId(),
                request.getRemarks()
        );
        return ResponseEntity.ok(transaction);
    }

    //return file
    @PostMapping("/return")
    public ResponseEntity<?> returnFile(@RequestBody TransferRequestDTO request) {
        Boolean returned = fileTrackingService.returnFile(request.getFileCode(), request.getFromDepartmentId());
        return ResponseEntity.ok(true);
    }



    // Accept a single file
    @PostMapping("/accept")
    public ResponseEntity<FileTracking> acceptFile(@RequestBody FileAcceptanceDTO request) {

        FileTracking file = fileTrackingService.acceptFile(
                request.getFileCode(),
                request.getDepartmentId(),
                getCurrentUserId(),
                request.isAccept(),
                request.getNotes()
        );
        return ResponseEntity.ok(file);
    }

    // Accept multiple files in batch
    @PostMapping("/accept-batch")
    public ResponseEntity<List<FileTracking>> acceptBatchFiles(@RequestBody BatchAcceptanceDTO request) {
        List<FileTracking> files = fileTrackingService.acceptBatchFiles(
                request.getFileCodes(),
                request.getDepartmentId(),
                request.getUserId(),
                request.isAccept(),
                request.getNotes()
        );
        return ResponseEntity.ok(files);
    }

    // Request a file from another department
    @PostMapping("/request")
    public ResponseEntity<FileRequest> requestFile(@RequestBody FileRequestDTO request) {
        FileRequest fileRequest = fileTrackingService.requestFile(
                request.getFileCode(),
                request.getRequestingDepartmentId(),
                request.getRequestedFromDepartmentId(),
                request.getRequestingUserId(),
                request.getNotes()
        );
        return ResponseEntity.ok(fileRequest);
    }

    // Get files pending acceptance in a department
    @GetMapping("/pending/{departmentId}")
    public ResponseEntity<List<FileTracking>> getPendingFiles(@PathVariable Long departmentId) {
        List<FileTracking> pendingFiles = fileTrackingService.getPendingAcceptanceFiles(departmentId);
        return ResponseEntity.ok(pendingFiles);
    }

    // Get file requests for a department
    @GetMapping("/requests/{departmentId}")
    public ResponseEntity<List<FileRequest>> getFileRequests(@PathVariable Long departmentId) {
        List<FileRequest> requests = fileTrackingService.getRequestsForDepartment(departmentId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/requests/{departmentId}/details")
    public ResponseEntity<List<FileRequestDTO>> getFileRequestsDetails(@PathVariable Long departmentId) {
        List<FileRequestDTO> requests = fileTrackingService.getDetailedRequestsForDepartment(departmentId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/department/{id}")
    public ResponseEntity<Map<String, Object>> getFilesByDepartmentId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastUpdated,desc") String[] sort) {

        try {
            // Create Pageable object with sorting
            Pageable pageable = createPageableWithSort(page, size, sort);

            // Get files with status "in" and pendingAcceptance as false
            Page<FileTrackingDTO> filesPage = fileTrackingService.getFileTrackingWithCustomerInfo(
                    id, FileStatus.in, false, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("files", filesPage.getContent());
            response.put("currentPage", filesPage.getNumber());
            response.put("totalItems", filesPage.getTotalElements());
            response.put("totalPages", filesPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Creates a Pageable object with sort parameters
     */
    private Pageable createPageableWithSort(int page, int size, String[] sort) {
        // Validate page and size
        if (page < 0) {
            page = 0;
        }

        if (size <= 0 || size > 100) {
            size = 10;  // Default size or max limit
        }

        // Parse sort parameters
        List<Sort.Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            // Multiple sort criteria like "field,direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                String field = _sort[0];

                Sort.Direction direction = _sort.length > 1 && _sort[1].equalsIgnoreCase("asc") ?
                        Sort.Direction.ASC : Sort.Direction.DESC;

                orders.add(new Sort.Order(direction, field));
            }
        } else {
            // Single sort parameter defaulting to DESC if direction not specified
            orders.add(new Sort.Order(Sort.Direction.DESC, sort[0]));
        }

        return PageRequest.of(page, size, Sort.by(orders));
    }
}
