package com.mycompany.myapp.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PdfExportResponse {

    private byte[] data;
    private String fileName;
}
