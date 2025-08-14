package com.example.hrm.scheduler;

import com.example.hrm.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractScheduler {

    private final ContractService contractService;

    @Scheduled(fixedRate = 100000)
    public void autoUpdateExpiredContracts() {
        contractService.updateExpiredContracts();
        System.out.println("Đã cập nhật trạng thái hợp đồng hết hạn");
    }
}
