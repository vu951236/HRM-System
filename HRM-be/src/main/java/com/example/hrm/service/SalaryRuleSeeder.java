package com.example.hrm.service;

import com.example.hrm.entity.SalaryRule;
import com.example.hrm.entity.SalaryRule.RuleType;
import com.example.hrm.repository.SalaryRuleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SalaryRuleSeeder implements CommandLineRunner {

    private final SalaryRuleRepository repository;

    @Override
    public void run(String... args) {
        List<SalaryRule> defaultRules = List.of(
                SalaryRule.builder()
                        .ruleName("Base Salary")
                        .description(null)
                        .formula("value * 50000")
                        .ruleType(RuleType.BASE)
                        .active(true)
                        .isDelete(false)
                        .build(),
                SalaryRule.builder()
                        .ruleName("Bonus Salary")
                        .description(null)
                        .formula("value >= 21 ? 1000000 : 0")
                        .ruleType(RuleType.BONUS)
                        .active(true)
                        .isDelete(false)
                        .build(),
                SalaryRule.builder()
                        .ruleName("Deduction")
                        .description(null)
                        .formula("value * 0.08")
                        .ruleType(RuleType.DEDUCTION)
                        .active(true)
                        .isDelete(false)
                        .build(),
                SalaryRule.builder()
                        .ruleName("Overtime Salary")
                        .description(null)
                        .formula("value * 75000")
                        .ruleType(RuleType.OVERTIME)
                        .active(true)
                        .isDelete(false)
                        .build(),
                SalaryRule.builder()
                        .ruleName("Late")
                        .description(null)
                        .formula("value * 2000")
                        .ruleType(RuleType.LATE)
                        .active(true)
                        .isDelete(false)
                        .build(),
                SalaryRule.builder()
                        .ruleName("Leave")
                        .description(null)
                        .formula("value * 300000")
                        .ruleType(RuleType.LEAVE)
                        .active(true)
                        .isDelete(false)
                        .build()
        );

        for (SalaryRule rule : defaultRules) {
            repository.findByRuleType(rule.getRuleType())
                    .orElseGet(() -> {
                        System.out.println("Inserted default rule: " + rule.getRuleName());
                        return repository.save(rule);
                    });
        }
    }
}
