package com.example.hrm.controller;

import com.example.hrm.dto.request.WorkScheduleTemplateRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.WorkScheduleTemplateResponse;
import com.example.hrm.service.WorkScheduleTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/work-schedule-templates")
@RequiredArgsConstructor
public class WorkScheduleTemplateController {

    private final WorkScheduleTemplateService service;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<WorkScheduleTemplateResponse>> create(@RequestBody WorkScheduleTemplateRequest request){
        WorkScheduleTemplateResponse response = service.createTemplate(request);
        return ResponseEntity.ok(ApiResponse.<WorkScheduleTemplateResponse>builder().data(response).build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<WorkScheduleTemplateResponse>> update(@PathVariable Integer id,
                                                                            @RequestBody WorkScheduleTemplateRequest request){
        WorkScheduleTemplateResponse response = service.updateTemplate(id, request);
        return ResponseEntity.ok(ApiResponse.<WorkScheduleTemplateResponse>builder().data(response).build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<WorkScheduleTemplateResponse>>> getAll(){
        List<WorkScheduleTemplateResponse> list = service.getAllTemplates();
        return ResponseEntity.ok(ApiResponse.<List<WorkScheduleTemplateResponse>>builder().data(list).build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<WorkScheduleTemplateResponse>> getById(@PathVariable Integer id){
        WorkScheduleTemplateResponse response = service.getTemplateById(id);
        return ResponseEntity.ok(ApiResponse.<WorkScheduleTemplateResponse>builder().data(response).build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id){
        service.deleteTemplate(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    @PostMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restoreTemplate(@PathVariable Integer id) {
        service.restoreTemplate(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Template restored successfully")
                .build());
    }

    @PostMapping("/apply/{templateId}")
    public ResponseEntity<ApiResponse<Void>> applyTemplate(
            @PathVariable Integer templateId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "overwrite", defaultValue = "false") boolean overwrite) {
        service.applyTemplate(templateId, startDate, endDate, overwrite);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }
}
