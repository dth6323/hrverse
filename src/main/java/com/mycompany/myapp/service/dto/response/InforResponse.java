package com.mycompany.myapp.service.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InforResponse {

    private String address;
    private LocalDate dateOfBirth;
    private String department;
    private String phone;
}
