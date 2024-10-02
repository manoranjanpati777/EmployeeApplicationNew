package com.employee.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxResponseDTO {
    private String employeeId;
    private String firstName;
    private String lastName;
    private double yearlySalary;
    private double taxAmount;
    private double cessAmount;
}
