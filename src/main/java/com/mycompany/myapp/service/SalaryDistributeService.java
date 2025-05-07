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
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SalaryDistributeService {

    SalaryDistributeRepository salaryDistributeRepository;
    AttendanceRepository attendanceRepository;
    EmployeeRepository employeeRepository;
    PayrollRepository payrollRepository;

    public ResponseEntity<List<Payroll>> salaryCaculate(CaculateSalaryRequest request) {
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
        return ResponseEntity.ok(payrolls);
    }
}
