package com.employee.servvice;


import com.employee.entity.Employee;
import com.employee.repository.EmpRepo;
import com.employee.service.EmpServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmpServiceImplTest {

    @InjectMocks
    private EmpServiceImpl empService;

    @Mock
    private EmpRepo empRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEmployee_Success() {
        Employee employee = new Employee();
        employee.setEmployeeId("E123");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPhoneNumbers(List.of("1234567890"));
        employee.setDoj(LocalDate.parse("2023-05-16"));
        employee.setSalary(50000.0);

        when(empRepo.existsById(employee.getEmployeeId())).thenReturn(false);
        when(empRepo.save(employee)).thenReturn(employee);

        Employee savedEmployee = empService.saveEmployee(employee);

        assertEquals(employee, savedEmployee);
        verify(empRepo, times(1)).save(employee);
    }

    @Test
    void testSaveEmployee_AlreadyExists() {
        Employee employee = new Employee();
        employee.setEmployeeId("E123");

        when(empRepo.existsById(employee.getEmployeeId())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> empService.saveEmployee(employee));
    }

    @Test
    void testGetEmployeeTaxDeductions_Success() {
        Employee employee = new Employee();
        employee.setEmployeeId("E123");
        employee.setSalary(60000.0);
        employee.setDoj(LocalDate.parse("2023-05-16"));

        when(empRepo.findById("E123")).thenReturn(Optional.of(employee));

        var responseDTO = empService.getEmployeeTaxDeductions("E123");

        assertEquals("E123", responseDTO.get().getEmployeeId());
        assertEquals(960000.0, responseDTO.get().getYearlySalary());
        assertEquals(58500.0, responseDTO.get().getTaxAmount());
    }

    @Test
    void testGetEmployeeTaxDeductions_NotFound() {
        when(empRepo.findById("E999")).thenReturn(Optional.empty());

        var responseDTO = empService.getEmployeeTaxDeductions("E999");

        assertEquals(Optional.empty(), responseDTO);
    }
}


