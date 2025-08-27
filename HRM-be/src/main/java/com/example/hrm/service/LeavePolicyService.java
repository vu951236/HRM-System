package com.example.hrm.service;

import com.example.hrm.dto.request.LeavePolicyRequest;
import com.example.hrm.dto.response.LeavePolicyResponse;
import com.example.hrm.entity.LeavePolicy;
import com.example.hrm.entity.Position;
import com.example.hrm.entity.Role;
import com.example.hrm.mapper.LeavePolicyMapper;
import com.example.hrm.repository.LeavePolicyRepository;
import com.example.hrm.repository.PositionRepository;
import com.example.hrm.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeavePolicyService {

    private final LeavePolicyRepository repository;
    private final LeavePolicyMapper mapper;
    private final PositionRepository positionRepository;
    private final RoleRepository roleRepository;
    private final PermissionChecker permissionChecker;

    @PreAuthorize("hasRole('admin')")
    public LeavePolicyResponse create(LeavePolicyRequest request) {
        permissionChecker.checkAdminOrHrRole();

        LeavePolicy policy = mapper.toEntity(request);

        Position position = positionRepository.findByNameIgnoreCase(request.getPositionName())
                .orElseThrow(() -> new RuntimeException("Position not found with name " + request.getPositionName()));
        Role role = roleRepository.findByNameIgnoreCase(request.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found with name " + request.getRoleName()));

        policy.setPosition(position);
        policy.setRole(role);
        policy.setIsDelete(false);

        return mapper.toResponse(repository.save(policy));
    }

    public List<LeavePolicyResponse> getAll() {
        Role currentRole = permissionChecker.getCurrentUserRole();
        List<LeavePolicy> policies;

        if ("admin".equalsIgnoreCase(currentRole.getName())) {
            policies = repository.findAll();
        } else if ("hr".equalsIgnoreCase(currentRole.getName())) {
            policies = repository.findAllByIsDeleteFalse();
        } else {
            throw new RuntimeException("Bạn không có quyền xem chính sách nghỉ phép");
        }

        return policies.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public LeavePolicyResponse getById(Integer id) {
        LeavePolicy policy = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Policy not found with id " + id));

        Role currentRole = permissionChecker.getCurrentUserRole();
        if (!policy.getIsDelete() || "admin".equalsIgnoreCase(currentRole.getName())) {
            return mapper.toResponse(policy);
        } else {
            throw new RuntimeException("Bạn không có quyền xem policy đã bị xóa");
        }
    }

    @PreAuthorize("hasRole('admin')")
    public LeavePolicyResponse update(Integer id, LeavePolicyRequest request) {
        permissionChecker.checkAdminOrHrRole();

        LeavePolicy policy = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Policy not found with id " + id));

        mapper.updateEntityFromRequest(request, policy);

        if (request.getPositionName() != null) {
            Position position = positionRepository.findByNameIgnoreCase(request.getPositionName())
                    .orElseThrow(() -> new RuntimeException("Position not found with name " + request.getPositionName()));
            policy.setPosition(position);
        }

        if (request.getRoleName() != null) {
            Role role = roleRepository.findByNameIgnoreCase(request.getRoleName())
                    .orElseThrow(() -> new RuntimeException("Role not found with name " + request.getRoleName()));
            policy.setRole(role);
        }

        return mapper.toResponse(repository.save(policy));
    }

    @PreAuthorize("hasRole('admin')")
    @Transactional
    public void delete(Integer id) {
        LeavePolicy entity = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi với id: " + id));
        entity.setIsDelete(true);
        repository.save(entity);
    }

    @PreAuthorize("hasRole('admin')")
    @Transactional
    public void restore(Integer id) {
        LeavePolicy entity = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi với id: " + id));
        entity.setIsDelete(false);
        repository.save(entity);
    }
}
