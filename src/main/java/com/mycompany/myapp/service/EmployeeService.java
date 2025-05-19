package com.mycompany.myapp.service;

import com.mycompany.myapp.repository.EmployeeRepository;
import com.mycompany.myapp.service.dto.response.InforResponse;
import com.mycompany.myapp.service.mapper.InforMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private InforMapper inforMapper;

    private EmployeeRepository employeeRepository;

    public EmployeeService(InforMapper inforMapper, EmployeeRepository employeeRepository) {
        this.inforMapper = inforMapper;
        this.employeeRepository = employeeRepository;
    }

    public List<InforResponse> showInfor(String email) {
        return inforMapper.toDtoList(employeeRepository.getInformation(email));
    }
}
