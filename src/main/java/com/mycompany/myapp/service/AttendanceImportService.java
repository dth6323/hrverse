package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Attendance;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.elasticRepository.AttendanceSearchRepository;
import com.mycompany.myapp.repository.AttendanceRepository;
import com.mycompany.myapp.repository.EmployeeRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class AttendanceImportService {

    private final Logger log = LoggerFactory.getLogger(AttendanceImportService.class);

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceSearchRepository attendanceSearchRepository;

    public AttendanceImportService(
        AttendanceRepository attendanceRepository,
        EmployeeRepository employeeRepository,
        AttendanceSearchRepository attendanceSearchRepository
    ) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceSearchRepository = attendanceSearchRepository;
    }

    public Page<Attendance> findAll(Pageable pageable) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        if (hasAuthority("ROLE_ADMIN") || hasAuthority("ROLE_MANAGER")) {
            return attendanceRepository.findAll(pageable);
        }
        return attendanceRepository.getAttend(login, pageable);
    }

    public ByteArrayInputStream exportToExcel() throws IOException {
        // Fetch all attendance records
        List<Attendance> attendances = attendanceRepository.findAll();

        // Create a new workbook
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Attendance");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] columns = { "Date of Work", "Check-In Time", "Check-Out Time", "Work Hours", "Employee ID" };
            CellStyle headerStyle = createHeaderStyle(workbook);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            int rowNum = 1;

            for (Attendance attendance : attendances) {
                Row row = sheet.createRow(rowNum++);

                // Write fields as strings using their default toString() representation, except employee_id
                row.createCell(0).setCellValue(attendance.getDateOfwork() != null ? attendance.getDateOfwork().toString() : "");
                row.createCell(1).setCellValue(attendance.getCheckInTime() != null ? attendance.getCheckInTime().toString() : "");
                row.createCell(2).setCellValue(attendance.getCheckOutTime() != null ? attendance.getCheckOutTime().toString() : "");
                row.createCell(3).setCellValue(attendance.getWorkHour() != null ? attendance.getWorkHour().toString() : "");

                // Write employee_id as NUMERIC
                Cell employeeIdCell = row.createCell(4);
                if (attendance.getEmployee() != null && attendance.getEmployee().getId() != null) {
                    employeeIdCell.setCellValue(attendance.getEmployee().getId());
                } else {
                    employeeIdCell.setCellValue("");
                }
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to output stream
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private boolean hasAuthority(String authority) {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .anyMatch(auth -> auth.getAuthority().equals(authority));
    }

    public int importFromExcel(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        List<Attendance> attendances = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter instantFormatter = DateTimeFormatter.ISO_INSTANT;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean firstRow = true;

            for (Row row : sheet) {
                if (firstRow) {
                    firstRow = false; // Skip header row
                    continue;
                }

                try {
                    Attendance attendance = new Attendance();

                    // Parse dateOfwork (cell 0)
                    Cell dateCell = row.getCell(0);
                    if (dateCell != null && dateCell.getCellType() == CellType.STRING) {
                        String dateStr = dateCell.getStringCellValue().trim();
                        if (!dateStr.isEmpty()) {
                            try {
                                attendance.setDateOfwork(LocalDate.parse(dateStr, dateFormatter));
                            } catch (DateTimeParseException e) {
                                log.warn("Invalid dateOfwork format in row {}: {}", row.getRowNum() + 1, dateStr);
                                continue;
                            }
                        } else {
                            log.warn("Empty dateOfwork in row {}", row.getRowNum() + 1);
                            continue;
                        }
                    } else {
                        log.warn("Missing or invalid dateOfwork cell in row {}", row.getRowNum() + 1);
                        continue;
                    }

                    // Parse checkInTime (cell 1)
                    Cell checkInCell = row.getCell(1);
                    if (checkInCell != null && checkInCell.getCellType() == CellType.STRING) {
                        String checkInStr = checkInCell.getStringCellValue().trim();
                        if (!checkInStr.isEmpty()) {
                            try {
                                attendance.setCheckInTime(Instant.from(instantFormatter.parse(checkInStr)));
                            } catch (DateTimeParseException e) {
                                log.warn("Invalid checkInTime format in row {}: {}", row.getRowNum() + 1, checkInStr);
                                continue;
                            }
                        } else {
                            log.warn("Empty checkInTime in row {}", row.getRowNum() + 1);
                            continue;
                        }
                    } else {
                        log.warn("Missing or invalid checkInTime cell in row {}", row.getRowNum() + 1);
                        continue;
                    }

                    // Parse checkOutTime (cell 2)
                    Cell checkOutCell = row.getCell(2);
                    if (checkOutCell != null && checkOutCell.getCellType() == CellType.STRING) {
                        String checkOutStr = checkOutCell.getStringCellValue().trim();
                        if (!checkOutStr.isEmpty()) {
                            try {
                                attendance.setCheckOutTime(Instant.from(instantFormatter.parse(checkOutStr)));
                            } catch (DateTimeParseException e) {
                                log.warn("Invalid checkOutTime format in row {}: {}", row.getRowNum() + 1, checkOutStr);
                                continue;
                            }
                        } else {
                            log.warn("Empty checkOutTime in row {}", row.getRowNum() + 1);
                            continue;
                        }
                    } else {
                        log.warn("Missing or invalid checkOutTime cell in row {}", row.getRowNum() + 1);
                        continue;
                    }

                    // Parse workHour (cell 3)
                    Cell workHourCell = row.getCell(3);
                    if (workHourCell != null) {
                        if (workHourCell.getCellType() == CellType.NUMERIC) {
                            attendance.setWorkHour((float) workHourCell.getNumericCellValue());
                        } else if (workHourCell.getCellType() == CellType.STRING) {
                            String workHourStr = workHourCell.getStringCellValue().trim();
                            if (!workHourStr.isEmpty()) {
                                try {
                                    attendance.setWorkHour(Float.parseFloat(workHourStr));
                                } catch (NumberFormatException e) {
                                    log.warn("Invalid workHour format in row {}: {}", row.getRowNum() + 1, workHourStr);
                                    continue;
                                }
                            } else {
                                log.warn("Empty workHour in row {}", row.getRowNum() + 1);
                                continue;
                            }
                        } else {
                            log.warn("Invalid workHour cell type in row {}", row.getRowNum() + 1);
                            continue;
                        }
                    } else {
                        log.warn("Missing workHour cell in row {}", row.getRowNum() + 1);
                        continue;
                    }

                    // Parse employeeId (cell 4)
                    Cell employeeIdCell = row.getCell(4);
                    if (employeeIdCell != null && employeeIdCell.getCellType() == CellType.NUMERIC) {
                        Long employeeId = (long) employeeIdCell.getNumericCellValue();
                        Employee employee = employeeRepository.findById(employeeId).orElse(null);
                        if (employee != null) {
                            attendance.setEmployee(employee);
                        } else {
                            log.warn("Employee with ID {} not found for row {}", employeeId, row.getRowNum() + 1);
                            continue;
                        }
                    } else {
                        log.warn("Missing or invalid employeeId cell in row {}", row.getRowNum() + 1);
                        continue;
                    }

                    attendances.add(attendance);
                } catch (Exception e) {
                    log.warn("Error processing row {}: {}", row.getRowNum() + 1, e.getMessage());
                    continue;
                }
            }

            log.info("Total rows processed: {}, Valid attendances: {}", sheet.getLastRowNum(), attendances.size());
            attendanceRepository.saveAll(attendances);
            for (Attendance attendance : attendances) {
                try {
                    // Create a detached copy with only necessary fields for Elasticsearch
                    Attendance elasticAttendance = new Attendance()
                        .id(attendance.getId())
                        .dateOfwork(attendance.getDateOfwork())
                        .checkInTime(attendance.getCheckInTime())
                        .checkOutTime(attendance.getCheckOutTime())
                        .workHour(attendance.getWorkHour());

                    // Add employee ID reference without the full object
                    if (attendance.getEmployee() != null) {
                        Employee employeeRef = new Employee();
                        employeeRef.setId(attendance.getEmployee().getId());
                        elasticAttendance.setEmployee(employeeRef);
                    }

                    attendanceSearchRepository.save(elasticAttendance);
                } catch (Exception e) {
                    log.error("Error saving attendance to Elasticsearch: {}", e.getMessage());
                    // Continue with the next record rather than failing the entire batch
                }
            }
            return attendances.size();
        }
    }
}
