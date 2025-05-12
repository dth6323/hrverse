package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.service.dto.response.SalaryDistributeDetailResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SalaryDistributeDetailMapper {

    public SalaryDistributeDetailResponse toDto(Object[] result) {
        return new SalaryDistributeDetailResponse(
            (String) result[0], // employeeName
            (BigDecimal) result[1], // wageCoefficients
            (BigDecimal) result[2], // wageBaseSalary
            (BigDecimal) result[3], // wageAllowance
            ((Number) result[4]).longValue(), // totalWorkDays
            (BigDecimal) result[5] // totalSalary
        );
    }

    public List<SalaryDistributeDetailResponse> toDtoList(List<Object[]> results) {
        return results.stream().map(this::toDto).collect(Collectors.toList());
    }
}
