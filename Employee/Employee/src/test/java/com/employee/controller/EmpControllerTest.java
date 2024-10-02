package com.employee.controller;


import com.employee.DTO.TaxResponseDTO;
import com.employee.controller.EmpController;
import com.employee.entity.Employee;
import com.employee.service.EmpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmpControllerTest {

    @InjectMocks
    private EmpController  empController;

    @Mock
    private EmpService empService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmployee_Success() {
        Employee employee = new Employee();
        employee.setEmployeeId("E123");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPhoneNumbers(List.of("1234567890"));
        employee.setDoj(LocalDate.parse("2023-05-16"));
        employee.setSalary(50000.0);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(empService.saveEmployee(employee)).thenReturn(employee);

        ResponseEntity<?> response = empController.createEmployee(employee, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    void testGetTaxDeductions_Success() {
        String employeeId = "E123";
        TaxResponseDTO taxResponseDTO = new TaxResponseDTO();
        taxResponseDTO.setEmployeeId(employeeId);
        taxResponseDTO.setFirstName("John");
        taxResponseDTO.setLastName("Doe");
        taxResponseDTO.setYearlySalary(600000);
        taxResponseDTO.setTaxAmount(37500);
        taxResponseDTO.setCessAmount(6000);

        when(empService.getEmployeeTaxDeductions(employeeId)).thenReturn(Optional.of(taxResponseDTO));

        ResponseEntity<?> response = empController.getTaxDeductions(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taxResponseDTO, response.getBody());
    }

    @Test
    void testGetTaxDeductions_NotFound() {
        String employeeId = "E999";

        when(empService.getEmployeeTaxDeductions(employeeId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = empController.getTaxDeductions(employeeId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee not found", response.getBody());
    }
}


