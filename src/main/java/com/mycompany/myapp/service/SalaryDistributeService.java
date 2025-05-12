package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Attendance;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.domain.Payroll;
import com.mycompany.myapp.domain.Wage;
import com.mycompany.myapp.repository.AttendanceRepository;
import com.mycompany.myapp.repository.EmployeeRepository;
import com.mycompany.myapp.repository.PayrollRepository;
import com.mycompany.myapp.repository.SalaryDistributeRepository;
import com.mycompany.myapp.service.dto.request.CaculateSalaryRequest;
import com.mycompany.myapp.service.dto.response.SalaryDistributeDetailResponse;
import com.mycompany.myapp.service.mapper.SalaryDistributeDetailMapper;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.persistence.EntityNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class SalaryDistributeService {

    SalaryDistributeRepository salaryDistributeRepository;
    AttendanceRepository attendanceRepository;
    EmployeeRepository employeeRepository;
    PayrollRepository payrollRepository;
    SalaryDistributeDetailMapper salaryDistributeDetailMapper;

    public SalaryDistributeService(
        SalaryDistributeRepository salaryDistributeRepository,
        AttendanceRepository attendanceRepository,
        EmployeeRepository employeeRepository,
        PayrollRepository payrollRepository,
        SalaryDistributeDetailMapper salaryDistributeDetailMapper
    ) {
        this.salaryDistributeRepository = salaryDistributeRepository;
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.payrollRepository = payrollRepository;
        this.salaryDistributeDetailMapper = salaryDistributeDetailMapper;
    }

    public List<SalaryDistributeDetailResponse> showSalaryDistribute(String id) {
        List<Object[]> details = salaryDistributeRepository.findDetails(id);
        return salaryDistributeDetailMapper.toDtoList(details);
    }

    public byte[] exportSalaryDistribute(String id) throws IOException {
        List<SalaryDistributeDetailResponse> l = showSalaryDistribute(id);

        // Tạo ByteArrayOutputStream để lưu PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Khởi tạo tài liệu PDF
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Khởi tạo content stream để vẽ nội dung
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Tiêu đề tài liệu
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText("Salary Distribution Report");
        contentStream.endText();

        // Thiết lập font và vị trí cho bảng
        float yPosition = 650;
        float margin = 50;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float columnWidth = tableWidth / 6; // 6 cột

        // Tiêu đề bảng (màu xanh)
        String[] columns = { "Employee Name", "Wage Coefficients", "Base Salary", "Allowance", "Total Work Days", "Total Salary" };
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.setNonStrokingColor(0, 1, 0); // Màu xanh lá cây (RGB: 0, 1, 0)
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        for (String column : columns) {
            contentStream.showText(column);
            contentStream.newLineAtOffset(columnWidth, 0);
        }
        contentStream.endText();

        // Đặt lại màu về đen cho dữ liệu
        contentStream.setNonStrokingColor(0, 0, 0); // Màu đen (RGB: 0, 0, 0)
        yPosition -= 20; // Dịch xuống cho dữ liệu

        // Dữ liệu
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        for (SalaryDistributeDetailResponse detail : l) {
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(detail.getEmployeeName());
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText(String.valueOf(detail.getWageCoefficients()));
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText(String.valueOf(detail.getWageBaseSalary()));
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText(String.valueOf(detail.getWageAllowance()));
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText(String.valueOf(detail.getTotalWorkDays()));
            contentStream.newLineAtOffset(columnWidth, 0);
            contentStream.showText(String.valueOf(detail.getTotalSalary()));
            contentStream.endText();
            yPosition -= 20; // Dịch xuống cho dòng tiếp theo

            // Kiểm tra nếu hết trang
            if (yPosition < 50) {
                contentStream.close();
                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                yPosition = 750;
            }
        }

        // Đóng content stream
        contentStream.close();

        // Lưu và đóng tài liệu
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }

    public List<Payroll> salaryCaculate(CaculateSalaryRequest request) {
        List<Attendance> att = this.attendanceRepository.findByDateOfworkBetween(request.getStartDate(), request.getEndDate());
        Map<Long, Integer> map = new HashMap<>();
        List<Payroll> payrolls = new ArrayList<>();

        for (Attendance attendance : att) {
            Long employeeId = attendance.getEmployee().getId();
            map.put(employeeId, map.getOrDefault(employeeId, 0) + 1);
        }

        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            Payroll payroll = new Payroll();
            Wage wage = new Wage();
            Employee employee = employeeRepository.findById(entry.getKey()).orElseThrow(EntityNotFoundException::new);
            payroll.setWorkDay(entry.getValue());
            payroll.setSalary(1);
            payroll.setEmployee(employee);
            wage.setId(employee.getId());
            payroll.setSalaryDistribute(
                salaryDistributeRepository
                    .findById(Long.valueOf(request.getId()))
                    .orElseThrow(() -> new EntityNotFoundException("SalaryDistribute not found"))
            );
            payrolls.add(payroll);
            payrollRepository.save(payroll);
        }
        return payrolls;
    }
}
