package com.example.hrm.service;

import com.example.hrm.dto.request.ContractRequest;
import com.example.hrm.dto.response.ContractResponse;
import com.example.hrm.entity.Contract;
import com.example.hrm.entity.ContractHistory;
import com.example.hrm.entity.User;
import com.example.hrm.mapper.ContractMapper;
import com.example.hrm.repository.ContractHistoryRepository;
import com.example.hrm.repository.ContractRepository;
import com.example.hrm.repository.ContractTypeRepository;
import com.example.hrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final ContractHistoryRepository contractHistoryRepository;

    private final ContractMapper contractMapper;
    private final PermissionChecker permissionChecker;

    public ContractResponse createContract(ContractRequest request) {
        if (request.getUserId() == null) {
            throw new RuntimeException("UserId không được null");
        }
        permissionChecker.checkEmployeeRecordPermission(request.getUserId());

        Contract contract = contractMapper.toEntity(request);

        contract.setUser(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));

        contract.setContractType(
                contractTypeRepository.findByName(request.getContractTypeName())
                        .orElseThrow(() -> new RuntimeException("Contract type not found"))
        );

        contractRepository.save(contract);
        return contractMapper.toResponse(contract);
    }

    public ContractResponse extendContract(Integer id, LocalDate newEndDate) {
        Contract contract = getContractEntity(id);
        permissionChecker.checkEmployeeRecordPermission(contract.getUser().getId());

        if (contract.getStatus() == Contract.ContractStatus.TERMINATED) {
            throw new RuntimeException("Không thể gia hạn hợp đồng đã chấm dứt");
        }

        contract.setEndDate(newEndDate);

        boolean hasHistory = contractHistoryRepository.existsByContractId(contract.getId());

        if (hasHistory) {
            contract.setStatus(Contract.ContractStatus.MODIFIED);
        } else {
            contract.setStatus(Contract.ContractStatus.ACTIVE);
        }

        contractRepository.save(contract);
        return contractMapper.toResponse(contract);
    }



    public ContractResponse terminateContract(Integer id) {
        Contract contract = getContractEntity(id);
        permissionChecker.checkEmployeeRecordPermission(contract.getUser().getId());

        contract.setStatus(Contract.ContractStatus.TERMINATED);
        contractRepository.save(contract);
        return contractMapper.toResponse(contract);
    }

    public ContractResponse getContractById(Integer id) {
        Contract contract = getContractEntity(id);
        permissionChecker.checkEmployeeRecordPermission(contract.getUser().getId());
        return contractMapper.toResponse(contract);
    }

    public List<ContractResponse> getAllContractsByRole() {
        User currentUser = permissionChecker.getCurrentUser();
        String role = currentUser.getRole().getName();

        List<Contract> contracts;
        if ("admin".equalsIgnoreCase(role)) {
            contracts = contractRepository.findAll();
        } else if ("HR".equalsIgnoreCase(role)) {
            contracts = contractRepository.findAllActive();
        } else {
            throw new RuntimeException("Bạn không có quyền xem hợp đồng");
        }

        return contracts.stream()
                .map(contractMapper::toResponse)
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasRole('admin')")
    public void softDeleteContract(Integer id) {
        Contract contract = getContractEntity(id);
        contract.setIsDelete(true);
        contractRepository.save(contract);
    }

    @PreAuthorize("hasRole('admin')")
    public void restoreContract(Integer id) {
        Contract contract = getContractEntity(id);
        contract.setIsDelete(false);
        contractRepository.save(contract);
    }

    public void updateExpiredContracts() {
        List<Contract> expiredContracts = contractRepository.findExpiredContracts(LocalDate.now());
        expiredContracts.forEach(c -> c.setStatus(Contract.ContractStatus.EXPIRED));
        contractRepository.saveAll(expiredContracts);
    }

    private Contract getContractEntity(Integer id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    public ContractResponse modifyContract(Integer id, ContractRequest request) {
        Contract contract = getContractEntity(id);
        permissionChecker.checkEmployeeRecordPermission(contract.getUser().getId());

        if (contract.getStatus() == Contract.ContractStatus.TERMINATED ||
                contract.getStatus() == Contract.ContractStatus.EXPIRED) {
            throw new RuntimeException("Không thể chỉnh sửa hợp đồng đã hết hạn hoặc chấm dứt");
        }

        ContractHistory history = ContractHistory.builder()
                .contract(contract)
                .changedAt(LocalDateTime.now())
                .oldContractType(contract.getContractType().getName())
                .oldStartDate(contract.getStartDate())
                .oldEndDate(contract.getEndDate())
                .oldSalary(contract.getSalary())
                .oldStatus(contract.getStatus().name())
                .note("Edit Contract")
                .build();

        contractMapper.updateEntity(contract, request);

        if (request.getContractTypeName() != null) {
            history.setNewContractType(request.getContractTypeName());
            contract.setContractType(
                    contractTypeRepository.findByName(request.getContractTypeName())
                            .orElseThrow(() -> new RuntimeException("Contract type not found"))
            );
        } else {
            history.setNewContractType(contract.getContractType().getName());
        }

        history.setNewStartDate(contract.getStartDate());
        history.setNewEndDate(contract.getEndDate());
        history.setNewSalary(contract.getSalary());
        history.setNewStatus(Contract.ContractStatus.MODIFIED.name());

        contract.setStatus(Contract.ContractStatus.MODIFIED);

        contractRepository.save(contract);
        contractHistoryRepository.save(history);

        return contractMapper.toResponse(contract);
    }
}
