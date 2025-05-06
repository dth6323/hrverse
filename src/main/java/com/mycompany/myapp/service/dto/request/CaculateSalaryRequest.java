package com.mycompany.myapp.service.dto.request;

import java.time.LocalDate;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaculateSalaryRequest {

    LocalDate startDate;
    LocalDate endDate;
    String id;
}
