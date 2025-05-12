package com.mycompany.myapp.service.dto.response;

import java.math.BigDecimal;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryDistributeDetailResponse {

    private String employeeName;
    private BigDecimal wageCoefficients;
    private BigDecimal wageBaseSalary;
    private BigDecimal wageAllowance;
    private Long totalWorkDays;
    private BigDecimal totalSalary;
}
