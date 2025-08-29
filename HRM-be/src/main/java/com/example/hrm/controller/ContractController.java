package com.example.hrm.controller;

import com.example.hrm.dto.request.ContractRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.ContractHistoryResponse;
import com.example.hrm.dto.response.ContractResponse;
import com.example.hrm.mapper.ContractHistoryMapper;
import com.example.hrm.repository.ContractHistoryRepository;
import com.example.hrm.service.ContractService;
import com.example.hrm.systemlog.LoggableAction;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final ContractHistoryRepository contractHistoryRepository;
    private final ContractHistoryMapper contractHistoryMapper;

    @LoggableAction(action = "CREATE_CONTRACT", description = "Tạo hợp đồng mới")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ContractResponse>> createContract(@RequestBody ContractRequest request) {
        ContractResponse result = contractService.createContract(request);
        return ResponseEntity.ok(ApiResponse.<ContractResponse>builder()
                .data(result)
                .build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<ContractResponse>>> getAllContractsByRole() {
        List<ContractResponse> result = contractService.getAllContractsByRole();
        return ResponseEntity.ok(ApiResponse.<List<ContractResponse>>builder()
                .data(result)
                .build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<ContractResponse>> getContractById(@PathVariable Integer id) {
        ContractResponse result = contractService.getContractById(id);
        return ResponseEntity.ok(ApiResponse.<ContractResponse>builder()
                .data(result)
                .build());
    }

    @LoggableAction(action = "EXTEND_CONTRACT", description = "Gia hạn hợp đồng")
    @PutMapping("/extend/{id}")
    public ResponseEntity<ApiResponse<ContractResponse>> extendContract(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate newEndDate) {
        ContractResponse result = contractService.extendContract(id, newEndDate);
        return ResponseEntity.ok(ApiResponse.<ContractResponse>builder()
                .data(result)
                .build());
    }

    @LoggableAction(action = "TERMINATE_CONTRACT", description = "Chấm dứt hợp đồng")
    @PutMapping("/terminate/{id}")
    public ResponseEntity<ApiResponse<ContractResponse>> terminateContract(@PathVariable Integer id) {
        ContractResponse result = contractService.terminateContract(id);
        return ResponseEntity.ok(ApiResponse.<ContractResponse>builder()
                .data(result)
                .build());
    }

    @LoggableAction(action = "DELETE_CONTRACT", description = "Xóa hợp đồng tạm thời")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteContract(@PathVariable Integer id) {
        contractService.softDeleteContract(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .build());
    }

    @LoggableAction(action = "RESTORE_CONTRACT", description = "Khôi phục hợp đồng")
    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restoreContract(@PathVariable Integer id) {
        contractService.restoreContract(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .build());
    }

    @LoggableAction(action = "MODIFY_CONTRACT", description = "Chỉnh sửa hợp đồng")
    @PutMapping("/modify/{id}")
    public ResponseEntity<ApiResponse<ContractResponse>> modifyContract(
            @PathVariable Integer id,
            @RequestBody ContractRequest request) {
        ContractResponse result = contractService.modifyContract(id, request);
        return ResponseEntity.ok(ApiResponse.<ContractResponse>builder()
                .data(result)
                .build());
    }

    @GetMapping("/history/{contractId}")
    public ResponseEntity<ApiResponse<List<ContractHistoryResponse>>> getHistory(@PathVariable Integer contractId) {
        List<ContractHistoryResponse> result = contractHistoryRepository
                .findByContract_IdOrderByChangedAtDesc(contractId)
                .stream()
                .map(contractHistoryMapper::toResponse)
                .toList();

        return ResponseEntity.ok(
                ApiResponse.<List<ContractHistoryResponse>>builder()
                        .data(result)
                        .build()
        );
    }

}
