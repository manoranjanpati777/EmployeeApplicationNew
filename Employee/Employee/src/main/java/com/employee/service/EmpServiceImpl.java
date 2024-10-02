package com.employee.service;

import com.employee.DTO.TaxResponseDTO;
import com.employee.entity.Employee;
import com.employee.repository.EmpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpRepo  empRepo;
    @Override
    public Employee saveEmployee(Employee employee) {
        if (empRepo.existsById(employee.getEmployeeId())) {
            throw new IllegalArgumentException("Employee with ID " + employee.getEmployeeId() + " already exists.");
        }

        return empRepo.save(employee);
    }

    @Override
    public Optional<TaxResponseDTO> getEmployeeTaxDeductions(String employeeId) {
        Optional<Employee> employee = empRepo.findById(employeeId);
        if (employee.isPresent()) {
            Employee emp = employee.get();
            double yearlySalary = calculateYearlySalary(emp);
            double taxAmount = calculateTax(yearlySalary);
            double cessAmount = calculateCess(yearlySalary);

            TaxResponseDTO responseDTO = new TaxResponseDTO();
            responseDTO.setEmployeeId(emp.getEmployeeId());
            responseDTO.setFirstName(emp.getFirstName());
            responseDTO.setLastName(emp.getLastName());
            responseDTO.setYearlySalary(yearlySalary);
            responseDTO.setTaxAmount(taxAmount);
            responseDTO.setCessAmount(cessAmount);

            return Optional.of(responseDTO);
        }
        return Optional.empty();
    }

    private double calculateYearlySalary(Employee employee) {
        LocalDate doj = employee.getDoj();
        long monthsWorked = ChronoUnit.MONTHS.between(doj, LocalDate.now());
        return employee.getSalary() * monthsWorked;
    }

    private double calculateTax(double yearlySalary) {
        if (yearlySalary <= 250000) {
            return 0;
        } else if (yearlySalary <= 500000) {
            return (yearlySalary - 250000) * 0.05;
        } else if (yearlySalary <= 1000000) {
            return (yearlySalary - 500000) * 0.10 + 12500;
        } else {
            return (yearlySalary - 1000000) * 0.20 + 62500;
        }
    }

    private double calculateCess(double yearlySalary) {
        if (yearlySalary > 2500000) {
            return (yearlySalary - 2500000) * 0.02;
        }
        return 0;
    }

}
