package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.service.dto.response.InforResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class InforMapper {

    public InforResponse toDto(Object[] result) {
        return new InforResponse(
            (String) result[0],
            ((Timestamp) result[1]).toLocalDateTime().toLocalDate(),
            (String) result[2],
            (String) result[3]
        );
    }

    public List<InforResponse> toDtoList(List<Object[]> results) {
        return results.stream().map(this::toDto).collect(Collectors.toList());
    }
}
