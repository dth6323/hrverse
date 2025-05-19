package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Attendance;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.repository.AttendanceRepository;
import com.mycompany.myapp.repository.EmployeeRepository;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service class for importing attendance data from an Excel file.
 */
@Service
@Transactional
public class AttendanceImportService {

    private final Logger log = LoggerFactory.getLogger(AttendanceImportService.class);

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceImportService(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    public int importFromExcel(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        List<Attendance> attendances = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

                    // Parse dateOfwork
                    Cell dateCell = row.getCell(1);
                    if (dateCell != null) {
                        String dateStr = dateCell.getStringCellValue();
                        if (dateStr != null && !dateStr.trim().isEmpty()) {
                            attendance.setDateOfwork(LocalDate.parse(dateStr, dateFormatter));
                        } else {
                            log.warn("Invalid or empty dateOfwork in row {}", row.getRowNum() + 1);
                            continue;
                        }
                    } else {
                        log.warn("Missing dateOfwork cell in row {}", row.getRowNum() + 1);
                        continue;
                    }

                    // Parse checkInTime
                    Cell checkInCell = row.getCell(2);
                    if (checkInCell != null) {
                        String checkInStr = checkInCell.getStringCellValue();
                        if (checkInStr != null && !checkInStr.trim().isEmpty()) {
                            attendance.setCheckInTime(Instant.parse(checkInStr));
                        } else {
                            log.warn("Invalid or empty checkInTime in row {}", row.getRowNum() + 1);
                            continue;
                        }
                    } else {
                        log.warn("Missing checkInTime cell in row {}", row.getRowNum() + 1);
                        continue;
                    }

                    // Parse checkOutTime
                    Cell checkOutCell = row.getCell(3);
                    if (checkOutCell != null) {
                        String checkOutStr = checkOutCell.getStringCellValue();
                        if (checkOutStr != null && !checkOutStr.trim().isEmpty()) {
                            attendance.setCheckOutTime(Instant.parse(checkOutStr));
                        } else {
                            log.warn("Invalid or empty checkOutTime in row {}", row.getRowNum() + 1);
                            continue;
                        }
                    } else {
                        log.warn("Missing checkOutTime cell in row {}", row.getRowNum() + 1);
                        continue;
                    }

                    // Parse workHour
                    Cell workHourCell = row.getCell(4);
                    if (workHourCell != null) {
                        if (workHourCell.getCellType() == CellType.NUMERIC) {
                            attendance.setWorkHour((float) workHourCell.getNumericCellValue());
                        } else {
                            String workHourStr = workHourCell.getStringCellValue();
                            if (workHourStr != null && !workHourStr.trim().isEmpty()) {
                                try {
                                    attendance.setWorkHour(Float.parseFloat(workHourStr));
                                } catch (NumberFormatException e) {
                                    log.warn("Invalid workHour format in row {}: {}", row.getRowNum() + 1, workHourStr);
                                    continue;
                                }
                            } else {
                                log.warn("Invalid or empty workHour in row {}", row.getRowNum() + 1);
                                continue;
                            }
                        }
                    } else {
                        log.warn("Missing workHour cell in row {}", row.getRowNum() + 1);
                        continue;
                    }

                    // Parse employeeId and fetch Employee
                    Cell employeeIdCell = row.getCell(5);
                    if (employeeIdCell != null) {
                        if (employeeIdCell.getCellType() == CellType.NUMERIC) {
                            Long employeeId = (long) employeeIdCell.getNumericCellValue();
                            Employee employee = employeeRepository.findById(employeeId).orElse(null);
                            if (employee != null) {
                                attendance.setEmployee(employee);
                            } else {
                                log.warn("Employee with ID {} not found for row {}", employeeId, row.getRowNum() + 1);
                                continue;
                            }
                        } else {
                            log.warn("Invalid employeeId format in row {}", row.getRowNum() + 1);
                            continue;
                        }
                    } else {
                        log.warn("Missing employeeId cell in row {}", row.getRowNum() + 1);
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
            return attendances.size();
        }
    }
}
