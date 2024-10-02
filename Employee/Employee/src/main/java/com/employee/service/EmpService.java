package com.employee.service;

import com.employee.DTO.TaxResponseDTO;
import com.employee.entity.Employee;

import java.util.Optional;

public interface EmpService {
    Employee saveEmployee(Employee employee);

    Optional<TaxResponseDTO> getEmployeeTaxDeductions(String employeeId);
}
