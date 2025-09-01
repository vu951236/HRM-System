package com.example.hrm.mapper;

import com.example.hrm.dto.request.PayrollRequest;
import com.example.hrm.dto.response.PayrollResponse;
import com.example.hrm.entity.Payroll;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PayrollMapper {

    @Mapping(source = "employee.user.id", target = "userId")
    @Mapping(source = "employee.user.profile.fullName", target = "employeeName")
    @Mapping(source = "employee.employeeCode", target = "employeeCode")
    @Mapping(source = "totalAbsentDays", target = "totalAbsentDays")
    @Mapping(source = "workedHours", target = "workedHours")
    @Mapping(source = "totalLateDays", target = "totalLateDays")
    PayrollResponse toResponse(Payroll payroll);

    List<PayrollResponse> toResponseList(List<Payroll> payrolls);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "generatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(com.example.hrm.entity.Payroll.PayrollStatus.GENERATED)")
    Payroll toEntity(PayrollRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "generatedAt", ignore = true)
    void updateEntity(@MappingTarget Payroll entity, PayrollRequest request);
}
